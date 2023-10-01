package com.yuo.enchants.Event;

import com.yuo.enchants.Config;
import com.yuo.enchants.Enchants.DoubleJump;
import com.yuo.enchants.Enchants.EnchantRegistry;
import com.yuo.enchants.NetWork.NetWorkHandler;
import com.yuo.enchants.NetWork.KeyPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * 用户输入/快捷键
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyBindingEvent {
    public static final KeyBinding ENCHANT_KEY_C = new KeyBinding("key.yuoenchants.key_c", //分类
            KeyConflictContext.IN_GAME, KeyModifier.NONE, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, "key.yuoenchants"); //按键C 按键描述

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null){
            if (player.world.isRemote)
                NetWorkHandler.INSTANCE.sendToServer(new KeyPacket(67, ENCHANT_KEY_C.isPressed()));
        }
        if (player == null) return;
        int doubleJump = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.doubleJump.get(), player.getItemStackFromSlot(EquipmentSlotType.FEET));
        if (doubleJump > 0  && Config.SERVER.isDoubleJump.get()){
            DoubleJump.jump(player, doubleJump);
        }
    }
}
