package com.yuo.Enchants.Enchants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.ItemFishedEvent;

import java.util.Random;

public class BadLuckOfTheSea extends ModEnchantBase {

    public BadLuckOfTheSea(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 10 + pLevel * 5;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    protected boolean checkCompatibility(Enchantment pOther) {
        //海之眷顾
        return this != pOther && pOther != Enchantments.FISHING_LUCK;
    }

    //钓上TNT
    public static void fishingTnt(ItemFishedEvent event, int badLuckOfTheSea, Player player, Level world){
        BlockPos blockPos = event.getHookEntity().getOnPos();
        int i = new Random().nextInt(100);
        if (i < badLuckOfTheSea * 10 + 30){ //引燃的TNT飞向玩家
            event.getDrops().clear();
            PrimedTnt tntEntity = new PrimedTnt(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), player);
            tntEntity.setFuse(40); //引燃时间40刻度 2S
            BlockPos pos = event.getHookEntity().getOnPos(); //获取鱼鳔实体坐标
            double d0 = player.getX() - pos.getX();
            double d1 = player.getY() - pos.getY();
            double d2 = player.getZ() - pos.getZ();
            //设置tnt运动方向
            tntEntity.setDeltaMovement(d0 * 0.1D, d1 * 0.1D + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08D, d2 * 0.1D);
            world.addFreshEntity(tntEntity);
        }
    }
}
