package com.yuo.Enchants.Enchants;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;

public class LightningDamage extends ModEnchantBase {

    public LightningDamage(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    //是宝藏吗？
    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    //雷击范围内随机生物
    public static void lighting(Player player, ItemStack stackLegs){
        Level world = player.level;
        if (!world.isRaining() && !world.isThundering()) return;
        AABB axisAlignedBB = player.getBoundingBox().deflate(16); //范围
        List<Entity> toAttack = player.getLevel().getEntities(player, axisAlignedBB);//生物列表
        if (toAttack.isEmpty()) return;
        long dayTime = world.getDayTime();
        if (dayTime % 60 == 0){ //每3秒触发一次
            //随机给予一个生物实体雷击
            Entity entity = toAttack.get(new Random().nextInt(toAttack.size()));
            if (entity instanceof LivingEntity){
                LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(world);
                if (lightningBoltEntity != null){
                    lightningBoltEntity.moveTo(entity.position()); //设置闪电运动路径 才能生成闪电
                    lightningBoltEntity.setCause(entity instanceof ServerPlayer ? (ServerPlayer)entity : null);
                    world.addFreshEntity(lightningBoltEntity);
                }
                stackLegs.hurtAndBreak(1, player, e->e.broadcastBreakEvent(EquipmentSlot.LEGS));
            }
        }
    }
}
