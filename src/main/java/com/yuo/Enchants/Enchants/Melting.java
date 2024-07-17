package com.yuo.Enchants.Enchants;

import com.yuo.Enchants.Config;
import com.yuo.Enchants.Event.EventHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Random;

public class Melting extends ModEnchantBase {

    public Melting(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinCost(int pLevel) {
        return 25;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.SILK_TOUCH;//精准采集
    }

    //熔炼方块掉落
    public static void melting(Block block, BlockState state, Level world, BlockPos pos, Player player, ItemStack tool, BlockEvent.BreakEvent event){
        if (!block.canHarvestBlock(state, world, pos, player) || block instanceof CropBlock) return;
        List<ItemStack> drops = Block.getDrops(state, (ServerLevel) world, pos, null);
        int unLuck = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.unLuck.get(), tool);
        //霉运影响
        boolean flag = unLuck > 0 && Config.SERVER.isUnLuck.get() &&  world.random.nextDouble() < unLuck * 0.2; //霉运判断结果 true触发
        if (drops.size() <= 0 || flag) return;
        drops.forEach(itemStack -> {
            ItemStack dropStack = Melting.getMeltingItem(world, itemStack, tool);
            if (!dropStack.equals(itemStack)){
                EventHelper.meltingAchieve(world, player, pos, event);
                world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dropStack));
//                    event.setCanceled(true);
            }
        });
    }

    /**
     * 获取物品烧炼后产物
     * @param world world
     * @param itemStack 烧炼前物品
     * @param tool 使用工具
     * @return 烧炼产物
     */
    public static ItemStack getMeltingItem(Level world,ItemStack itemStack, ItemStack tool){
        ItemStack dropStack = world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemStack), world)
                .map(SmeltingRecipe::getResultItem).filter(e -> !e.isEmpty())
                .map(e -> ItemHandlerHelper.copyStackWithSize(e, e.getCount()))
                .orElse(itemStack);
        int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
        if (fortune > 0){ //时运影响产物数量
            Random random = new Random();
            int count = 1;
            if (random.nextDouble() < 0.3 + fortune * 0.1)
                count += random.nextInt(0, fortune + 1);
            if (random.nextDouble() < 0.1 + fortune * 0.05){ //触发暴击
                count *= random.nextInt(1, fortune);
            }
            dropStack.setCount(count);
        }
        return dropStack;
    }
}
