package com.yuo.Enchants.Event.Loot;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SuperBookModifier extends LootModifier {
    public final Item item;

    protected SuperBookModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }
    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        float luck = context.getLuck();
        if (context.getRandom().nextFloat() < 0.15f + luck * 0.04)
            generatedLoot.add(LootModifierHelper.getRandomSuperBook(item, luck));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<SuperBookModifier> {
        @Override
        public SuperBookModifier read(ResourceLocation resourceLocation, JsonObject jsonObject, LootItemCondition[] lootItemConditions) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(jsonObject, "addition")));
            return new SuperBookModifier(lootItemConditions, item);
        }

        @Override
        public JsonObject write(SuperBookModifier oldBookFormBoxAddModifier) {
            JsonObject jsonObject = makeConditions(oldBookFormBoxAddModifier.conditions);
            jsonObject.addProperty("addition", ForgeRegistries.ITEMS.getKey(oldBookFormBoxAddModifier.item).toString());
            return jsonObject;
        }
    }

}
