package com.bmc.coremod.worldgen.biomes;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

import java.util.stream.IntStream;

public class BMCBiomeLayer implements IAreaTransformer0 {

    private static PerlinNoiseGenerator perlinGen;

    public BMCBiomeLayer(long seed, Registry<Biome> dynamicRegistry) {
        if (perlinGen == null)
        {
            SharedSeedRandom sharedseedrandom = new SharedSeedRandom(seed);
            perlinGen = new PerlinNoiseGenerator(sharedseedrandom, IntStream.rangeClosed(0, 0));
        }
    }

    @Override
    public int applyPixel(INoiseRandom noise, int x, int z) {
        return BMCBiomeProvider.LAYER_BIOME_REGISTRY.getId(BMCBiomeProvider.LAYER_BIOME_REGISTRY.get(BMCBiomeProvider.BMCFOREST));
    }

    public static void setSeed(long seed) {
        SharedSeedRandom sharedseedrandom = new SharedSeedRandom(seed);
    }
}