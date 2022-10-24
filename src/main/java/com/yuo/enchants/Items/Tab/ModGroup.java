package com.yuo.enchants.Items.Tab;

import com.yuo.enchants.Items.YEItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

//创造模式物品栏 实例化
public class ModGroup extends ItemGroup{
	public static ItemGroup youEnchants = new ModGroup();

	public ModGroup() {
		super(ItemGroup.GROUPS.length, "YuoEnchants");
	}
	//图标
	@Override
	public ItemStack createIcon() {
		return new ItemStack(YEItems.rawIron.get());
	}

}
