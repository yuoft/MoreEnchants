package com.yuo.Enchants.Enchants;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class FireShield extends ModEnchantBase {

    public FireShield(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 17 + enchantmentLevel * 7;
    }

    //火焰盾
    public static void fireShield(DamageSource source, int rebound, Player player){
        Entity immediateSource = source.getDirectEntity(); //发起攻击者
        if (player.isBlocking() && immediateSource instanceof LivingEntity living){ //燃烧攻击者
            living.setSecondsOnFire(2 + rebound);
        }
    }
}
