package com.bmc.coremod.worldgen.biomes;

import com.bmc.coremod.BMCSkyblockCore;
import com.bmc.coremod.mixin.common.LayerAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;

import java.util.Map;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

public class BMCBiomeProvider extends BiomeProvider {

    public static final Codec<BMCBiomeProvider> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((biomeSource) ->
                    biomeSource.biomeRegistry), Codec.intRange(1, 20).fieldOf("biome_size").orElse(2).forGetter((biomeSource) ->
                    biomeSource.biomeSize), Codec.LONG.fieldOf("seed").stable().forGetter((biomeSource) ->
                    biomeSource.seed)).apply(instance, instance.stable(BMCBiomeProvider::new)));

    public static Registry<Biome> biomeRegistry;
    public static long seed;
    public static int biomeSize;
    private static Layer BIOME_SAMPLER;
    public static Registry<Biome> LAYER_BIOME_REGISTRY;
    public static ResourceLocation BMCFOREST = new ResourceLocation(BMCSkyblockCore.MODID, "bmcforest");

    public BMCBiomeProvider(Registry<Biome> biomeRegistry, int biomeSize, long seed) {
        super(biomeRegistry.entrySet().stream()
                .filter(entry -> entry.getKey().getRegistryName().getNamespace().equals(BMCSkyblockCore.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));
        BMCBiomeProvider.BIOME_SAMPLER = buildWorldProcedure(seed);
        BMCBiomeProvider.biomeRegistry = biomeRegistry;
        BMCBiomeProvider.biomeSize = biomeSize;
        BMCBiomeProvider.LAYER_BIOME_REGISTRY = biomeRegistry;
        BMCBiomeProvider.seed = seed;
        BMCBiomeLayer.setSeed(seed);
    }

    @Override
    protected Codec<? extends BiomeProvider> codec() {
        return CODEC;
    }

    public static Layer buildWorldProcedure(long seed) {
        IAreaFactory<LazyArea> layerFactory = build((salt) ->
                new LazyAreaLayerContext(25, seed, salt));
        return new Layer(layerFactory);
    }

    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> stack(long seed, IAreaTransformer1 parent, IAreaFactory<T> incomingArea, int count, LongFunction<C> contextFactory) {
        IAreaFactory<T> IAreaFactory = incomingArea;

        for (int i = 0; i < count; ++i) {
            IAreaFactory = parent.run(contextFactory.apply(seed + (long) i), IAreaFactory);
        }

        return IAreaFactory;
    }

    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> build(LongFunction<C> contextFactory) {
        IAreaFactory<T> layer = (new BMCBiomeLayer(seed, biomeRegistry)).run(contextFactory.apply(200L));
        for(int currentExtraZoom = 0; currentExtraZoom < biomeSize; currentExtraZoom++){
            if((currentExtraZoom + 2) % 3 != 0){
                layer = ZoomLayer.NORMAL.run(contextFactory.apply(2001L + currentExtraZoom), layer);
            }
            else{
                layer= ZoomLayer.FUZZY.run(contextFactory.apply(2000L + (currentExtraZoom * 31L)), layer);
            }
        }
        return layer;
    }

    @Override
    public BiomeProvider withSeed(long seed) {
        return new BMCBiomeProvider(biomeRegistry, biomeSize, seed);
    }

    public Biome sample(Registry<Biome> dynamicBiomeRegistry, int x, int z) {
        int resultBiomeID = ((LayerAccessor)this.BIOME_SAMPLER).getSampler().get(x, z);
        Biome biome = dynamicBiomeRegistry.byId(resultBiomeID);
        if (biome == null) {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw Util.pauseInIde(new IllegalStateException("Unknown biome id: " + resultBiomeID));
            } else {
                // Spawn ocean if we can't resolve the biome from the layers.
                RegistryKey<Biome> backupBiomeKey = Biomes.OCEAN;
                BMCSkyblockCore.LOGGER.warn("Unknown biome id: ${}. Will spawn ${} instead.", resultBiomeID, backupBiomeKey.getRegistryName());
                return dynamicBiomeRegistry.get(backupBiomeKey);
            }
        } else {
            return biome;
        }
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return this.sample(biomeRegistry, x, z);
    }
}
