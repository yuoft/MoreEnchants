package com.yuo.enchants.Enchants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Repulsion extends ModEnchantBase {

    public Repulsion(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20 + enchantmentLevel * 7;
    }

    /**
     * 吸引或排斥实体
     * @param world 世界
     * @param pos 玩家坐标
     * @param xRange 水平距离
     * @param yRange 垂直距离
     */
    public static void moveLivingEntityInRegion(World world, BlockPos pos, int xRange, int yRange) {
        AxisAlignedBB range = Magnet.makeBoundingBox(pos, xRange, yRange);
        List<Entity> all = getEntity(world, range);
        Magnet.pullEntityList(pos, false, all);
    }

    /**
     * 获取范围内所以有生物和投掷物
     * @param world 世界
     * @param range 范围
     * @return 列表
     */
    public static List<Entity> getEntity(World world, AxisAlignedBB range) {
        List<Entity> all = new ArrayList<>();
        all.addAll(world.getEntitiesWithinAABB(LivingEntity.class, range));
        all.addAll(world.getEntitiesWithinAABB(ProjectileEntity.class, range));
        return all;
    }
}
