package com.yuo.Enchants.Item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ExpDrip extends Item {

    public ExpDrip() {
        super(new Properties().tab(ModTab.youEnchants));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        Item item = pStack.getItem();
        if (item == YEItems.smallExpDrip.get()){
            pTooltipComponents.add(new TranslatableComponent("yuoenchants.text.itemInfo.small_exp_drip"));
        }
        if (item == YEItems.bigExpDrip.get()){
            pTooltipComponents.add(new TranslatableComponent("yuoenchants.text.itemInfo.big_exp_drip"));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack heldItem = pPlayer.getItemInHand(pUsedHand);
        int expValue = heldItem.getItem() == YEItems.smallExpDrip.get() ? 10 : 100;
        if (pPlayer.isCrouching()){
            int count = heldItem.getCount();
            pPlayer.giveExperiencePoints(count * expValue);
            heldItem.shrink(count);
        }else {
            pPlayer.giveExperiencePoints(expValue);
            heldItem.shrink(1);
        }
        return InteractionResultHolder.consume(heldItem);
    }
}
