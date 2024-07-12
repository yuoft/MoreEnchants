package com.yuo.Enchants.Proxy;

import com.yuo.Enchants.Blocks.DiamondAnvilScreen;
import com.yuo.Enchants.Blocks.MenuRegister;
import com.yuo.Enchants.Event.KeyBindingEvent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * 客户端属性注册
 */
public class ClientProxy implements IProxy {

    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {
        //绑定Container和ContainerScreen
        event.enqueueWork(() -> {
            MenuScreens.register(MenuRegister.diamondAnvil.get(), DiamondAnvilScreen::new);
        });
        ClientRegistry.registerKeyBinding(KeyBindingEvent.ENCHANT_KEY_C); //快捷键注册
    }


    @Override
    public void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::clientSetup);
    }

}
