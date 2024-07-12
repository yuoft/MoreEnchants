package com.yuo.Enchants.Blocks;

import com.yuo.Enchants.Item.ModEnchantBook;
import com.yuo.Enchants.Item.YEItems;
import com.yuo.Enchants.Item.YETags;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.Map;

public class DiamondAnvilMenu extends ItemCombinerMenu {
    public int repairItemCountCost;
    private String itemName;
    private final DataSlot cost = DataSlot.standalone();
    private final TagKey<Block> DIAMOND_ANVIL_TAG = BlockTags.create(YETags.DIAMOND_ANVIL);

    public DiamondAnvilMenu(int id, Inventory playerInventory) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public DiamondAnvilMenu(int id, Inventory playerInventory, ContainerLevelAccess worldPosCallable) {
        super(MenuRegister.diamondAnvil.get(), id, playerInventory, worldPosCallable);
        this.addDataSlot(this.cost);
    }

    protected boolean isValidBlock(BlockState state) {
        return state.is(DIAMOND_ANVIL_TAG);
    }

    protected boolean mayPickup(Player player, boolean bool) {
        return (player.getAbilities().instabuild || player.experienceLevel >= this.cost.get()) && this.cost.get() > 0;
    }

    protected void onTake(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            player.giveExperienceLevels(-this.cost.get()); //扣除经验
        }

        float breakChance = onAnvilRepair(player, stack, DiamondAnvilMenu.this.inputSlots.getItem(0), DiamondAnvilMenu.this.inputSlots.getItem(1));

