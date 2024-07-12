package com.yuo.Enchants;

import com.yuo.Enchants.Blocks.MenuRegister;
import com.yuo.Enchants.Blocks.YEBlocks;
import com.yuo.Enchants.Enchants.EnchantRegistry;
import com.yuo.Enchants.Item.YEItems;
import com.yuo.Enchants.NetWork.NetWorkHandler;
import com.yuo.Enchants.Proxy.ClientProxy;
import com.yuo.Enchants.Proxy.CommonProxy;
import com.yuo.Enchants.Proxy.IProxy;
import com.yuo.Enchants.World.ModConfiguredFeatures;
import com.yuo.Enchants.World.ModPlaceFeatures;
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
        ModConfiguredFeatures.CONFIGURED_FEATURES.register(bus); //注册生成规则
        ModPlaceFeatures.PLACED_FEATURES.register(bus); //注册世界生成
//        ModLootModifier.LOOT_MODIFIERS.register(bus);

//        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, OreGen::generateOres); //注册矿物生成
        proxy.registerHandlers();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
    }
}
