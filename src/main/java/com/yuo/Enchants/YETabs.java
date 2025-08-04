package com.yuo.Enchants;

import com.yuo.Enchants.Enchants.YEEnchants;
import com.yuo.Enchants.Items.ModEnchantBook;
import com.yuo.Enchants.Items.OldBook;
import com.yuo.Enchants.Items.YEItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.yuo.Enchants.Items.ModEnchantBook.getStack;

public class YETabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, YuoEnchants.MOD_ID);
    public static final RegistryObject<CreativeModeTab> YE_TAB = TABS.register(YuoEnchants.MOD_ID + "_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.tab.YuoEnchants"))
            .icon(() -> YEItems.oldBook.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                for (RegistryObject<Item> entry : YEItems.ITEMS.getEntries()) {
                    if (entry.get() instanceof ModEnchantBook){
                        for(RegistryObject<Enchantment> obj : YEEnchants.ENCHANTMENTS.getEntries()) {
                            Enchantment enchantment = obj.get();
                            for(int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                                output.accept(getStack(new EnchantmentInstance(enchantment, i)));
                            }
                        }
                    }else if (entry.get() instanceof OldBook){
                        for(Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
                            if (enchantment.getMaxLevel() != 1)
                                output.accept(OldBook.getStack(new EnchantmentInstance(enchantment, enchantment.getMaxLevel())));
                        }
                    }else output.accept(new ItemStack(entry.get()));
                }
            }).build());
}
