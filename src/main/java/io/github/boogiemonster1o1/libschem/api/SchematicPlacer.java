package io.github.boogiemonster1o1.libschem.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

import net.fabricmc.loader.api.FabricLoader;

public final class SchematicPlacer {
    public static final Logger LOGGER = LogManager.getLogger();

    private SchematicPlacer() {
    }

    public static void place(Schematic schematic, StructureWorldAccess world, BlockPos origin) {
        LOGGER.debug("Placing schematic: {}", schematic.getMetadata().getName());
        for (String id : schematic.getMetadata().getRequiredMods()) {
            if (!FabricLoader.getInstance().isModLoaded(id)) {
                LOGGER.warn("Schematic \"" + schematic.getMetadata().getName() + "\" depends on mod \"" + id + "\", which is missing!");
            }
        }
        RelativeBlockSample blockSample = Schematic.getBlockSample(schematic, world);
        blockSample.place(origin);
    }
}
