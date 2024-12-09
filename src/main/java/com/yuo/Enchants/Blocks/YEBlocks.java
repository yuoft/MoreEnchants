package com.yuo.Enchants.Blocks;

import com.yuo.Enchants.YuoEnchants;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YEBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, YuoEnchants.MOD_ID);

    public static RegistryObject<Block> overworldIron = BLOCKS.register("overworld_iron",
            () -> new ModOre(Material.STONE, UniformInt.of(0, 0), 3.5f, 7));
    public static RegistryObject<Block> deepOverworldIron = BLOCKS.register("deep_overworld_iron",
            () -> new DeepModOre(Material.STONE, MaterialColor.DEEPSLATE, SoundType.DEEPSLATE, UniformInt.of(0, 0), 5.0f, 8));
    public static RegistryObject<Block> overworldGold = BLOCKS.register("overworld_gold",
            () -> new ModOre(Material.STONE, UniformInt.of(0, 0), 3.5f, 7));
    public static RegistryObject<Block> deepOverworldGold = BLOCKS.register("deep_overworld_gold",
            () -> new DeepModOre(Material.STONE, MaterialColor.DEEPSLATE, SoundType.DEEPSLATE, UniformInt.of(0, 1), 5.0f, 8));
    public static RegistryObject<Block> netherIron = BLOCKS.register("nether_iron",
            () -> new ModOre(Material.STONE, UniformInt.of(0, 0), 3, 6));
    public static RegistryObject<Block> netherGold = BLOCKS.register("nether_gold",
            () -> new ModOre(Material.STONE, UniformInt.of(0, 2),3, 6));
    public static RegistryObject<Block> diamondAnvil = BLOCKS.register("diamond_anvil", DiamondAnvil::new);
    public static RegistryObject<Block> chippedDiamondAnvil = BLOCKS.register("chipped_diamond_anvil", DiamondAnvil::new);
    public static RegistryObject<Block> damagedDiamondAnvil = BLOCKS.register("damaged_diamond_anvil", DiamondAnvil::new);
    public static RegistryObject<Block> coolingLava = BLOCKS.register("cooling_lava", CoolingLava::new);
}