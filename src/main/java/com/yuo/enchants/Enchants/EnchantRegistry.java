package com.yuo.enchants.Enchants;

import com.yuo.enchants.MoreEnchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MoreEnchants.MODID);
    //火焰免疫
    public static RegistryObject<Enchantment> fireImmune = ENCHANTMENTS.register("fire_immune", () ->{
        return new FireImmune(Enchantment.Rarity.VERY_RARE, EnchantmentType.ARMOR_LEGS, EquipmentSlotType.values());
    });
    //以战养战
    public static RegistryObject<Enchantment> warToWar = ENCHANTMENTS.register("war_to_war", () ->{
        return new WarToWar(Enchantment.Rarity.COMMON, EnchantmentType.WEAPON, EquipmentSlotType.values());
    });
    //爆炸箭
    public static RegistryObject<Enchantment> blastArrow = ENCHANTMENTS.register("blast_arrow", () ->{
        return new BlastArrow(Enchantment.Rarity.RARE, EnchantmentType.BOW, EquipmentSlotType.values());
    });
    //脆弱
    public static RegistryObject<Enchantment> unDurable = ENCHANTMENTS.register("un_durable", () ->{
        return new UnDurable(Enchantment.Rarity.COMMON, EnchantmentType.DIGGER, EquipmentSlotType.values());
    });
    //万箭
    public static RegistryObject<Enchantment> manyArrow = ENCHANTMENTS.register("many_arrow", () ->{
        return new ManyArrow(Enchantment.Rarity.COMMON, EnchantmentType.BOW, EquipmentSlotType.values());
    });
    //经验腐蚀
    public static RegistryObject<Enchantment> expCorrode = ENCHANTMENTS.register("exp_corrode", () ->{
        return new ExpCorrode(Enchantment.Rarity.VERY_RARE, EnchantmentType.WEAPON, EquipmentSlotType.values());
    });
    //岩浆行者
    public static RegistryObject<Enchantment> lavaWalker = ENCHANTMENTS.register("lava_walker", () ->{
        return new LavaWalker(Enchantment.Rarity.RARE, EnchantmentType.ARMOR_FEET, EquipmentSlotType.values());
    });
    //洞察
    public static RegistryObject<Enchantment> insight = ENCHANTMENTS.register("insight", () ->{
        return new Insight(Enchantment.Rarity.COMMON, EnchantmentType.DIGGER, EquipmentSlotType.values());
    });
    //吸血
    public static RegistryObject<Enchantment> leech = ENCHANTMENTS.register("leech", () ->{
        return new Leech(Enchantment.Rarity.COMMON, EnchantmentType.WEAPON, EquipmentSlotType.values());
    });
    //熔炼
    public static RegistryObject<Enchantment> melting = ENCHANTMENTS.register("melting", () ->{
        return new Melting(Enchantment.Rarity.RARE, EnchantmentType.DIGGER, EquipmentSlotType.values());
    });
    //粉碎
//    public static RegistryObject<Enchantment> smash = ENCHANTMENTS.register("smash", () ->{
//        return new Smash(Enchantment.Rarity.COMMON, EnchantmentType.DIGGER, EquipmentSlotType.values());
//    });
    //海之嫌弃
    public static RegistryObject<Enchantment> badLuckOfTheSea = ENCHANTMENTS.register("bad_luck_of_the_sea", () ->{
        return new BadLuckOfTheSea(Enchantment.Rarity.COMMON, EnchantmentType.FISHING_ROD, EquipmentSlotType.values());
    });
    //屹立不倒
    public static RegistryObject<Enchantment> lastStand = ENCHANTMENTS.register("last_stand", () ->{
        return new LastStand(Enchantment.Rarity.VERY_RARE, EnchantmentType.ARMOR_FEET, EquipmentSlotType.values());
    });
    //雷击
    public static RegistryObject<Enchantment> lightningDamage = ENCHANTMENTS.register("lightning_damage", () ->{
        return new LightningDamage(Enchantment.Rarity.RARE, EnchantmentType.ARMOR_LEGS, EquipmentSlotType.values());
    });
    //真荆棘
    public static RegistryObject<Enchantment> thorns = ENCHANTMENTS.register("thorns", () ->{
        return new Thorns(Enchantment.Rarity.COMMON, EnchantmentType.ARMOR, EquipmentSlotType.values());
    });
    //拖拉
    public static RegistryObject<Enchantment> slow = ENCHANTMENTS.register("slow", () ->{
        return new Slow(Enchantment.Rarity.COMMON, EnchantmentType.DIGGER, EquipmentSlotType.values());
    });
    //斩首
    public static RegistryObject<Enchantment> vorpal = ENCHANTMENTS.register("vorpal", () ->{
        return new Vorpal(Enchantment.Rarity.RARE, EnchantmentType.WEAPON, EquipmentSlotType.values());
    });
    //范围挖掘
    public static RegistryObject<Enchantment> rangBreak = ENCHANTMENTS.register("rang_break", () ->{
        return new RangBreak(Enchantment.Rarity.VERY_RARE, EnchantmentType.DIGGER, EquipmentSlotType.values());
    });
    //生机
    public static RegistryObject<Enchantment> health = ENCHANTMENTS.register("health", () ->{
        return new Health(Enchantment.Rarity.VERY_RARE, EnchantmentType.ARMOR_CHEST, EquipmentSlotType.values());
    });
    //距离提升
    public static RegistryObject<Enchantment> handRange = ENCHANTMENTS.register("hand_range", () ->{
        return new HandRange(Enchantment.Rarity.VERY_RARE, EnchantmentType.DIGGER, EquipmentSlotType.values());
    });
}
