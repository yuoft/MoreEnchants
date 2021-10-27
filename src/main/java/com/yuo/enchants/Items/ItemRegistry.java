package com.yuo.enchants.Items;

import com.yuo.enchants.Blocks.BlockRegistry;
import com.yuo.enchants.Items.Tab.ModGroup;
import com.yuo.enchants.MoreEnchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreEnchants.MODID);

    public static RegistryObject<Item> rawIron = ITEMS.register("raw_iron", () ->{
        return new RawOre();
    });
    public static RegistryObject<Item> rawGold = ITEMS.register("raw_gold", () ->{
        return new RawOre();
    });
    public static RegistryObject<Item> bambooCoal = ITEMS.register("bamboo_coal", () ->{
        return new RawOre();
    });
    public static RegistryObject<Item> BrokenMagicPearl = ITEMS.register("broken_magic_pearl", () ->{
        return new BrokenMagicPearl();
    });
    public static RegistryObject<Item> BrokenMagicPearlSuper = ITEMS.register("broken_magic_pearl_super", () ->{
        return new BrokenMagicPearl();
    });

    //方块
    private static Item.Properties GROUP = new Item.Properties().group(ModGroup.myGroup);
    public static RegistryObject<Item> netherIron = ITEMS.register("nether_iron", () ->{
        return new BlockItem(BlockRegistry.netherIron.get(), GROUP);
    });
    public static RegistryObject<Item> netherGold = ITEMS.register("nether_gold", () ->{
        return new BlockItem(BlockRegistry.netherGold.get(), GROUP);
    });
    public static RegistryObject<Item> overworldIron = ITEMS.register("overworld_iron", () ->{
        return new BlockItem(BlockRegistry.overworldIron.get(), GROUP);
    });
    public static RegistryObject<Item> overworldGold = ITEMS.register("overworld_gold", () ->{
        return new BlockItem(BlockRegistry.overworldGold.get(), GROUP);
    });
}
