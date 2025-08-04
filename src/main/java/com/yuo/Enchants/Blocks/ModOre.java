package com.yuo.Enchants.Blocks;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.material.MapColor;

public class ModOre extends DropExperienceBlock {
	//UniformInt掉落经验范围
	public ModOre(MapColor mapColor, UniformInt xpRange, float hardness, float resistancelln) {
		super(Properties.of().mapColor(mapColor).requiresCorrectToolForDrops().strength(hardness, resistancelln), xpRange);
	}

}
