package com.yuo.Enchants.Enchants;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;

public class DoubleJump extends ModEnchantBase {

    //根据附魔等级控制玩家跳跃次数
    public static String USES = "jumpNum";

    public DoubleJump(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 22 + pLevel * 7;
    }

    //跳跃控制
    public static void jump(Player player, int level) {
        if (player.getRootVehicle() instanceof Boat || player.isCrouching())
            return; //坐船 或 潜行不能跳
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
        if (player.getCooldowns().isOnCooldown(feet.getItem())) return; //鞋子冷却时不能跳
        int uses = feet.getOrCreateTag().getInt(USES);
        if (!player.isOnGround() && !player.getAbilities().flying) { //不在地面 未飞行
            if (Screen.isPaste(32) //按下跳跃键 未在最高高度 在空中 不在水里 不在岩浆里
                    && player.isFallFlying() && !player.isInWater() && !player.isInLava()) {
                player.fallDistance = 0;
//                float angle = (player.getMotion().x == 0 && player.getMotion().z == 0) ? 90 : 70;
//                launch(player, angle, player.rotationYaw, 1.05f);
                player.jumpFromGround();
                for (int i = 0; i < 10; i++) {
                    player.level.addParticle(ParticleTypes.CRIT, player.getX() + player.level.random.nextGaussian(),
                            player.getY(), player.getZ() + player.level.random.nextGaussian(), 0, 0, 0);
                }
                uses++;
                if (uses >= level) {
                    player.getCooldowns().addCooldown(feet.getItem(), 40);
                    uses = 0;
                }
                feet.getOrCreateTag().putInt(USES, uses);
            }
        }
    }


    /**
     * 玩家跳跃
     *
     * @param player        玩家
     * @param rotationPitch p
     * @param rotationYaw   y
     * @param power         高度
     */
    public static void launch(Player player, float rotationPitch, float rotationYaw, float power) {
        float mountPower = (float) (power + 0.5);

        //获取跳跃方向
        double velX = -Math.sin(rotationYaw / 180.0F * (float) Math.PI) * Math.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
        double velZ = Math.cos(rotationYaw / 180.0F * (float) Math.PI) * Math.cos(rotationPitch / 180.0F * (float) Math.PI) * power;
        double velY = -Math.sin((rotationPitch) / 180.0F * (float) Math.PI) * power;
        if (velY < 0) {
            velY *= -1;
        }
        Entity ridingEntity = player.getRootVehicle();
        player.setDeltaMovement(player.getDeltaMovement().x, mountPower, player.getDeltaMovement().z);
        ridingEntity.fallDistance = 0;
        ridingEntity.setDeltaMovement(velX * mountPower, velY * mountPower, velZ * mountPower);
    }
}
