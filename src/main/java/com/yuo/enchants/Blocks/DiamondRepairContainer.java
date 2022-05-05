package com.yuo.enchants.Blocks;

import com.yuo.enchants.Items.ItemRegistry;
import com.yuo.enchants.Items.ModEnchantBook;
import com.yuo.enchants.Items.ModTags;
import com.yuo.enchants.Items.OldBook;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;

public class DiamondRepairContainer extends AbstractRepairContainer {
    private static final Logger LOGGER = LogManager.getLogger();
    public int materialCost;
    private String repairedItemName;
    private final IntReferenceHolder maximumCost = IntReferenceHolder.single();
    private final ITag<Block> DIAMOND_ANVIL_TAG = BlockTags.getCollection().get(ModTags.DIAMOND_ANVIL);

    public DiamondRepairContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, IWorldPosCallable.DUMMY);
    }

    public DiamondRepairContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
        super(ContainerRegister.diamondAnvil.get(), id, playerInventory, worldPosCallable);
        this.trackInt(this.maximumCost);
    }

    protected boolean func_230302_a_(BlockState state) {
        return state.isIn(DIAMOND_ANVIL_TAG);
    }

    protected boolean func_230303_b_(PlayerEntity player, boolean bool) {
        return (player.abilities.isCreativeMode || player.experienceLevel >= this.maximumCost.get()) && this.maximumCost.get() > 0;
    }

    protected ItemStack func_230301_a_(PlayerEntity player, ItemStack stack) {
        if (!player.abilities.isCreativeMode) {
            player.addExperienceLevel(-this.maximumCost.get());
        }

        float breakChance = onAnvilRepair(player, stack, DiamondRepairContainer.this.field_234643_d_.getStackInSlot(0), DiamondRepairContainer.this.field_234643_d_.getStackInSlot(1));

        this.field_234643_d_.setInventorySlotContents(0, ItemStack.EMPTY);
        if (this.materialCost > 0) {
            ItemStack itemstack = this.field_234643_d_.getStackInSlot(1);
            if (!itemstack.isEmpty() && itemstack.getCount() > this.materialCost) {
                itemstack.shrink(this.materialCost);
                this.field_234643_d_.setInventorySlotContents(1, itemstack);
            } else {
                this.field_234643_d_.setInventorySlotContents(1, ItemStack.EMPTY);
            }
        } else {
            this.field_234643_d_.setInventorySlotContents(1, ItemStack.EMPTY);
        }

        this.maximumCost.set(0);
        this.field_234644_e_.consume((world, pos) -> {
            BlockState blockstate = world.getBlockState(pos);
            if (!player.abilities.isCreativeMode && blockstate.isIn(DIAMOND_ANVIL_TAG) && player.getRNG().nextFloat() < breakChance) {
                BlockState blockstate1 = DiamondAnvil.damage(blockstate);
                if (blockstate1 == null) {
                    world.removeBlock(pos, false);
                    world.playEvent(1029, pos, 0);
                } else {
                    world.setBlockState(pos, blockstate1, 2);
                    world.playEvent(1030, pos, 0);
                }
            } else {
                world.playEvent(1030, pos, 0);
            }

        });
        return stack;
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    public void updateRepairOutput() {
        ItemStack itemstack = this.field_234643_d_.getStackInSlot(0);
        this.maximumCost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        if (itemstack.isEmpty()) {
            this.field_234642_c_.setInventorySlotContents(0, ItemStack.EMPTY);
            this.maximumCost.set(0);
        } else {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = this.field_234643_d_.getStackInSlot(1);
            Map<Enchantment, Integer> map = ModEnchantBook.getEnchantments(itemstack1);
            j = j + itemstack.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());
            this.materialCost = 0;
            boolean flag = false;

            if (!itemstack2.isEmpty()) {
                if (!onAnvilChange(this, itemstack, itemstack2, field_234642_c_, repairedItemName, j, this.field_234645_f_)) return;
                flag = (itemstack2.getItem() == Items.ENCHANTED_BOOK || itemstack2.getItem() == ItemRegistry.modEnchantBook.get())
                        && !ModEnchantBook.getEnchantments(itemstack2).isEmpty();
                if (itemstack1.isDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) { //修复
                    int l2 = Math.min(itemstack1.getDamage(), itemstack1.getMaxDamage() / 4);
                    if (l2 <= 0) {
                        this.field_234642_c_.setInventorySlotContents(0, ItemStack.EMPTY);
                        this.maximumCost.set(0);
                        return;
                    }

                    int i3;
                    for(i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
                        int j3 = itemstack1.getDamage() - l2;
                        itemstack1.setDamage(j3);
                        ++i;
                        l2 = Math.min(itemstack1.getDamage(), itemstack1.getMaxDamage() / 4);
                    }

                    this.materialCost = i3;
                } else {
                    //附魔
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isDamageable())) {
                        this.field_234642_c_.setInventorySlotContents(0, ItemStack.EMPTY);
                        this.maximumCost.set(0);
                        return;
                    }

                    if (itemstack1.isDamageable() && !flag) {
                        int l = itemstack.getMaxDamage() - itemstack.getDamage();
                        int i1 = itemstack2.getMaxDamage() - itemstack2.getDamage();
                        int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int k1 = l + j1;
                        int l1 = itemstack1.getMaxDamage() - k1;
                        if (l1 < 0) {
                            l1 = 0;
                        }

                        if (l1 < itemstack1.getDamage()) {
                            itemstack1.setDamage(l1);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map1 = ModEnchantBook.getEnchantments(itemstack2);
                    boolean flag2 = false;
                    boolean flag3 = false;

                    for(Enchantment enchantment1 : map1.keySet()) {
                        if (enchantment1 != null) {
                            int i2 = map.getOrDefault(enchantment1, 0);
                            int j2 = map1.get(enchantment1);
                            j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                            boolean flag1 = enchantment1.canApply(itemstack);
                            if (this.field_234645_f_.abilities.isCreativeMode || itemstack.getItem() == ItemRegistry.modEnchantBook.get()
                                    || itemstack.getItem() == Items.ENCHANTED_BOOK) {
                                flag1 = true;
                            }

                            for(Enchantment enchantment : map.keySet()) {
                                if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
                                    flag1 = false;
                                    ++i;
                                }
                            }

                            if (!flag1) {
                                flag3 = true;
                            } else {
                                flag2 = true;
                                if (j2 > enchantment1.getMaxLevel()) {
                                    j2 = enchantment1.getMaxLevel();
                                }

                                map.put(enchantment1, j2);
                                int k3 = 0;
                                switch(enchantment1.getRarity()) {
                                    case COMMON:
                                        k3 = 1;
                                        break;
                                    case UNCOMMON:
                                        k3 = 2;
                                        break;
                                    case RARE:
                                        k3 = 4;
                                        break;
                                    case VERY_RARE:
                                        k3 = 8;
                                }

                                if (flag) {
                                    k3 = Math.max(1, k3 / 2);
                                }

                                i += k3 * j2;
                                if (itemstack.getCount() > 1) {
                                    i = 40;
                                }
                            }
                        }
                    }

                    if (flag3 && !flag2) {
                        this.field_234642_c_.setInventorySlotContents(0, ItemStack.EMPTY);
                        this.maximumCost.set(0);
                        return;
                    }
                }
            }

            if (StringUtils.isBlank(this.repairedItemName)) {
                if (itemstack.hasDisplayName()) {
                    k = 1;
                    i += k;
                    itemstack1.clearCustomName();
                }
            } else if (!this.repairedItemName.equals(itemstack.getDisplayName().getString())) {
                k = 1;
                i += k;
                itemstack1.setDisplayName(new StringTextComponent(this.repairedItemName));
            }
            if (flag && !itemstack1.isBookEnchantable(itemstack2)) itemstack1 = ItemStack.EMPTY;

            this.maximumCost.set(j + i);
            if (i <= 0) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && this.maximumCost.get() >= 256) {
                this.maximumCost.set(255);
            }

            if (this.maximumCost.get() >= 256 && !this.field_234645_f_.abilities.isCreativeMode) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty()) {
                int k2 = itemstack1.getRepairCost();
                if (!itemstack2.isEmpty() && k2 < itemstack2.getRepairCost()) {
                    k2 = itemstack2.getRepairCost();
                }

                if (k != i || k == 0) {
                    k2 = getNewRepairCost(k2);
                }

                itemstack1.setRepairCost(k2);
                ModEnchantBook.setEnchantments(map, itemstack1);
            }

            this.field_234642_c_.setInventorySlotContents(0, itemstack1);
            this.detectAndSendChanges();
        }
    }

    public static int getNewRepairCost(int oldRepairCost) {
        return oldRepairCost * 2 + 1;
    }

   /**
     * used by the Anvil GUI to update the Item Name being typed by the player
     */
    public void updateItemName(String newName) {
        this.repairedItemName = newName;
        if (this.getSlot(2).getHasStack()) {
            ItemStack itemstack = this.getSlot(2).getStack();
            if (StringUtils.isBlank(newName)) {
                itemstack.clearCustomName();
            } else {
                itemstack.setDisplayName(new StringTextComponent(this.repairedItemName));
            }
        }

        this.updateRepairOutput();
    }

    /**
     * Get's the maximum xp cost
     */
    @OnlyIn(Dist.CLIENT)
    public int getMaximumCost() {
        return this.maximumCost.get();
    }

    public void setMaximumCost(int value) {
        this.maximumCost.set(value);
    }

    //触发铁砧更新事件
    public static boolean onAnvilChange(DiamondRepairContainer container, @Nonnull ItemStack left, @Nonnull ItemStack right, IInventory outputSlot, String name, int baseCost, PlayerEntity player)
    {
        AnvilUpdateEvent e = new AnvilUpdateEvent(left, right, name, baseCost, player);
        if (MinecraftForge.EVENT_BUS.post(e)) return false;
        if (e.getOutput().isEmpty()) return true;

        outputSlot.setInventorySlotContents(0, e.getOutput());
        container.setMaximumCost(e.getCost());
        container.materialCost = e.getMaterialCost();
        return false;
    }

    //触发铁砧修理事件
    public static float onAnvilRepair(PlayerEntity player, @Nonnull ItemStack output, @Nonnull ItemStack left, @Nonnull ItemStack right)
    {
        AnvilRepairEvent e = new AnvilRepairEvent(player, left, right, output);
        MinecraftForge.EVENT_BUS.post(e);
        e.setBreakChance(0.1f);
        return e.getBreakChance();
    }
}
