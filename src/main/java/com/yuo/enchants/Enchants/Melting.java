package com.yuo.enchants.Enchants;

import com.yuo.enchants.Event.EventHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Random;

public class Melting extends ModEnchantBase {

    public Melting(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.SILK_TOUCH;//精准采集
    }

    //熔炼方块掉落
    public static void melting(Block block, BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack tool, BlockEvent.BreakEvent event){
        if (!block.canHarvestBlock(state, world, pos, player)) return;
        List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);
        if (drops.size() <= 0) return;
        drops.forEach(itemStack -> {
            ItemStack dropStack = Melting.getMeltingItem(world, itemStack, tool);
            if (!dropStack.equals(itemStack)){
                EventHelper.meltingAchieve(world, player, pos, event);
                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dropStack));
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
    public static ItemStack getMeltingItem(World world,ItemStack itemStack, ItemStack tool){
        ItemStack dropStack = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(itemStack), world)
                .map(FurnaceRecipe::getRecipeOutput).filter(e -> !e.isEmpty())
                .map(e -> ItemHandlerHelper.copyStackWithSize(e, tool.getCount() * e.getCount()))
                .orElse(itemStack);
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
        if (fortune > 0){ //时运影响产物数量
            Random random = new Random();
            int count = 1;
            if (random.nextDouble() < 0.3 + fortune * 0.1)
                count += MathHelper.nextInt(random, 0, fortune + 1);
            if (random.nextDouble() < 0.1 + fortune * 0.05){ //触发暴击
                count *= MathHelper.nextInt(random, 1, fortune);
            }
            dropStack.setCount(count);
        }
        return dropStack;
    }
}
