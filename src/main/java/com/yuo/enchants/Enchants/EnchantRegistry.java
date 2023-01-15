package com.yuo.enchants.Enchants;

import com.yuo.enchants.YuoEnchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, YuoEnchants.MOD_ID);
    //火焰免疫
    public static RegistryObject<Enchantment> fireImmune = ENCHANTMENTS.register("fire_immune",
            () -> new FireImmune(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.ARMOR_LEGS, EquipmentSlotType.values()));
    //以战养战
    public static RegistryObject<Enchantment> warToWar = ENCHANTMENTS.register("war_to_war",
            () -> new WarToWar(Enchantment.Rarity.COMMON, ModEnchantBase.EnchantType.WEAPON, EquipmentSlotType.values()));
    //爆炸箭
    public static RegistryObject<Enchantment> blastArrow = ENCHANTMENTS.register("blast_arrow",
            () -> new BlastArrow(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.BOW_ALL, EquipmentSlotType.values()));
    //脆弱
    public static RegistryObject<Enchantment> unDurable = ENCHANTMENTS.register("un_durable",
            () -> new UnDurable(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.BREAKABLE, EquipmentSlotType.values()));
    //万箭
    public static RegistryObject<Enchantment> manyArrow = ENCHANTMENTS.register("many_arrow",
            () -> new ManyArrow(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.BOW, EquipmentSlotType.values()));
    //经验腐蚀
    public static RegistryObject<Enchantment> expCorrode = ENCHANTMENTS.register("exp_corrode",
            () -> new ExpCorrode(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.BREAKABLE, EquipmentSlotType.values()));
    //岩浆行者
    public static RegistryObject<Enchantment> lavaWalker = ENCHANTMENTS.register("lava_walker",
            () -> new LavaWalker(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.ARMOR_FEET, EquipmentSlotType.values()));
    //洞察
    public static RegistryObject<Enchantment> insight = ENCHANTMENTS.register("insight",
            () -> new Insight(Enchantment.Rarity.COMMON, ModEnchantBase.EnchantType.TOOLS, EquipmentSlotType.values()));
    //吸血
    public static RegistryObject<Enchantment> leech = ENCHANTMENTS.register("leech",
            () -> new Leech(Enchantment.Rarity.COMMON, ModEnchantBase.EnchantType.WEAPON, EquipmentSlotType.values()));
    //熔炼
    public static RegistryObject<Enchantment> melting = ENCHANTMENTS.register("melting",
            () -> new Melting(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.DIGGER, EquipmentSlotType.values()));
    //粉碎
