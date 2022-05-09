package com.yuo.enchants.Items;

import com.yuo.enchants.Enchants.WaterWalk;
import com.yuo.enchants.Items.Tab.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class BrokenMagicPearl extends Item {

    public BrokenMagicPearl() {
        super(new Properties().maxStackSize(1).group(ModGroup.myGroup));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item.equals(ItemRegistry.BrokenMagicPearl)){
            tooltip.add(new TranslationTextComponent("yuoenchants.text.itemInfo.broken"));
        }
        if (item.equals(ItemRegistry.BrokenMagicPearlSuper)){
            tooltip.add(new TranslationTextComponent("yuoenchants.text.itemInfo.broken_super"));
        }
    }

//    @Override
//    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
//        if (itemSlot >= PlayerInventory.getHotbarSize() || !(entityIn instanceof PlayerEntity)) return; //快捷栏
//        PlayerEntity player = (PlayerEntity) entityIn;
//        WaterWalk.waterWalk(player, worldIn);
//    }
}
