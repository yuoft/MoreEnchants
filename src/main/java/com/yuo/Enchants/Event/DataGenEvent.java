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

//    @SubscribeEvent
//    public static void registerAddItemModifier(RegisterEvent event) {
//        event.getForgeRegistry().register();
//        event.getRegistry().registerAll(
////                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_abandoned_mineshaft")), //废弃矿井中的运输矿车
//                new OldBookModifier.setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_bastion_treasure")), //堡垒遗迹宝藏室中的箱子
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_buried_treasure")), //埋藏的宝藏
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_desert_pyramid")), //沙漠神殿的宝藏室里的箱子
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_end_city_treasure")), //末地城里的箱子
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_igloo_chest")), //雪屋地下室里的箱子
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_jungle_temple")), //丛林神庙里的箱子
////                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_nether_bridge")), //下界要塞里的箱子
////                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_shipwreck_treasure")), //沉船的宝箱
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_stronghold_library")), //要塞图书馆里的箱子
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_woodland_mansion")), //林地府邸的箱子
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_fish_treasure")), //钓鱼附魔书
//
//                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_abandoned_mineshaft")),
//                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_bastion_treasure")),
//                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_desert_pyramid")),
//                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_nether_bridge")),
//                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_end_city_treasure")),
//                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_fish_treasure")),
//                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_stronghold_library"))
//        );
//    }

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