//    public static RegistryObject<Enchantment> smash = ENCHANTMENTS.register("smash", () ->{
//        return new Smash(Enchantment.Rarity.COMMON, EnchantmentType.DIGGER, EquipmentSlotType.values());
//    });
    //海之嫌弃
    public static RegistryObject<Enchantment> badLuckOfTheSea = ENCHANTMENTS.register("bad_luck_of_the_sea",
            () -> new BadLuckOfTheSea(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.FISHING_ROD, EquipmentSlotType.values()));
    //屹立不倒
    public static RegistryObject<Enchantment> lastStand = ENCHANTMENTS.register("last_stand",
            () -> new LastStand(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.ARMOR_FEET, EquipmentSlotType.values()));
    //雷击
    public static RegistryObject<Enchantment> lightningDamage = ENCHANTMENTS.register("lightning_damage",
            () -> new LightningDamage(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.ARMOR_LEGS, EquipmentSlotType.values()));
    //真荆棘
    public static RegistryObject<Enchantment> thorns = ENCHANTMENTS.register("thorns",
            () -> new Thorns(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.ARMOR, EquipmentSlotType.values()));
    //拖拉
    public static RegistryObject<Enchantment> slow = ENCHANTMENTS.register("slow",
            () -> new Slow(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.DIGGER, EquipmentSlotType.values()));
    //斩首
    public static RegistryObject<Enchantment> beHead = ENCHANTMENTS.register("behead",
            () -> new BeHead(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.WEAPON, EquipmentSlotType.values()));
    //范围挖掘
    public static RegistryObject<Enchantment> rangBreak = ENCHANTMENTS.register("rang_break",
            () -> new RangBreak(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.DIGGER, EquipmentSlotType.values()));
    //生机
    public static RegistryObject<Enchantment> health = ENCHANTMENTS.register("health",
            () -> new Health(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.ARMOR_CHEST, EquipmentSlotType.values()));
    //距离提升
    public static RegistryObject<Enchantment> handRange = ENCHANTMENTS.register("hand_range",
            () -> new HandRange(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.HAND_RANGE, EquipmentSlotType.values()));
    //二段跳 纵云梯
    public static RegistryObject<Enchantment> doubleJump = ENCHANTMENTS.register("double_jump",
            () -> new DoubleJump(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.ARMOR_FEET, EquipmentSlotType.values()));
    //磁力
    public static RegistryObject<Enchantment> magnet = ENCHANTMENTS.register("magnet",
            () -> new Magnet(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.ARMOR_LEGS, EquipmentSlotType.values()));
    //水上行走 水上漂
    public static RegistryObject<Enchantment> waterWalk = ENCHANTMENTS.register("water_walk",
            () -> new WaterWalk(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.ARMOR_FEET, EquipmentSlotType.values()));
    //快速拉弓
    public static RegistryObject<Enchantment> fastBow = ENCHANTMENTS.register("fast_bow",
            () -> new FastBow(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.BOW, EquipmentSlotType.values()));
    //农夫
    public static RegistryObject<Enchantment> farmer = ENCHANTMENTS.register("farmer",
            () -> new Farmer(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.FARMER, EquipmentSlotType.values()));
    //超级锋利
    public static RegistryObject<Enchantment> superSharp = ENCHANTMENTS.register("super_sharp",
            () -> new SuperSharp(Enchantment.Rarity.VERY_RARE,0, ModEnchantBase.EnchantType.WEAPON, EquipmentSlotType.values()));
    //超级亡灵杀手
    public static RegistryObject<Enchantment> superSmite = ENCHANTMENTS.register("super_smite",
            () -> new SuperSharp(Enchantment.Rarity.RARE,1, ModEnchantBase.EnchantType.WEAPON, EquipmentSlotType.values()));
    //超级节肢杀手
    public static RegistryObject<Enchantment> superArthropod = ENCHANTMENTS.register("super_arthropod",
            () -> new SuperSharp(Enchantment.Rarity.RARE,2, ModEnchantBase.EnchantType.WEAPON, EquipmentSlotType.values()));
    //高级保护
    public static RegistryObject<Enchantment> superProtect = ENCHANTMENTS.register("super_protect",
            () -> new SuperProtect(Enchantment.Rarity.VERY_RARE,0, ModEnchantBase.EnchantType.ARMOR, EquipmentSlotType.values()));
    //高级火焰保护
    public static RegistryObject<Enchantment> superFire = ENCHANTMENTS.register("super_fire",
            () -> new SuperProtect(Enchantment.Rarity.RARE,1, ModEnchantBase.EnchantType.ARMOR_LEGS, EquipmentSlotType.values()));
    //高级摔落保护
    public static RegistryObject<Enchantment> superFall = ENCHANTMENTS.register("super_fall",
            () -> new SuperProtect(Enchantment.Rarity.RARE,2, ModEnchantBase.EnchantType.ARMOR_FEET, EquipmentSlotType.values()));
    //高级爆炸保护
    public static RegistryObject<Enchantment> superBlast = ENCHANTMENTS.register("super_blast",
            () -> new SuperProtect(Enchantment.Rarity.RARE,3, ModEnchantBase.EnchantType.ARMOR_CHEST, EquipmentSlotType.values()));
    //高级弹射物保护
    public static RegistryObject<Enchantment> superArrow = ENCHANTMENTS.register("super_arrow",
            () -> new SuperProtect(Enchantment.Rarity.RARE,4, ModEnchantBase.EnchantType.ARMOR_HEAD, EquipmentSlotType.values()));
    //快速恢复
    public static RegistryObject<Enchantment> fastHeal = ENCHANTMENTS.register("fast_heal",
            () -> new FastHeal(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.ARMOR_CHEST, EquipmentSlotType.values()));
    //斥力
    public static RegistryObject<Enchantment> repulsion = ENCHANTMENTS.register("repulsion",
            () -> new Repulsion(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.BOW_ALL, EquipmentSlotType.values()));
    //超级力量
    public static RegistryObject<Enchantment> superPower = ENCHANTMENTS.register("super_power",
            () -> new SuperPower(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.BOW_ALL, EquipmentSlotType.values()));
    //弹反
    public static RegistryObject<Enchantment> rebound = ENCHANTMENTS.register("rebound",
            () -> new Rebound(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.SHIELD, EquipmentSlotType.values()));
    //火焰盾
    public static RegistryObject<Enchantment> fireShield = ENCHANTMENTS.register("fire_shield",
            () -> new FireShield(Enchantment.Rarity.COMMON, ModEnchantBase.EnchantType.SHIELD, EquipmentSlotType.values()));
    //生命献祭
    public static RegistryObject<Enchantment> healthToSacrifice = ENCHANTMENTS.register("health_to_sacrifice",
            () -> new HealthToSacrifice(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.WEAPON, EquipmentSlotType.values()));
    //1.3.3
    //强运
    public static RegistryObject<Enchantment> strengthLuck = ENCHANTMENTS.register("strength_luck",
            () -> new StrengthLuck(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.DIGGER, EquipmentSlotType.values()));
    //掠夺
    public static RegistryObject<Enchantment> robbery = ENCHANTMENTS.register("robbery",
            () -> new Robbery(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.WEAPON, EquipmentSlotType.values()));
    //高级荆棘
    public static RegistryObject<Enchantment> superThorns = ENCHANTMENTS.register("super_thorns",
            () -> new SuperThorns(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.ARMOR, EquipmentSlotType.values()));
    //火焰荆棘
    public static RegistryObject<Enchantment> fireThorns = ENCHANTMENTS.register("fire_thorns",
            () -> new FireThorns(Enchantment.Rarity.COMMON, ModEnchantBase.EnchantType.ARMOR, EquipmentSlotType.values()));
    //分子重构
    public static RegistryObject<Enchantment> diamondDrop = ENCHANTMENTS.register("diamond_drop",
            () -> new DiamondDrop(Enchantment.Rarity.COMMON, ModEnchantBase.EnchantType.PICKAXE, EquipmentSlotType.values()));
    //失稳
    public static RegistryObject<Enchantment> instability = ENCHANTMENTS.register("instability",
            () -> new Instability(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.HAND, EquipmentSlotType.values()));
    //深海恐惧
    public static RegistryObject<Enchantment> deepFear = ENCHANTMENTS.register("deep_fear",
            () -> new DeepFear(Enchantment.Rarity.RARE, ModEnchantBase.EnchantType.ARMOR_FEET, EquipmentSlotType.values()));
    //霉运
    public static RegistryObject<Enchantment> unLuck = ENCHANTMENTS.register("un_luck",
            () -> new UnLuck(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.DIGGER, EquipmentSlotType.values()));
    //抢夺不到
    public static RegistryObject<Enchantment> unLooting = ENCHANTMENTS.register("un_looting",
            () -> new UnLooting(Enchantment.Rarity.VERY_RARE, ModEnchantBase.EnchantType.WEAPON, EquipmentSlotType.values()));
}
