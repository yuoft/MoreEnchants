package com.yuo.enchants;

import com.yuo.enchants.Blocks.ContainerRegister;
import com.yuo.enchants.Blocks.YEBlocks;
import com.yuo.enchants.Enchants.EnchantRegistry;
import com.yuo.enchants.Items.YEItems;
import com.yuo.enchants.NetWork.NetWorkHandler;
import com.yuo.enchants.Proxy.ClientProxy;
import com.yuo.enchants.Proxy.CommonProxy;
import com.yuo.enchants.Proxy.IProxy;
import com.yuo.enchants.world.OreGen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("yuoenchants")
public class YuoEnchants {
	public static final String MOD_ID = "yuoenchants";
    public static final IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public YuoEnchants() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
		//注册至mod总线
        YEItems.ITEMS.register(modEventBus);
        YEBlocks.BLOCKS.register(modEventBus);
        EnchantRegistry.ENCHANTMENTS.register(modEventBus);
        ContainerRegister.CONTAINER_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, OreGen::generateOres); //注册矿物生成
        proxy.registerHandlers();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
    }
}
