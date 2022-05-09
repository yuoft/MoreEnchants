package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import java.util.Random;

public class Insight extends ModEnchantBase {

    public Insight(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    //额外经验掉落
    public static void addDropExp(BlockEvent.BreakEvent event, int insight){
        //额外获取 原本经验值 * （1 + insight * 30%）经验值
        double exp = event.getExpToDrop() + (100 + insight * 30) / 100.0;
        event.setExpToDrop((int) Math.ceil(exp));
    }

    //增加钓鱼经验
    public static void addFishingExp(PlayerEntity player, World world, int insight){
        ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, player.getPosX(), player.getPosY(),
                player.getPosZ(), new Random().nextInt(insight * 3) + 1);
        world.addEntity(experienceOrbEntity);
    }
}
