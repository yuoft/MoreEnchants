package com.yuo.Enchants.Enchants;

import com.yuo.Enchants.Config;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class SuperSharp extends ModEnchantBase {

    private final int enchantType;

    public SuperSharp(Rarity rarityIn, int type, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
        this.enchantType = type;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 20 + enchantmentLevel * 5;
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        //锋利
        if (enchantType == 0)
            return this != ench && ench != Enchantments.SHARPNESS;
        else if (enchantType == 1){
            return this != ench && ench != Enchantments.SMITE;
        }else return this != ench && ench != Enchantments.BANE_OF_ARTHROPODS;
    }

    //额外伤害
    @Override
    public float getDamageBonus(int level, MobType mobType) {
        if (enchantType == 0 && Config.SERVER.isSuperSharp.get())
            return 2F + (float) Math.max(0, level - 1);
        else if (this.enchantType == 1 && mobType == MobType.UNDEAD && Config.SERVER.isSuperSmite.get()) {
            return 1f + (float)level * 3F;
        } else {
            return this.enchantType == 2 && mobType == MobType.ARTHROPOD && Config.SERVER.isSuperArthropod.get() ? 1f + (float)level * 3F : 0.0F;
        }
    }
    //攻击实体时
    @Override
    public void doPostAttack(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity living) {
            if (this.enchantType == 2 && living.getMobType() == MobType.ARTHROPOD && Config.SERVER.isSuperArthropod.get()) {
                int i = 20 + user.getRandom().nextInt(10 * level);
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, i, 3));
            }
        }
    }
}
