package com.yuo.Enchants.Enchants.Weapon;

import com.yuo.Enchants.Enchants.ModEnchantBase;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;

import java.util.Random;

public class CriticalHit extends ModEnchantBase {
    public CriticalHit(Rarity rarityIn, EnchantmentCategory type, EquipmentSlot[] slots) {
        super(rarityIn, type, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 23 + enchantmentLevel * 6;
    }

    public static float getDamage(float damage, int hitLv, Player player, Level level, LivingEntity living) {
        Random random = new Random();
        if (random.nextDouble() < 0.15 * hitLv){
            level.playSound(player, player.getOnPos(), SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0f, 1.0f);
            ServerLevel serverLevel = (ServerLevel) level;//暴击粒子
            serverLevel.sendParticles(ParticleTypes.CRIT, living.getX(), living.getY(0.5), living.getZ(), 5, 0.1D, 0.0D, 0.1D, 0.2D);
            if (random.nextDouble() < 0.005 * hitLv){
                return damage * (1 + 2.5f * hitLv);
            }
            return damage * (1 + 0.5f * hitLv);
        }
        return damage;
    }
}
