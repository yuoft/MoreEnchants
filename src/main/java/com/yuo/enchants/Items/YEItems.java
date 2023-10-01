package com.yuo.enchants.Items;

import com.yuo.enchants.Blocks.YEBlocks;
import com.yuo.enchants.Items.Tab.ModGroup;
import com.yuo.enchants.YuoEnchants;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class YEItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, YuoEnchants.MOD_ID);

    public static RegistryObject<Item> bambooCoal = ITEMS.register("bamboo_coal",
            () -> new Item(new Item.Properties().group(ModGroup.youEnchants)));
    public static RegistryObject<Item> BrokenMagicPearl = ITEMS.register("broken_magic_pearl", ModPearl::new);
    public static RegistryObject<Item> SuperBrokenMagicPearl = ITEMS.register("broken_magic_pearl_super", ModPearl::new);
    public static RegistryObject<Item> modEnchantBook = ITEMS.register("mod_enchant_book", ModEnchantBook::new);
    public static RegistryObject<Item> oldBook = ITEMS.register("old_book", OldBook::new);
    public static RegistryObject<Item> smallExpDrip = ITEMS.register("small_exp_drip", ExpDrip::new);
    public static RegistryObject<Item> bigExpDrip = ITEMS.register("big_exp_drip", ExpDrip::new);
    public static RegistryObject<Item> CuresPearl = ITEMS.register("remove_cures_pearl", ModPearl::new);
    public static RegistryObject<Item> SuperCuresPearl = ITEMS.register("remove_cures_pearl_super", ModPearl::new);

    //方块
    private static final Item.Properties GROUP = new Item.Properties().group(ModGroup.youEnchants);
    public static RegistryObject<Item> netherIron = ITEMS.register("nether_iron",
            () -> new BlockItem(YEBlocks.netherIron.get(), GROUP));
    public static RegistryObject<Item> netherGold = ITEMS.register("nether_gold",
            () -> new BlockItem(YEBlocks.netherGold.get(), GROUP));
    public static RegistryObject<Item> overworldIron = ITEMS.register("overworld_iron",
            () -> new BlockItem(YEBlocks.overworldIron.get(), GROUP));
    public static RegistryObject<Item> overworldGold = ITEMS.register("overworld_gold",
            () -> new BlockItem(YEBlocks.overworldGold.get(), GROUP));
    public static RegistryObject<Item> diamondAnvil = ITEMS.register("diamond_anvil",
            () -> new BlockItem(YEBlocks.diamondAnvil.get(), GROUP));
    public static RegistryObject<Item> chippedDiamondAnvil = ITEMS.register("chipped_diamond_anvil",
            () -> new BlockItem(YEBlocks.chippedDiamondAnvil.get(), GROUP));
    public static RegistryObject<Item> damagedDiamondAnvil = ITEMS.register("damaged_diamond_anvil",
            () -> new BlockItem(YEBlocks.damagedDiamondAnvil.get(), GROUP));
    public static RegistryObject<Item> coolingLava = ITEMS.register("cooling_lava",
            () -> new BlockItem(YEBlocks.coolingLava.get(), GROUP));
}
