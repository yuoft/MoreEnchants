package com.yuo.Enchants.Enchants;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ManyArrow extends ModEnchantBase {

    public ManyArrow(Rarity rarityIn, EnchantType typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 1+ enchantmentLevel * 7;
    }

    //射出额外箭矢
    public static void manyArrow(int chargeIn, Player player, ItemStack bow, int manyArrow, Level world){
        float charge = BowItem.getPowerForTime(chargeIn); //弓的状态
        ItemStack itemStack = player.getProjectile(bow);
        int fireArrow = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, bow);
        for (int i = 0; i < manyArrow; i++){
            ArrowItem arrowitem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
            AbstractArrow arrow = arrowitem.createArrow(world, itemStack, player);
            arrow.setBaseDamage(bow.getDamageValue());
            if (fireArrow > 0) arrow.setRemainingFireTicks(100);
            if (charge == 1.0F) arrow.setCritArrow(true);
            arrow.setOwner(player);

            Vec3 vec31 = player.getUpVector(1.0F);
            Quaternion quaternion = new Quaternion(new Vector3f(vec31), -9 + i * 10, true);
            Vec3 vec3 = player.getViewVector(1.0F);
            Vector3f vector3f = new Vector3f(vec3);
            vector3f.transform(quaternion);
            arrow.shoot(vector3f.x(), vector3f.y(), vector3f.z(), charge * 3.0f, 1.0f);

            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY; //万箭附魔的额外箭不可回收
            world.addFreshEntity(arrow);
        }
    }
}
