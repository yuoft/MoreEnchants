package com.yuo.enchants.Event;

import com.yuo.enchants.Enchants.EnchantRegistry;
import com.yuo.enchants.Enchants.ModEnchantBase;
import com.yuo.enchants.Items.OldBook;
import com.yuo.enchants.Items.YEItems;
import com.yuo.enchants.YuoEnchants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;

/**
 * 处理其它功能事件
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = YuoEnchants.MOD_ID)
public class OtherEvents {
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
        if (right.getItem().equals(YEItems.BrokenMagicPearl.get()) && left.getRepairCost() > 0) { //物品附魔惩罚大于0
            ItemStack copy = left.copy();
            copy.setRepairCost(((copy.getRepairCost() + 1) / 2 + 1) / 2); //降低一级物品附魔惩罚
            event.setCost(5);
            event.setOutput(copy);
        }
        if (right.getItem().equals(YEItems.SuperBrokenMagicPearl.get()) && left.getRepairCost() > 0) {
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
                    EnchantedBookItem.addEnchantment(stack, new EnchantmentData(enchantment0, level0 + 1));
                    event.setOutput(stack);
                    event.setCost(15);
                }
            }
        }
        //必灭宝珠
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(left);
        if (enchantments.size() > 0 && isCures(enchantments)){
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

    private static final ResourceLocation[] RS = { //将要追加的战利品表 列表
            LootTables.CHESTS_JUNGLE_TEMPLE, //丛林神殿
            LootTables.GAMEPLAY_FISHING, //钓鱼
            LootTables.CHESTS_DESERT_PYRAMID, //沙漠神殿
            LootTables.BASTION_TREASURE, //猪灵堡垒
            LootTables.CHESTS_END_CITY_TREASURE, //末地城
            LootTables.CHESTS_STRONGHOLD_LIBRARY, //末地地牢图书馆
            LootTables.CHESTS_BURIED_TREASURE, //埋藏的宝藏
            LootTables.CHESTS_WOODLAND_MANSION, //林地府邸
            LootTables.CHESTS_SHIPWRECK_TREASURE}; //沉船宝箱

    //战利品添加
    @SubscribeEvent
    public static void lootTableAdd(LootTableLoadEvent event){
        ResourceLocation name = event.getName();
        for (ResourceLocation r : RS) {
            if (r.equals(name)) {
                LootTable table = event.getTable();
                table.addPool(getPoolOldBook());
                table.addPool(getPoolSuperBook());
            }
        }
    }

    /**
     * 获取一个战利品奖池
     * @return 含有5个项的奖池
     */
    private static LootPool getPoolOldBook(){
        LootPool.Builder builder = new LootPool.Builder().name("old_book")
                .addEntry(getEntryOldBook()).addEntry(getEntryOldBook()).addEntry(getEntryOldBook()).addEntry(getEntryOldBook()).addEntry(getEntryOldBook())
                .acceptCondition(RandomChance.builder(0.1f)) //通过概率
                .rolls(new RandomValueRange(1, 2)).bonusRolls(0, 1); //抽取次数：1~4 幸运增加的抽取次数
        return builder.build();
    }

    private static LootPool getPoolSuperBook(){
        LootPool.Builder builder = new LootPool.Builder().name("super_enchant_book")
                .addEntry(getEntrySuperBook()).addEntry(getEntrySuperBook()).addEntry(getEntrySuperBook()).addEntry(getEntrySuperBook()).addEntry(getEntrySuperBook())
                .acceptCondition(RandomChance.builder(0.15f))
                .rolls(new RandomValueRange(1, 3)).bonusRolls(0, 2);
        return builder.build();
    }

    /**
     * 构造一个奖池项目
     * @return 奖池项目
     */
    private static StandaloneLootEntry.Builder<?> getEntryOldBook(){
        return ItemLootEntry.builder(YEItems.oldBook.get()).quality(8).weight(4)//物品 幸运影响 权重
                .acceptFunction(SetNBT.builder(getCompoundNbt(getRandomOldBook()))); //添加nbt数据
    }

    private static StandaloneLootEntry.Builder<?> getEntrySuperBook(){
        return ItemLootEntry.builder(YEItems.modEnchantBook.get()).quality(10).weight(5)
                .acceptFunction(SetNBT.builder(getCompoundNbt(getRandomSuperBook())));
    }

    /**
     * 根据物品数据来构建一个CompoundNbt数据
     * @param stack 数据来源
     * @return nbt
     */
    private static CompoundNBT getCompoundNbt(ItemStack stack){
        ListNBT listNBT = EnchantedBookItem.getEnchantments(stack);
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("StoredEnchantments", listNBT);
        return nbt;
    }

    //初始化原版附魔列表
    private static final ArrayList<Enchantment> ENCHANTS = new ArrayList<>();
    static {
        for(Enchantment enchantment : Registry.ENCHANTMENT) {
            if (enchantment.getMaxLevel() != 1 && enchantment.type != null){
                ENCHANTS.add(enchantment);
            }
        }
    }

    /**
     * 获取一个含有随机附魔的古卷
     * @return 物品
     */
    private static ItemStack getRandomOldBook(){
        ItemStack stack = new ItemStack(YEItems.oldBook.get());
        Enchantment enchantment = ENCHANTS.get(new Random().nextInt(ENCHANTS.size()));
        if (enchantment != null)
            EnchantedBookItem.addEnchantment(stack, new EnchantmentData(enchantment, enchantment.getMaxLevel()));
        else EnchantedBookItem.addEnchantment(stack, new EnchantmentData(Enchantments.EFFICIENCY, 5));
        return stack;
    }

    private static ItemStack getRandomSuperBook(){
        ItemStack stack = new ItemStack(YEItems.modEnchantBook.get());
        ArrayList<Enchantment> SUPER_ENCHANTS = new ArrayList<>();
        for (RegistryObject<Enchantment> entry : EnchantRegistry.ENCHANTMENTS.getEntries()) {
            Enchantment enchantment = entry.get();
            if (enchantment instanceof ModEnchantBase){
                SUPER_ENCHANTS.add(enchantment);
            }
        }
        Enchantment enchantment = SUPER_ENCHANTS.get(new Random().nextInt(ENCHANTS.size()));
        if (enchantment != null)
            EnchantedBookItem.addEnchantment(stack, new EnchantmentData(enchantment, enchantment.getMaxLevel()));
        else EnchantedBookItem.addEnchantment(stack, new EnchantmentData(Enchantments.EFFICIENCY, 5));
        return stack;
    }

    //村民交易
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        VillagerProfession type = event.getType();
        if (VillagerProfession.LIBRARIAN.equals(type)) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
            trades.get(5).add(new ItemsForEmeraldsAndItemsTrade(Items.NETHER_STAR, 1, YEItems.BrokenMagicPearl.get(), 1, YEItems.SuperBrokenMagicPearl.get(), 1, 5, 8));
            trades.get(5).add(new ItemsForEmeraldsAndItemsTrade(Items.NETHER_STAR, 1, YEItems.CuresPearl.get(), 1, YEItems.SuperCuresPearl.get(), 1, 5, 8));
            trades.get(4).add(new EnchantedBookForEmeraldsTrade(getRandomOldBook(),  6));
            trades.get(5).add(new EnchantedBookForEmeraldsTrade(getRandomOldBook(), 8));
        }
    }

    static class EnchantedBookForEmeraldsTrade implements VillagerTrades.ITrade {
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

        public ItemsForEmeraldsAndItemsTrade(IItemProvider buyingItem, int buyingItemCount, IItemProvider buyingItem1, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
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
