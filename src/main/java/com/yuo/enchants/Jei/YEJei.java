package com.yuo.enchants.Jei;

import com.yuo.enchants.Items.ModEnchantBook;
import com.yuo.enchants.Items.OldBook;
import com.yuo.enchants.Items.YEItems;
import com.yuo.enchants.YuoEnchants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

@JeiPlugin
public class YEJei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(YuoEnchants.MOD_ID, "jei_plugin");
    }

    //注册物品不同nbt  使用nbt来在jei中显示
    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        IModPlugin.super.registerItemSubtypes(registration);
        registration.registerSubtypeInterpreter(YEItems.modEnchantBook.get(), (e, u) -> {
            Map<Enchantment, Integer> enchantments = ModEnchantBook.getEnchantments(e);
            if (enchantments.size() > 0){
                StringBuilder s = new StringBuilder();
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    s.append(entry.getKey().getName());
                    s.append(entry.getKey().getMaxLevel());
                }
                return s.toString();
            }
            return "";
        });
        registration.registerSubtypeInterpreter(YEItems.oldBook.get(), (e, u) -> {
            OldBook.OldBookEnchant ench = OldBook.getEnch(e);
            return ench.getEnchantment().getName();
        });
    }
}
