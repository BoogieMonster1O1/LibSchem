package io.github.boogiemonster1o1.libschem.api;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.boogiemonster1o1.libschem.impl.SchematicBlockPalette;

import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;

@SuppressWarnings("CodeBlock2Expr")
public class Schematic {
    private static final Consumer<String> PRINT_TO_STDERR = System.err::println;
    public static final Codec<Schematic> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
                Codec.INT.fieldOf("Version").forGetter(Schematic::getVersion),
                Codec.INT.optionalFieldOf("Data Version", SharedConstants.getGameVersion().getWorldVersion()).forGetter(Schematic::getDataVersion),
                SchematicMetadata.CODEC.optionalFieldOf("Metadata", SchematicMetadata.EMPTY).forGetter(Schematic::getMetadata),
                Codec.SHORT.fieldOf("Width").forGetter(Schematic::getWidth),
                Codec.SHORT.fieldOf("Height").forGetter(Schematic::getHeight),
                Codec.SHORT.fieldOf("Length").forGetter(Schematic::getLength),
                Vec3i.field_25123.fieldOf("Offset").forGetter(Schematic::getOffset),
                Codec.INT.fieldOf("PaletteMax").forGetter(Schematic::getPaletteMax),
                SchematicBlockPalette.CODEC.fieldOf("Palette").forGetter(Schematic::getBlockPalette),
                Codec.BYTE_BUFFER.fieldOf("BlockData").forGetter(Schematic::getBlockData),
                Codec.list(CompoundTag.CODEC).optionalFieldOf("BlockEntities", ImmutableList.of()).forGetter(Schematic::getBlockEntities),
                Codec.list(CompoundTag.CODEC).optionalFieldOf("Entities", ImmutableList.of()).forGetter(Schematic::getEntities)
        ).apply(instance, Schematic::new);
    });

    private final int version;
    private final int dataVersion;
    private final SchematicMetadata metadata;
    private final short width;
    private final short height;
    private final short length;
    private final Vec3i offset;
    private final int paletteMax;
    private final Map<BlockState, Integer> blockPalette;
    private final ByteBuffer blockData;
    private final List<CompoundTag> blockEntities;
    private final List<CompoundTag> entities;

    public Schematic(int version, int dataVersion, SchematicMetadata metadata, short width, short height, short length, Vec3i offset, int paletteMax, Map<BlockState, Integer> blockPalette, ByteBuffer blockData, List<CompoundTag> blockEntities, List<CompoundTag> entities) {
        this.version = version;
        this.dataVersion = dataVersion;
        this.metadata = metadata;
        this.width = width;
        this.height = height;
        this.length = length;
        this.offset = offset;
        this.paletteMax = paletteMax;
        this.blockPalette = blockPalette;
        this.blockData = blockData;
        this.blockEntities = blockEntities;
        this.entities = entities;
    }

    public int getVersion() {
        return this.version;
    }

    public int getDataVersion() {
        return this.dataVersion;
    }

    public SchematicMetadata getMetadata() {
        return this.metadata;
    }

    public short getWidth() {
        return this.width;
    }

    public short getHeight() {
        return this.height;
    }

    public short getLength() {
        return this.length;
    }

    public Vec3i getOffset() {
        return this.offset;
    }

    public int getPaletteMax() {
        return this.paletteMax;
    }

    public Map<BlockState, Integer> getBlockPalette() {
        return this.blockPalette;
    }

    public ByteBuffer getBlockData() {
        return this.blockData;
    }

    public List<CompoundTag> getBlockEntities() {
        return this.blockEntities;
    }

    public List<CompoundTag> getEntities() {
        return this.entities;
    }

    public static RelativeBlockSample getBlockSample(Schematic schem) {
        return new RelativeBlockSample(schem);
    }

    public static RelativeBlockSample getBlockSample(Schematic schem, StructureWorldAccess world) {
        return getBlockSample(schem).setWorld(world);
    }

    public static Schematic fromTag(CompoundTag tag) {
        return CODEC.decode(NbtOps.INSTANCE, tag).getOrThrow(false, PRINT_TO_STDERR).getFirst();
    }

    public static CompoundTag toTag(Schematic schem) {
        return (CompoundTag) CODEC.encodeStart(NbtOps.INSTANCE, schem).getOrThrow(false, PRINT_TO_STDERR);
    }

    public static Schematic fromJson(JsonObject json) {
        return CODEC.decode(JsonOps.INSTANCE, json).getOrThrow(false, PRINT_TO_STDERR).getFirst();
    }

    public static JsonObject toJson(Schematic schem) {
        return (JsonObject) CODEC.encodeStart(JsonOps.INSTANCE, schem).getOrThrow(false, PRINT_TO_STDERR);
    }

    public static int[][][] getBlockData(Schematic schematic, int width, int height, int length) {
        byte[] blockDataIntArray = schematic.getBlockData().array();
        int[][][] blockData = new int[width][height][length];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    blockData[x][y][z] = blockDataIntArray[x + z * width + y * width * length];
                }
            }
        }
        return blockData;
    }
}
