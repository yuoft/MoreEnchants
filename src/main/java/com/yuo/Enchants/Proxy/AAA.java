package com.yuo.Enchants.Proxy;

import com.yuo.Enchants.YuoEnchants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AAA {
    public static final DeferredRegister<EntityType<?>> TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, YuoEnchants.MOD_ID);

    public static final RegistryObject<EntityType<ColorBolt>> COLOR_LIGHT_BOLT = TYPES.register("color_light_bolt",
            () -> EntityType.Builder.<ColorBolt>of(ColorBolt::new, MobCategory.CREATURE).noSave().setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(100).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f).build("color_light_bolt"));
}
