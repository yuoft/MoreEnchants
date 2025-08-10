package com.yuo.Enchants.Event.Loot;

import com.yuo.Enchants.Items.YEItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class GlobalLootModifier extends GlobalLootModifierProvider {
    public GlobalLootModifier(PackOutput output, String modid) {
        super(output, modid);
    }

    @Override
    protected void start() {
        add("chest_oldbook", new OldBookModifier(new LootItemCondition[]{}, YEItems.oldBook.get()));
        add("end_city_treasure", new SuperBookModifier(new LootItemCondition[]{}, YEItems.modEnchantBook.get()));
    }
}
