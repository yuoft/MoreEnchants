package com.yuo.Enchants.Items;

import com.google.common.collect.Maps;
import com.yuo.Enchants.Enchants.EnchantRegistry;
import com.yuo.Enchants.Enchants.ModEnchantBase;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModEnchantBook extends Item {

    public ModEnchantBook() {
        super(new Properties().tab(ModTab.youEnchants).stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (tab == ModTab.youEnchants) {
            for(RegistryObject<Enchantment> obj : EnchantRegistry.ENCHANTMENTS.getEntries()) {
                Enchantment enchantment = obj.get();
                for(int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                    items.add(getStack(new EnchantmentInstance(enchantment, i)));
                }
            }
        }
    }

    public static ItemStack getStack(EnchantmentInstance enchantData) {
        ItemStack itemstack = new ItemStack(YEItems.modEnchantBook.get());
        EnchantedBookItem.addEnchantment(itemstack, enchantData);
        return itemstack;
    }

    @Override
    public Component getName(ItemStack pStack) {
        ListTag listNBT = EnchantedBookItem.getEnchantments(pStack);
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundTag compoundnbt = listNBT.getCompound(i);
            Optional<Enchantment> optional = Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(compoundnbt.getString("id")));
            if (optional.isPresent()){
                if (optional.get().isCurse()){
                    return new TranslatableComponent("item.yuoenchants.mod_enchant_book").withStyle(ChatFormatting.GREEN);
                }else return new TranslatableComponent("item.yuoenchants.mod_enchant_book").withStyle(ChatFormatting.DARK_PURPLE);
            }
        }
        return super.getName(pStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        ListTag listNBT = EnchantedBookItem.getEnchantments(pStack);
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundTag compoundnbt = listNBT.getCompound(i);
            Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(compoundnbt.getString("id"))).ifPresent((enchantment) -> {
                pTooltipComponents.add(enchantment.getFullname(compoundnbt.getInt("lvl")));
                if (enchantment instanceof ModEnchantBase){
                    if (Screen.hasShiftDown()){
                        ModEnchantBase base = (ModEnchantBase) enchantment; //适用类型
                        pTooltipComponents.add(new TranslatableComponent("enchantType.yuoenchants.type")
                                .append(new TranslatableComponent("enchantType.yuoenchants." + base.getTypeName())));
                        //附魔描述
                        pTooltipComponents.add(new TranslatableComponent("enchantInfo." + compoundnbt.getString("id")));
                    } else pTooltipComponents.add(new TranslatableComponent("enchantInfo.yuoenchants:info"));
                }
            });
        }
    }

    //获取物品附魔map
    public static ListTag getEnchantments(ItemStack stack) {
        if (stack.getItem() == Items.ENCHANTED_BOOK || stack.getItem() == YEItems.modEnchantBook.get()
                || stack.getItem() == YEItems.oldBook.get()){
            deserializeEnchantments(stack.getEnchantmentTags());
//            CompoundTag compoundtag = stack.getTag();
//            return compoundtag != null ? compoundtag.getList("StoredEnchantments", 10) : new ListTag();
        }
        return EnchantedBookItem.getEnchantments(stack);
    }

    public static Map<Enchantment, Integer> deserializeEnchantments(ListTag serialized) {
        Map<Enchantment, Integer> map = Maps.newLinkedHashMap();

        for(int i = 0; i < serialized.size(); ++i) {
            CompoundTag compoundnbt = serialized.getCompound(i);
            Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(compoundnbt.getString("id"))).ifPresent((enchantment) -> {
                map.put(enchantment, compoundnbt.getInt("lvl"));
            });
        }

        return map;
    }

    //设置物品附魔
    public static void setEnchantments(Map<Enchantment, Integer> enchMap, ItemStack stack) {
        ListTag listnbt = new ListTag();

        for(Map.Entry<Enchantment, Integer> entry : enchMap.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                int i = entry.getValue();
                CompoundTag compoundnbt = new CompoundTag();
                compoundnbt.putString("id", String.valueOf((Object)Registry.ENCHANTMENT.getKey(enchantment)));
                compoundnbt.putShort("lvl", (short)i);
                listnbt.add(compoundnbt);
                if (stack.getItem() == Items.ENCHANTED_BOOK || stack.getItem() == YEItems.modEnchantBook.get()) {
                    EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(enchantment, i));
                }
            }
        }

        if (listnbt.isEmpty()) {
            stack.removeTagKey("Enchantments");
        } else if (stack.getItem() != Items.ENCHANTED_BOOK && stack.getItem() != YEItems.modEnchantBook.get()) {
            stack.addTagElement("Enchantments", listnbt);
        }

    }
}
