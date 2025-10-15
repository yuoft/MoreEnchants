package com.yuo.Enchants;

import net.minecraft.resources.ResourceLocation;

public class RlUtils {

    public static ResourceLocation fa(String path){
        return ResourceLocation.fromNamespaceAndPath(YuoEnchants.MOD_ID, path);
    }

    public static ResourceLocation wdn(String path){
        return ResourceLocation.withDefaultNamespace(path);
    }

    public static ResourceLocation tryParse(String path){
        return ResourceLocation.tryParse(path);
    }
}
