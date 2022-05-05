package com.yuo.enchants;

import com.yuo.enchants.Blocks.BlockRegistry;
import com.yuo.enchants.Blocks.ContainerRegister;
import com.yuo.enchants.Blocks.DiamondAnvilScreen;
import com.yuo.enchants.Enchants.EnchantRegistry;
import com.yuo.enchants.Event.KeyBindingEvent;
import com.yuo.enchants.Items.ItemRegistry;
import com.yuo.enchants.world.OreGen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("yuoenchants")
public class MoreEnchants {
	public static final String MODID = "yuoenchants";
	public MoreEnchants() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);
		//注册至mod总线
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        EnchantRegistry.ENCHANTMENTS.register(modEventBus);
        ContainerRegister.CONTAINER_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, OreGen::generateOres); //注册矿物生成
        ClientRegistry.registerKeyBinding(KeyBindingEvent.ENCHANT_KEY_C); //快捷键注册
    }

    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {
        //绑定Container和ContainerScreen
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(ContainerRegister.diamondAnvil.get(), DiamondAnvilScreen::new);
        });
    }

}
