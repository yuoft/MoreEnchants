package com.yuo.enchants.Items;

import com.yuo.enchants.Items.Tab.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BrokenMagicPearl extends Item {

    public BrokenMagicPearl() {
        super(new Properties().maxStackSize(1).group(ModGroup.myGroup));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item.equals(ItemRegistry.BrokenMagicPearl)){
            tooltip.add(new TranslationTextComponent("enchantment.text.itemInfo.broken"));
        }
        if (item.equals(ItemRegistry.BrokenMagicPearlSuper)){
            tooltip.add(new TranslationTextComponent("enchantment.text.itemInfo.broken_super"));
        }
    }
}
