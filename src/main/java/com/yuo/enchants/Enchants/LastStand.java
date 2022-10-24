package com.yuo.enchants.Enchants;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class LastStand extends ModEnchantBase {

    public LastStand(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    //用经验抵挡伤害
    public static void lastStand(PlayerEntity player, LivingHurtEvent event, ItemStack stackFeet){
        float amount = event.getAmount(); //伤害值
        float health = player.getHealth();
        if ((health - amount) < 1){ //受到致命伤害
            int exp = player.experienceTotal; //玩家经验值
            int ceil = MathHelper.ceil((amount - (health - 1)) * 20); //将玩家血量扣到半颗心时 剩余的伤害值 * 抵消倍率
            if (exp >= ceil){ //玩家经验值能够抵消伤害
                player.giveExperiencePoints(-ceil); //扣除经验值
                player.setHealth(1);
                event.setAmount(0);
                stackFeet.damageItem(1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
            }

        }
    }
}
