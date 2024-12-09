package com.yuo.Enchants.Jei;

import com.yuo.Enchants.Items.ModEnchantBook;
import com.yuo.Enchants.Items.OldBook;
import com.yuo.Enchants.Items.YEItems;
import com.yuo.Enchants.YuoEnchants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

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
            ListTag tag = ModEnchantBook.getEnchantments(e);
//            for (Enchantment enchant : LootModifierHelper.MOD_ENCHANTS) {
//                String id = enchant.getDescriptionId();
//                int level = enchant.getMaxLevel();
//                return id + level;
//            }
            if (!tag.isEmpty()){
                for (Tag t : tag) {
                    CompoundTag com = (CompoundTag) t;
                    StringBuilder s = new StringBuilder();
                    s.append(com.getString("id")).append(com.getString("lvl"));
                    return s.toString();
                }
            }
            return "";
        });
        registration.registerSubtypeInterpreter(YEItems.oldBook.get(), (e, u) -> {
            OldBook.OldBookEnchant ench = OldBook.getEnch(e);
            return ench.getEnchantment().getDescriptionId();
        });
    }
}
