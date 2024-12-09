package com.yuo.Enchants.Items;

import com.yuo.Enchants.Proxy.AAA;
import com.yuo.Enchants.Proxy.ColorBolt;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ExpDrip extends Item {

    public ExpDrip() {
        super(new Properties().tab(ModTab.youEnchants));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        Item item = pStack.getItem();
        if (item == YEItems.smallExpDrip.get()){
            pTooltipComponents.add(new TranslatableComponent("yuoenchants.text.itemInfo.small_exp_drip"));
        }
        if (item == YEItems.bigExpDrip.get()){
            pTooltipComponents.add(new TranslatableComponent("yuoenchants.text.itemInfo.big_exp_drip"));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack heldItem = pPlayer.getItemInHand(pUsedHand);
        int expValue = heldItem.getItem() == YEItems.smallExpDrip.get() ? 10 : 100;
//        if (pPlayer.isCrouching()){
//            int count = heldItem.getCount();
//            pPlayer.giveExperiencePoints(count * expValue);
//            heldItem.shrink(count);
//        }else {
//            pPlayer.giveExperiencePoints(expValue);
//            heldItem.shrink(1);
//        }
        if (pPlayer.isCrouching() && !pLevel.isClientSide){
            BlockPos pos = pPlayer.getOnPos();
//            AABB aabb = new AABB(pos.offset(-16,0,-16),pos.offset(16,8,16));
//            for (LivingEntity living : pLevel.getEntitiesOfClass(LivingEntity.class, aabb)) {

//                if (living.isAlive() && !(living instanceof Player)){
                    ColorBolt colorBolt = new ColorBolt(AAA.COLOR_LIGHT_BOLT.get(), pLevel);
            colorBolt.moveTo(Vec3.atBottomCenterOf(pos));
            colorBolt.setCause(pPlayer instanceof ServerPlayer ? (ServerPlayer)pPlayer : null);
                    colorBolt.setVisualOnly(true);
                    pLevel.addFreshEntity(colorBolt);
//                }
//            }

        }
        return InteractionResultHolder.consume(heldItem);
    }
}
