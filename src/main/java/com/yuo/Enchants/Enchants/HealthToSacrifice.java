package com.yuo.Enchants.Enchants;

import com.yuo.Enchants.Item.YEItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HealthToSacrifice extends ModEnchantBase {

    protected HealthToSacrifice(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 10 + pLevel * 6;
    }

    /**
     * 掉落经验液滴
     * @param world 世界
     * @param level 附魔等级
     * @param looting 抢夺影响
     * @param pos 被攻击生物坐标
     * @param maxHealth 被攻击生物坐标
     */
    public static void dropExpDrip(Level world, int level, int looting, BlockPos pos, float maxHealth){
        if (world.random.nextDouble() < 0.05 + level * 0.05){ //每级增加5%掉率
            ItemStack smallExp = new ItemStack(YEItems.smallExpDrip.get());
            smallExp.setCount(world.random.nextInt(level) + looting);
            if (level > 3 && maxHealth >= 300){
                ItemStack bigExp = new ItemStack(YEItems.bigExpDrip.get(), world.random.nextInt(level) + looting);
                world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), bigExp));
            }
            world.addFreshEntity(new ItemEntity(world,pos.getX(),pos.getY(),pos.getZ(), smallExp));
        }
    }
}
