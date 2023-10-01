package com.yuo.enchants.mixin;

import com.yuo.enchants.Config;
import com.yuo.enchants.Enchants.EnchantRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends Item {
    public BowItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        int fastBow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.fastBow.get(), stack);
        return 72000 - (Config.SERVER.isFastBow.get() ? 20000 * fastBow : 0);
    }
}
