package com.yuo.Enchants.Blocks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DiamondAnvilScreen extends ItemCombinerScreen<DiamondAnvilMenu> {
    private static final ResourceLocation ANVIL_RESOURCE = new ResourceLocation("textures/gui/container/anvil.png");
    private static final Component TOO_EXPENSIVE_TEXT = new TranslatableComponent("container.repair.expensive");
    private EditBox name;
    private final Player player;

    public DiamondAnvilScreen(DiamondAnvilMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title, ANVIL_RESOURCE);
        this.player = playerInventory.player;
        this.titleLabelX = 60;
    }

    public void containerTick() {
        super.containerTick();
        this.name.tick();
    }

    protected void initFields() {
        if (this.minecraft != null){
            this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;
            this.name = new EditBox(this.font, i + 62, j + 24, 103, 12, new TranslatableComponent("container.repair"));
            this.name.setCanLoseFocus(false);
            this.name.setTextColor(-1);
            this.name.setTextColorUneditable(-1);
            this.name.setBordered(false);
            this.name.setMaxLength(35);
            this.name.setResponder(this::onNameChanged);
            this.name.setValue("");
            this.addWidget(this.name);
            this.setInitialFocus(this.name);
            this.name.setEditable(false);
        }
    }

    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.name.getValue();
        this.init(minecraft, width, height);
        this.name.setValue(s);
    }

    public void onClose() {
        super.onClose();
        if (this.minecraft != null)
            this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.minecraft != null) {
            if (this.minecraft.player != null)
                this.minecraft.player.closeContainer();
        }

        return this.name.keyPressed(keyCode, scanCode, modifiers) || this.name.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onNameChanged(String name) {
        if (!name.isEmpty()) {
            String s = name;
            Slot slot = this.menu.getSlot(0);
            if (slot.hasItem() && !slot.getItem().hasCustomHoverName() && name.equals(slot.getItem().getHoverName().getString())) {
                s = "";
            }

            this.menu.setItemName(s);
            if (this.minecraft != null && this.minecraft.player != null)
                this.minecraft.player.connection.send(new ServerboundRenameItemPacket(s));
        }
    }

    protected void renderLabels(PoseStack pPoseStack, int pX, int pY) {
        RenderSystem.disableBlend();
        super.renderLabels(pPoseStack, pX, pY);
        int i = this.menu.getCost();
        if (i > 0) {
            int j = 8453920;
            Component component;
            if (i >= 256 && this.minecraft != null  && this.minecraft.player != null
                    && !this.minecraft.player.getAbilities().instabuild) {
                component = TOO_EXPENSIVE_TEXT;
                j = 16736352;
            } else if (!this.menu.getSlot(2).hasItem()) {
                component = null;
            } else {
                component = new TranslatableComponent("container.repair.cost", i);
                if (!this.menu.getSlot(2).mayPickup(this.player)) {
                    j = 16736352;
                }
            }

            if (component != null) {
                int k = this.imageWidth - 8 - this.font.width(component) - 2;
                int l = 69;
                fill(pPoseStack, k - 2, 67, this.imageWidth - 8, 79, 1325400064);
                this.font.drawShadow(pPoseStack, component, (float)k, 69.0F, j);
            }
        }

    }

    public void renderNameField(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.name.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot.
     */
    public void sendSlotContents(AbstractContainerMenu containerToSend, int slotInd, ItemStack stack) {
        if (slotInd == 0) {
            this.name.setValue(stack.isEmpty() ? "" : stack.getDisplayName().getString());
            this.name.setEditable(!stack.isEmpty());
            this.setFocused(this.name);
        }

    }
}