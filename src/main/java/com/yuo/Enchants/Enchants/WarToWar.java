package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class WarToWar extends ModEnchantBase {

    public WarToWar(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 1 + pLevel * 9;
    }

    //回血
    public static void heal(int warToWar, Player player){
        //回血效果和概率与等级相关
        int i = new Random().nextInt(100);
        if (i < (20 + warToWar * 15)){
            player.heal(warToWar / 2.0f);
        }
    }
}
