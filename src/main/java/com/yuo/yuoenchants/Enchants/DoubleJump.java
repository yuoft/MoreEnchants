package com.yuo.yuoenchants.Enchants;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class DoubleJump extends ModEnchantBase {

    //根据附魔等级控制玩家跳跃次数
    public static String USES = "jumpNum";

    public DoubleJump(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    //跳跃控制
    public static void jump(PlayerEntity player, int level){
        if (player.getRidingEntity() instanceof BoatEntity || player.isCrouching() || player.isSneaking()) return; //坐船 或 潜行不能跳
        ItemStack feet = player.getItemStackFromSlot(EquipmentSlotType.FEET);
        if (player.getCooldownTracker().hasCooldown(feet.getItem())) return; //鞋子冷却时不能跳
        int uses = feet.getOrCreateTag().getInt(USES);
        if (!player.isOnGround() && !player.abilities.isFlying){ //不在地面 未飞行
            if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown() //按下跳跃键 未在最高高度 在空中 不在水里 不在岩浆里
                && player.getPosY() < player.lastTickPosY && player.isAirBorne && !player.isInWater() && !player.isInLava()) {
                player.fallDistance = 0;
//                float angle = (player.getMotion().x == 0 && player.getMotion().z == 0) ? 90 : 70;
//                launch(player, angle, player.rotationYaw, 1.05f);
                player.jump();
                for (int i = 0; i < 10; i++){
                    player.world.addParticle(ParticleTypes.CRIT, player.getPosX() + player.world.rand.nextGaussian(),
                            player.getPosY(), player.getPosZ() + player.world.rand.nextGaussian(), 0, 0, 0);
                }
                uses++;
                if (uses >= level){
                    player.getCooldownTracker().setCooldown(feet.getItem(), 40);
                    uses = 0;
                }
                feet.getOrCreateTag().putInt(USES, uses);
            }
        }
    }



    /**
     * 玩家跳跃
     * @param player 玩家
     * @param rotationPitch  p
     * @param rotationYaw y
     * @param power 高度
     */
    public static void launch(PlayerEntity player, float rotationPitch, float rotationYaw, float power) {
        float mountPower = (float) (power + 0.5);
        //获取跳跃方向
        double velX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
        double velZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
        double velY = -MathHelper.sin((rotationPitch) / 180.0F * (float) Math.PI) * power;
        if (velY < 0) {
            velY *= -1;
        }
        Entity ridingEntity = player.getRidingEntity();
        if (ridingEntity != null) {
            player.setMotion(player.getMotion().x, mountPower, player.getMotion().z);
            ridingEntity.fallDistance = 0;
            ridingEntity.addVelocity(velX * mountPower, velY * mountPower, velZ * mountPower);
        }
        else {
            player.setMotion(player.getMotion().x, mountPower, player.getMotion().z);
            player.fallDistance = 0;
            player.addVelocity(velX, velY, velZ);
        }
    }
}
