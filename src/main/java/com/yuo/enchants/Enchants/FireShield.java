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
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    //弹反
    public static void fireShield(DamageSource source, int rebound, PlayerEntity player){
        Entity immediateSource = source.getImmediateSource(); //发起攻击者
        if (player.isActiveItemStackBlocking() && immediateSource instanceof LivingEntity){ //燃烧攻击者
            LivingEntity living = (LivingEntity) immediateSource;
            living.setFire(2 + rebound);
        }
    }
}
