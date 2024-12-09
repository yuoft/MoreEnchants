package com.yuo.Enchants.World;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;

public class ModOreGen {
    public static void genOres(BiomeLoadingEvent event){
        List<Holder<PlacedFeature>> features = event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES);
        features.addAll(List.of(ModPlaceFeatures.OVERWORLD_IRON, ModPlaceFeatures.OVERWORLD_GOLD, ModPlaceFeatures.NETHER_IRON,
                ModPlaceFeatures.NETHER_GOLD));
//        features.add(ModPlaceFeatures.OVERWORLD_IRON);
//        features.add(ModPlaceFeatures.OVERWORLD_GOLD);
//        features.add(ModPlaceFeatures.NETHER_IRON);
//        features.add(ModPlaceFeatures.NETHER_GOLD);
    }
}
