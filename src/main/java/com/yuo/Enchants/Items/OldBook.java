package com.yuo.Enchants.Items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OldBook extends Item {

    public OldBook() {
        super(new Properties().tab(ModTab.youEnchants).stacksTo(1).rarity(Rarity.EPIC));
    }

    //附魔光效
    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack mainhand = pPlayer.getMainHandItem();
        ItemStack offhand = pPlayer.getOffhandItem();
        if (offhand.isEmpty() || pUsedHand == InteractionHand.OFF_HAND){
            pPlayer.displayClientMessage(new TranslatableComponent("yuoenchants.message.old_book.fail"),true);
            return InteractionResultHolder.consume(mainhand);
        }
        OldBookEnchant ench = getEnch(mainhand);
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(offhand);
        if (!ench.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                if (entry.getKey() == ench.getEnchantment() && entry.getValue() == ench.getLevel()){ //附魔和等级相同
                    map.keySet().removeIf(enchantment -> enchantment == entry.getKey()); //删除旧附魔
                    map.put(ench.getEnchantment(), ench.getLevel() + 1); //添加新附魔
                    //复制物品 添加附魔
                    ItemStack copy = offhand.copy();
                    EnchantmentHelper.setEnchantments(map, copy);
                    pPlayer.setItemSlot(EquipmentSlot.OFFHAND, copy);
                    //摆臂 消耗物品 提示 音效
                    pPlayer.swing(InteractionHand.MAIN_HAND);
                    mainhand.shrink(1);
                    pPlayer.displayClientMessage(new TranslatableComponent("yuoenchants.message.old_book.success"), true);
                    if (pLevel.isClientSide)
                        pPlayer.playSound(SoundEvents.PLAYER_LEVELUP, 1.0f, 3.0f);
                    return InteractionResultHolder.consume(mainhand);
                }
            }
        }
        //客户端消息
        pPlayer.displayClientMessage(new TranslatableComponent("yuoenchants.message.old_book.fail"),true);
        return InteractionResultHolder.consume(mainhand);
    }

    //获取古卷上的附魔
    public static OldBookEnchant getEnch(ItemStack mainhand){
        ListTag listNBT = EnchantedBookItem.getEnchantments(mainhand);
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundTag compoundnbt = listNBT.getCompound(i);
            Optional<Enchantment> optional = Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(compoundnbt.getString("id")));
            if (optional.isPresent()){
                return new OldBookEnchant(optional.get(), compoundnbt.getInt("lvl"));
            }
        }
        return new OldBookEnchant();
    }

    public static class OldBookEnchant{
        private final Enchantment enchantment;
        private final int level;
        public OldBookEnchant(){
            this.enchantment = Enchantments.BLOCK_EFFICIENCY;
            this.level = 1;
        }

        public OldBookEnchant(Enchantment enchantmentIn, int levelIn){
            this.enchantment = enchantmentIn;
            this.level = levelIn;
        }

        public boolean isEmpty(){
            return enchantment == null || level == 0;
        }

        public Enchantment getEnchantment() {
            return enchantment;
        }

        public int getLevel() {
            return level;
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (tab == ModTab.youEnchants) {
            for(Enchantment enchantment : Registry.ENCHANTMENT) {
                if (enchantment.getMaxLevel() != 1 && enchantment.category != null)
                    items.add(getStack(new EnchantmentInstance(enchantment, enchantment.getMaxLevel())));
            }
        }
    }

    public static ItemStack getStack(EnchantmentInstance enchantData) {
        ItemStack itemstack = new ItemStack(YEItems.oldBook.get());
        EnchantedBookItem.addEnchantment(itemstack, enchantData);
        return itemstack;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        addEnchantmentTooltips(pTooltipComponents, EnchantedBookItem.getEnchantments(pStack));
    }

    @OnlyIn(Dist.CLIENT)
    public static void addEnchantmentTooltips(List<Component> components, ListTag tag) {
        for(int i = 0; i < tag.size(); ++i) {
            CompoundTag compoundnbt = tag.getCompound(i);
            Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(compoundnbt.getString("id"))).ifPresent((enchantment) -> {
                TranslatableComponent textComponent = new TranslatableComponent(enchantment.getFullname(compoundnbt.getInt("lvl")).getString());
                components.add(textComponent.withStyle(ChatFormatting.BLUE));
            });
        }
    }
}
