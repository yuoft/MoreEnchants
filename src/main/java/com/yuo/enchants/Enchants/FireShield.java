package com.yuo.enchants.Enchants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;

public class FireShield extends ModEnchantBase {

    public FireShield(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 17 + enchantmentLevel * 7;
    }

    //火焰盾
    public static void fireShield(DamageSource source, int rebound, PlayerEntity player){
        Entity immediateSource = source.getImmediateSource(); //发起攻击者
        if (player.isActiveItemStackBlocking() && immediateSource instanceof LivingEntity){ //燃烧攻击者
            LivingEntity living = (LivingEntity) immediateSource;
            living.setFire(2 + rebound);
        }
    }
}
