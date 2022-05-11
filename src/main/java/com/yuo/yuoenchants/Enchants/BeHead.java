package com.yuo.yuoenchants.Enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Random;

public class BeHead extends ModEnchantBase {

    public BeHead(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    //掉落头颅
    public static ItemEntity dropHead(int beHead, LivingEntity living){
        int i = new Random().nextInt(100);
        ItemStack skull = ItemStack.EMPTY;
        if (i < 20 * beHead){ //每级+20%掉落几率
            if (living instanceof PlayerEntity){
                skull = new ItemStack(Items.PLAYER_HEAD, 1);
                CompoundNBT nbt = new CompoundNBT(); //添加玩家头颅信息
                nbt.putString("playerName", living.getName().getString());
                skull.setTag(nbt);
            }else if (living instanceof WitherSkeletonEntity){
                skull = new ItemStack(Items.WITHER_SKELETON_SKULL, 1);
            }
        }
        return new ItemEntity(living.world, living.getPosX(), living.getPosY(), living.getPosZ(), skull);
    }

    //暴击增加伤害
    public static void addDamage(int beHead, LivingHurtEvent event, PlayerEntity player, LivingEntity living){
        int i = new Random().nextInt(100);
        if (i < 5 * beHead){ // 暴击概率 5% * 等级
            event.setAmount(event.getAmount() * 5); //暴击伤害*5
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1.0F, 1.0F);
            for (int j = 0; j < beHead * 2; j++) {
                ((ServerWorld) living.world).spawnParticle(ParticleTypes.CRIT,
                        living.getPosX() + living.world.rand.nextDouble(), living.getPosY() + 1.5D,
                        living.getPosZ() + living.world.rand.nextDouble(), 1, 0, 0, 0, 0);
            }
        }
    }
}
