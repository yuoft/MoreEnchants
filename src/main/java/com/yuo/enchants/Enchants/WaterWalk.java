package com.yuo.enchants.Enchants;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WaterWalk extends ModEnchantBase {

    public WaterWalk(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 30;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.FROST_WALKER && ench != EnchantRegistry.lavaWalker.get();
    }

    /**
     * 玩家在液体上行走
     * @param player 玩家
     */
    public static void walk(PlayerEntity player){
        waterWalk(player, player.world);
    }

    public static void waterWalk(PlayerEntity player, World worldIn){
        Entity riding = player.getRidingEntity();
        if (riding == null){
            BlockPos pos = new BlockPos(player.getPosX(), Math.ceil(player.getPosY()), player.getPosZ());
            FluidState fluidState = worldIn.getFluidState(pos.down());
            Vector3d motion = player.getMotion();
            if (!fluidState.isEmpty() && fluidState.isSource() && !player.isSneaking()) {
                if (worldIn.isAirBlock(pos) || (player.isAirBorne && !player.isInWater() && !player.isInLava() && !player.abilities.isFlying)){
                    player.setMotion(motion.x, 0, motion.z);
                    player.fallDistance = 0.0F;
                    player.setOnGround(true);
                }
            }
        }else {
            BlockPos pos = new BlockPos(riding.getPosX(), Math.ceil(riding.getPosY()), riding.getPosZ());
            FluidState fluidState = worldIn.getFluidState(pos.down());
            Vector3d motion = riding.getMotion();
            if (!fluidState.isEmpty() && fluidState.isSource() && !riding.isSneaking()) {
                if (worldIn.isAirBlock(pos) || (riding.isAirBorne && !riding.isInWater() && !riding.isInLava())){
                    riding.setMotion(motion.x, 0, motion.z);
                    riding.fallDistance = 0.0F;
                    riding.setOnGround(true);
                }
            }
        }
    }
}
