package com.yuo.Enchants.Event;

import com.mojang.serialization.Codec;
import com.yuo.Enchants.Event.Loot.GlobalLootModifier;
import com.yuo.Enchants.Event.Loot.OldBookModifier;
import com.yuo.Enchants.Event.Loot.SuperBookModifier;
import com.yuo.Enchants.World.ModWorldGen;
import com.yuo.Enchants.YuoEnchants;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider.Factory;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = YuoEnchants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenEvent {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> CODEC_DEFERRED_REGISTERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, YuoEnchants.MOD_ID);

    public static final RegistryObject<Codec<OldBookModifier>> oldBookModifier = CODEC_DEFERRED_REGISTERS.register("chest_oldbook", OldBookModifier.CODEC);
    public static final RegistryObject<Codec<SuperBookModifier>> superBookModifier = CODEC_DEFERRED_REGISTERS.register("chest_superbook", SuperBookModifier.CODEC);

    @SubscribeEvent
    public static void addLoot(GatherDataEvent event){
        boolean b = event.includeServer();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(b, new GlobalLootModifier(output, YuoEnchants.MOD_ID));
        generator.addProvider(b, (Factory<ModWorldGen>) e -> new ModWorldGen(output, lookupProvider));
    }
}
