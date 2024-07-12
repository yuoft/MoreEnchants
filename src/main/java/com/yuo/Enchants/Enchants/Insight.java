package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.world.BlockEvent;

import java.util.Random;

public class Insight extends ModEnchantBase {

    public Insight(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 25;
    }

    //额外经验掉落
    public static void addDropExp(BlockEvent.BreakEvent event, int insight){
        //额外获取 原本经验值 * （1 + insight * 30%）经验值
        double exp = event.getExpToDrop() + (100 + insight * 30) / 100.0;
        event.setExpToDrop((int) Math.ceil(exp));
    }

    //增加钓鱼经验
    public static void addFishingExp(Player player, Level world, int insight){
        ExperienceOrb experienceOrbEntity = new ExperienceOrb(world, player.getX(), player.getY(), player.getZ(),
                new Random().nextInt(insight * 3) + 1);
        world.addFreshEntity(experienceOrbEntity);
    }
}
