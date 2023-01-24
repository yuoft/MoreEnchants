package com.yuo.enchants.Enchants;

import net.minecraft.block.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class Farmer extends ModEnchantBase {

    public Farmer(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
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

    /**
     * 催熟周围作物并且收获成熟作物
     * @param playerIn 使用玩家
     * @param handIn 活动手
     * @param worldIn 世界
     * @param farmer 附魔等级
     */
    public static void harvestCrop(PlayerEntity playerIn, Hand handIn, World worldIn, BlockPos blockPos, int farmer){
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if (playerIn.getCooldownTracker().hasCooldown(heldItem.getItem())) return;
        if (!worldIn.isRemote){
            playerIn.swingArm(handIn); //玩家摆臂
            BlockPos minPos = blockPos.add(-farmer, -1, -farmer);
            BlockPos maxPos = blockPos.add(farmer, 1, farmer);
            for (BlockPos pos : BlockPos.getAllInBoxMutable(minPos, maxPos)) {
                BlockState state = worldIn.getBlockState(pos);
                Block block = state.getBlock();
                //收获 可食用作物
                if (block instanceof CropsBlock){ //普通作物
                    if (block instanceof BeetrootBlock ? state.get(BeetrootBlock.BEETROOT_AGE) >= 3 : state.get(CropsBlock.AGE) >= 7){
                        //对收获作物进行打包
                        dropCrop(worldIn, state, pos, playerIn);
                        worldIn.setBlockState(pos, state.with(block instanceof BeetrootBlock ?
                                BeetrootBlock.BEETROOT_AGE:CropsBlock.AGE, 0), 11); //重新种植
                    }
                }
                if (block instanceof CocoaBlock){ //可可豆
                    if (state.get(CocoaBlock.AGE) >= 2){
                        dropCrop(worldIn, state, pos, playerIn);
                        worldIn.setBlockState(pos, state.with(CocoaBlock.AGE, 0), 11);
                    }
                }
                if (block instanceof StemGrownBlock){ //南瓜
                    dropCrop(worldIn, state, pos, playerIn);
                    worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
                }
                if (block instanceof SweetBerryBushBlock){ //浆果
                    if (state.get(SweetBerryBushBlock.AGE) >= 3){
                        dropCrop(worldIn, state, pos, playerIn);
                        worldIn.setBlockState(pos, state.with(SweetBerryBushBlock.AGE, 0), 11);
                    }
                }
                //催熟次数由附魔等级决定
                if (block instanceof IGrowable && !(block instanceof GrassBlock) && ((IGrowable) block).canGrow(worldIn, pos, state, true)){
                    for (int i = 0; i < farmer; i++)
                        ((IGrowable) block).grow((ServerWorld) worldIn, worldIn.rand, pos, state);
                }
            }
            heldItem.damageItem(1, playerIn, e -> e.sendBreakAnimation(handIn));
            playerIn.getCooldownTracker().setCooldown(heldItem.getItem(), 20); //冷却
        }
    }

    //生成作物掉落
    public static void dropCrop(World world, BlockState state, BlockPos pos, PlayerEntity player){
        for (ItemStack drop : Block.getDrops(state, (ServerWorld) world, pos, null, player, new ItemStack(Items.DIAMOND_HOE))) {
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop));
        }
    }
}
