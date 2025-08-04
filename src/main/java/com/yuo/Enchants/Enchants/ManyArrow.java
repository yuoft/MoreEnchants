package com.yuo.Enchants.Enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ManyArrow extends ModEnchantBase {

    public ManyArrow(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
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
            Quaternionf quaternion = new Quaternionf().setAngleAxis(-0.02 + i * 0.017453292F , vec31.x, vec31.y, vec31.z);
            Vec3 vec3 = player.getViewVector(1.0F);
            Vector3f vector3f = vec3.toVector3f().rotate(quaternion);
            arrow.shoot(vector3f.x(), vector3f.y(), vector3f.z(), charge * 3.0f, 1.0f);

            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY; //万箭附魔的额外箭不可回收
            world.addFreshEntity(arrow);
        }
    }
}
