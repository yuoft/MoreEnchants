package com.yuo.enchants.Enchants;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

import java.util.Random;

public class WarToWar extends ModEnchantBase {

    public WarToWar(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + enchantmentLevel * 9;
    }

    //回血
    public static void heal(int warToWar, PlayerEntity player){
        //回血效果和概率与等级相关
        int i = new Random().nextInt(100);
        if (i < (20 + warToWar * 15)){
            player.heal(warToWar / 2.0f);
        }
    }
}
