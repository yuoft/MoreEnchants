package com.yuo.enchants.Blocks;

import com.yuo.enchants.YuoEnchants;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegister {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, YuoEnchants.MOD_ID);

    public static final RegistryObject<ContainerType<DiamondRepairContainer>> diamondAnvil = CONTAINER_TYPES.register("diamond_anvil",() ->
            IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) ->
                    new DiamondRepairContainer(windowId, inv)));
}
