package com.yuo.Enchants.Items;

import com.google.common.collect.Maps;
import com.yuo.Enchants.Enchants.ModEnchantBase;
import com.yuo.Enchants.RlUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModEnchantBook extends Item {

    public ModEnchantBook() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
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
            Optional<Enchantment> optional = BuiltInRegistries.ENCHANTMENT.getOptional(RlUtils.tryParse(compoundnbt.getString("id")));
            if (optional.isPresent()){
                if (optional.get().isCurse()){
                    return Component.translatable("item.yuoenchants.mod_enchant_book").withStyle(ChatFormatting.GREEN);
                }else return Component.translatable("item.yuoenchants.mod_enchant_book").withStyle(ChatFormatting.DARK_PURPLE);
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
            BuiltInRegistries.ENCHANTMENT.getOptional(RlUtils.tryParse(compoundnbt.getString("id"))).ifPresent((enchantment) -> {
                pTooltipComponents.add(enchantment.getFullname(compoundnbt.getInt("lvl")));
                if (enchantment instanceof ModEnchantBase){
                    if (Screen.hasShiftDown()){
                        ModEnchantBase base = (ModEnchantBase) enchantment; //适用类型
                        pTooltipComponents.add(Component.translatable("enchantType.yuoenchants.type")
                                .append(Component.translatable("enchantType.yuoenchants." + base.category.name())));
                        //附魔描述
                        pTooltipComponents.add(Component.translatable("enchantInfo." + compoundnbt.getString("id")));
                    } else pTooltipComponents.add(Component.translatable("enchantInfo.yuoenchants:info"));
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

    public static Map<Enchantment, Integer> getEnchantments(ItemStack stack, boolean f) {
        if (stack.getItem() == Items.ENCHANTED_BOOK || stack.getItem() == YEItems.modEnchantBook.get()
                || stack.getItem() == YEItems.oldBook.get()){
            ListTag tag = stack.getTag().getList(EnchantedBookItem.TAG_STORED_ENCHANTMENTS, 0);
            return deserializeEnchantments(tag);
        }
        return EnchantmentHelper.getEnchantments(stack);
    }

    public static Map<Enchantment, Integer> deserializeEnchantments(ListTag serialized) {
        Map<Enchantment, Integer> map = Maps.newLinkedHashMap();

        for(int i = 0; i < serialized.size(); ++i) {
            CompoundTag compoundnbt = serialized.getCompound(i);
            BuiltInRegistries.ENCHANTMENT.getOptional(RlUtils.tryParse(compoundnbt.getString("id"))).ifPresent((enchantment) -> {
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
                compoundnbt.putString("id", String.valueOf(BuiltInRegistries.ENCHANTMENT.getKey(enchantment)));
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
