package com.yuo.Enchants.Event;

import com.yuo.Enchants.Proxy.AAA;
import com.yuo.Enchants.Proxy.ColorBoltRender;
import com.yuo.Enchants.YuoEnchants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * 用户输入/快捷键
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT,  modid = YuoEnchants.MOD_ID, bus = Bus.MOD)
public class ClientEventA {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AAA.COLOR_LIGHT_BOLT.get(), ColorBoltRender::new);
    }
}
