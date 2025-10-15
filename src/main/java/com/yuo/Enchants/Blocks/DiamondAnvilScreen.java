package com.yuo.Enchants.Blocks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.Enchants.NetWorkHandler;
import com.yuo.Enchants.RlUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DiamondAnvilScreen extends ItemCombinerScreen<DiamondAnvilMenu> {
    private static final ResourceLocation ANVIL_RESOURCE = RlUtils.wdn("textures/gui/container/anvil.png");
    private static final Component TOO_EXPENSIVE_TEXT = Component.translatable("container.repair.expensive");
    private EditBox name;
    private final Player player;

    public DiamondAnvilScreen(DiamondAnvilMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title, ANVIL_RESOURCE);
        this.player = playerInventory.player;
        this.titleLabelX = 60;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.name.tick();
    }

    @Override
    protected void subInit() {
        if (this.minecraft != null){
            int $$0 = (this.width - this.imageWidth) / 2;
            int $$1 = (this.height - this.imageHeight) / 2;
            this.name = new EditBox(this.font, $$0 + 62, $$1 + 24, 103, 12, Component.translatable("container.repair"));
            this.name.setCanLoseFocus(false);
            this.name.setTextColor(-1);
            this.name.setTextColorUneditable(-1);
            this.name.setBordered(false);
            this.name.setMaxLength(50);
            this.name.setResponder(this::onNameChanged);
            this.name.setValue("");
            this.addWidget(this.name);
            this.setInitialFocus(this.name);
            this.name.setEditable(false);
        }
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.name.getValue();
        this.init(minecraft, width, height);
        this.name.setValue(s);
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int i, int i1) {
        if (((this.menu).getSlot(0).hasItem() || (this.menu).getSlot(1).hasItem()) && !(this.menu).getSlot((this.menu).getResultSlot()).hasItem()) {
            guiGraphics.blit(ANVIL_RESOURCE, i + 99, i1 + 45, this.imageWidth, 0, 28, 21);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.minecraft != null) {
            if (this.minecraft.player != null)
                this.minecraft.player.closeContainer();
        }
        return this.name.keyPressed(keyCode, scanCode, modifiers) || this.name.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onNameChanged(String name) {
        if (!name.isEmpty() && this.player.level().isClientSide) {
            String s = name;
            Slot slot = this.menu.getSlot(0);
            if (slot.hasItem() && !slot.getItem().hasCustomHoverName() && name.equals(slot.getItem().getHoverName().getString())) {
                s = "";
            }

            this.menu.setItemName(s);
            NetWorkHandler.INSTANCE.sendToServer(new ServerAnvilRenamePacket(s));
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int pX, int pY) {
        super.renderLabels(graphics, pX, pY);
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
                component = Component.translatable("container.repair.cost", i);
                if (!this.menu.getSlot(2).mayPickup(this.player)) {
                    j = 16736352;
                }
            }

            if (component != null) {
                int k = this.imageWidth - 8 - this.font.width(component) - 2;
                int l = 69;
                graphics.fill(k - 2, 67, this.imageWidth - 8, 79, 1325400064);
                graphics.drawString(this.font, component, k, 69, j);
            }
        }

    }

    @Override
    public void renderFg(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.name.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    protected void renderBg(GuiGraphics graphics, float v, int i, int i1) {
        super.renderBg(graphics, v, i, i1);
        graphics.blit(ANVIL_RESOURCE, this.leftPos + 59, this.topPos + 20, 0, this.imageHeight + ((this.menu).getSlot(0).hasItem() ? 0 : 16), 110, 16);
    }

    @Override
    public void slotChanged(AbstractContainerMenu pContainerToSend, int pSlotInd, ItemStack pStack) {
        if (pSlotInd == 0) {
            this.name.setValue(pStack.isEmpty() ? "" : pStack.getHoverName().getString());
            this.name.setEditable(!pStack.isEmpty());
            this.setFocused(this.name);
        }

    }
}
