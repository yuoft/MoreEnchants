package com.yuo.Enchants.Enchants;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Random;

public class BeHead extends ModEnchantBase {

    public BeHead(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int pLevel) {
        return 25;
    }

    //掉落头颅
    public static ItemEntity dropHead(int beHead, LivingEntity living){
        int i = new Random().nextInt(100);
        ItemStack skull = ItemStack.EMPTY;
        if (i < 20 * beHead){ //每级+20%掉落几率
            if (living instanceof Player){
                skull = new ItemStack(Items.PLAYER_HEAD, 1);
                CompoundTag nbt = new CompoundTag(); //添加玩家头颅信息
                nbt.putString("playerName", living.getName().getString());
                skull.setTag(nbt);
            }else if (living instanceof WitherSkeleton){
                skull = new ItemStack(Items.WITHER_SKELETON_SKULL, 1);
            }
        }
        return new ItemEntity(living.level, living.getX(), living.getY(), living.getZ(), skull);
    }

    //暴击增加伤害
    public static void addDamage(int beHead, LivingHurtEvent event, Player player, LivingEntity living){
        int i = new Random().nextInt(100);
        if (i < 5 * beHead){ // 暴击概率 5% * 等级
            event.setAmount(event.getAmount() * 5); //暴击伤害*5
            player.level.playSound(null, player.getOnPos(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS, 1.0F, 1.0F);
            for (int j = 0; j < beHead * 2; j++) {
                living.level.addParticle(ParticleTypes.CRIT,
                        living.getX() + living.level.random.nextDouble(), living.getY() + 1.5D,
                        living.getZ() + living.level.random.nextDouble(), 1, 0, 0);
            }
        }
    }
}
