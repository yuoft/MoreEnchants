package com.yuo.Enchants.Event.Loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

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
        if (context.getRandom().nextFloat() < 0.15f + luck * 0.04)
            generatedLoot.add(LootModifierHelper.getRandomSuperBook(item, luck));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
