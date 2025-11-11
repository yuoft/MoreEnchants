package com.yuo.Enchants.Enchants;

import com.yuo.Enchants.YuoEnchants;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class YEEnchants {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, YuoEnchants.MOD_ID);
    //火焰免疫
    public static RegistryObject<Enchantment> fireImmune = ENCHANTMENTS.register("fire_immune",
            () ->new FireImmune(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.values()));
    //以战养战
    public static RegistryObject<Enchantment> warToWar = ENCHANTMENTS.register("war_to_war",
            () -> new WarToWar(Rarity.COMMON, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    //爆炸箭
    public static RegistryObject<Enchantment> blastArrow = ENCHANTMENTS.register("blast_arrow",
            () -> new BlastArrow(Rarity.RARE, ModEnchantBase.BOW_ALL, EquipmentSlot.values()));
    //脆弱
    public static RegistryObject<Enchantment> unDurable = ENCHANTMENTS.register("un_durable",
            () -> new UnDurable(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));
    //万箭
    public static RegistryObject<Enchantment> manyArrow = ENCHANTMENTS.register("many_arrow",
            () -> new ManyArrow(Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.values()));
    //经验腐蚀
    public static RegistryObject<Enchantment> expCorrode = ENCHANTMENTS.register("exp_corrode",
            () -> new ExpCorrode(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));
    //岩浆行者
    public static RegistryObject<Enchantment> lavaWalker = ENCHANTMENTS.register("lava_walker",
            () -> new LavaWalker(Rarity.RARE, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.values()));
    //洞察
    public static RegistryObject<Enchantment> insight = ENCHANTMENTS.register("insight",
            () -> new Insight(Rarity.COMMON, ModEnchantBase.TOOLS, EquipmentSlot.values()));
    //吸血
    public static RegistryObject<Enchantment> leech = ENCHANTMENTS.register("leech",
            () -> new Leech(Rarity.COMMON, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    //熔炼
    public static RegistryObject<Enchantment> melting = ENCHANTMENTS.register("melting",
            () -> new Melting(Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.values()));
    //粉碎
//    public static RegistryObject<Enchantment> smash = ENCHANTMENTS.register("smash", () ->{
//        return new Smash(Rarity.COMMON, EnchantmentType.DIGGER, EquipmentSlot.values());
//    });
    //海之嫌弃
    public static RegistryObject<Enchantment> badLuckOfTheSea = ENCHANTMENTS.register("bad_luck_of_the_sea",
            () -> new BadLuckOfTheSea(Rarity.RARE, EnchantmentCategory.FISHING_ROD, EquipmentSlot.values()));
    //屹立不倒
    public static RegistryObject<Enchantment> lastStand = ENCHANTMENTS.register("last_stand",
            () -> new LastStand(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.values()));
    //雷击
    public static RegistryObject<Enchantment> lightningDamage = ENCHANTMENTS.register("lightning_damage",
            () -> new LightningDamage(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.values()));
    //真荆棘
    public static RegistryObject<Enchantment> thorns = ENCHANTMENTS.register("thorns",
            () -> new Thorns(Rarity.VERY_RARE, EnchantmentCategory.ARMOR, EquipmentSlot.values()));
    //拖拉
    public static RegistryObject<Enchantment> slow = ENCHANTMENTS.register("slow",
            () -> new Slow(Rarity.VERY_RARE, EnchantmentCategory.DIGGER, EquipmentSlot.values()));
    //斩首
    public static RegistryObject<Enchantment> beHead = ENCHANTMENTS.register("behead",
            () -> new BeHead(Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    //范围挖掘
    public static RegistryObject<Enchantment> rangBreak = ENCHANTMENTS.register("rang_break",
            () -> new RangBreak(Rarity.VERY_RARE, EnchantmentCategory.DIGGER, EquipmentSlot.values()));
    //生机
    public static RegistryObject<Enchantment> health = ENCHANTMENTS.register("health",
            () -> new Health(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.values()));
    //距离提升
    public static RegistryObject<Enchantment> handRange = ENCHANTMENTS.register("hand_range",
            () -> new HandRange(Rarity.RARE, ModEnchantBase.HAND_RANGE, EquipmentSlot.values()));
    //二段跳 纵云梯
    public static RegistryObject<Enchantment> doubleJump = ENCHANTMENTS.register("double_jump",
            () -> new DoubleJump(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.values()));
    //磁力
    public static RegistryObject<Enchantment> magnet = ENCHANTMENTS.register("magnet",
            () -> new Magnet(Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.values()));
    //水上行走 水上漂
    public static RegistryObject<Enchantment> waterWalk = ENCHANTMENTS.register("water_walk",
            () -> new WaterWalk(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.values()));
    //快速拉弓
    public static RegistryObject<Enchantment> fastBow = ENCHANTMENTS.register("fast_bow",
            () -> new FastBow(Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.values()));
    //农夫
    public static RegistryObject<Enchantment> farmer = ENCHANTMENTS.register("farmer",
            () -> new Farmer(Rarity.RARE, ModEnchantBase.FARMER, EquipmentSlot.values()));
    //超级锋利
    public static RegistryObject<Enchantment> superSharp = ENCHANTMENTS.register("super_sharp",
            () -> new SuperSharp(Rarity.RARE,0, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    //超级亡灵杀手
    public static RegistryObject<Enchantment> superSmite = ENCHANTMENTS.register("super_smite",
            () -> new SuperSharp(Rarity.UNCOMMON,1, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    //超级节肢杀手
    public static RegistryObject<Enchantment> superArthropod = ENCHANTMENTS.register("super_arthropod",
            () -> new SuperSharp(Rarity.UNCOMMON,2, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    //高级保护
    public static RegistryObject<Enchantment> superProtect = ENCHANTMENTS.register("super_protect",
            () -> new SuperProtect(Rarity.RARE,0, EnchantmentCategory.ARMOR, EquipmentSlot.values()));
    //高级火焰保护
    public static RegistryObject<Enchantment> superFire = ENCHANTMENTS.register("super_fire",
            () -> new SuperProtect(Rarity.UNCOMMON,1, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.values()));
    //高级摔落保护
    public static RegistryObject<Enchantment> superFall = ENCHANTMENTS.register("super_fall",
            () -> new SuperProtect(Rarity.UNCOMMON,2, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.values()));
    //高级爆炸保护
    public static RegistryObject<Enchantment> superBlast = ENCHANTMENTS.register("super_blast",
            () -> new SuperProtect(Rarity.UNCOMMON,3, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.values()));
    //高级弹射物保护
    public static RegistryObject<Enchantment> superArrow = ENCHANTMENTS.register("super_arrow",
            () -> new SuperProtect(Rarity.UNCOMMON,4, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.values()));
    //快速恢复
    public static RegistryObject<Enchantment> fastHeal = ENCHANTMENTS.register("fast_heal",
            () -> new FastHeal(Rarity.RARE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.values()));
    //斥力
    public static RegistryObject<Enchantment> repulsion = ENCHANTMENTS.register("repulsion",
            () -> new Repulsion(Rarity.RARE, ModEnchantBase.BOW_ALL, EquipmentSlot.values()));
    //超级力量
    public static RegistryObject<Enchantment> superPower = ENCHANTMENTS.register("super_power",
            () -> new SuperPower(Rarity.UNCOMMON, ModEnchantBase.BOW_ALL, EquipmentSlot.values()));
    //弹反
    public static RegistryObject<Enchantment> rebound = ENCHANTMENTS.register("rebound",
            () -> new Rebound(Rarity.RARE, ModEnchantBase.SHIELD, EquipmentSlot.values()));
    //火焰盾
    public static RegistryObject<Enchantment> fireShield = ENCHANTMENTS.register("fire_shield",
            () -> new FireShield(Rarity.COMMON, ModEnchantBase.SHIELD, EquipmentSlot.values()));
    //生命献祭
    public static RegistryObject<Enchantment> healthToSacrifice = ENCHANTMENTS.register("health_to_sacrifice",
            () -> new HealthToSacrifice(Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    //1.3.3
    //强运
    public static RegistryObject<Enchantment> strengthLuck = ENCHANTMENTS.register("strength_luck",
            () -> new StrengthLuck(Rarity.VERY_RARE, EnchantmentCategory.DIGGER, EquipmentSlot.values()));
    //掠夺
    public static RegistryObject<Enchantment> robbery = ENCHANTMENTS.register("robbery",
            () -> new Robbery(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
    //高级荆棘
    public static RegistryObject<Enchantment> superThorns = ENCHANTMENTS.register("super_thorns",
            () -> new SuperThorns(Rarity.UNCOMMON, EnchantmentCategory.ARMOR, EquipmentSlot.values()));
    //火焰荆棘
    public static RegistryObject<Enchantment> fireThorns = ENCHANTMENTS.register("fire_thorns",
            () -> new FireThorns(Rarity.COMMON, EnchantmentCategory.ARMOR, EquipmentSlot.values()));
    //分子重构
    public static RegistryObject<Enchantment> diamondDrop = ENCHANTMENTS.register("diamond_drop",
            () -> new DiamondDrop(Rarity.COMMON, ModEnchantBase.PICKAXE, EquipmentSlot.values()));
    //失稳
    public static RegistryObject<Enchantment> instability = ENCHANTMENTS.register("instability",
            () -> new Instability(Rarity.VERY_RARE, ModEnchantBase.HAND, EquipmentSlot.values()));
    //深海恐惧
    public static RegistryObject<Enchantment> deepFear = ENCHANTMENTS.register("deep_fear",
            () -> new DeepFear(Rarity.VERY_RARE, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.values()));
    //霉运
    public static RegistryObject<Enchantment> unLuck = ENCHANTMENTS.register("un_luck",
            () -> new UnLuck(Rarity.VERY_RARE, EnchantmentCategory.DIGGER, EquipmentSlot.values()));
    //抢夺不到
    public static RegistryObject<Enchantment> unLooting = ENCHANTMENTS.register("un_looting",
            () -> new UnLooting(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, EquipmentSlot.values()));
}
