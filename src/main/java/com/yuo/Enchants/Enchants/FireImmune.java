package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class FireImmune extends ModEnchantBase {

    public FireImmune(Enchantment.Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }
    //是宝藏吗？
    @Override
    public boolean isTreasureOnly() {
        return true;
    }
    //附魔冲突
    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return this != ench && ench != Enchantments.FIRE_PROTECTION && ench != EnchantRegistry.superFire.get(); //火焰保护 高级火焰保护
    }

    //免疫火焰伤害
    public static void fireImmune(LivingHurtEvent event, ItemStack stackLegs, Player player){
        //伤害来源：火焰，岩浆，熔岩石，燃烧
        if(event.getSource().isFire()) {
            stackLegs.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(EquipmentSlot.LEGS));
            event.setCanceled(true);
        }
    }
}
