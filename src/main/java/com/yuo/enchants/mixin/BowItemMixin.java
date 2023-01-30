package com.yuo.enchants.mixin;

import com.yuo.enchants.Enchants.EnchantRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BowItem.class)
public class BowItemMixin {

    /**
     * @author yuo
     * @reason 附魔快速拉弓 修改弓蓄力时间
     */
    @Overwrite
    public int getUseDuration(ItemStack stack) {
        int fastBow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.fastBow.get(), stack);
        return fastBow == 0 ? 72000 : Math.max(900, 72000 - 12000 * fastBow);
    }
}
