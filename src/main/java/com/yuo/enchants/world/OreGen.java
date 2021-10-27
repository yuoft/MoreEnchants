package com.yuo.enchants.world;

import com.yuo.enchants.Blocks.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 矿物生成
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OreGen {
	private static int topOffset = 0;

	public static void generateOres(final BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder generation = event.getGeneration();
		//主世界
		if (!(event.getCategory().equals(Biome.Category.THEEND) || event.getCategory().equals(Biome.Category.NETHER))){
			addFeatureOverWorld(generation, BlockRegistry.overworldIron.get().getDefaultState(),
					12, 0, 128, 10);
			addFeatureOverWorld(generation, BlockRegistry.overworldGold.get().getDefaultState(),
					8, 0, 128, 8);
		}
		//下届
		if (event.getCategory().equals(Biome.Category.NETHER)){
			addFeatureNether(generation, BlockRegistry.netherIron.get().getDefaultState(),
					12, 30, 128, 30);
			addFeatureNether(generation, BlockRegistry.netherGold.get().getDefaultState(),
					8, 0, 96, 25);
		}
		//末地
		if (event.getCategory().equals(Biome.Category.THEEND)){
		}
	}

	/**
	 * 主世界矿物生成规则
	 */
	private static void addFeatureOverWorld(BiomeGenerationSettingsBuilder builder, BlockState state, int maxSize,
											int minHeight, int maxHeight, int genCount){
		builder.withFeature(Decoration.UNDERGROUND_ORES,
				Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, state, maxSize)) // 替换方块, 生成矿物， 最大生成数量
						.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, topOffset , maxHeight))) //最低高度, 0,最高高度
						.square().func_242731_b(genCount)); //生成次数
	}
	/**
	 * 下届矿物生成规则
	 */
	private static void addFeatureNether(BiomeGenerationSettingsBuilder builder, BlockState state, int maxSize,
										 int minHeight, int maxHeight, int genCount){
		builder.withFeature(Decoration.UNDERGROUND_DECORATION,
				Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, state, maxSize))
						.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, topOffset , maxHeight)))
						.square().func_242731_b(genCount));
	}
	/**
	 * 末地矿物生成规则
	 */
	private static void addFeatureTheend(BiomeGenerationSettingsBuilder builder, BlockState state, int maxSize,
										 int minHeight, int maxHeight, int genCount){
		builder.withFeature(Decoration.UNDERGROUND_STRUCTURES,
				Feature.ORE.withConfiguration(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.END_STONE), state, maxSize))
						.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, topOffset , maxHeight)))
						.square().func_242731_b(genCount));
	}
}
