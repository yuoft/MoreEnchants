package com.yuo.Enchants.World;

import com.yuo.Enchants.Blocks.YEBlocks;
import com.yuo.Enchants.YuoEnchants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;

public class ModConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?,?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, YuoEnchants.MOD_ID);

    //主世界矿物生成规则
    public static final List<OreConfiguration.TargetBlockState> OVERWORLD_IRON_ORES =
            //------------------------------生成替换规则(替换石头) ------要生成的矿物
            List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, YEBlocks.overworldIron.get().defaultBlockState()),
                    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,YEBlocks.deepOverworldIron.get().defaultBlockState()) //深板岩
            );
    public static final List<OreConfiguration.TargetBlockState> OVERWORLD_GOLD_ORES =
            List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, YEBlocks.overworldGold.get().defaultBlockState()),
                    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES,YEBlocks.deepOverworldGold.get().defaultBlockState())
            );

    public static final List<OreConfiguration.TargetBlockState> NETHER_IRON_ORES =
            List.of(OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, YEBlocks.netherIron.get().defaultBlockState())
            );
    public static final List<OreConfiguration.TargetBlockState> NETHER_GOLD_ORES =
            List.of(OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES,YEBlocks.netherGold.get().defaultBlockState())
            );

    //注册生成规则
    public static final Holder<ConfiguredFeature<OreConfiguration,?>> OVERWORLD_IRON_GEN = FeatureUtils.register("overworld_iron_gen",
            Feature.ORE, new OreConfiguration(OVERWORLD_IRON_ORES, 12 )); //生成数量
    public static final Holder<ConfiguredFeature<OreConfiguration,?>> NETHER_OVERWORLD_IRON_GEN = FeatureUtils.register("nether_overworld_iron_gen",
            Feature.ORE, new OreConfiguration(NETHER_IRON_ORES, 12 ));
    public static final Holder<ConfiguredFeature<OreConfiguration,?>> OVERWORLD_GOLD_GEN = FeatureUtils.register("overworld_gold_gen",
            Feature.ORE, new OreConfiguration(OVERWORLD_GOLD_ORES, 8 )); //生成数量
    public static final Holder<ConfiguredFeature<OreConfiguration,?>> NETHER_OVERWORLD_GOLD_GEN = FeatureUtils.register("nether_overworld_gold_gen",
            Feature.ORE, new OreConfiguration(NETHER_GOLD_ORES, 8 ));

}
