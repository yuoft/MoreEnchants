package com.yuo.enchants;

import com.yuo.enchants.Blocks.BlockRegistry;
import com.yuo.enchants.Enchants.EnchantRegistry;
import com.yuo.enchants.Event.KeyBindingEvent;
import com.yuo.enchants.Items.ItemRegistry;
import com.yuo.enchants.world.OreGen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("moreenchants")
public class MoreEnchants {
	public static final String MODID = "moreenchants";
	public MoreEnchants() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		//注册至mod总线
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        EnchantRegistry.ENCHANTMENTS.register(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, OreGen::generateOres); //注册矿物生成
        ClientRegistry.registerKeyBinding(KeyBindingEvent.MESSAGE_KEY); //快捷键注册
    }
}
