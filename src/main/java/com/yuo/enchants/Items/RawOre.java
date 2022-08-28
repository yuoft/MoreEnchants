package com.yuo.enchants.Items;

import com.yuo.enchants.Items.Tab.ModGroup;
import net.minecraft.item.Item;

//破碎矿物
public class RawOre extends Item {

    public RawOre() {
        super(new Properties().group(ModGroup.youEnchants));
    }

//    @Override
//    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
//        boolean result = super.hitEntity(stack, target, attacker);
//
//        if (result && !target.isImmuneToFire()) {
//            if (!target.world.isRemote) {
//                target.setFire(15);
//            } else {
//                target.world.addParticle(ParticleTypes.FLAME, target.getPosX(), target.getPosY() + target.getHeight() * 0.5, target.getPosZ(), target.getWidth() * 0.5, target.getHeight() * 0.5, target.getWidth() * 0.5);
//            }
//        }
//
//        return result;
//    }

//    @Override
//    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
//        if (!worldIn.isRemote){
//            double value = playerIn.getAttribute(Attributes.MAX_HEALTH).getValue();
//            double value1 = playerIn.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
//            playerIn.sendMessage(new StringTextComponent("血量：" + value + " 距离：" + value1), UUID.randomUUID());
//        }
//        return super.onItemRightClick(worldIn, playerIn, handIn);
//    }
}
