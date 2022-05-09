package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class FireImmune extends ModEnchantBase {

    public FireImmune(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }
    //是宝藏吗？
    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    //附魔冲突
    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return this != ench && ench != Enchantments.FIRE_PROTECTION; //火焰保护
    }

    //免疫火焰伤害
    public static void fireImmune(LivingHurtEvent event, ItemStack stackLegs, PlayerEntity player){
        //伤害来源：火焰，岩浆，熔岩石，燃烧
        if(event.getSource().isFireDamage()) {
            event.setAmount(0);
            stackLegs.damageItem(1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
        }
    }
}
