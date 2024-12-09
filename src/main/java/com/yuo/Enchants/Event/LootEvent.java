package com.yuo.Enchants.Event;

import com.yuo.Enchants.Event.Loot.OldBookModifier;
import com.yuo.Enchants.Event.Loot.SuperBookModifier;
import com.yuo.Enchants.YuoEnchants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = YuoEnchants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootEvent {
    @SubscribeEvent
    public static void registerAddItemModifier(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_abandoned_mineshaft")), //废弃矿井中的运输矿车
                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_bastion_treasure")), //堡垒遗迹宝藏室中的箱子
                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_buried_treasure")), //埋藏的宝藏
                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_desert_pyramid")), //沙漠神殿的宝藏室里的箱子
                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_end_city_treasure")), //末地城里的箱子
                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_igloo_chest")), //雪屋地下室里的箱子
                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_jungle_temple")), //丛林神庙里的箱子
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_nether_bridge")), //下界要塞里的箱子
//                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_shipwreck_treasure")), //沉船的宝箱
                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_stronghold_library")), //要塞图书馆里的箱子
                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_woodland_mansion")), //林地府邸的箱子
                new OldBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "old_book_from_fish_treasure")), //钓鱼附魔书

                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_abandoned_mineshaft")),
                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_bastion_treasure")),
                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_desert_pyramid")),
                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_nether_bridge")),
                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_end_city_treasure")),
                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_fish_treasure")),
                new SuperBookModifier.Serializer().setRegistryName(new ResourceLocation(YuoEnchants.MOD_ID, "super_book_from_stronghold_library"))
        );
    }
}
