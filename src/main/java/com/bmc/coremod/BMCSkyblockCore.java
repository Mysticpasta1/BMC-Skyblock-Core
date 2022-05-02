package com.bmc.coremod;

import com.bmc.coremod.config.Config;
import com.bmc.coremod.lootable.conditions.HasSilkTouch;
import com.bmc.coremod.lootable.conditions.RandomChanceWithLuck;
import com.bmc.coremod.lootable.conditions.ToolHasHarvestLevel;
import com.bmc.coremod.worldgen.structures.BMCConfiguredStructures;
import com.bmc.coremod.worldgen.structures.BMCStructures;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.bmc.coremod.blocks.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Mod("bmcsbc")
public class BMCSkyblockCore
{

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "bmcsbc";



    private void setup(FMLCommonSetupEvent evt) {
        evt.enqueueWork(() -> {
            BMCStructures.setupStructures();
            BMCConfiguredStructures.registerConfiguredStructures();
        });
    }

    public static class ModTab extends ItemGroup {

        public ModTab(String label) {
            super(label);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.MineCoin.get());
        }
    }

    public static final ItemGroup CoreTab = new ModTab("bmc_skyblock");

    public static class BlockRegistry {
        private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);




        public static final RegistryObject<Block> MarketStall = BLOCKS.register("market_stall", () -> new MarketStall(AbstractBlock.Properties.of(Material.WOOD)
                .sound(SoundType.WOOD)
                .strength(1)
                .harvestTool(ToolType.AXE)
                .harvestLevel(0)));

        public static final RegistryObject<Block> MultiOre = BLOCKS.register("multi_ore", () -> new MultiOre(AbstractBlock.Properties.of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(2)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops()
                .harvestLevel(0)));
    }

    public static class ConditionRegistry {
        public static void init() {}

        public static final LootConditionType RANDOM_CHANCE_WITH_LUCK = register("random_chance_with_luck", new RandomChanceWithLuck.Serializer());
        public static final LootConditionType TOOL_HAS_HARVEST_LEVEL = register("tool_has_harvest_level", new ToolHasHarvestLevel.Serializer());
        public static final LootConditionType HAS_SILK_TOUCH = register("has_silk_touch", new HasSilkTouch.Serializer());

        private static LootConditionType register(String id, ILootSerializer<? extends ILootCondition> serializer) {
            return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(MODID, id), new LootConditionType(serializer));
        }
    }

    public static class ItemRegistry {



        private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

        public static final RegistryObject<Item> MultiOre = ITEMS.register("multi_ore", () -> new BlockItem(BlockRegistry.MultiOre.get(), new Item.Properties().tab(BMCSkyblockCore.CoreTab)));
        public static final RegistryObject<Item> MarketStall = ITEMS.register("market_stall", () -> new BlockItem(BlockRegistry.MarketStall.get(), new Item.Properties().tab(BMCSkyblockCore.CoreTab)));
        public static final RegistryObject<Item> MineCoin = ITEMS.register("minecoin", () -> new Item(new Item.Properties().tab(BMCSkyblockCore.CoreTab)));

    }

/*    @SubscribeEvent
    public static void registerTE(RegistryEvent.Register<TileEntityType<?>> evt) {e
        TileEntityType<?> type = TileEntityType.Builder.of(factory, validBlocks).build(null);
        type.setRegistryName("bmcsbc", "market_stall");
        evt.getRegistry().register(type);
    } */


    @SubscribeEvent
    public void pistonPushed(final PistonEvent.Post event) {

        if(event.getPistonMoveType() == PistonEvent.PistonMoveType.EXTEND && !event.getWorld().isClientSide()) {
            System.out.println("extendo");
            System.out.println(String.valueOf(ToolType.AXE));
        }

    }


    public BMCSkyblockCore() {
        ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BMCStructures.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);

        modEventBus.addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.server_config);
        ConditionRegistry.init();

        Config.loadConfig(Config.server_config, FMLPaths.CONFIGDIR.get().resolve("bmcsbc-server.toml").toString());
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void biomeModification(final BiomeLoadingEvent event) {
        event.getGeneration().getStructures().add(() -> BMCConfiguredStructures.CONFIGURED_START_ISLAND);
    }

    private static Method GETCODEC_METHOD;
    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)event.getWorld();

            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch(Exception e){
                BMCSkyblockCore.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            if(serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator &&
                    serverWorld.dimension().equals(World.OVERWORLD)){
                return;
            }

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.putIfAbsent(BMCStructures.START_ISLAND.get(), DimensionStructuresSettings.DEFAULTS.get(BMCStructures.START_ISLAND.get()));
            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
    }
}
