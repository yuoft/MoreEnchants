package com.yuo.yuoenchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemFishedEvent;

import java.util.Random;

public class BadLuckOfTheSea extends ModEnchantBase {

    public BadLuckOfTheSea(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
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
        //海之眷顾
        return this != ench && ench != Enchantments.LUCK_OF_THE_SEA;
    }

    //钓上TNT
    public static void fishingTnt(ItemFishedEvent event, int badLuckOfTheSea, PlayerEntity player, World world){
        BlockPos blockPos = event.getHookEntity().getPosition();
        int i = new Random().nextInt(100);
        if (i < badLuckOfTheSea * 10 + 30){ //引燃的TNT飞向玩家
            event.getDrops().clear();
            TNTEntity tntEntity = new TNTEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), player);
            tntEntity.setFuse(40); //引燃时间40刻度（2S）
            BlockPos pos = event.getHookEntity().getPosition(); //获取鱼鳔实体坐标
            double d0 = player.getPosX() - pos.getX();
            double d1 = player.getPosY() - pos.getY();
            double d2 = player.getPosZ() - pos.getZ();
            //设置tnt运动方向
            tntEntity.setMotion(d0 * 0.1D, d1 * 0.1D + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08D, d2 * 0.1D);
            world.addEntity(tntEntity);
        }
    }
}
