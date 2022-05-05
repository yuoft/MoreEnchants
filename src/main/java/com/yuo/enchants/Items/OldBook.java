package com.yuo.enchants.Items;

import com.yuo.enchants.Enchants.EnchantRegistry;
import com.yuo.enchants.Enchants.ModEnchantBase;
import com.yuo.enchants.Items.Tab.ModGroup;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.*;

public class OldBook extends Item {

    public OldBook() {
        super(new Properties().group(ModGroup.myGroup).maxStackSize(1).rarity(Rarity.EPIC));
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack mainhand = playerIn.getHeldItemMainhand();
        ItemStack offhand = playerIn.getHeldItemOffhand();
        if (offhand.isEmpty() || handIn == Hand.OFF_HAND){
            playerIn.sendStatusMessage(new TranslationTextComponent("yuoenchants.message.old_book.fail"),true);
            return ActionResult.resultFail(mainhand);
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
                    playerIn.setItemStackToSlot(EquipmentSlotType.OFFHAND, copy);
                    //摆臂 消耗物品 提示 音效
                    playerIn.swingArm(Hand.MAIN_HAND);
                    mainhand.shrink(1);
                    playerIn.sendStatusMessage(new TranslationTextComponent("yuoenchants.message.old_book.success"), true);
                    if (worldIn.isRemote)
                        playerIn.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 3.0f);
                    return ActionResult.resultSuccess(mainhand);
                }
            }
        }
        playerIn.sendStatusMessage(new TranslationTextComponent("yuoenchants.message.old_book.fail"),true);
        return ActionResult.resultFail(mainhand);
    }

    //获取古卷上的附魔
    public static OldBookEnchant getEnch(ItemStack mainhand){
        ListNBT listNBT = EnchantedBookItem.getEnchantments(mainhand);
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT compoundnbt = listNBT.getCompound(i);
            Optional<Enchantment> optional = Registry.ENCHANTMENT.getOptional(ResourceLocation.tryCreate(compoundnbt.getString("id")));
            if (optional.isPresent()){
                return new OldBookEnchant(optional.get(), compoundnbt.getInt("lvl"));
            }
        }
        return new OldBookEnchant();
    }

    public static class OldBookEnchant{
        private Enchantment enchantment;
        private int level;
        public OldBookEnchant(){
            this.enchantment = null;
            this.level = 0;
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
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group == ModGroup.myGroup) {
            for(Enchantment enchantment : Registry.ENCHANTMENT) {
                if (enchantment.getMaxLevel() != 1 && enchantment.type != null)
                    items.add(getStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
            }
        }
    }

    public static ItemStack getStack(EnchantmentData enchantData) {
        ItemStack itemstack = new ItemStack(ItemRegistry.oldBook.get());
        EnchantedBookItem.addEnchantment(itemstack, enchantData);
        return itemstack;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        addEnchantmentTooltips(tooltip, EnchantedBookItem.getEnchantments(stack));
    }

    @OnlyIn(Dist.CLIENT)
    public static void addEnchantmentTooltips(List<ITextComponent> p_222120_0_, ListNBT p_222120_1_) {
        for(int i = 0; i < p_222120_1_.size(); ++i) {
            CompoundNBT compoundnbt = p_222120_1_.getCompound(i);
            Registry.ENCHANTMENT.getOptional(ResourceLocation.tryCreate(compoundnbt.getString("id"))).ifPresent((enchantment) -> {
                IFormattableTextComponent textComponent = new TranslationTextComponent(enchantment.getDisplayName(compoundnbt.getInt("lvl")).getString());
                p_222120_0_.add(textComponent.mergeStyle(TextFormatting.BLUE));
            });
        }

    }
}
