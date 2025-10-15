package com.yuo.Enchants.World;

import com.yuo.Enchants.Blocks.YEBlocks;
import com.yuo.Enchants.RlUtils;
import com.yuo.Enchants.YuoEnchants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class OreFeatures {
    // 创建OreFeature对应的ResourceKey
    //
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON = createKey("ore_iron");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_NETHER = createKey("ore_iron_nether");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD = createKey("ore_gold");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_NETHER = createKey("ore_gold_nether");

    //BootstapContext 是我们datagen的上下文，等会我们使用数据生成的时候说。
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> pContext) {
        //  创建对应的tag，如果有多个就创建多个
        RuleTest stoneOreReplaceRuleTest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepSlateOreReplaceRuleTest = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherOreReplaceRuleTest = new BlockMatchTest(Blocks.NETHERRACK);

        // 创建一个list
        List<TargetBlockState> list0 = List.of(
                OreConfiguration.target(stoneOreReplaceRuleTest, YEBlocks.overworldIron.get().defaultBlockState()),
                OreConfiguration.target(deepSlateOreReplaceRuleTest, YEBlocks.overworldIron.get().defaultBlockState())
        );
        List<TargetBlockState> list1 = List.of(
                OreConfiguration.target(stoneOreReplaceRuleTest, YEBlocks.overworldGold.get().defaultBlockState()),
                OreConfiguration.target(deepSlateOreReplaceRuleTest, YEBlocks.overworldGold.get().defaultBlockState())
        );
        List<TargetBlockState> list00 = List.of(
                OreConfiguration.target(netherOreReplaceRuleTest, YEBlocks.netherIron.get().defaultBlockState())
        );
        List<TargetBlockState> list11 = List.of(
                OreConfiguration.target(netherOreReplaceRuleTest, YEBlocks.netherGold.get().defaultBlockState())
        );
        // 注册对应orefeature，使用listOreConfiguration，9 上文提到的size
        FeatureUtils.register(pContext, ORE_IRON, Feature.ORE, new OreConfiguration(list0, 12));
        FeatureUtils.register(pContext, ORE_IRON_NETHER, Feature.ORE, new OreConfiguration(list00, 8));
        FeatureUtils.register(pContext, ORE_GOLD, Feature.ORE, new OreConfiguration(list1, 8));
        FeatureUtils.register(pContext, ORE_GOLD_NETHER, Feature.ORE, new OreConfiguration(list11, 12));

    }
    // 创建ResourceKey的方法
    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String pName) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, RlUtils.fa(pName));
    }
}
