package com.yuo.yuoenchants.Blocks;

import com.yuo.yuoenchants.MoreEnchants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MoreEnchants.MODID);

    public static RegistryObject<Block> netherIron = BLOCKS.register("nether_iron",
            () -> new NetherOre(Material.ROCK, 1, ToolType.PICKAXE, 4, 8));
    public static RegistryObject<Block> netherGold = BLOCKS.register("nether_gold",
            () -> new NetherOre(Material.ROCK, 2, ToolType.PICKAXE, 4, 8));
    public static RegistryObject<Block> overworldIron = BLOCKS.register("overworld_iron",
            () -> new NetherOre(Material.ROCK, 2, ToolType.PICKAXE, 3.5f, 7));
    public static RegistryObject<Block> overworldGold = BLOCKS.register("overworld_gold",
            () -> new NetherOre(Material.ROCK, 2, ToolType.PICKAXE, 3.5f, 7));
    public static RegistryObject<Block> diamondAnvil = BLOCKS.register("diamond_anvil", DiamondAnvil::new);
    public static RegistryObject<Block> chippedDiamondAnvil = BLOCKS.register("chipped_diamond_anvil", DiamondAnvil::new);
    public static RegistryObject<Block> damagedDiamondAnvil = BLOCKS.register("damaged_diamond_anvil", DiamondAnvil::new);
}
