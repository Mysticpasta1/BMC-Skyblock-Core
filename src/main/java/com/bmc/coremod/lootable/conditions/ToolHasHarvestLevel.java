package com.bmc.coremod.lootable.conditions;

import com.bmc.coremod.BMCSkyblockCore;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.potion.Effects;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.ToolType;

import java.util.function.IntPredicate;

public class ToolHasHarvestLevel implements ILootCondition {
    private final int harvestLevel;

    public ToolHasHarvestLevel(int harvestLevel) {
        this.harvestLevel = harvestLevel;
    }


    public LootConditionType getType() {
        return BMCSkyblockCore.ConditionRegistry.TOOL_HAS_HARVEST_LEVEL;
    }

    public boolean test(LootContext lootContext) {
        Entity target = lootContext.getParamOrNull(LootParameters.THIS_ENTITY);
        ItemStack itemStack = lootContext.getParamOrNull(LootParameters.TOOL);
        BlockState blockState = lootContext.getParamOrNull(LootParameters.BLOCK_STATE);

        if(target instanceof PlayerEntity && itemStack != null && blockState != null) {
            return itemStack.getHarvestLevel(ToolType.PICKAXE, (PlayerEntity) target, blockState) >= this.harvestLevel;
        }

        return true;
    }

    public static class Serializer implements ILootSerializer<ToolHasHarvestLevel> {
        public void serialize(JsonObject JSONObject, ToolHasHarvestLevel toolHasHarvestLevel, JsonSerializationContext JSONSerializationContext) {
            JSONObject.addProperty("harvestLevel", toolHasHarvestLevel.harvestLevel);
        }

        public ToolHasHarvestLevel deserialize(JsonObject JSONObject, JsonDeserializationContext jsonDeserializationContext) {
            return new ToolHasHarvestLevel(JSONUtils.getAsInt(JSONObject, "harvestLevel"));
        }
    }
}
