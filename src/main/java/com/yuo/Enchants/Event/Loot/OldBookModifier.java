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

public class OldBookModifier extends LootModifier {
    public final Item item; //添加的物品

    protected OldBookModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }
    //修改战利品表
    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        float luck = context.getLuck();
        if (context.getRandom().nextFloat() < 0.1f + luck * 0.03) //概率
            generatedLoot.add(LootModifierHelper.getRandomOldBook(item)); //添加物品
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<OldBookModifier> {
        @Override
        public OldBookModifier read(ResourceLocation resourceLocation, JsonObject jsonObject, LootItemCondition[] lootItemConditions) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(jsonObject, "addition")));
            return new OldBookModifier(lootItemConditions, item);
        }

        @Override
        public JsonObject write(OldBookModifier oldBookFormBoxAddModifier) {
            JsonObject jsonObject = makeConditions(oldBookFormBoxAddModifier.conditions);
            jsonObject.addProperty("addition", ForgeRegistries.ITEMS.getKey(oldBookFormBoxAddModifier.item).toString());
            return jsonObject;
        }
    }

}
