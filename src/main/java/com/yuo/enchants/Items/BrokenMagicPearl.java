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
        super(new Properties().maxStackSize(1).group(ModGroup.youEnchants));
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
