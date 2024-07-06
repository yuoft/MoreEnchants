package com.yuo.enchants.Enchants;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class ManyArrow extends ModEnchantBase {

    public ManyArrow(Rarity rarityIn, EnchantType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1+ enchantmentLevel * 7;
    }

    //射出额外箭矢
    public static void manyArrow(int chargeIn, PlayerEntity player, ItemStack bow, int manyArrow, World world){
        float charge = BowItem.getArrowVelocity(chargeIn); //弓的状态
        ItemStack itemStack = player.findAmmo(bow);
        int fireArrow = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow);
        for (int i = 0; i < manyArrow; i++){
            ArrowItem arrowitem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
            AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(world, itemStack, player);
            abstractarrowentity.setDamage(bow.getDamage());
            if (fireArrow > 0) abstractarrowentity.setFire(100);
            if (charge == 1.0F) abstractarrowentity.setIsCritical(true);
            abstractarrowentity.setShooter(player);
            Vector3d vector3d1 = player.getUpVector(1.0F);
            Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), -9 + i * 10, true);
            Vector3d vector3d = player.getLook(1.0F);
            Vector3f vector3f = new Vector3f(vector3d);
            vector3f.transform(quaternion);
            abstractarrowentity.shoot(vector3f.getX(), vector3f.getY(), vector3f.getZ(),charge * 3.0f, 1.0f);
//            abstractarrowentity.func_234612_a_(player, rotationPitch, rotationYaw, 0.0F, charge * 3.0F, 1.0F);
            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY; //万箭附魔的额外箭不可回收
            world.addEntity(abstractarrowentity);
        }
    }
}
