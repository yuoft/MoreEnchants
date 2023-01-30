package com.yuo.enchants.Blocks;

import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

/**
 * 下届矿物
 */
public class NetherOre extends OreBlock {

	public NetherOre(Material material, int harvestLevel, ToolType toolType,float hardness, float resistancelln) {
		super(Properties.create(material).harvestLevel(harvestLevel).harvestTool(toolType)
				.hardnessAndResistance(hardness, resistancelln));
	}
}
