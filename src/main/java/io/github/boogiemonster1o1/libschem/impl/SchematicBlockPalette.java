package io.github.boogiemonster1o1.libschem.impl;

import java.util.Iterator;
import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.UnboundedMapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.registry.Registry;

public class SchematicBlockPalette {
    public static final UnboundedMapCodec<BlockState, Integer> CODEC = Codec.unboundedMap(Entry.CODEC, Codec.INT);

    private static <T extends Comparable<T>> BlockState process(Property<T> property, String value, BlockState state) {
        return state.with(property, property.parse(value).orElseThrow(NullPointerException::new));
    }

    public interface Entry {
        Codec<BlockState> CODEC = Codec.STRING.comapFlatMap(Entry::to, Entry::from);

        static DataResult<BlockState> to(String string) {
            if (!string.contains("[") && !string.contains("]")) {
                BlockState state = Registry.BLOCK.get(new Identifier(string)).getDefaultState();
                return DataResult.success(state);
            } else {
                Block block = Registry.BLOCK.get(new Identifier(string.substring(0, string.indexOf("["))));
                BlockState state = block.getDefaultState();

                System.out.println(state);

                String[] stateArray = string.substring(string.indexOf("[") + 1, string.length() - 1).split(",");
                for (String stateString : stateArray) {
                    String name = stateString.split("=")[0];
                    Property<?> property = block.getStateManager().getProperty(name);
                    if (property == null) {
                        return DataResult.error("Unknown property '" + name + "' for block " + block.toString(), state);
                    }
                    state = process(property, stateString.split("=")[1], state);
                }

                System.out.println(state);

                return DataResult.success(state);
            }
        }

        static String from(BlockState state) {
            StringBuilder builder = new StringBuilder();
            builder.append(Objects.requireNonNull(Registry.BLOCK.getId(state.getBlock())));
            // Ensures that [ and ] are only added when properties are present
            boolean flag = true;
            Iterator<Property<?>> iterator = state.getProperties().iterator();
            while (iterator.hasNext()) {
                if (flag) {
                    builder.append("[");
                    flag = false;
                }

                Property<?> property = iterator.next();
                builder.append(property.getName());
                builder.append("=");

                if (state.get(property) instanceof Enum<?>) {
                    // Enum might have override toString
                    builder.append(((StringIdentifiable) state.get(property)).asString());
                } else {
                    builder.append(state.get(property).toString());
                }

                if (iterator.hasNext()) {
                    builder.append(",");
                }
            }
            if (!flag) {
                builder.append("]");
            }
            return builder.toString();
        }
    }
}
