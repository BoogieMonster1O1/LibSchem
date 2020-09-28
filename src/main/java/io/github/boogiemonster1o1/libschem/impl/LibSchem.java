package io.github.boogiemonster1o1.libschem.impl;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;

import net.minecraft.server.MinecraftServer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

public class LibSchem implements ModInitializer {
    static MinecraftServer server;

    @Override
    public void onInitialize() {
        LogManager.getLogger().info("Initializing LibSchem - Powered by DataFixerUpper");

        if (FabricLoader.getInstance().isModLoaded("fabric")) {
            this.doFapiStuff();
        }
    }

    private void doFapiStuff() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> LibSchem.server = server);
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                SchematicTest.test();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
