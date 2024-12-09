package com.yuo.Enchants.Items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

//创造模式物品栏 实例化
public class ModTab extends CreativeModeTab {
	public static CreativeModeTab youEnchants = new ModTab();

	public ModTab() {
		super(CreativeModeTab.getGroupCountSafe(), "YuoEnchants");
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(YEItems.modEnchantBook.get());
	}
}
