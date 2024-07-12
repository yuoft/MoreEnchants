package com.yuo.Enchants.Enchants;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.Random;

public class Rebound extends ModEnchantBase {

    public Rebound(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 15 + pLevel * 5;
    }

    //弹反
    public static void rebound(LivingAttackEvent event, int rebound, Player player, ItemStack shield){
        DamageSource source = event.getSource();
        Entity immediateSource = source.getDirectEntity(); //发起攻击者
        if (!player.isBlocking()) return; //使用盾牌
        if (new Random().nextDouble() < 0.5 + 0.05 * rebound){ //概率完全防御 但是消耗耐久
            if (player.getCooldowns().isOnCooldown(shield.getItem()))
                player.getCooldowns().removeCooldown(shield.getItem());
            shield.hurtAndBreak(2, player, e->e.broadcastBreakEvent(e.getUsedItemHand()));
            event.setCanceled(true);
        }
        if (immediateSource instanceof LivingEntity) {//击退生命实体
            Rebound.knockbackEntity((LivingEntity) immediateSource, rebound);
            //给予攻击者一定反伤 lv * 15%
            immediateSource.hurt(DamageSource.playerAttack(player), event.getAmount() * rebound * 0.15f);
        }
        player.hurtTime = 5 + 3 * rebound; //受击无敌时间
    }

    //击退攻击者
    public static void knockbackEntity(LivingEntity living, int rebound){
        Vec3 vec3 = living.getViewVector(1.0f);
        Vec3 scale = vec3.scale(0.75 + rebound * 0.25);
        living.setDeltaMovement(scale);
    }
}
