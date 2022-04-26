package com.bmc.coremod.lootable.conditions;

import com.bmc.coremod.BMCSkyblockCore;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.potion.Effects;
import net.minecraft.util.JSONUtils;

import java.util.function.IntPredicate;

public class RandomChanceWithLuck implements ILootCondition {
    private final float luckModifier;
    private final float baseChance;
    private final LootContext.EntityTarget entity;


    public RandomChanceWithLuck(LootContext.EntityTarget entity, float baseChance, float luckModifier) {
        this.entity = entity;
        this.luckModifier = luckModifier;
        this.baseChance = baseChance;
    }

    @Override
    public LootConditionType getType() {
        return BMCSkyblockCore.ConditionRegistry.RANDOM_CHANCE_WITH_LUCK;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity target = lootContext.getParamOrNull(entity.getParam());
        float chance = this.baseChance;

        if(target instanceof LivingEntity) {
            if(((LivingEntity) target).hasEffect(Effects.LUCK)) {
                return lootContext.getRandom().nextFloat() < chance * this.luckModifier;
            }
            else {
                return lootContext.getRandom().nextFloat() < chance;
            }
        }

        return lootContext.getRandom().nextFloat() < chance;
    }

    public static class Serializer implements ILootSerializer<RandomChanceWithLuck> {
        @Override
        public void serialize(JsonObject JSONObject, RandomChanceWithLuck randomChanceWithLuck, JsonSerializationContext JSONSerializationContext) {
            JSONObject.addProperty("entity", String.valueOf(JSONSerializationContext.serialize(randomChanceWithLuck.entity)));
            JSONObject.addProperty("luckModifier", randomChanceWithLuck.luckModifier);
            JSONObject.addProperty("baseChance", randomChanceWithLuck.baseChance);
        }

        public RandomChanceWithLuck deserialize(JsonObject JSONObject, JsonDeserializationContext jsonDeserializationContext) {
            return new RandomChanceWithLuck(JSONUtils.getAsObject(JSONObject, "entity", jsonDeserializationContext, LootContext.EntityTarget.class), JSONUtils.getAsFloat(JSONObject, "baseChance"), JSONUtils.getAsFloat(JSONObject, "luckModifier"));
        }
    }
}
