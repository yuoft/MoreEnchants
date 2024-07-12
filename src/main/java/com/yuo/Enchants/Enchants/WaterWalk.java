package com.yuo.Enchants.Enchants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class WaterWalk extends ModEnchantBase {

    public WaterWalk(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 30;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.FROST_WALKER && ench != EnchantRegistry.lavaWalker.get();
    }

    /**
     * 玩家在液体上行走
     * @param player 玩家
     */
    public static void walk(Player player){
        waterWalk(player, player.level);
    }

    public static void waterWalk(Player player, Level worldIn){
        Entity riding = player.getRootVehicle();
        BlockPos pos = new BlockPos(riding.getX(), Math.ceil(riding.getY()), riding.getZ());
        FluidState fluidState = worldIn.getFluidState(pos.below());
        Vec3 motion = riding.getDeltaMovement();
        if (!fluidState.isEmpty() && fluidState.isSource() && !riding.isCrouching()) {
            if (worldIn.isEmptyBlock(pos) || (riding.isVehicle() && !riding.isInWater() && !riding.isInLava())){
                riding.setDeltaMovement(motion.x, 0, motion.z);
                riding.fallDistance = 0.0F;
                riding.setOnGround(true);
            }
        }
    }
}
