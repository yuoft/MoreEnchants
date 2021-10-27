package com.yuo.enchants.Event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
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

    private static boolean IS_KEY_C = false; //是否按住快捷键

    public static final KeyBinding MESSAGE_KEY = new KeyBinding("key.message", //分类
            KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_C, //按键C
            "key.category.key_c"); //按键描述

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (MESSAGE_KEY.isPressed()) {
            if (Minecraft.getInstance().player != null){
                IS_KEY_C = true;
                return;
            }
        }
        IS_KEY_C = false;
    }

    public static boolean isIsKeyC() {
        return IS_KEY_C;
    }

    public static void setIsKeyC(boolean isKeyC) {
        IS_KEY_C = isKeyC;
    }
}
