package com.yuo.Enchants.Enchants;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ModEnchantBase extends Enchantment {
    public static final EnchantmentCategory BOW_ALL = EnchantmentCategory.create("bow_all", e -> e instanceof ProjectileWeaponItem);
    public static final EnchantmentCategory TOOLS = EnchantmentCategory.create("tools", e -> e instanceof DiggerItem || e instanceof FishingRodItem);
    public static final EnchantmentCategory HAND_RANGE = EnchantmentCategory.create("hand_range", e -> e instanceof TieredItem || e instanceof TridentItem);
    public static final EnchantmentCategory FARMER = EnchantmentCategory.create("farmer", e -> e instanceof HoeItem);
    public static final EnchantmentCategory SHIELD = EnchantmentCategory.create("shield", e -> e instanceof ShieldItem);
    public static final EnchantmentCategory PICKAXE = EnchantmentCategory.create("pickaxe", e -> e instanceof PickaxeItem);
    public static final EnchantmentCategory HAND = EnchantmentCategory.create("hand", e -> e instanceof TieredItem || e instanceof FishingRodItem || e instanceof TridentItem || e instanceof ProjectileWeaponItem || e instanceof ShieldItem);
    protected final EnchantmentCategory type;

    protected ModEnchantBase(Enchantment.Rarity rarityIn, EnchantmentCategory type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
        this.type = type;
    }

//    public String getTypeName() {
//        return type.getName();
//    }

    @Override
    public int getMaxCost(int pLevel) {
        return this.getMinCost(pLevel) + getMaxLv();
    }

    @Override
    public int getMinCost(int pLevel) {
        return 30;
    }

    /**
     * 根据获取难度决定 刷新等级范围
     * @return lv
     */
    private int getMaxLv(){
        Rarity rarity = getRarity();
        return switch (rarity) {
            case COMMON -> 5;
            case UNCOMMON -> 10;
            case RARE -> 20;
            case VERY_RARE -> 30;
        };
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return type.canEnchant(stack.getItem());
    }

    @Override
    public Component getFullname(int level) {
        MutableComponent mutableComponent = Component.translatable(this.getDescriptionId());

        if (level != 1 || this.getMaxLevel() != 1) {
            mutableComponent.append(" ").append(Component.translatable("enchantment.level." + level));
        }
        if (this.isCurse()) {
            mutableComponent.withStyle(ChatFormatting.GREEN);
        } else {
            mutableComponent.withStyle(ChatFormatting.DARK_PURPLE);
        }

        return mutableComponent;
    }

}
