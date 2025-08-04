package com.yuo.Enchants.Blocks;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public class DeepModOre extends DropExperienceBlock {
	//UniformInt掉落经验范围
	public DeepModOre(MapColor mapColor, SoundType soundType, UniformInt xpRange, float hardness, float resistancelln) {
		super(Properties.of().mapColor(mapColor).requiresCorrectToolForDrops().strength(hardness, resistancelln).sound(soundType), xpRange);
	}
}
