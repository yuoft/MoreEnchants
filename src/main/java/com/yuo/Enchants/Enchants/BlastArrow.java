package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;

public class BlastArrow extends ModEnchantBase {

    public BlastArrow(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinCost(int pLevel) {
        return 21;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        //火矢
        return this != ench && ench != Enchantments.FIRE_ASPECT;
    }


    //产生爆炸
    public static void boom(AbstractArrow arrow, int blastArrow){
        //产生爆炸
        arrow.level.explode(arrow, arrow.getX(), arrow.getY(), arrow.getZ(), blastArrow * 4.0f, false, Explosion.BlockInteraction.NONE);
        arrow.remove(Entity.RemovalReason.DISCARDED); //删除实体
    }
}
