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

public class ModPearl extends Item {

    public ModPearl() {
        super(new Properties().maxStackSize(2).group(ModGroup.youEnchants));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item == YEItems.BrokenMagicPearl.get()){
            tooltip.add(new TranslationTextComponent("yuoenchants.text.itemInfo.broken"));
        }
        if (item == YEItems.SuperBrokenMagicPearl.get()){
            tooltip.add(new TranslationTextComponent("yuoenchants.text.itemInfo.broken_super"));
        }
        if (item == YEItems.CuresPearl.get()){
            tooltip.add(new TranslationTextComponent("yuoenchants.text.itemInfo.clear"));
        }
        if (item == YEItems.SuperCuresPearl.get()){
            tooltip.add(new TranslationTextComponent("yuoenchants.text.itemInfo.clear_super"));
        }
    }
}
