package com.yuo.Enchants.Loot;

import com.yuo.Enchants.YuoEnchants;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifier {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS.get(), YuoEnchants.MOD_ID);

    public static final RegistryObject<GlobalLootModifierSerializer<OldBookFromBoxAddModifier>> AAA = LOOT_MODIFIERS.register("aaa", () ->
            new OldBookFromBoxAddModifier.Serializer().setRegistryName("aaa"));

}
