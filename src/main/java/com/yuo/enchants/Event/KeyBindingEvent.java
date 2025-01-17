package com.yuo.enchants.Event;

import com.yuo.enchants.Config;
import com.yuo.enchants.Enchants.DoubleJump;
import com.yuo.enchants.Enchants.EnchantRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 用户输入/快捷键
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyBindingEvent {

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        int doubleJump = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.doubleJump.get(), player.getItemStackFromSlot(EquipmentSlotType.FEET));
        if (doubleJump > 0  && Config.SERVER.isDoubleJump.get()){
            DoubleJump.jump(player, doubleJump);
        }
    }
}
