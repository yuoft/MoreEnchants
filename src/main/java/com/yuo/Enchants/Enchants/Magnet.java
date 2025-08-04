package com.yuo.Enchants.Enchants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class Magnet extends ModEnchantBase {

    public Magnet(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 18 + pLevel * 8;
    }

    /**
     * 吸引或排斥实体
     * @param world 世界
     * @param pos 玩家坐标
     * @param xRange 水平距离
     * @param yRange 垂直距离
     */
    public static void moveEntityItemsInRegion(Level world, BlockPos pos, int xRange, int yRange) {
        AABB range = makeBoundingBox(pos, xRange, yRange);
        List<Entity> all = getItemExp(world, range);
        pullEntityList(pos, true, all);
    }

    /**
     * 获取范围中所有物品实体 和 经验实体
     * @param world  世界
     * @param range 范围
     * @return 物品实体列表
     */
    public static List<Entity> getItemExp(Level world, AABB range) {
        List<Entity> all = new ArrayList<>();
        all.addAll(world.getEntitiesOfClass(ItemEntity.class, range));
        all.addAll(world.getEntitiesOfClass(ExperienceOrb.class, range));
        return all;
    }

    /**
     * 返回一个范围
     * @param pos 基点
     * @param xRange 水平范围
     * @param yRange 垂直范围
     * @return 范围
     */
    public static AABB makeBoundingBox(BlockPos pos, int xRange, int yRange) {
        return new AABB(
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
            if (entity instanceof Player && entity.isCrouching()) {
                continue;
            }
            BlockPos p = entity.getOnPos();
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
        Vec3 originalPosVector = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        Vec3 finalVector = originalPosVector.vectorTo(entity.position());
        if (finalVector.length() > 1) { //向量长度超过1
            finalVector.normalize(); //化为标准1单位
        }
        modifier *= -1;
        double motionX = finalVector.x * modifier;
        double motionY = finalVector.y * modifier;
        double motionZ = finalVector.z * modifier;
//        entity.setDeltaMovement(motionX, motionY, motionZ);
        entity.move(MoverType.SELF, new Vec3(motionX, motionY, motionZ));
    }
}
