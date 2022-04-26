package com.bmc.coremod.lootable.conditions;

import com.bmc.coremod.BMCSkyblockCore;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.potion.Effects;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.ToolType;

public class HasSilkTouch implements ILootCondition {
    private final boolean shouldHave;


    public HasSilkTouch(boolean shouldHave) {
        this.shouldHave = shouldHave;
    }

    public LootConditionType getType() {
        return BMCSkyblockCore.ConditionRegistry.HAS_SILK_TOUCH;
    }

    public boolean test(LootContext lootContext) {
        ItemStack itemStack = lootContext.getParamOrNull(LootParameters.TOOL);

        if(itemStack != null) {
            if(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) > 0) {
                return shouldHave;

            }
            else {
                return !shouldHave;
            }
        }

        return false;
    }

    public static class Serializer implements ILootSerializer<HasSilkTouch> {
        @Override
        public void serialize(JsonObject JSONObject, HasSilkTouch hasSilkTouch, JsonSerializationContext JSONSerializationContext) {
            JSONObject.addProperty("shouldHave", hasSilkTouch.shouldHave);
        }

        public HasSilkTouch deserialize(JsonObject JSONObject, JsonDeserializationContext jsonDeserializationContext) {
            return new HasSilkTouch(JSONUtils.getAsBoolean(JSONObject, "shouldHave"));
        }
    }
}
