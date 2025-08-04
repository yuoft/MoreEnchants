package com.yuo.Enchants.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ModPearl extends Item {

    public ModPearl() {
        super(new Properties().stacksTo(2));
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item == YEItems.BrokenMagicPearl.get()){
            tooltip.add(Component.translatable("yuoenchants.text.itemInfo.broken"));
        }
        if (item == YEItems.SuperBrokenMagicPearl.get()){
            tooltip.add(Component.translatable("yuoenchants.text.itemInfo.broken_super"));
        }
        if (item == YEItems.CuresPearl.get()){
            tooltip.add(Component.translatable("yuoenchants.text.itemInfo.clear"));
        }
        if (item == YEItems.SuperCuresPearl.get()){
            tooltip.add(Component.translatable("yuoenchants.text.itemInfo.clear_super"));
        }
    }
}
