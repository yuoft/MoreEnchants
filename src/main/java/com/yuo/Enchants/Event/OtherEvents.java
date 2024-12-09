package com.yuo.Enchants.Event;

import com.yuo.Enchants.Event.Loot.LootModifierHelper;
import com.yuo.Enchants.Items.OldBook;
import com.yuo.Enchants.Items.YEItems;
import com.yuo.Enchants.World.ModOreGen;
import com.yuo.Enchants.YuoEnchants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 处理其它功能事件
 */
@Mod.EventBusSubscriber(modid = YuoEnchants.MOD_ID)
public class OtherEvents {

    @SubscribeEvent
    public static void worldGen(BiomeLoadingEvent event) {
        ModOreGen.genOres(event);
    }

    //燃烧时间 竹炭
    @SubscribeEvent
    public static void smeltingItem(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem().equals(YEItems.bambooCoal.get())) {
            event.setBurnTime(200);
        }
    }

    //破魔宝珠在铁砧使用
    @SubscribeEvent
    public static void brokenMagicPearl(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        if (right.getItem().equals(YEItems.BrokenMagicPearl.get()) && left.getBaseRepairCost() > 0) { //物品附魔惩罚大于0
            ItemStack copy = left.copy();
            copy.setRepairCost(((copy.getBaseRepairCost() + 1) / 2 + 1) / 2); //降低一级物品附魔惩罚
            event.setCost(5);
            event.setOutput(copy);
        }
        if (right.getItem().equals(YEItems.SuperBrokenMagicPearl.get()) && left.getBaseRepairCost() > 0) {
            ItemStack copy = left.copy();
            copy.setRepairCost(0); //清除物品附魔惩罚
            event.setCost(10);
            event.setOutput(copy);
        }
        //古卷升级
        if (left.getItem() == YEItems.oldBook.get() && right.getItem() == YEItems.oldBook.get()) {
            OldBook.OldBookEnchant enchLeft = OldBook.getEnch(left);
            OldBook.OldBookEnchant enchRight = OldBook.getEnch(right);
            if (!enchLeft.isEmpty() && !enchRight.isEmpty()) {
                Enchantment enchantment0 = enchLeft.getEnchantment();
                Enchantment enchantment1 = enchRight.getEnchantment();
                int level0 = enchLeft.getLevel();
                int level1 = enchRight.getLevel();
                if (enchantment0 == enchantment1 && level0 == level1) {
                    if (level0 >= 9) return;
                    ItemStack stack = new ItemStack(YEItems.oldBook.get());
                    EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(enchantment0, level0 + 1));
                    event.setOutput(stack);
                    event.setCost(15);
                }
            }
        }
        //必灭宝珠
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(left);
        if (!enchantments.isEmpty() && isCures(enchantments)){
            if (right.getItem() == YEItems.CuresPearl.get()){
                Iterator<Enchantment> iterator = enchantments.keySet().iterator();
                while (iterator.hasNext()){
                    Enchantment enchantment = iterator.next();
                    if (enchantment.isCurse()){
                        iterator.remove();
                        break;
                    }
                }
                ItemStack stack = new ItemStack(left.getItem());
                EnchantmentHelper.setEnchantments(enchantments, stack);
                event.setOutput(stack);
                event.setCost(5);
            }

            if (right.getItem() == YEItems.SuperCuresPearl.get()){
                enchantments.keySet().removeIf(Enchantment::isCurse);
                ItemStack stack = new ItemStack(left.getItem());
                EnchantmentHelper.setEnchantments(enchantments, stack);
                event.setOutput(stack);
                event.setCost(10);
            }
        }
    }

    /**
     * 判断附魔集合中是否有负面附魔
     * @param map 集合
     * @return 是 true
     */
    private static boolean isCures(Map<Enchantment, Integer> map){
        for (Enchantment enchantment : map.keySet()) {
            if (enchantment.isCurse()) return true;
        }
        return false;
    }

    /**
     * 根据物品数据来构建一个CompoundNbt数据
     * @param stack 数据来源
     * @return nbt
     */
    private static CompoundTag getCompoundNbt(ItemStack stack){
        ListTag listNBT = EnchantedBookItem.getEnchantments(stack);
        CompoundTag nbt = new CompoundTag();
        nbt.put("StoredEnchantments", listNBT);
        return nbt;
    }



    //村民交易
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        VillagerProfession type = event.getType();
        if (VillagerProfession.LIBRARIAN.equals(type)) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(5).add(new ItemsForEmeraldsAndItemsTrade(Items.NETHER_STAR, 1, YEItems.BrokenMagicPearl.get(), 1, YEItems.SuperBrokenMagicPearl.get(), 1, 5, 8));
            trades.get(5).add(new ItemsForEmeraldsAndItemsTrade(Items.NETHER_STAR, 1, YEItems.CuresPearl.get(), 1, YEItems.SuperCuresPearl.get(), 1, 5, 8));
            trades.get(4).add(new EnchantedBookForEmeraldsTrade(LootModifierHelper.getRandomOldBook(YEItems.oldBook.get()),  6));
            trades.get(5).add(new EnchantedBookForEmeraldsTrade(LootModifierHelper.getRandomOldBook(YEItems.oldBook.get()), 8));
        }
    }

    static class EnchantedBookForEmeraldsTrade implements VillagerTrades.ItemListing {
        private final int xpValue;
        private final ItemStack stack;

        public EnchantedBookForEmeraldsTrade(ItemStack stack, int xpValueIn) {
            this.xpValue = xpValueIn;
            this.stack = stack;
        }

        public MerchantOffer getOffer(Entity trader, Random rand) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, 64), new ItemStack(Items.BOOK), this.stack, 2, this.xpValue, 0.1F);
        }
    }

    static class ItemsForEmeraldsAndItemsTrade implements VillagerTrades.ItemListing {
        private final ItemStack buyingItem;
        private final ItemStack buyingItem1;
        private final int buyingItemCount;
        private final int buyingItemCount1;
        //        private final int emeraldCount;
        private final ItemStack sellingItem;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForEmeraldsAndItemsTrade(ItemLike buyingItem, int buyingItemCount, ItemLike buyingItem1, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this(buyingItem, buyingItemCount, buyingItem1, 1, sellingItem, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForEmeraldsAndItemsTrade(ItemLike buyingItem, int buyingItemCount, ItemLike buyingItem1, int buyingItemCount1, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this.buyingItem = new ItemStack(buyingItem);
            this.buyingItemCount = buyingItemCount;
            this.buyingItem1 = new ItemStack(buyingItem1);
            this.buyingItemCount1 = buyingItemCount1;
            this.sellingItem = new ItemStack(sellingItem);
            this.sellingItemCount = sellingItemCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Nullable
        public MerchantOffer getOffer(Entity trader, Random rand) {
            return new MerchantOffer(new ItemStack(buyingItem1.getItem(), this.buyingItemCount1), new ItemStack(this.buyingItem.getItem(), this.buyingItemCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }
}
