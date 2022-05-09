package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ModEnchantBase extends Enchantment {
    private final EnchantType type;

    protected ModEnchantBase(Rarity rarityIn, EnchantType type, EquipmentSlotType[] slots) {
        super(rarityIn, null, slots);
        this.type = type;
    }

    public String getTypeName() {
        return type.getName();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return type.canEnchantItem(stack.getItem());
    }

    @Override
    public ITextComponent getDisplayName(int level) {
        IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(this.getName());

        if (level != 1 || this.getMaxLevel() != 1) {
            iformattabletextcomponent.appendString(" ").appendSibling(new TranslationTextComponent("enchantment.level." + level));
        }
        if (this.isCurse()) {
            iformattabletextcomponent.mergeStyle(TextFormatting.GREEN);
        } else {
            iformattabletextcomponent.mergeStyle(TextFormatting.DARK_PURPLE);
        }

        return iformattabletextcomponent;
    }

    enum EnchantType{
        ARMOR{ //盔甲
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof ArmorItem;
            }
            @Override
            public String getName() {
                return "armor";
            }
        },
        ARMOR_HEAD{ //头盔
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof ArmorItem && ((ArmorItem) itemIn).getEquipmentSlot() == EquipmentSlotType.HEAD;
            }
            @Override
            public String getName() {
                return "armor_head";
            }
        },
        ARMOR_CHEST{ // 胸甲
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof ArmorItem && ((ArmorItem) itemIn).getEquipmentSlot() == EquipmentSlotType.CHEST;
            }
            @Override
            public String getName() {
                return "armor_chest";
            }
        },
        ARMOR_LEGS{ // 护腿
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof ArmorItem && ((ArmorItem) itemIn).getEquipmentSlot() == EquipmentSlotType.LEGS;
            }
            @Override
            public String getName() {
                return "armor_legs";
            }
        },
        ARMOR_FEET{ // 鞋子
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof ArmorItem && ((ArmorItem) itemIn).getEquipmentSlot() == EquipmentSlotType.FEET;
            }
            @Override
            public String getName() {
                return "armor_feet";
            }
        },
        WEAPON { //武器 剑 斧
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof SwordItem || itemIn instanceof AxeItem;
            }
            @Override
            public String getName() {
                return "weapon";
            }
        },
        BREAKABLE { //有耐久的
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn.isDamageable();
            }
            @Override
            public String getName() {
                return "breakable";
            }
        },
        FISHING_ROD { //钓鱼竿
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof FishingRodItem;
            }
            @Override
            public String getName() {
                return "fishing_rod";
            }
        },
        HAND_RANGE { // 有使用距离
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof ToolItem || itemIn instanceof SwordItem || itemIn instanceof TridentItem;
            }
            @Override
            public String getName() {
                return "hand_range";
            }
        },
        DIGGER { //工具 镐 斧 铲 锄
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof ToolItem;
            }
            @Override
            public String getName() {
                return "digger";
            }
        },
        FARMER { //锄
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof HoeItem;
            }
            @Override
            public String getName() {
                return "farmer";
            }
        },
        SHIELD { //盾牌
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof ShieldItem;
            }
            @Override
            public String getName() {
                return "shield";
            }
        },
        BOW_ALL { // 弓 弩
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof ShootableItem;
            }
            @Override
            public String getName() {
                return "bow_all";
            }
        },
        BOW { // 弓
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof BowItem;
            }
            @Override
            public String getName() {
                return "bow";
            }
        },
        CROSSBOW { // 弩
            @Override
            public boolean canEnchantItem(Item itemIn) {
                return itemIn instanceof CrossbowItem;
            }

            @Override
            public String getName() {
                return "crossbow";
            }
        };
        EnchantType() {
        }

        private java.util.function.Predicate<Item> delegate;
        private String name;
        EnchantType(java.util.function.Predicate<Item> delegate) {
            this.delegate = delegate;
        }

        public static EnchantmentType create(String name, java.util.function.Predicate<Item> delegate) {
            throw new IllegalStateException("Enum not extended");
        }

        /**
         * 此附魔能否用于此物品
         */
        public boolean canEnchantItem(Item itemIn) {
            return this.delegate != null && this.delegate.test(itemIn);
        }

        public String getName(){
            return this.name;
        }
    }
}
