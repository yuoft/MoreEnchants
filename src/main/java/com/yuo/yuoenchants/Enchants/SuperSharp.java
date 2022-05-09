package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;

public class SuperSharp extends ModEnchantBase {

    private final int enchantType;

    public SuperSharp(Rarity rarityIn, int type, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
        this.enchantType = type;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        //锋利
        if (enchantType == 0)
            return this != ench && ench != Enchantments.SHARPNESS;
        else if (enchantType == 1){
            return this != ench && ench != Enchantments.SMITE;
        }else return this != ench && ench != Enchantments.BANE_OF_ARTHROPODS;
    }

    //额外伤害
    @Override
    public float calcDamageByCreature(int level, CreatureAttribute creatureType) {
        if (enchantType == 0)
            return 2F + (float) Math.max(0, level - 1);
        else if (this.enchantType == 1 && creatureType == CreatureAttribute.UNDEAD) {
            return 1f + (float)level * 3F;
        } else {
            return this.enchantType == 2 && creatureType == CreatureAttribute.ARTHROPOD ? 1f + (float)level * 3F : 0.0F;
        }
    }
    //攻击实体时
    @Override
    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)target;
            if (this.enchantType == 2 && livingentity.getCreatureAttribute() == CreatureAttribute.ARTHROPOD) {
                int i = 20 + user.getRNG().nextInt(10 * level);
                livingentity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, i, 3));
            }
        }
    }
}
