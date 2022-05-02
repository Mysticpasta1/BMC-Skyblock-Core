package com.bmc.coremod.worldgen.structures;

import com.bmc.coremod.BMCSkyblockCore;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class BMCConfiguredStructures {

    public static StructureFeature<?, ?> CONFIGURED_START_ISLAND = BMCStructures.START_ISLAND.get().configured(IFeatureConfig.NONE);

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(BMCSkyblockCore.MODID, "configured_start_island"), CONFIGURED_START_ISLAND);

        FlatGenerationSettings.STRUCTURE_FEATURES.put(BMCStructures.START_ISLAND.get(), CONFIGURED_START_ISLAND);
    }
}