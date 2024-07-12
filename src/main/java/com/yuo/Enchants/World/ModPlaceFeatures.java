package com.yuo.Enchants.World;

import com.yuo.Enchants.YuoEnchants;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlaceFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, YuoEnchants.MOD_ID);

//    GenerationStep.Decoration
    public static final RegistryObject<PlacedFeature> OVERWORLD_IRON = PLACED_FEATURES.register("overworld_iron_place",
            () -> new PlacedFeature(ModConfiguredFeatures.OVERWORLD_IRON_GEN.getHolder().get(), commonOrePlacement(12,
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(128)))));
    public static final RegistryObject<PlacedFeature> OVERWORLD_GOLD = PLACED_FEATURES.register("overworld_gold_place",
            () -> new PlacedFeature(ModConfiguredFeatures.OVERWORLD_IRON_GEN.getHolder().get(), commonOrePlacement(8,
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(64)))));
    public static final RegistryObject<PlacedFeature> NETHER_IRON = PLACED_FEATURES.register("nether_iron_place",
            () -> new PlacedFeature(ModConfiguredFeatures.OVERWORLD_IRON_GEN.getHolder().get(), commonOrePlacement(12,
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(96)))));
    public static final RegistryObject<PlacedFeature> NETHER_GOLD = PLACED_FEATURES.register("nether_gold_place",
            () -> new PlacedFeature(ModConfiguredFeatures.OVERWORLD_IRON_GEN.getHolder().get(), commonOrePlacement(8,
                    HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(32), VerticalAnchor.aboveBottom(128)))));

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
