package com.bmc.coremod.worldgen.chunkgenerator;

import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.bmc.coremod.BMCSkyblockCore.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler{
    @SubscribeEvent
    public static void registerWorldType(RegistryEvent.Register<ForgeWorldType> event){
        event.getRegistry().register(new BMCWorldType());
    }
}
