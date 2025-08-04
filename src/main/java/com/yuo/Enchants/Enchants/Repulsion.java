package com.yuo.Enchants.Enchants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class Repulsion extends ModEnchantBase {

    public Repulsion(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 20 + pLevel * 7;
    }

    /**
     * 吸引或排斥实体
     * @param world 世界
     * @param pos 玩家坐标
     * @param xRange 水平距离
     * @param yRange 垂直距离
     */
    public static void moveLivingEntityInRegion(Level world, BlockPos pos, int xRange, int yRange) {
        AABB range = Magnet.makeBoundingBox(pos, xRange, yRange);
        List<Entity> all = getEntity(world, range);
        Magnet.pullEntityList(pos, false, all);
    }

    /**
     * 获取范围内所以有生物和投掷物
     * @param world 世界
     * @param range 范围
     * @return 列表
     */
    public static List<Entity> getEntity(Level world, AABB range) {
        List<Entity> all = new ArrayList<>();
        all.addAll(world.getEntitiesOfClass(LivingEntity.class, range));
        all.addAll(world.getEntitiesOfClass(Projectile.class, range));
        return all;
    }
}
