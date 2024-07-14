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

public class OldBookFromBoxAddModifier extends LootModifier {
    public final Item item;

    /**
     * Constructs a LootModifier.
     * 修改战利品之前要满足的条件， 要添加的物品
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected OldBookFromBoxAddModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }
    //生成的战利品表
    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (context.getRandom().nextFloat() >= 0.5f) //概率
            generatedLoot.add(new ItemStack(item)); //添加物品
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<OldBookFromBoxAddModifier> {
        @Override
        public OldBookFromBoxAddModifier read(ResourceLocation resourceLocation, JsonObject jsonObject, LootItemCondition[] lootItemConditions) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(jsonObject, "addition")));
            return new OldBookFromBoxAddModifier(lootItemConditions, item);
        }

        @Override
        public JsonObject write(OldBookFromBoxAddModifier oldBookFormBoxAddModifier) {
            JsonObject jsonObject = makeConditions(oldBookFormBoxAddModifier.conditions);
            jsonObject.addProperty("addition", ForgeRegistries.ITEMS.getKey(oldBookFormBoxAddModifier.item).toString());
            return jsonObject;
        }
    }

}
