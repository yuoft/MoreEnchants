package com.yuo.enchants.Enchants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Magnet extends ModEnchantBase {

    public Magnet(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 18 + enchantmentLevel * 8;
    }

    /**
     * 吸引或排斥实体
     * @param world 世界
     * @param pos 玩家坐标
     * @param xRange 水平距离
     * @param yRange 垂直距离
     */
    public static void moveEntityItemsInRegion(World world, BlockPos pos, int xRange, int yRange) {
        AxisAlignedBB range = makeBoundingBox(pos, xRange, yRange);
        List<Entity> all = getItemExp(world, range);
        pullEntityList(pos, true, all);
    }

    /**
     * 获取范围中所有物品实体 和 经验实体
     * @param world  世界
     * @param range 范围
     * @return 物品实体列表
     */
    public static List<Entity> getItemExp(World world, AxisAlignedBB range) {
        List<Entity> all = new ArrayList<>();
        all.addAll(world.getEntitiesWithinAABB(ItemEntity.class, range));
        all.addAll(world.getEntitiesWithinAABB(ExperienceOrbEntity.class, range));
        return all;
    }

    /**
     * 返回一个范围
     * @param pos 基点
     * @param xRange 水平范围
     * @param yRange 垂直范围
     * @return 范围
     */
    public static AxisAlignedBB makeBoundingBox(BlockPos pos, int xRange, int yRange) {
        return new AxisAlignedBB(
                pos.getX() - xRange, pos.getY() - yRange, pos.getZ() - xRange,
                pos.getX() + xRange, pos.getY() + yRange, pos.getZ() + xRange);
    }

    public static void pullEntityList(BlockPos pos, boolean towardsPos, List<? extends Entity> all) {
        double hdist, xDist, zDist; //方向
        float speed;
        int direction = (towardsPos) ? 1 : -1; //吸引还是排斥
        for (Entity entity : all) {
            if (entity == null) {
                continue;
            }
            if (entity instanceof PlayerEntity && entity.isCrouching()) {
                continue;
            }
            BlockPos p = entity.getPosition();
            xDist = Math.abs(pos.getX() - p.getX());
            zDist = Math.abs(pos.getZ() - p.getZ());
            hdist = Math.sqrt(xDist * xDist + zDist * zDist); //距离玩家距离
            if (hdist > 0.4) {
                speed = (hdist > 4) ? 0.5f : 0.2f;
                setEntityMotionFromVector(entity, pos, direction * speed);
            }
        }
    }

    /**
     * 设置实体移动
     * @param entity 要移动的实体
     * @param pos 玩家坐标
     * @param modifier 移动距离
     */
    public static void setEntityMotionFromVector(Entity entity, BlockPos pos, float modifier) {
        Vector3d originalPosVector = new Vector3d(pos.getX(), pos.getY(), pos.getZ());
        Vector3d finalVector = originalPosVector.subtract(entity.getPositionVec());
        if (finalVector.length() > 1) { //向量长度超过1
            finalVector.normalize(); //化为标准1单位
        }
        double motionX = finalVector.x * modifier;
        double motionY = finalVector.y * modifier;
        double motionZ = finalVector.z * modifier;
        entity.setMotion(motionX, motionY, motionZ);
    }
}
