package com.yuo.Enchants.Event;

import com.yuo.Enchants.Config;
import com.yuo.Enchants.Enchants.*;
import com.yuo.Enchants.Proxy.CommonProxy;
import com.yuo.Enchants.YuoEnchants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PotatoBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * 事件处理类 附魔实现
 */
@Mod.EventBusSubscriber(modid = YuoEnchants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    private static final Random RANDOM = new Random(); //随机数

    //附魔，火焰免疫 屹立不倒 受到伤害
    @SubscribeEvent
    public static void livingHurt(LivingHurtEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof Player player) { //只对玩家生效
            ItemStack stackLegs = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack stackFeet = player.getItemBySlot(EquipmentSlot.FEET);
            int lastStand = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.lastStand.get(), stackFeet);
            if (lastStand > 0   && Config.SERVER.isLastStand.get()) {
                LastStand.lastStand(player, event, stackFeet);
            }
            int superFire = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.superFire.get(), stackLegs);
            if (superFire > 0 && event.getSource().isFire() && Config.SERVER.isSuperFire.get()) {
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superFire));
            }
            int superBlast = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.superBlast.get(), player.getItemBySlot(EquipmentSlot.CHEST));
            if (superBlast > 0 && event.getSource().isExplosion() && Config.SERVER.isSuperBlast.get()) {
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superBlast));
            }
            int superArrow = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.superArrow.get(), player.getItemBySlot(EquipmentSlot.HEAD));
            if (superArrow > 0 && event.getSource().isProjectile() && Config.SERVER.isSuperArrow.get()) {
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superArrow));
            }
        }
        Entity trueSource = event.getSource().getDirectEntity();
        if (trueSource instanceof Player player) {
            ItemStack mainHand = player.getUseItem();
            int beHead = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.beHead.get(), mainHand);
            if (beHead > 0  && Config.SERVER.isBehead.get()) {
                BeHead.addDamage(beHead, event, player, entityLiving);
            }
        }
    }

    //高级摔落保护
    @SubscribeEvent
    public static void livingFall(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            int superFall = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.superFall.get(), player.getItemBySlot(EquipmentSlot.FEET));
            if (superFall > 0 && Config.SERVER.isSuperFall.get()) {
                event.setDamageMultiplier(SuperProtect.getDamage(event.getDamageMultiplier(), superFall));
            }
        }
    }

    //附魔，以战养战 脆弱 生命献祭 攻击生物
    @SubscribeEvent
    public static void attackEntity(AttackEntityEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        ItemStack stack = player.getUseItem();
        int warToWar = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.warToWar.get(), stack);
        if (warToWar > 0 && Config.SERVER.isWarToWar.get()) { //有附魔
            WarToWar.heal(warToWar, player);
        }
        int unDurable = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
        if (unDurable > 0  && Config.SERVER.isUnDurable.get()) {
            UnDurable.unDurable(stack, unDurable, player);
        }

        int instability = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.instability.get(), player.getMainHandItem());
        EventHelper.dropItem(player, instability);
    }

    //爆炸箭 \ 超级力量 箭碰撞方块或实体
    @SubscribeEvent
    public static void projectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile instanceof AbstractArrow arrow){
            if (arrow.getOwner() instanceof LivingEntity shooter) {
                ItemStack bow = shooter.getItemInHand(shooter.getUsedItemHand());
                int blastArrow = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.blastArrow.get(), bow);
                if (blastArrow > 0  && Config.SERVER.isBlastArrow.get()) {
                    BlastArrow.boom(arrow, blastArrow);
                }
                int superPower = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.superPower.get(), bow);
                if (superPower > 0 && Config.SERVER.isSuperPower.get()) {
                    arrow.setBaseDamage(arrow.getBaseDamage() + 1.25D + (double) superPower * 0.75D);
                }
            }
        }
    }

    //脆弱 洞察 熔炼 粉碎 范围挖掘 强运  --破坏方块
    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        ItemStack tool = player.getUseItem();
        if (tool.isEmpty()) return;
        Item item = tool.getItem();
        if (item instanceof DiggerItem || item instanceof ShearsItem) {
            int unDurable = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.unDurable.get(), tool);
            if (unDurable > 0  && Config.SERVER.isUnDurable.get()) {
                tool.hurtAndBreak(RANDOM.nextInt(unDurable) + 1, player, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND)); //破坏方块时消耗更多耐久
            }
        }
        if (event.getExpToDrop() > 0) {
            int insight = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.insight.get(), tool);
            if (insight > 0  && Config.SERVER.isInsight.get()) {
                Insight.addDropExp(event, insight);
            }
        }
        Level world = (Level) event.getWorld();
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        BlockState state = event.getState();

        int strengthLuck = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.strengthLuck.get(), tool);
        int unLuck = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.unLuck.get(), tool);
        if (strengthLuck > 0 && Config.SERVER.isStrengthLuck.get()) {
            StrengthLuck.strengthLuck(block, state, world, pos, strengthLuck, player);
            return;
        }
        if (unLuck > 0 && Config.SERVER.isUnLuck.get() &&  RANDOM.nextDouble() < unLuck * 0.2){
            if (event.getExpToDrop() > 0){
                event.setExpToDrop(0);
            }
            if (block == Blocks.POTATOES){ //破坏成熟马铃薯，掉落毒马铃薯
                Integer cropAge = state.getValue(PotatoBlock.AGE);
                if (cropAge >= 7) world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(Items.POISONOUS_POTATO)));
            }
            world.setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState());
        }
        int rangBreak = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.rangBreak.get(), tool);

        if (CommonProxy.isIsKeyC() && rangBreak > 0  && Config.SERVER.isRangBreak.get()) {
            EventHelper.breakBlocks(tool, world, pos, state, player, Math.min(rangBreak, 5)); //最大等级5
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            return;
        }
        int melting = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.melting.get(), tool); //熔炼
        if (melting > 0  && Config.SERVER.isMelting.get()) {
            Melting.melting(block, state, world, pos, player, tool, event);
        }
        int diamondDrop = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.diamondDrop.get(), tool);
        int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
        if (diamondDrop > 0 && state.getBlock() == Blocks.COAL_ORE && Config.SERVER.isDiamondDrop.get()){
            DiamondDrop.diamondDrop(diamondDrop, world, fortune, pos);
        }
    }

    //脆弱 盔甲
