package com.bmc.coremod.worldgen.chunkgenerator;

import com.bmc.coremod.BMCSkyblockCore;
import com.bmc.coremod.worldgen.biomes.BMCBiomeProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraftforge.common.world.ForgeWorldType;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class BMCWorldType extends ForgeWorldType {
    public BMCWorldType() {
        super((biomeRegistry, dimensionSettingsRegistry, seed) -> new BMCChunkGenerator(new BMCBiomeProvider(biomeRegistry, 30, seed)));
        setRegistryName(new ResourceLocation(BMCSkyblockCore.MODID, "void"));
    }
}
