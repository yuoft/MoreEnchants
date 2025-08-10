package com.yuo.Enchants.Event.Loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class SuperBookModifier extends LootModifier {
    public final Item item;
    public static final Supplier<Codec<SuperBookModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(e ->
                    codecStart(e).and(ForgeRegistries.ITEMS.getCodec().fieldOf("superbook").forGetter(m -> m.item))
                            .apply(e, SuperBookModifier::new)));


    protected SuperBookModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }
    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        float luck = context.getLuck();
        ResourceLocation id = context.getQueriedLootTableId();
        if (context.getRandom().nextFloat() < 0.15f + luck * 0.04 && isChests(id.toString()))
            generatedLoot.add(LootModifierHelper.getRandomSuperBook(item, luck));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    private static final List<String> CHESTS_LOOTS = Arrays.asList("minecraft:chests/abandoned_mineshaft", //废弃矿井中的运输矿车
            "minecraft:chests/bastion_treasure",//堡垒遗迹宝藏室中的箱子
            "minecraft:chests/buried_treasure",//埋藏的宝藏
            "minecraft:chests/desert_pyramid",//沙漠神殿的宝藏室里的箱子
            "minecraft:chests/end_city_treasure", //末地城里的箱子
            "minecraft:chests/igloo_chest",//雪屋地下室里的箱子
            "minecraft:chests/jungle_temple",//丛林神庙里的箱子
            "minecraft:chests/nether_bridge",//下界要塞里的箱子
            "minecraft:chests/shipwreck_treasure",//沉船的宝箱
            "minecraft:chests/simple_dungeon",//地牢里的箱子
            "minecraft:chests/stronghold_library",//要塞图书馆里的箱子
            "minecraft:chests/woodland_mansion",//林地府邸的箱子
            "minecraft:gameplay/fishing/treasure",//钓鱼附魔书
            "minecraft:gameplay/piglin_bartering");//猪灵交易

    /**
     * 判断是否是要生成的战利品
     */
    public static boolean isChests(String s){
        for (String loot : CHESTS_LOOTS) {
            if (loot.equals(s)) return true;
        }
        return false;
    }
}
