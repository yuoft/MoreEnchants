package com.yuo.Enchants.Event.Loot;

import com.google.common.base.Suppliers;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OldBookModifier extends LootModifier {
    public final Item item; //添加的物品

    public static final Supplier<Codec<OldBookModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(e ->
                            codecStart(e).and(ForgeRegistries.ITEMS.getCodec().fieldOf("oldbook").forGetter(m -> m.item))
                                    .apply(e, OldBookModifier::new)));

    protected OldBookModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }
    //修改战利品表
    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        float luck = context.getLuck();
        if (context.getRandom().nextFloat() < 0.1f + luck * 0.03) //概率
            generatedLoot.add(LootModifierHelper.getRandomOldBook(item)); //添加物品
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

//    public static class Serializers implements Serializer<OldBookModifier> {
//        @Override
//        public OldBookModifier read(ResourceLocation resourceLocation, JsonObject jsonObject, LootItemCondition[] lootItemConditions) {
//            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(jsonObject, "addition")));
//            return new OldBookModifier(lootItemConditions, item);
//        }
//
//        @Override
//        public JsonObject write(OldBookModifier oldBookFormBoxAddModifier) {
//            JsonObject jsonObject = makeConditions(oldBookFormBoxAddModifier.conditions);
//            jsonObject.addProperty("addition", ForgeRegistries.ITEMS.getKey(oldBookFormBoxAddModifier.item).toString());
//            return jsonObject;
//        }

//        @Override
//        public void serialize(JsonObject jsonObject, OldBookModifier oldBookModifier, JsonSerializationContext jsonSerializationContext) {
//            JsonObject jsonObject = makeConditions(oldBookFormBoxAddModifier.conditions);
//            jsonObject.addProperty("addition", ForgeRegistries.ITEMS.getKey(oldBookFormBoxAddModifier.item).toString());
//        }
//
//        @Override
//        public OldBookModifier deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
//            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(jsonObject, "addition")));
//            return new OldBookModifier(jsonDeserializationContext, item);
//        }
//    }

}
