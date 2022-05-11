package com.yuo.yuoenchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Hand;

import java.util.Random;

public class UnDurable extends ModEnchantBase {

    public UnDurable(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        //耐久
        return this != ench && ench != Enchantments.UNBREAKING;
    }

    //消耗武器耐久
    public static void unDurable(ItemStack stack, int unDurable, PlayerEntity player){
        Item item = stack.getItem();
        if (item instanceof SwordItem || item instanceof ToolItem || item instanceof TridentItem){
            stack.damageItem(new Random().nextInt(unDurable) + 1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
        }
    }

    //根据附魔等级 有60%概率额外消耗耐久对应耐久 盔甲
    public static boolean addDamage(ItemStack stack, int level, Random rand) {
        if (stack.getItem() instanceof ArmorItem && rand.nextFloat() < 0.6F) {
            return false;
        } else {
            return rand.nextInt(level + 1) > 0;
        }
    }
}
