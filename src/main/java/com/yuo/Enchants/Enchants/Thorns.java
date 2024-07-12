package com.yuo.Enchants.Enchants;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class Thorns extends ModEnchantBase {

    public Thorns(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 20 + pLevel * 5;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.THORNS && ench != EnchantRegistry.superThorns.get() &&
                ench != EnchantRegistry.fireThorns.get(); //荆棘
    }

    //真荆棘
    public static void thorns(Player player, ItemStack stackChest, int thorns){
        long dayTime = player.level.getDayTime();
        if (dayTime % 40 == 0){
            player.hurt(DamageSource.GENERIC, thorns / 2.0f);
            stackChest.hurtAndBreak(2, player, e -> e.broadcastBreakEvent(EquipmentSlot.CHEST));
        }
    }
}
