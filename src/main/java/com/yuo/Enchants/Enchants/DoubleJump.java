package com.yuo.Enchants.Enchants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;

public class DoubleJump extends ModEnchantBase {

    //玩家跳跃计数
    public static int num = 0;

    public DoubleJump(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
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
        if (isJumping(player) && num > 0) {
            jumping(player);
            num--;
        }
        if (num <= 0){ //冷却
            resetJump(player, feet);
        }
    }

    /**
     * 是否可以进行跳跃
     * @param player 玩家
     * @return 可以 true
     */
    public static boolean isJumping(Player player) {
        //在地面 飞行 爬梯子
        if (player.onGround() || player.getAbilities().flying || player.onClimbable())
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
        jumpFromGround(player);
        player.awardStat(Stats.JUMP);
        if (player.isSprinting()) {
            player.causeFoodExhaustion(0.2F);
        } else {
            player.causeFoodExhaustion(0.05F);
        }
        for (int i = 0; i < 10; i++) {
            player.level().addParticle(ParticleTypes.CRIT, player.getX() + player.level().random.nextGaussian(), player.getY(), player.getZ() + player.level().random.nextGaussian(), 0, 0, 0);
        }
    }

    protected static void jumpFromGround(Player player) {
        Vec3 vec3 = player.getDeltaMovement();
        player.setDeltaMovement(vec3.x, getJumpPower(player) * 1.25d, vec3.z);
        if (player.isSprinting()) {
            float f = player.getYRot() * 0.017453292F;
            player.setDeltaMovement(player.getDeltaMovement().add((double)(-Mth.sin(f) * 0.2F), 0.0, (double)(Mth.cos(f) * 0.2F)));
        }

        player.hasImpulse = true;
        ForgeHooks.onLivingJump(player);
    }

    protected static float getJumpPower(Player player) {
        return 0.42F * getBlockJumpFactor(player) + player.getJumpBoostPower();
    }

    protected static float getBlockJumpFactor(Player player) {
        float f = player.level().getBlockState(player.blockPosition()).getBlock().getJumpFactor();
        float f1 = player.level().getBlockState(getOnPos(0.500001F, player)).getBlock().getJumpFactor();
        return (double)f == 1.0 ? f1 : f;
    }

    protected static BlockPos getOnPos(float v, Player player) {
        if (player.mainSupportingBlockPos.isPresent()) {
            BlockPos blockpos = (BlockPos)player.mainSupportingBlockPos.get();
            if (!(v > 1.0E-5F)) {
                return blockpos;
            } else {
                BlockState blockstate = player.level().getBlockState(blockpos);
                return (double)v <= 0.5 && blockstate.collisionExtendsVertically(player.level(), blockpos, player) ? blockpos : blockpos.atY(Mth.floor(player.position().y - (double)v));
            }
        } else {
            int i = Mth.floor(player.position().x);
            int j = Mth.floor(player.position().y - (double)v);
            int k = Mth.floor(player.position().z);
            return new BlockPos(i, j, k);
        }
    }

    /**
     * 重置跳跃次数
     * @param player 玩家
     * @param feet 鞋子
     */
    public static void resetJump(Player player, ItemStack feet){
        BlockPos pos = player.getOnPos();
        BlockState state = player.level().getBlockState(pos);
        if (state.isAir()) return;
        if (player.onGround() || state.liquid()) //不满足跳跃条件时 重置次数
            num = EnchantmentHelper.getItemEnchantmentLevel(YEEnchants.doubleJump.get(), feet);
    }
}
