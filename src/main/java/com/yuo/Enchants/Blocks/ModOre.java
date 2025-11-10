package com.yuo.Enchants.Blocks;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams.Builder;

import java.util.List;

public class ModOre extends DropExperienceBlock {
	//UniformInt掉落经验范围
	public ModOre(MapColor mapColor, SoundType deepslate, UniformInt xpRange, float hardness, float resistancelln) {
		super(Properties.of().mapColor(mapColor).requiresCorrectToolForDrops().strength(hardness, resistancelln), xpRange);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);
		for (ItemStack drop : drops) {
			if (this == YEBlocks.netherIron.get() || this == YEBlocks.netherGold.get())
				drop.setCount(drop.getCount() * 3);
			else drop.setCount(drop.getCount() * 2);
		}

		return drops;
	}
}
