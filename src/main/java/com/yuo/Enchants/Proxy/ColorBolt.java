package com.yuo.Enchants.Proxy;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;

public class ColorBolt extends LightningBolt {
    public ColorBolt(EntityType<? extends LightningBolt> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setDamage(1000);
    }

    public ColorBolt(Level level) {
        super(AAA.COLOR_LIGHT_BOLT.get(), level);
    }
}
