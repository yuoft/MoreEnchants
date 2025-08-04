package com.yuo.Enchants.Enchants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

public class Farmer extends ModEnchantBase {

    public Farmer(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 11 + pLevel * 8;
    }

    /**
     * 催熟周围作物并且收获成熟作物
     * @param playerIn 使用玩家
     * @param handIn 活动手
     * @param worldIn 世界
     * @param farmer 附魔等级
     */
    public static void harvestCrop(Player playerIn, InteractionHand handIn, Level worldIn, BlockPos blockPos, int farmer){
        ItemStack heldItem = playerIn.getItemInHand(handIn);
        BlockState blockState = worldIn.getBlockState(blockPos);
        Block clickBlock = blockState.getBlock();
        if (playerIn.getCooldowns().isOnCooldown(heldItem.getItem()) || !(clickBlock instanceof BushBlock)) return; //对作物右键
        if (!worldIn.isClientSide){
            BlockPos minPos = blockPos.offset(-farmer, -1, -farmer);
            BlockPos maxPos = blockPos.offset(farmer, 1, farmer);
            for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                BlockState state = worldIn.getBlockState(pos);
                Block block = state.getBlock();
                //收获 可食用作物
                if (block instanceof CropBlock){ //普通作物
                    if (block instanceof BeetrootBlock ? state.getValue(BeetrootBlock.AGE) >= 3 : state.getValue(CropBlock.AGE) >= 7){
                        //对收获作物进行打包
                        dropCrop(worldIn, state, pos, playerIn);
                        worldIn.setBlock(pos, state.setValue(block instanceof BeetrootBlock ?
                                BeetrootBlock.AGE : CropBlock.AGE, 0), 11); //重新种植
                    }
                }
                if (block instanceof CocoaBlock){ //可可豆
                    if (state.getValue(CocoaBlock.AGE) >= 2){
                        dropCrop(worldIn, state, pos, playerIn);
                        worldIn.setBlock(pos, state.setValue(CocoaBlock.AGE, 0), 11);
                    }
                }
                if (block instanceof StemGrownBlock){ //南瓜
                    dropCrop(worldIn, state, pos, playerIn);
                    worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                }
                if (block instanceof SweetBerryBushBlock){ //浆果
                    if (state.getValue(SweetBerryBushBlock.AGE) >= 3){
                        dropCrop(worldIn, state, pos, playerIn);
                        worldIn.setBlock(pos, state.setValue(SweetBerryBushBlock.AGE, 0), 11);
                    }
                }
                //催熟次数由附魔等级决定
                if (block instanceof BonemealableBlock && !(block instanceof GrassBlock) && ((BonemealableBlock) block).isValidBonemealTarget(worldIn, pos, state, true)){
                    for (int i = 0; i < farmer; i++)
                        ((BonemealableBlock) block).performBonemeal((ServerLevel) worldIn, worldIn.random, pos, state);
                }
            }
            heldItem.hurtAndBreak(farmer, playerIn, e -> e.broadcastBreakEvent(handIn)); //根据附魔等级消耗耐久
            playerIn.getCooldowns().addCooldown(heldItem.getItem(), 20); //冷却
            playerIn.swing(handIn, true); //玩家摆臂
        }
    }

    //生成作物掉落
    public static void dropCrop(Level world, BlockState state, BlockPos pos, Player player){
        for (ItemStack drop : Block.getDrops(state, (ServerLevel) world, pos, null, player, new ItemStack(Items.DIAMOND_HOE))) {
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop));
        }
    }
}
