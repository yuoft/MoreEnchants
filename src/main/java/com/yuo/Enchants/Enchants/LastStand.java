package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class LastStand extends ModEnchantBase {

    public LastStand(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 30;
    }

    //用经验抵挡伤害
    public static void lastStand(Player player, LivingHurtEvent event, ItemStack stackFeet){
        float amount = event.getAmount(); //伤害值
        float health = player.getHealth();
        if ((health - amount) < 1){ //受到致命伤害
            int exp = player.totalExperience; //玩家经验值
            int ceil = (int) Math.ceil((amount - (health - 1)) * 20); //将玩家血量扣到半颗心时 剩余的伤害值 * 抵消倍率
            if (exp >= ceil){ //玩家经验值能够抵消伤害
                player.giveExperiencePoints(-ceil); //扣除经验值
                player.setHealth(1);
                event.setAmount(0);
                stackFeet.hurtAndBreak(1, player, e->e.broadcastBreakEvent(EquipmentSlot.FEET));
            }

        }
    }
}
