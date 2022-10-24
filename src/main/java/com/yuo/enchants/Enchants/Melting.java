package com.yuo.enchants.Enchants;

import com.yuo.enchants.Event.EventHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class Melting extends ModEnchantBase {

    public Melting(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.SILK_TOUCH;//精准采集
    }

    //熔炼方块掉落
    public static void melting(Block block, BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack, BlockEvent.BreakEvent event){
        if (!block.canHarvestBlock(state, world, pos, player)) return;
        List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);
        if (drops.size() <= 0) return;
        drops.forEach(itemStack -> {
            //获取物品烧炼后产物
            ItemStack dropStack = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(itemStack), world)
                    .map(FurnaceRecipe::getRecipeOutput).filter(e -> !e.isEmpty())
                    .map(e -> ItemHandlerHelper.copyStackWithSize(e, stack.getCount() * e.getCount()))
                    .orElse(itemStack);
            if (!dropStack.equals(itemStack)){
                EventHelper.meltingAchieve(world, player, pos, event);
                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dropStack));
//                    event.setCanceled(true);
            }
        });
    }
}
