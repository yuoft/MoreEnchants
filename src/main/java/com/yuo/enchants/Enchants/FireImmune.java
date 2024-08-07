package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
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
        return this != ench && ench != Enchantments.FIRE_PROTECTION && ench != EnchantRegistry.superFire.get(); //火焰保护 高级火焰保护
    }

    //免疫火焰伤害
    public static void fireImmune(LivingHurtEvent event, ItemStack stackLegs, PlayerEntity player){
        //伤害来源：火焰，岩浆，熔岩石，燃烧
        if(event.getSource().isFireDamage()) {
            stackLegs.damageItem(1, player, e -> e.sendBreakAnimation(EquipmentSlotType.LEGS));
            event.setCanceled(true);
        }
    }
}
