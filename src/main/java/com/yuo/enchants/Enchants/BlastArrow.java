package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.world.Explosion;

public class BlastArrow extends ModEnchantBase {

    public BlastArrow(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        //火矢
        return this != ench && ench != Enchantments.FLAME;
    }

    //产生爆炸
    public static void boom(AbstractArrowEntity arrow, int blastArrow){
        //产生爆炸
        arrow.world.createExplosion(arrow, arrow.getPosX(), arrow.getPosY(), arrow.getPosZ(), blastArrow * 4.0f, false, Explosion.Mode.NONE);
        arrow.remove(); //删除实体
    }
}
