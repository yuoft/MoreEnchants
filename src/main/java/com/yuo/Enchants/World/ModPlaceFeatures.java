package com.yuo.Enchants.World;

import com.yuo.Enchants.YuoEnchants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;

public class ModPlaceFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, YuoEnchants.MOD_ID);

    public static final Holder<PlacedFeature> OVERWORLD_IRON = PlacementUtils.register("overworld_iron_place",
            ModConfiguredFeatures.OVERWORLD_IRON_GEN, commonOrePlacement(12,
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(128))));
    public static final Holder<PlacedFeature> OVERWORLD_GOLD = PlacementUtils.register("overworld_gold_place",
            ModConfiguredFeatures.OVERWORLD_GOLD_GEN, commonOrePlacement(8,
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(64))));
    public static final Holder<PlacedFeature> NETHER_IRON = PlacementUtils.register("nether_iron_place",
            ModConfiguredFeatures.NETHER_OVERWORLD_IRON_GEN, commonOrePlacement(8,
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(96))));
    public static final Holder<PlacedFeature> NETHER_GOLD = PlacementUtils.register("nether_gold_place",
            ModConfiguredFeatures.NETHER_OVERWORLD_GOLD_GEN, commonOrePlacement(12,
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(32), VerticalAnchor.aboveBottom(128))));

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pCount), pHeightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }
}
