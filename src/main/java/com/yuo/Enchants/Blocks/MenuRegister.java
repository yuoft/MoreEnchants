package com.yuo.Enchants.Blocks;

import com.yuo.Enchants.YuoEnchants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegister {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, YuoEnchants.MOD_ID);

    public static final RegistryObject<MenuType<DiamondAnvilMenu>> diamondAnvil = CONTAINER_TYPES.register("diamond_anvil",() ->
            IForgeMenuType.create((int windowId, Inventory inv, FriendlyByteBuf data) ->
                    new DiamondAnvilMenu(windowId, inv)));
}
