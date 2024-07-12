package com.yuo.Enchants.Enchants;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ModEnchantBase extends Enchantment {
    private final EnchantType type;

    protected ModEnchantBase(Enchantment.Rarity rarityIn, EnchantType type, EquipmentSlot[] slots) {
        super(rarityIn, null, slots);
        this.type = type;
    }

    public String getTypeName() {
        return type.getName();
    }

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
        MutableComponent mutableComponent = new TranslatableComponent(this.getDescriptionId());

        if (level != 1 || this.getMaxLevel() != 1) {
            mutableComponent.append(" ").append(new TranslatableComponent("enchantment.level." + level));
        }
        if (this.isCurse()) {
            mutableComponent.withStyle(ChatFormatting.GREEN);
        } else {
            mutableComponent.withStyle(ChatFormatting.DARK_PURPLE);
        }

        return mutableComponent;
    }

    enum EnchantType {
        ARMOR{ //盔甲
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof ArmorItem;
            }
            @Override
            public String getName() {
                return "armor";
            }
        },
        ARMOR_HEAD{ //头盔
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof ArmorItem && ((ArmorItem) itemIn).getSlot() == EquipmentSlot.HEAD;
            }
            @Override
            public String getName() {
                return "armor_head";
            }
        },
        ARMOR_CHEST{ // 胸甲
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof ArmorItem && ((ArmorItem) itemIn).getSlot() == EquipmentSlot.CHEST;
            }
            @Override
            public String getName() {
                return "armor_chest";
            }
        },
        ARMOR_LEGS{ // 护腿
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof ArmorItem && ((ArmorItem) itemIn).getSlot() == EquipmentSlot.LEGS;
            }
            @Override
            public String getName() {
                return "armor_legs";
            }
        },
        ARMOR_FEET{ // 鞋子
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof ArmorItem && ((ArmorItem) itemIn).getSlot() == EquipmentSlot.FEET;
            }
            @Override
            public String getName() {
                return "armor_feet";
            }
        },
        WEAPON { //武器 剑 斧
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof SwordItem || itemIn instanceof AxeItem;
            }
            @Override
            public String getName() {
                return "weapon";
            }
        },
        BREAKABLE { //有耐久的
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn.canBeDepleted();
            }
            @Override
            public String getName() {
                return "breakable";
            }
        },
        TOOLS { //工具
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof DiggerItem || itemIn instanceof FishingRodItem;
            }
            @Override
            public String getName() {
                return "tools";
            }
        },
        HAND { //在手上使用
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof DiggerItem || itemIn instanceof FishingRodItem || itemIn instanceof TridentItem || itemIn instanceof ProjectileWeaponItem
                        || itemIn instanceof ShieldItem;
            }
            @Override
            public String getName() {
                return "hand";
            }
        },
        FISHING_ROD { //钓鱼竿
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof FishingRodItem;
            }
            @Override
            public String getName() {
                return "fishing_rod";
            }
        },
        HAND_RANGE { // 有使用距离
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof DiggerItem || itemIn instanceof SwordItem || itemIn instanceof TridentItem;
            }
            @Override
            public String getName() {
                return "hand_range";
            }
        },
        DIGGER { //工具 镐 斧 铲 锄
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof DiggerItem;
            }
            @Override
            public String getName() {
                return "digger";
            }
        },
        PICKAXE { //镐
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof PickaxeItem;
            }
            @Override
            public String getName() {
                return "pickaxe";
            }
        },
        FARMER { //锄
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof HoeItem;
            }
            @Override
            public String getName() {
                return "farmer";
            }
        },
        SHIELD { //盾牌
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof ShieldItem;
            }
            @Override
            public String getName() {
                return "shield";
            }
        },
        BOW_ALL { // 弓 弩
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof ProjectileWeaponItem;
            }
            @Override
            public String getName() {
                return "bow_all";
            }
        },
        BOW { // 弓
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof BowItem;
            }
            @Override
            public String getName() {
                return "bow";
            }
        },
        CROSSBOW { // 弩
            @Override
            public boolean canEnchant(Item itemIn) {
                return itemIn instanceof CrossbowItem;
            }

            @Override
            public String getName() {
                return "crossbow";
            }
        };
        private java.util.function.Predicate<Item> delegate;

        private String name;
        EnchantType() {
        }
        private EnchantType(java.util.function.Predicate<Item> delegate) {
            this.delegate = delegate;
        }

        public static EnchantmentCategory create(String name, java.util.function.Predicate<Item> delegate) {
            throw new IllegalStateException("Enum not extended");
        }

        /**
         * 此附魔能否用于此物品
         */
        public boolean canEnchant(Item itemIn) {
            return this.delegate != null && this.delegate.test(itemIn);
        }

        public String getName() {
            return name;
        }
    }
}