        this.inputSlots.setItem(0, ItemStack.EMPTY);
        if (this.repairItemCountCost > 0) {
            ItemStack itemstack = this.inputSlots.getItem(1);
            if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
                itemstack.shrink(this.repairItemCountCost);
                this.inputSlots.setItem(1, itemstack);
            } else {
                this.inputSlots.setItem(1, ItemStack.EMPTY);
            }
        } else {
            this.inputSlots.setItem(1, ItemStack.EMPTY);
        }

        this.cost.set(0);
        this.access.execute((world, pos) -> {
            BlockState blockstate = world.getBlockState(pos);
            if (!player.getAbilities().instabuild && blockstate.is(DIAMOND_ANVIL_TAG) && player.getRandom().nextFloat() < breakChance) {
                BlockState blockstate1 = DiamondAnvil.damage(blockstate);
                if (blockstate1 == null) {
                    world.removeBlock(pos, false);
                    world.levelEvent(1029, pos, 0);
                } else {
                    world.setBlock(pos, blockstate1, 2);
                    world.levelEvent(1030, pos, 0);
                }
            } else {
                world.levelEvent(1030, pos, 0);
            }

        });
    }

    /**
     * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
     */
    public void createResult() {
        ItemStack itemstack = this.inputSlots.getItem(0);
        this.cost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        if (itemstack.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            this.cost.set(0);
        } else {
            ItemStack itemstack1 = itemstack.copy();
            ItemStack itemstack2 = this.inputSlots.getItem(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
            j = j + itemstack.getBaseRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getBaseRepairCost());
            this.repairItemCountCost = 0;
            boolean flag = false;

            if (!itemstack2.isEmpty()) {
                if (!onAnvilChange(this, itemstack, itemstack2, resultSlots, itemName, j, this.player)) return;
                flag = (itemstack2.getItem() == Items.ENCHANTED_BOOK || itemstack2.getItem() == YEItems.modEnchantBook.get())
                        && !ModEnchantBook.getEnchantments(itemstack2).isEmpty();
                if (itemstack1.isDamageableItem() && itemstack1.getItem().isValidRepairItem(itemstack, itemstack2)) { //修复
                    int l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    if (l2 <= 0) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    int i3;
                    for(i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
                        int j3 = itemstack1.getDamageValue() - l2;
                        itemstack1.setDamageValue(j3);
                        ++i;
                        l2 = Math.min(itemstack1.getDamageValue(), itemstack1.getMaxDamage() / 4);
                    }

                    this.repairItemCountCost = i3;
                } else {
                    //附魔
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isDamageableItem())) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }

                    if (itemstack1.isDamageableItem() && !flag) {
                        int l = itemstack.getMaxDamage() - itemstack.getDamageValue();
                        int i1 = itemstack2.getMaxDamage() - itemstack2.getDamageValue();
                        int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int k1 = l + j1;
                        int l1 = itemstack1.getMaxDamage() - k1;
                        if (l1 < 0) {
                            l1 = 0;
                        }

                        if (l1 < itemstack1.getDamageValue()) {
                            itemstack1.setDamageValue(l1);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    boolean flag2 = false;
                    boolean flag3 = false;

                    for(Enchantment enchantment1 : map1.keySet()) {
                        if (enchantment1 != null) {
                            int i2 = map.getOrDefault(enchantment1, 0);
                            int j2 = map1.get(enchantment1);
                            j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                            boolean flag1 = enchantment1.canEnchant(itemstack);
                            if (this.player.getAbilities().instabuild || itemstack.getItem() == YEItems.modEnchantBook.get()
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
                                int k3 = switch (enchantment1.getRarity()) {
                                    case COMMON -> 1;
                                    case UNCOMMON -> 2;
                                    case RARE -> 4;
                                    case VERY_RARE -> 8;
                                };

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
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.cost.set(0);
                        return;
                    }
                }
            }

            if (StringUtils.isBlank(this.itemName)) {
                if (itemstack.hasCustomHoverName()) {
                    k = 1;
                    i += k;
                    itemstack1.resetHoverName();
                }
            } else if (!this.itemName.equals(itemstack.getDisplayName().getString())) {
                k = 1;
                i += k;
                itemstack1.setHoverName(new TextComponent(this.itemName));
            }
            if (flag && !itemstack1.isBookEnchantable(itemstack2)) itemstack1 = ItemStack.EMPTY;

            this.cost.set(j + i);
            if (i <= 0) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && this.cost.get() >= 256) {
                this.cost.set(255);
            }

            if (this.cost.get() >= 256 && !this.player.getAbilities().instabuild) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty()) {
                int k2 = itemstack1.getBaseRepairCost();
                if (!itemstack2.isEmpty() && k2 < itemstack2.getBaseRepairCost()) {
                    k2 = itemstack2.getBaseRepairCost();
                }

                if (k != i || k == 0) {
                    k2 = getNewRepairCost(k2);
                }

                itemstack1.setRepairCost(k2);
                ModEnchantBook.setEnchantments(map, itemstack1);
            }

            this.resultSlots.setItem(0, itemstack1);
            this.broadcastChanges();
        }
    }

    public static int getNewRepairCost(int oldRepairCost) {
        return oldRepairCost * 2 + 1;
    }

   /**
     * used by the Anvil GUI to update the Item Name being typed by the player
     */
    public void setItemName(String newName) {
        this.itemName = newName;
        if (this.getSlot(2).hasItem()) {
            ItemStack itemstack = this.getSlot(2).getItem();
            if (StringUtils.isBlank(newName)) {
                itemstack.resetHoverName();
            } else {
                itemstack.setHoverName(new TextComponent(this.itemName));
            }
        }

        this.createResult();
    }

    /**
     * Get's the maximum xp cost
     */
    public int getCost() {
        return this.cost.get();
    }

    public void setMaximumCost(int value) {
        this.cost.set(value);
    }

    //触发铁砧更新事件
    public static boolean onAnvilChange(DiamondAnvilMenu container, @Nonnull ItemStack left, @Nonnull ItemStack right, Container outputSlot, String name, int baseCost, Player player)
    {
        AnvilUpdateEvent e = new AnvilUpdateEvent(left, right, name, baseCost, player);
        if (MinecraftForge.EVENT_BUS.post(e)) return false;
        if (e.getOutput().isEmpty()) return true;

        outputSlot.setItem(0, e.getOutput());
        container.setMaximumCost(e.getCost());
        container.repairItemCountCost = e.getMaterialCost();
        return false;
    }

    //触发铁砧修理事件
    public static float onAnvilRepair(Player player, @Nonnull ItemStack output, @Nonnull ItemStack left, @Nonnull ItemStack right)
    {
        AnvilRepairEvent e = new AnvilRepairEvent(player, left, right, output);
        MinecraftForge.EVENT_BUS.post(e);
        e.setBreakChance(0.08f); //损坏概率
        return e.getBreakChance();
    }
}
