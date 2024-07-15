package com.yuo.Enchants.Enchants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;

public class DoubleJump extends ModEnchantBase {

    //玩家跳跃计数
    public static int num = 0;

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

    /**
     * 二段跳
     * @param player 玩家
     */
    public static void jump(Player player) {
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
        resetJump(player, feet);
        if (isJumping(player) && num > 0) {
            jumping(player);
            num--;
        }
        if (num <= 0){ //冷却
            player.getCooldowns().addCooldown(feet.getItem(), 40);
        }
    }

    /**
     * 是否可以进行跳跃
     * @param player 玩家
     * @return 可以 true
     */
    public static boolean isJumping(Player player) {
        //在地面 飞行 爬梯子
        if (player.isOnGround() || player.getAbilities().flying || player.onClimbable())
            return false;
        if (player.getRootVehicle() instanceof Boat || player.isCrouching()) return false;
        return !(player.getOnPos().getY() >= player.yOld) && !player.isInWater() && !player.isInLava();
    }

    /**
     * 跳跃
     * @param player 玩家
     */
    public static void jumping(Player player){
        player.resetFallDistance();
        player.jumpFromGround();
        for (int i = 0; i < 10; i++) {
            player.level.addParticle(ParticleTypes.CRIT, player.getX() + player.level.random.nextGaussian(), player.getY(), player.getZ() + player.level.random.nextGaussian(), 0, 0, 0);
        }
    }

    /**
     * 重置跳跃次数
     * @param player 玩家
     * @param feet 鞋子
     */
    public static void resetJump(Player player, ItemStack feet){
        BlockPos pos = player.getOnPos();
        BlockState state = player.level.getBlockState(pos);
        if (!isJumping(player) || state.getMaterial().isLiquid()) //不满足跳跃条件时 重置次数
            num = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.doubleJump.get(), feet);
    }
}
