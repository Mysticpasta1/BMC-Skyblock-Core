package com.bmc.coremod.worldgen.chunkgenerator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

import java.util.Optional;

public class BMCChunkGenerator extends ChunkGenerator {

    public static final Codec<BMCChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BiomeProvider.CODEC.fieldOf("biome_source").forGetter(BMCChunkGenerator::getBiomeSource)
    ).apply(instance, instance.stable(BMCChunkGenerator::new)));

    public BMCChunkGenerator(BiomeProvider biomeProvider) {
        super(biomeProvider, new DimensionStructuresSettings(Optional.empty(), DimensionStructuresSettings.DEFAULTS));
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }


    @Override
    public ChunkGenerator withSeed(long p_230349_1_) {
        return this;
    }

    @Override
    public void buildSurfaceAndBedrock(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {}

    @Override
    public void fillFromNoise(IWorld p_230352_1_, StructureManager p_230352_2_, IChunk p_230352_3_) {}

    @Override
    public int getBaseHeight(int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_) {
        return 256;
    }

    @Override
    public IBlockReader getBaseColumn(int p_230348_1_, int p_230348_2_) {
        return new Blockreader(new BlockState[] {Blocks.AIR.defaultBlockState()});
    }

    public int getSeaLevel() {
        return -256;
    }
}