//    @SubscribeEvent
//    public static void unDurableDamage(LivingAttackEvent event){
//        LivingEntity entityLiving = event.getEntityLiving();
//        if (entityLiving instanceof Player){
//            Player player = (Player) entityLiving;
//            player.getArmorInventoryList().forEach(e -> {
//                int level = EnchantmentHelper.getItemEnchantmentLevel(EnchantsRegistry.unDurable.get(), e);
//                if (level > 0){
//                    e.damageItem(level, player, f -> f.sendBreakAnimation(Hand.MAIN_HAND));
//                }
//            });
//        }
//    }
    //脆弱 物品右键使用 弓 弩 三叉戟

    //脆弱 弓 弩 三叉戟 停止使用物品
    @SubscribeEvent
    public static void stopUseItem(LivingEntityUseItemEvent.Stop event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof Player player) {
            ItemStack stack = event.getItem();
            Item item = stack.getItem();
            if (item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem) {
                int unDurable = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
                if (unDurable > 0  && Config.SERVER.isUnDurable.get())
                    stack.hurtAndBreak(RANDOM.nextInt(unDurable) + 1, player, e -> e.broadcastBreakEvent(event.getEntityLiving().getUsedItemHand()));
            }
        }
    }

    //脆弱 右键物品 钓鱼竿
    @SubscribeEvent
    public static void rightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        Player player = event.getPlayer();
        Item item = stack.getItem();
        if (item instanceof FishingRodItem) {
            int unDurable = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
            if (unDurable > 0  && Config.SERVER.isUnDurable.get())
                stack.hurtAndBreak(RANDOM.nextInt(unDurable) + 1, player, e -> e.broadcastBreakEvent(event.getHand()));
        }
    }

    //农夫
    @SubscribeEvent
    public static void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        Level world = event.getWorld();
        BlockPos pos = event.getPos();
        int farmer = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.farmer.get(), player);
        if (farmer > 0 && Config.SERVER.isFarmer.get()) {
            Farmer.harvestCrop(player, hand, world, pos, farmer);
        }
    }

    //脆弱 使用锄头
    @SubscribeEvent
    public static void toolModification(BlockEvent.BlockToolModificationEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getHeldItemStack();
        int unDurable = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.unDurable.get(), item);
        if (unDurable > 0  && Config.SERVER.isUnDurable.get()) {
            item.hurtAndBreak(RANDOM.nextInt(unDurable) + 1, player, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        }
    }

    //脆弱 洞察 海之嫌弃 钓鱼竿钓起鱼
    @SubscribeEvent
    public static void itemFished(ItemFishedEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            ItemStack stack = player.getUseItem();
            if (stack.getItem() instanceof FishingRodItem) {
                Level world = player.level;
                int unDurable = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
                int insight = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.insight.get(), stack);
                int badLuckOfTheSea = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.badLuckOfTheSea.get(), stack);
                if (unDurable > 0  && Config.SERVER.isUnDurable.get()) {
                    event.damageRodBy(event.getRodDamage() + RANDOM.nextInt(unDurable) + 1);
                }
                if (insight > 0  && Config.SERVER.isInsight.get()) {
                    Insight.addFishingExp(player, world, insight);
                }
                if (badLuckOfTheSea > 0  && Config.SERVER.isBadLuckOfTheSea.get()) {
                    BadLuckOfTheSea.fishingTnt(event, badLuckOfTheSea, player, world);
                }
            }
        }
    }

    //拖拉 挖掘速度
    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getPlayer();
        if (player == null) return;
        int slow = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.slow.get(), player.getItemInHand(player.getUsedItemHand()));
        if (slow > 0 && Config.SERVER.isSlow.get()) {
            event.setNewSpeed(event.getOriginalSpeed() * (1 - slow * 0.2f)); //挖掘速度变慢
        }
    }

    //万箭
    @SubscribeEvent
    public static void arrowLoose(ArrowLooseEvent event) {
        Level world = event.getWorld();
        ItemStack bow = event.getBow();
        if (!bow.isEmpty() && bow.getItem() instanceof BowItem){
            int manyArrow = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.manyArrow.get(), bow);
            if (manyArrow > 0  && Config.SERVER.isManyArrow.get()) {
                ManyArrow.manyArrow(event.getCharge(), event.getPlayer(), bow, manyArrow, world);
            }
        }
    }

    //快速拉弓 失稳
    @SubscribeEvent
    public static void  useItem(LivingEntityUseItemEvent event){
        ItemStack item = event.getItem();
        int fastBow = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.fastBow.get(), item);
        int duration = event.getDuration();
        if (fastBow > 0 && Config.SERVER.isFastBow.get() && duration > fastBow){
            event.setDuration(duration - fastBow);
        }
        LivingEntity living = event.getEntityLiving();
        if (living instanceof Player){
            int instability = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.instability.get(), living.getMainHandItem());
            EventHelper.dropItem((Player) living, instability);
        }
    }

    //经验腐蚀 获取经验
    @SubscribeEvent
    public static void pickupXp(PlayerXpEvent.PickupXp event) {
        Player player = event.getPlayer();
        if (player.takeXpDelay != 0) return;
        int xpValue = event.getOrb().getValue(); //经验值
        player.takeXpDelay = 2;
        player.take(event.getOrb(), 1); //捡起经验
        //获取含有此附魔的装备map
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(EnchantRegistry.expCorrode.get(), player);
        if (entry != null  && Config.SERVER.isExpCorrode.get()) {
            ItemStack itemstack = entry.getValue();
            if (!itemstack.isEmpty() && itemstack.getDamageValue() > 0) { //物品非空，且有耐久
                int i = (int) (xpValue * 1.0f);  //获取经验值 * 物品经验修复率
                xpValue -= i / 2; //玩家最终获取的经验
                itemstack.hurtAndBreak(i, player, e -> e.broadcastBreakEvent(entry.getKey())); //腐蚀物品
            }
        }
        if (xpValue > 0) { //如果经验有剩余
            player.giveExperiencePoints(xpValue); //玩家获取经验
        }
    }

    //岩浆行者 生机 距离提升 实体更新
    @SubscribeEvent
    public static void livingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof Player player) {
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            //重置二段跳次数
            if ((!player.getAbilities().flying || player.isOnGround()) && !feet.isEmpty() && feet.getOrCreateTag().getInt(DoubleJump.USES) > 0) {
                feet.getOrCreateTag().putInt(DoubleJump.USES, 0);
            }
            int lavaWalker = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.lavaWalker.get(), feet);
            if (lavaWalker > 0  && Config.SERVER.isLavaWalker.get()) {
                LavaWalker.freezingNearby(player, player.level, player.getOnPos(), lavaWalker);
            }
            int magnet = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.magnet.get(), player.getItemBySlot(EquipmentSlot.LEGS));
            if (magnet > 0 && player.isCrouching() && Config.SERVER.isMagnet.get()) {
                Magnet.moveEntityItemsInRegion(player.level, player.getOnPos(), 3 + magnet * 2, magnet);
            }
            int waterWalk = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.waterWalk.get(), feet);
            if (waterWalk > 0 && Config.SERVER.isWaterWalk.get()) {
                WaterWalk.walk(player);
            }
            if (!player.level.isClientSide){
                EventHelper.changeMaxHealth(player);
                EventHelper.changeHandRange(player);
                EventHelper.changeSwimSpeed(player);
            }
        }
    }

    //玩家登入
    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();
        if (!player.getPersistentData().getBoolean("yuoenchants:login")){
            player.getPersistentData().putBoolean("yuoenchants:login", true);
            //发送消息
            player.sendMessage(new TranslatableComponent("yuoenchants.message.login")
                    .setStyle(Style.EMPTY.withHoverEvent(HoverEvent.Action.SHOW_TEXT.deserializeFromLegacy(new TranslatableComponent("yuoenchants.message.login0")))
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))), UUID.randomUUID());
        }
    }

    //雷击 真荆棘
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player == null || player.level.isClientSide) return;
        ItemStack stackLegs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack stackChest = player.getItemBySlot(EquipmentSlot.CHEST);
        int lightningDamage = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.lightningDamage.get(), stackLegs);
        if (lightningDamage > 0   && Config.SERVER.isLightningDamage.get()) { //在雨天生效
            LightningDamage.lighting(player, stackLegs);
        }
        int thorns = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.thorns.get(), stackChest);
        if (thorns > 0   && Config.SERVER.isThorns.get()) {
            Thorns.thorns(player, stackChest, thorns);
        }
    }

    //洞察 生物掉落经验
    @SubscribeEvent
    public static void expDrop(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (player != null) {
            int insight = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.insight.get(), player.getUseItem());
            if (insight > 0  && Config.SERVER.isInsight.get()) {
                double exp = event.getOriginalExperience() * (100 + insight * 30) / 100.0;
                event.setDroppedExperience((int) Math.ceil(exp));
            }
            int unLooting = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.unLooting.get(), player.getUseItem());
            if (unLooting > 0 && Config.SERVER.isUnLooting.get() && event.getDroppedExperience() > 0){
                int luck = -1;
                MobEffectInstance instance = player.getEffect(MobEffects.LUCK);
                if (instance != null)
                    luck = instance.getAmplifier();
                if (RANDOM.nextDouble() < 0.2 * unLooting - (luck + 1) * 0.1){
                    event.setDroppedExperience(0);
                }
            }
        }
    }

    //生命汲取 生物死亡
    @SubscribeEvent
    public static void livingDeath(LivingDeathEvent event) {
        Entity trueSource = event.getSource().getDirectEntity(); //伤害来源
        if (trueSource instanceof Player player) {
            ItemStack mainHand = player.getUseItem();
            int leech = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.leech.get(), mainHand);
            if (leech > 0  && Config.SERVER.isLeech.get()) {
                player.heal(leech / 2.0f); //回血
            }
            int healthToS = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.healthToSacrifice.get(), mainHand);
            if (healthToS > 0 && Config.SERVER.isHealthToSacrifice.get()) {
                LivingEntity entityLiving = event.getEntityLiving();
                Level world = entityLiving.level;
                int looting = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, mainHand);
                HealthToSacrifice.dropExpDrip(world, healthToS, looting, entityLiving.getOnPos(), entityLiving.getMaxHealth());
            }
        }
    }

    //斩首 抢劫 生物掉落
    @SubscribeEvent
    public static void livingDrop(LivingDropsEvent event) {
        Entity trueSource = event.getSource().getDirectEntity();
        if (trueSource instanceof Player player) {
            int beHead = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.beHead.get(), player.getUseItem());
            if (beHead > 0  && Config.SERVER.isBehead.get()) {
                event.getDrops().add(BeHead.dropHead(beHead, event.getEntityLiving()));
            }
            int unLooting = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.unLooting.get(), player.getUseItem());
            if (unLooting > 0 && Config.SERVER.isUnLooting.get()){
                int luck = -1;
                MobEffectInstance instance = player.getEffect(MobEffects.LUCK);
                if (instance != null)
                    luck = instance.getAmplifier();
                int finalLuck = luck;
                event.getDrops().removeIf(e -> RANDOM.nextDouble() < 0.2 * unLooting - (finalLuck + 1) * 0.1);
            }
        }
    }

    //抢夺等级
    @SubscribeEvent
    public static void lootingLevel(LootingLevelEvent event) {
        DamageSource damageSource = event.getDamageSource();
        if (damageSource == null) return;
        Entity source = damageSource.getDirectEntity();
        if (source instanceof Player player) {
            int robbery = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.robbery.get(), player.getUseItem());
            if (robbery > 0 && Config.SERVER.isRobbery.get()){
                event.setLootingLevel(Robbery.getLootingLevel(event.getLootingLevel(), robbery));
            }
        }
    }

    //快速恢复
    @SubscribeEvent
    public static void livingHeal(LivingHealEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            int fastHeal = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.fastHeal.get(), player.getItemBySlot(EquipmentSlot.CHEST));
            if (fastHeal > 0 && Config.SERVER.isFastHeal.get()) {
                event.setAmount(FastHeal.fastHeal(fastHeal, event.getAmount()));
            }
        }
    }

    //斥力
    @SubscribeEvent
    public static void useItemTick(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (event.getItem().getItem() instanceof ProjectileWeaponItem) {
                int repulsion = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.repulsion.get(), event.getItem());
                if (repulsion > 0 && Config.SERVER.isRepulsion.get()) {
                    Repulsion.moveLivingEntityInRegion(player.level, player.getOnPos(), 1 + repulsion, repulsion);
                }
            }
        }
    }

    //弹反 火焰盾 火焰免疫 熔岩行者
    @SubscribeEvent
    public static void livingAttack(LivingAttackEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof Player player) {
            ItemStack shield = player.getOffhandItem();
            DamageSource source = event.getSource();
            if (!shield.isEmpty() && player.getUseItem() == shield) {
                int rebound = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.rebound.get(), shield);
                if (rebound > 0 && Config.SERVER.isRebound.get()) {
                    Rebound.rebound(event, rebound, player, shield);
                }
                int fireShield = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.fireShield.get(), shield);
                if (fireShield > 0 && Config.SERVER.isFireShield.get()) {
                    FireShield.fireShield(source, fireShield, player);
                }
            }
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            int fireImmune = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.fireImmune.get(), legs);
            if (fireImmune > 0 && Config.SERVER.isFireImmune.get() && source.isFire()) {
                FireImmune.fireImmune(event, legs, player);
            }
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            int lavaWalker = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.lavaWalker.get(), feet);
            if (lavaWalker > 0 && event.getSource() == DamageSource.HOT_FLOOR  && Config.SERVER.isLavaWalker.get()){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        ItemStack stack = event.getItemStack();
        Player player = event.getPlayer();
        int instability = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.instability.get(), stack);
        if (!player.level.isClientSide && Config.SERVER.isInstability.get()){
            EventHelper.dropItem(player, instability);
        }
    }
}

