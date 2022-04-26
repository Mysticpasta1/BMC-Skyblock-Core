package com.bmc.coremod.config;

import com.bmc.coremod.BMCSkyblockCore;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

@Mod.EventBusSubscriber
public class Config {
    private static final ForgeConfigSpec.Builder server_builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec server_config;
    
    static {
        server_config = server_builder.build();

    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        BMCSkyblockCore.LOGGER.info("Loading config: " + path);
        final CommentedFileConfig configFile = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        BMCSkyblockCore.LOGGER.info("Built config: " + path);
        configFile.load();
        BMCSkyblockCore.LOGGER.info("Loaded config: " + path);
        config.setConfig(configFile);

    }
}
