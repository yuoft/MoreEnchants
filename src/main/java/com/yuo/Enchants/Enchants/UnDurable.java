package com.yuo.Enchants.Enchants;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Random;

public class UnDurable extends ModEnchantBase {

    public UnDurable(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 3 + pLevel * 7;
    }

    //负面负面
    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        //耐久
        return this != ench && ench != Enchantments.UNBREAKING;
    }

    //消耗武器耐久
    public static void unDurable(ItemStack stack, int unDurable, Player player){
        Item item = stack.getItem();
        if (item instanceof SwordItem || item instanceof DiggerItem || item instanceof TridentItem){
            stack.hurtAndBreak(new Random().nextInt(unDurable) + 1, player, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND));
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
