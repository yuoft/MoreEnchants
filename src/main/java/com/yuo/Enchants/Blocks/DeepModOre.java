package com.yuo.Enchants.Blocks;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class DeepModOre extends OreBlock {
	//UniformInt掉落经验范围
	public DeepModOre(Material material, MaterialColor color, SoundType soundType, UniformInt xpRange, float hardness, float resistancelln) {
		super(Properties.of(material).color(color).requiresCorrectToolForDrops().strength(hardness, resistancelln).sound(soundType), xpRange);
	}
}
