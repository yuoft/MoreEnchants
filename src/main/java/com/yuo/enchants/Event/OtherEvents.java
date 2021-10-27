package com.yuo.enchants.Event;

import com.yuo.enchants.Items.ItemRegistry;
import com.yuo.enchants.MoreEnchants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 *  处理其它功能事件
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MoreEnchants.MODID)
public class OtherEvents {
    //燃烧时间 竹炭
    @SubscribeEvent
    public static void smeltingItem(FurnaceFuelBurnTimeEvent event){
        ItemStack stack = event.getItemStack();
        if (stack.getItem().equals(ItemRegistry.bambooCoal.get())){
            event.setBurnTime(200);
        }
    }
    //破魔宝珠在铁砧使用
    @SubscribeEvent
    public static void brokenMagicPearl(AnvilUpdateEvent event) {
        ItemStack stack=event.getLeft();
        ItemStack stack2=event.getRight();
        if (stack.getRepairCost() <= 0) return;
        if(stack2.getItem().equals(ItemRegistry.BrokenMagicPearl.get()))
        {
            ItemStack copy = stack.copy();
            copy.setRepairCost(((copy.getRepairCost() + 1) / 2 + 1) / 2); //降低一级物品附魔惩罚
            event.setCost(5);
            event.setOutput(copy);
        }
        if(stack2.getItem().equals(ItemRegistry.BrokenMagicPearlSuper.get()))
        {
            ItemStack copy = stack.copy();
            copy.setRepairCost(0); //清除物品附魔惩罚
            event.setCost(10);
            event.setOutput(copy);
        }
    }
    //村民交易
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        VillagerProfession type = event.getType();
        if (VillagerProfession.LIBRARIAN.equals(type)){
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
            trades.get(5).add(new ItemsForEmeraldsAndItemsTrade(Items.NETHER_STAR, 1,ItemRegistry.BrokenMagicPearl.get(), 1,  ItemRegistry.BrokenMagicPearlSuper.get(), 1,5, 8));
        }
    }
    static class ItemsForEmeraldsAndItemsTrade implements VillagerTrades.ITrade {
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

        public ItemsForEmeraldsAndItemsTrade(IItemProvider buyingItem, int buyingItemCount,IItemProvider buyingItem1 , Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this(buyingItem, buyingItemCount, buyingItem1, 1, sellingItem, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForEmeraldsAndItemsTrade(IItemProvider buyingItem, int buyingItemCount, IItemProvider buyingItem1, int buyingItemCount1, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
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
