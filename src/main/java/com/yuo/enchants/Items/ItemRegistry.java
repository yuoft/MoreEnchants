package com.yuo.enchants.Items;

import com.yuo.enchants.Blocks.BlockRegistry;
import com.yuo.enchants.Items.Tab.ModGroup;
import com.yuo.enchants.MoreEnchants;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreEnchants.MODID);

    public static RegistryObject<Item> rawIron = ITEMS.register("raw_iron", RawOre::new);
    public static RegistryObject<Item> rawGold = ITEMS.register("raw_gold", RawOre::new);
    public static RegistryObject<Item> bambooCoal = ITEMS.register("bamboo_coal", RawOre::new);
    public static RegistryObject<Item> BrokenMagicPearl = ITEMS.register("broken_magic_pearl", BrokenMagicPearl::new);
    public static RegistryObject<Item> BrokenMagicPearlSuper = ITEMS.register("broken_magic_pearl_super", BrokenMagicPearl::new);
    public static RegistryObject<Item> modEnchantBook = ITEMS.register("mod_enchant_book", ModEnchantBook::new);
    public static RegistryObject<Item> oldBook = ITEMS.register("old_book", OldBook::new);
    public static RegistryObject<Item> smallExpDrip = ITEMS.register("small_exp_drip", ExpDrip::new);
    public static RegistryObject<Item> bigExpDrip = ITEMS.register("big_exp_drip", ExpDrip::new);

    //方块
    private static final Item.Properties GROUP = new Item.Properties().group(ModGroup.youEnchants);
    public static RegistryObject<Item> netherIron = ITEMS.register("nether_iron",
            () -> new BlockItem(BlockRegistry.netherIron.get(), GROUP));
    public static RegistryObject<Item> netherGold = ITEMS.register("nether_gold",
            () -> new BlockItem(BlockRegistry.netherGold.get(), GROUP));
    public static RegistryObject<Item> overworldIron = ITEMS.register("overworld_iron",
            () -> new BlockItem(BlockRegistry.overworldIron.get(), GROUP));
    public static RegistryObject<Item> overworldGold = ITEMS.register("overworld_gold",
            () -> new BlockItem(BlockRegistry.overworldGold.get(), GROUP));
    public static RegistryObject<Item> diamondAnvil = ITEMS.register("diamond_anvil",
            () -> new BlockItem(BlockRegistry.diamondAnvil.get(), GROUP));
    public static RegistryObject<Item> chippedDiamondAnvil = ITEMS.register("chipped_diamond_anvil",
            () -> new BlockItem(BlockRegistry.chippedDiamondAnvil.get(), GROUP));
    public static RegistryObject<Item> damagedDiamondAnvil = ITEMS.register("damaged_diamond_anvil",
            () -> new BlockItem(BlockRegistry.damagedDiamondAnvil.get(), GROUP));
}
