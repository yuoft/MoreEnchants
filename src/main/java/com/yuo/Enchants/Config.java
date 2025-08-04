package com.yuo.Enchants;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

public class Config {
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ServerConfig SERVER;
    public static ClientConfig CLIENT;

    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> specPair1 = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT = specPair1.getLeft();
            CLIENT_CONFIG = specPair1.getRight();
        }
        {
            final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
            SERVER_CONFIG = specPair.getRight();
            SERVER = specPair.getLeft();
        }
    }

    public static class ServerConfig{

        public final ForgeConfigSpec.BooleanValue isFireImmune;
        public final ForgeConfigSpec.BooleanValue isWarToWar;
        public final ForgeConfigSpec.BooleanValue isBlastArrow;
        public final ForgeConfigSpec.BooleanValue isUnDurable;
        public final ForgeConfigSpec.BooleanValue isManyArrow;
        public final ForgeConfigSpec.BooleanValue isExpCorrode;
        public final ForgeConfigSpec.BooleanValue isLavaWalker;
        public final ForgeConfigSpec.BooleanValue isInsight;
        public final ForgeConfigSpec.BooleanValue isLeech;
        public final ForgeConfigSpec.BooleanValue isMelting;
        public final ForgeConfigSpec.BooleanValue isBadLuckOfTheSea;
        public final ForgeConfigSpec.BooleanValue isLastStand;
        public final ForgeConfigSpec.BooleanValue isLightningDamage;
        public final ForgeConfigSpec.BooleanValue isThorns;
        public final ForgeConfigSpec.BooleanValue isSlow;
        public final ForgeConfigSpec.BooleanValue isBehead;
        public final ForgeConfigSpec.BooleanValue isRangBreak;
        public final ForgeConfigSpec.BooleanValue isHealth;
        public final ForgeConfigSpec.BooleanValue isHandRange;
        public final ForgeConfigSpec.BooleanValue isDoubleJump;
        public final ForgeConfigSpec.BooleanValue isMagnet;
        public final ForgeConfigSpec.BooleanValue isWaterWalk;
        public final ForgeConfigSpec.BooleanValue isFastBow;
        public final ForgeConfigSpec.BooleanValue isFarmer;
        public final ForgeConfigSpec.BooleanValue isSuperSharp;
        public final ForgeConfigSpec.BooleanValue isSuperSmite;
        public final ForgeConfigSpec.BooleanValue isSuperArthropod;
        public final ForgeConfigSpec.BooleanValue isSuperProtect;
        public final ForgeConfigSpec.BooleanValue isSuperFire;
        public final ForgeConfigSpec.BooleanValue isSuperFall;
        public final ForgeConfigSpec.BooleanValue isSuperBlast;
        public final ForgeConfigSpec.BooleanValue isSuperArrow;
        public final ForgeConfigSpec.BooleanValue isFastHeal;
        public final ForgeConfigSpec.BooleanValue isRepulsion;
        public final ForgeConfigSpec.BooleanValue isSuperPower;
        public final ForgeConfigSpec.BooleanValue isRebound;
        public final ForgeConfigSpec.BooleanValue isFireShield;
        public final ForgeConfigSpec.BooleanValue isHealthToSacrifice;
        public final ForgeConfigSpec.BooleanValue isStrengthLuck;
        public final ForgeConfigSpec.BooleanValue isRobbery;
        public final ForgeConfigSpec.BooleanValue isSuperThorns;
        public final ForgeConfigSpec.BooleanValue isFireThorns;
        public final ForgeConfigSpec.BooleanValue isDiamondDrop;
        public final ForgeConfigSpec.BooleanValue isInstability;
        public final ForgeConfigSpec.BooleanValue isDeepFear;
        public final ForgeConfigSpec.BooleanValue isUnLuck;
        public final ForgeConfigSpec.BooleanValue isUnLooting;

        public ServerConfig(ForgeConfigSpec.Builder builder){
            builder.comment("YuoEnchants enchant is effective Config").push("general");
            this.isFireImmune = buildBoolean(builder, "FireImmune", true, "enchant is effective");
            this.isWarToWar = buildBoolean(builder, "WarToWar", true, "enchant is effective");
            this.isBlastArrow = buildBoolean(builder, "BlastArrow", true, "enchant is effective");
            this.isUnDurable = buildBoolean(builder, "UnDurable", true, "enchant is effective");
            this.isManyArrow = buildBoolean(builder, "ManyArrow", true, "enchant is effective");
            this.isExpCorrode = buildBoolean(builder, "ExpCorrode", true, "enchant is effective");
            this.isLavaWalker = buildBoolean(builder, "LavaWalker", true, "enchant is effective");
            this.isInsight = buildBoolean(builder, "Insight", true, "enchant is effective");
            this.isLeech = buildBoolean(builder, "Leech", true, "enchant is effective");
            this.isMelting = buildBoolean(builder, "Melting", true, "enchant is effective");
            this.isBadLuckOfTheSea = buildBoolean(builder, "BadLuckOfTheSea", true, "enchant is effective");
            this.isLastStand = buildBoolean(builder, "LastStand", true, "enchant is effective");
            this.isLightningDamage = buildBoolean(builder, "LightningDamage", true, "enchant is effective");
            this.isThorns = buildBoolean(builder, "Thorns", true, "enchant is effective");
            this.isSlow = buildBoolean(builder, "Slow", true, "enchant is effective");
            this.isBehead = buildBoolean(builder, "Behead", true, "enchant is effective");
            this.isRangBreak = buildBoolean(builder, "RangBreak", true, "enchant is effective");
            this.isHealth = buildBoolean(builder, "Health", true, "enchant is effective");
            this.isHandRange = buildBoolean(builder, "HandRange", true, "enchant is effective");
            this.isDoubleJump = buildBoolean(builder, "DoubleJump", true, "enchant is effective");
            this.isMagnet = buildBoolean(builder, "Magnet", true, "enchant is effective");
            this.isWaterWalk = buildBoolean(builder, "WaterWalk", true, "enchant is effective");
            this.isFastBow = buildBoolean(builder, "FastBow", true, "enchant is effective");
            this.isFarmer = buildBoolean(builder, "Farmer", true, "enchant is effective");
            this.isSuperSharp = buildBoolean(builder, "SuperSharp", true, "enchant is effective");
            this.isSuperSmite = buildBoolean(builder, "SuperSmite", true, "enchant is effective");
            this.isSuperArthropod = buildBoolean(builder, "SuperArthropod", true, "enchant is effective");
            this.isSuperProtect = buildBoolean(builder, "SuperProtect", true, "enchant is effective");
            this.isSuperFire = buildBoolean(builder, "SuperFire", true, "enchant is effective");
            this.isSuperFall = buildBoolean(builder, "SuperFall", true, "enchant is effective");
            this.isSuperBlast = buildBoolean(builder, "SuperBlast", true, "enchant is effective");
            this.isSuperArrow = buildBoolean(builder, "SuperArrow", true, "enchant is effective");
            this.isFastHeal = buildBoolean(builder, "FastHeal", true, "enchant is effective");
            this.isRepulsion = buildBoolean(builder, "Repulsion", true, "enchant is effective");
            this.isSuperPower = buildBoolean(builder, "SuperPower", true, "enchant is effective");
            this.isRebound = buildBoolean(builder, "Rebound", true, "enchant is effective");
            this.isFireShield = buildBoolean(builder, "FireShield", true, "enchant is effective");
            this.isHealthToSacrifice = buildBoolean(builder, "HealthToSacrifice", true, "enchant is effective");
            this.isStrengthLuck = buildBoolean(builder, "StrengthLuck", true, "enchant is effective");
            this.isRobbery = buildBoolean(builder, "Robbery", true, "enchant is effective");
            this.isSuperThorns = buildBoolean(builder, "SuperThorns", true, "enchant is effective");
            this.isFireThorns = buildBoolean(builder, "FireThorns", true, "enchant is effective");
            this.isDiamondDrop = buildBoolean(builder, "DiamondDrop", true, "enchant is effective");
            this.isInstability = buildBoolean(builder, "Instability", true, "enchant is effective");
            this.isDeepFear = buildBoolean(builder, "DeepFear", true, "enchant is effective");
            this.isUnLuck = buildBoolean(builder, "UnLuck", true, "enchant is effective");
            this.isUnLooting = buildBoolean(builder, "UnLooting", true, "enchant is effective");
            builder.pop();

        }
    }

    public static class ClientConfig{
        public ClientConfig(ForgeConfigSpec.Builder builder){

        }
    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, double defaultValue, double min, double max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> buildConfig(ForgeConfigSpec.Builder builder, String name, String comment){
        return builder.comment(comment).translation(name).defineList(name, Collections.emptyList(), s -> s instanceof String && ResourceLocation.tryParse((String) s) != null);
    }

}
