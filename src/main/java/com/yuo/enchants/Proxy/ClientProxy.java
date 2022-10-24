package com.yuo.enchants.Proxy;

import com.yuo.enchants.Blocks.ContainerRegister;
import com.yuo.enchants.Blocks.DiamondAnvilScreen;
import net.minecraft.client.gui.ScreenManager;
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
            ScreenManager.registerFactory(ContainerRegister.diamondAnvil.get(), DiamondAnvilScreen::new);
        });
    }


    @Override
    public void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::clientSetup);
    }

}
