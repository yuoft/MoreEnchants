package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;
import java.util.Random;

public class LightningDamage extends ModEnchantBase {

    public LightningDamage(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    //是宝藏吗？
    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    //雷击范围内随机生物
    public static void lighting(PlayerEntity player, ItemStack stackLegs){
        if (!player.world.isRaining() && !player.world.isThundering()) return;
        AxisAlignedBB axisAlignedBB = player.getBoundingBox().grow(16); //范围
        List<Entity> toAttack = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, axisAlignedBB);//生物列表
        if (toAttack.size() < 1) return;
        long dayTime = player.world.getDayTime();
        if (dayTime % 60 == 0){ //每3秒触发一次
            //随机给予一个生物实体雷击
            Entity entity = toAttack.get(new Random().nextInt(toAttack.size()));
            if (entity instanceof LivingEntity){
                LightningBoltEntity lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(player.world);
                lightningBoltEntity.moveForced(Vector3d.copyCenteredHorizontally(entity.getPosition())); //设置闪电运动路径 才能生成闪电
                lightningBoltEntity.setCaster(entity instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity : null);
                player.world.addEntity(lightningBoltEntity);
                stackLegs.damageItem(1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
            }
        }
    }
}
