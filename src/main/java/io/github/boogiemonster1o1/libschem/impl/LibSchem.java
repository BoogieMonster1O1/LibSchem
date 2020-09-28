package io.github.boogiemonster1o1.libschem.impl;

import org.apache.logging.log4j.LogManager;

import net.fabricmc.api.ModInitializer;

public class LibSchem implements ModInitializer {
    @Override
    public void onInitialize() {
        LogManager.getLogger().info("Initializing LibSchem - Powered by DataFixerUpper");
    }
}
