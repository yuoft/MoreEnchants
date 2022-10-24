package com.yuo.enchants.Enchants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.Random;

public class Rebound extends ModEnchantBase {

    public Rebound(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    //弹反
    public static void rebound(LivingAttackEvent event, int rebound, PlayerEntity player, ItemStack shield){
        DamageSource source = event.getSource();
        Entity immediateSource = source.getImmediateSource(); //发起攻击者
        if (!player.isActiveItemStackBlocking()) return; //使用盾牌
        if (new Random().nextDouble() < 0.5 + 0.05 * rebound){ //概率完全防御 但是消耗耐久
            if (player.getCooldownTracker().hasCooldown(shield.getItem()))
                player.getCooldownTracker().removeCooldown(shield.getItem());
            shield.damageItem(1, player, e -> e.sendBreakAnimation(Hand.OFF_HAND));
            event.setCanceled(true);
        }
        if (immediateSource instanceof LivingEntity) {//击退生命实体
            Rebound.knockbackEntity((LivingEntity) immediateSource, rebound);
            //给予攻击者一定反伤 lv * 15%
            immediateSource.attackEntityFrom(DamageSource.causePlayerDamage(player), event.getAmount() * rebound * 0.15f);
        }
        player.hurtResistantTime = 5 + 3 * rebound; //受击无敌时间
    }

    //击退攻击者
    public static void knockbackEntity(LivingEntity living, int rebound){
        Vector3d v3d = Vector3d.fromPitchYaw(living.rotationPitch, living.rotationYaw).scale(-1);
        Vector3d scale = v3d.scale(0.75 + rebound * 0.25);
        living.setMotion(scale);
    }
}
