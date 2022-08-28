package com.yuo.enchants.Enchants;

import com.yuo.enchants.Items.ItemRegistry;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HealthToSacrifice extends ModEnchantBase {

    protected HealthToSacrifice(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    /**
     * 掉落经验液滴
     * @param world 世界
     * @param level 附魔等级
     * @param looting 抢夺影响
     * @param pos 被攻击生物坐标
     */
    public static void dropExpDrip(World world, int level, int looting, BlockPos pos, boolean flag){
        if (world.rand.nextDouble() < 0.05 + level * 0.05){ //每级增加10%掉率
            ItemStack smallExp = new ItemStack(ItemRegistry.smallExpDrip.get());
            smallExp.setCount(world.rand.nextInt(level) + looting);
            if (level > 3 && flag){
                ItemStack bigExp = new ItemStack(ItemRegistry.bigExpDrip.get(), world.rand.nextInt(level) + looting);
                world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), bigExp));
            }
            world.addEntity(new ItemEntity(world,pos.getX(),pos.getY(),pos.getZ(), smallExp));
        }
    }
}
