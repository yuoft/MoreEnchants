package com.yuo.Enchants;

import com.yuo.Enchants.Blocks.MenuRegister;
import com.yuo.Enchants.Blocks.YEBlocks;
import com.yuo.Enchants.Enchants.EnchantRegistry;
import com.yuo.Enchants.Items.YEItems;
import com.yuo.Enchants.Proxy.AAA;
import com.yuo.Enchants.Proxy.ClientProxy;
import com.yuo.Enchants.Proxy.CommonProxy;
import com.yuo.Enchants.Proxy.IProxy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("yuoenchants")
public class YuoEnchants
{

    public static final String MOD_ID = "yuoenchants";
    public static final IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public YuoEnchants() {
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SERVER_CONFIG); //配置文件
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::commonSetup);
        //注册至mod总线
        YEItems.ITEMS.register(bus);
        YEBlocks.BLOCKS.register(bus);
        EnchantRegistry.ENCHANTMENTS.register(bus);
        MenuRegister.CONTAINER_TYPES.register(bus);
        AAA.TYPES.register(bus);
        proxy.registerHandlers();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
//        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
    }
}
