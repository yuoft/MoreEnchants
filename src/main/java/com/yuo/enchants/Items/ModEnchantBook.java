package com.yuo.enchants.Items;

import com.google.common.collect.Maps;
import com.yuo.enchants.Enchants.EnchantRegistry;
import com.yuo.enchants.Enchants.ModEnchantBase;
import com.yuo.enchants.Items.Tab.ModGroup;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ModEnchantBook extends Item {

    public ModEnchantBook() {
        super(new Properties().group(ModGroup.youEnchants).maxStackSize(1));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group == ModGroup.youEnchants) {
            for(RegistryObject<Enchantment> obj : EnchantRegistry.ENCHANTMENTS.getEntries()) {
                Enchantment enchantment = obj.get();
                for(int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                    items.add(getStack(new EnchantmentData(enchantment, i)));
                }
            }
        }
    }

    public static ItemStack getStack(EnchantmentData enchantData) {
        ItemStack itemstack = new ItemStack(YEItems.modEnchantBook.get());
        EnchantedBookItem.addEnchantment(itemstack, enchantData);
        return itemstack;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        ListNBT listNBT = EnchantedBookItem.getEnchantments(stack);
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT compoundnbt = listNBT.getCompound(i);
            Optional<Enchantment> optional = Registry.ENCHANTMENT.getOptional(ResourceLocation.tryCreate(compoundnbt.getString("id")));
            if (optional.isPresent()){
                if (optional.get().isCurse()){
                    return new TranslationTextComponent("item.yuoenchants.mod_enchant_book").mergeStyle(TextFormatting.GREEN);
                }else return new TranslationTextComponent("item.yuoenchants.mod_enchant_book").mergeStyle(TextFormatting.DARK_PURPLE);
            }
        }
        return super.getDisplayName(stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        ListNBT listNBT = EnchantedBookItem.getEnchantments(stack);
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT compoundnbt = listNBT.getCompound(i);
            Registry.ENCHANTMENT.getOptional(ResourceLocation.tryCreate(compoundnbt.getString("id"))).ifPresent((enchantment) -> {
                tooltip.add(enchantment.getDisplayName(compoundnbt.getInt("lvl")));
                if (enchantment instanceof ModEnchantBase){
                    if (Screen.hasShiftDown()){
                        ModEnchantBase base = (ModEnchantBase) enchantment; //适用类型
                        tooltip.add(new TranslationTextComponent("enchantType.yuoenchants.type")
                                .appendSibling(new TranslationTextComponent("enchantType.yuoenchants." + base.getTypeName())));
                        //附魔描述
                        tooltip.add(new TranslationTextComponent("enchantInfo." + compoundnbt.getString("id")));
                    } else tooltip.add(new TranslationTextComponent("enchantInfo.yuoenchants:info"));
                }
            });
        }
    }

    //获取物品附魔map
    public static Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
        ListNBT listnbt = stack.getItem() == Items.ENCHANTED_BOOK || stack.getItem() == YEItems.modEnchantBook.get()
                || stack.getItem() == YEItems.oldBook.get() ? EnchantedBookItem.getEnchantments(stack) : stack.getEnchantmentTagList();
        return deserializeEnchantments(listnbt);
    }

    public static Map<Enchantment, Integer> deserializeEnchantments(ListNBT serialized) {
        Map<Enchantment, Integer> map = Maps.newLinkedHashMap();

        for(int i = 0; i < serialized.size(); ++i) {
            CompoundNBT compoundnbt = serialized.getCompound(i);
            Registry.ENCHANTMENT.getOptional(ResourceLocation.tryCreate(compoundnbt.getString("id"))).ifPresent((enchantment) -> {
                map.put(enchantment, compoundnbt.getInt("lvl"));
            });
        }

        return map;
    }

    //设置物品附魔
    public static void setEnchantments(Map<Enchantment, Integer> enchMap, ItemStack stack) {
        ListNBT listnbt = new ListNBT();

        for(Map.Entry<Enchantment, Integer> entry : enchMap.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                int i = entry.getValue();
                CompoundNBT compoundnbt = new CompoundNBT();
                compoundnbt.putString("id", String.valueOf((Object)Registry.ENCHANTMENT.getKey(enchantment)));
                compoundnbt.putShort("lvl", (short)i);
                listnbt.add(compoundnbt);
                if (stack.getItem() == Items.ENCHANTED_BOOK || stack.getItem() == YEItems.modEnchantBook.get()) {
                    EnchantedBookItem.addEnchantment(stack, new EnchantmentData(enchantment, i));
                }
            }
        }

        if (listnbt.isEmpty()) {
            stack.removeChildTag("Enchantments");
        } else if (stack.getItem() != Items.ENCHANTED_BOOK && stack.getItem() != YEItems.modEnchantBook.get()) {
            stack.setTagInfo("Enchantments", listnbt);
        }

    }
}
