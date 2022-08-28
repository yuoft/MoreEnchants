package com.yuo.enchants.Items;

import com.yuo.enchants.Items.Tab.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ExpDrip extends Item {

    public ExpDrip() {
        super(new Properties().group(ModGroup.youEnchants));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item == ItemRegistry.smallExpDrip.get()){
            tooltip.add(new TranslationTextComponent("yuoenchants.text.itemInfo.small_exp_drip"));
        }
        if (item == ItemRegistry.bigExpDrip.get()){
            tooltip.add(new TranslationTextComponent("yuoenchants.text.itemInfo.big_exp_drip"));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        int expValue = heldItem.getItem() == ItemRegistry.smallExpDrip.get() ? 10 : 100;
        if (playerIn.isSneaking()){
            int count = heldItem.getCount();
            playerIn.giveExperiencePoints(count * expValue);
            heldItem.shrink(count);
        }else {
            playerIn.giveExperiencePoints(expValue);
            heldItem.shrink(1);
        }
        return ActionResult.resultSuccess(heldItem);
    }
}
