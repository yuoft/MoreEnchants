package com.yuo.Enchants.Event;

import com.mojang.blaze3d.platform.InputConstants;
import com.yuo.Enchants.Config;
import com.yuo.Enchants.Enchants.DoubleJump;
import com.yuo.Enchants.Enchants.EnchantRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
    public static final KeyMapping ENCHANT_KEY_C = new KeyMapping("key.yuoenchants.key_c", //分类
            KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, "key.yuoenchants"); //按键C 按键描述

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.level.isClientSide){
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            int doubleJump = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.doubleJump.get(), feet);
            boolean keyDown = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_SPACE);
            //按下空格 有附魔
            if (keyDown && doubleJump > 0 && Config.SERVER.isDoubleJump.get()){
                DoubleJump.jump(player);
            }
        }
    }
}
