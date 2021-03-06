package com.yuo.yuoenchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;

public class Thorns extends ModEnchantBase {

    public Thorns(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.THORNS; //荆棘
    }

    //真荆棘
    public static void thorns(PlayerEntity player, ItemStack stackChest, int thorns){
        long dayTime = player.world.getDayTime();
        if (dayTime % 40 == 0){
            player.attackEntityFrom(DamageSource.GENERIC, thorns / 2.0f);
            stackChest.damageItem(2, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
        }
    }
}
