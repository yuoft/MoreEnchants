package com.yuo.Enchants.Event.Loot;

import com.yuo.Enchants.Enchants.EnchantRegistry;
import com.yuo.Enchants.Enchants.ModEnchantBase;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class LootModifierHelper {
    public static final Random RANDOM = new Random();
    //初始化模组附魔列表
    public static final ArrayList<Enchantment> MOD_ENCHANTS = new ArrayList<>();
    //初始化原版附魔列表
    private static final ArrayList<Enchantment> ENCHANTS = new ArrayList<>();

    static {
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            if (enchantment.getMaxLevel() != 1 && enchantment.category != null) {
                ENCHANTS.add(enchantment);
            }
        }
    }

    static {
        for (RegistryObject<Enchantment> entry : EnchantRegistry.ENCHANTMENTS.getEntries()) {
            Enchantment enchantment = entry.get();
            if (enchantment instanceof ModEnchantBase) {
                MOD_ENCHANTS.add(enchantment);
            }
        }
    }

    /**
     * 获取一个含有随机附魔的古卷
     *
     * @return 物品
     */
    public static ItemStack getRandomOldBook(Item item) {
        ItemStack stack = new ItemStack(item);
        Enchantment enchantment = ENCHANTS.get(new Random().nextInt(ENCHANTS.size()));
        if (enchantment != null)
            EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(enchantment, enchantment.getMaxLevel()));
        else EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(Enchantments.BLOCK_EFFICIENCY, 5));
        return stack;
    }

    /**
     * 生成一本随机高级附魔书
     *
     * @param item 高级附魔书
     * @return 带附魔的书
     */
    public static ItemStack getRandomSuperBook(Item item, float luck) {
        ItemStack stack = new ItemStack(item);
        //抽奖次数
        int num = 0;
        if (RANDOM.nextDouble() < 0.5 + luck * 0.1)
            Mth.nextInt(RANDOM, 1, 2 + Mth.floor(luck / 2.0));
        else num = 1;
        while (num > 0) {
            num--;
            Enchantment enchantment = MOD_ENCHANTS.get(new Random().nextInt(MOD_ENCHANTS.size()));
            if (!isModEnchantment(stack, enchantment)) continue;
            if (enchantment != null) {
                EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(enchantment, Mth.nextInt(RANDOM, 1, enchantment.getMaxLevel())));
            }
        }
        if (stack.getEnchantmentTags().isEmpty())
            EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(EnchantRegistry.warToWar.get(), Mth.nextInt(RANDOM, 1, 5)));
        return stack;
    }

    /**
     * 附魔是否冲突
     *
     * @param stack       物品
     * @param enchantment 附魔
     * @return 冲突 true
     */
    public static boolean isModEnchantment(ItemStack stack, Enchantment enchantment) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        return EnchantmentHelper.isEnchantmentCompatible(enchantments.keySet(), enchantment);
    }
}
