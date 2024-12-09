package com.yuo.Enchants.Blocks;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.material.Material;

public class ModOre extends OreBlock {
	//UniformInt掉落经验范围
	public ModOre(Material material, UniformInt xpRange, float hardness, float resistancelln) {
		super(Properties.of(material).requiresCorrectToolForDrops().strength(hardness, resistancelln), xpRange);
	}

}
