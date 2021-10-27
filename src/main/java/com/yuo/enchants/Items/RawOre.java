package com.yuo.enchants.Items;

import com.yuo.enchants.Items.Tab.ModGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;

//破碎矿物
public class RawOre extends Item {

    public RawOre() {
        super(new Properties().group(ModGroup.myGroup));
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hitEntity(stack, target, attacker);

        if (result && !target.isImmuneToFire()) {
            if (!target.world.isRemote) {
                target.setFire(15);
            } else {
                target.world.addParticle(ParticleTypes.FLAME, target.getPosX(), target.getPosY() + target.getHeight() * 0.5, target.getPosZ(), target.getWidth() * 0.5, target.getHeight() * 0.5, target.getWidth() * 0.5);
            }
        }

        return result;
    }
}
