package com.yuo.Enchants.Event;

import com.yuo.Enchants.Config;
import com.yuo.Enchants.Enchants.*;
import com.yuo.Enchants.YuoEnchants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.DamageTypeTags;
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
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Stop;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Tick;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp;
import net.minecraftforge.event.level.BlockEvent.BlockToolModificationEvent;
import net.minecraftforge.event.level.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.Map.Entry;
import java.util.Random;

/**
 * 事件处理类 附魔实现
 */
@EventBusSubscriber(modid = YuoEnchants.MOD_ID, bus = Bus.FORGE)
public class EventHandler {
    private static final Random RANDOM = new Random(); //随机数

    //附魔，火焰免疫 屹立不倒 受到伤害
    @SubscribeEvent
    public static void livingHurt(LivingHurtEvent event) {
        LivingEntity entityLiving = event.getEntity();
        if (entityLiving instanceof Player player) { //只对玩家生效
            ItemStack stackLegs = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack stackFeet = player.getItemBySlot(EquipmentSlot.FEET);
            int lastStand = stackFeet.getEnchantmentLevel(YEEnchants.lastStand.get());
            if (lastStand > 0   && Config.SERVER.isLastStand.get()) {
                LastStand.lastStand(player, event, stackFeet);
            }
            int superFire = stackLegs.getEnchantmentLevel(YEEnchants.superFire.get());
            if (superFire > 0 && event.getSource().is(DamageTypeTags.IS_FIRE) && Config.SERVER.isSuperFire.get()) {
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superFire));
            }
            int superBlast = player.getItemBySlot(EquipmentSlot.CHEST).getEnchantmentLevel(YEEnchants.superBlast.get());
            if (superBlast > 0 && event.getSource().is(DamageTypeTags.IS_EXPLOSION) && Config.SERVER.isSuperBlast.get()) {
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superBlast));
            }
            int superArrow = player.getItemBySlot(EquipmentSlot.HEAD).getEnchantmentLevel(YEEnchants.superArrow.get());
            if (superArrow > 0 && event.getSource().is(DamageTypeTags.IS_PROJECTILE) && Config.SERVER.isSuperArrow.get()) {
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superArrow));
            }
        }
        Entity trueSource = event.getSource().getDirectEntity();
        if (trueSource instanceof Player player) {
            ItemStack mainHand = player.getItemBySlot(EquipmentSlot.MAINHAND);
            int beHead = mainHand.getEnchantmentLevel(YEEnchants.beHead.get());
            if (beHead > 0  && Config.SERVER.isBehead.get()) {
                BeHead.addDamage(beHead, event, player, entityLiving);
            }
        }
    }

    //高级摔落保护
    @SubscribeEvent
    public static void livingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            int superFall = player.getItemBySlot(EquipmentSlot.FEET).getEnchantmentLevel(YEEnchants.superFall.get());
            if (superFall > 0 && Config.SERVER.isSuperFall.get()) {
                event.setDamageMultiplier(SuperProtect.getDamage(event.getDamageMultiplier(), superFall));
            }
        }
    }

    //附魔，以战养战 脆弱 生命献祭 攻击生物
    @SubscribeEvent
    public static void attackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (player == null) return;
        ItemStack stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
        int warToWar = stack.getEnchantmentLevel(YEEnchants.warToWar.get());
        if (warToWar > 0 && Config.SERVER.isWarToWar.get()) { //有附魔
            WarToWar.heal(warToWar, player);
        }
        int unDurable = stack.getEnchantmentLevel(YEEnchants.unDurable.get());
        if (unDurable > 0  && Config.SERVER.isUnDurable.get()) {
            UnDurable.unDurable(stack, unDurable, player);
        }

        int instability = player.getMainHandItem().getEnchantmentLevel(YEEnchants.instability.get());
        EventHelper.dropItem(player, instability);
    }

    //爆炸箭 \ 超级力量 箭碰撞方块或实体
    @SubscribeEvent
    public static void projectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile instanceof AbstractArrow arrow){
            if (arrow.getOwner() instanceof LivingEntity shooter) {
                ItemStack bow = shooter.getItemInHand(shooter.getUsedItemHand());
                int blastArrow = bow.getEnchantmentLevel(YEEnchants.blastArrow.get());
                if (blastArrow > 0  && Config.SERVER.isBlastArrow.get()) {
                    BlastArrow.boom(arrow, blastArrow);
                }
                int superPower = bow.getEnchantmentLevel(YEEnchants.superPower.get());
                if (superPower > 0 && Config.SERVER.isSuperPower.get()) {
                    arrow.setBaseDamage(arrow.getBaseDamage() + 1.25D + (double) superPower * 0.75D);
                }
            }
        }
    }

    //脆弱 洞察 熔炼 粉碎 范围挖掘 强运  --破坏方块
    @SubscribeEvent
    public static void breakBlock(BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.isCreative()) return;
        ItemStack tool = player.getItemBySlot(EquipmentSlot.MAINHAND);
        if (tool.isEmpty()) return;
        Item item = tool.getItem();
        if (item instanceof DiggerItem || item instanceof ShearsItem) {
            int unDurable = tool.getEnchantmentLevel(YEEnchants.unDurable.get());
            if (unDurable > 0  && Config.SERVER.isUnDurable.get()) {
                tool.hurtAndBreak(RANDOM.nextInt(unDurable) + 1, player, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND)); //破坏方块时消耗更多耐久
            }
        }
        if (event.getExpToDrop() > 0) {
            int insight = tool.getEnchantmentLevel(YEEnchants.insight.get());
            if (insight > 0  && Config.SERVER.isInsight.get()) {
                Insight.addDropExp(event, insight);
            }
        }
        Level world = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        BlockState state = event.getState();

        int strengthLuck = tool.getEnchantmentLevel(YEEnchants.strengthLuck.get());
        int unLuck = tool.getEnchantmentLevel(YEEnchants.unLuck.get());
        if (strengthLuck > 0 && Config.SERVER.isStrengthLuck.get()) {
            StrengthLuck.strengthLuck(block, state, world, pos, strengthLuck, player);
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
        int rangBreak = tool.getEnchantmentLevel(YEEnchants.rangBreak.get());
        if (player.isCrouching() && rangBreak > 0  && Config.SERVER.isRangBreak.get()) {
            EventHelper.breakBlocks(tool, world, pos, state, player, Math.min(rangBreak, 5)); //最大等级5
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            event.setCanceled(true);
            return;
        }
        int melting = tool.getEnchantmentLevel(YEEnchants.melting.get()); //熔炼
        if (melting > 0  && Config.SERVER.isMelting.get()) {
            Melting.melting(block, state, world, pos, player, tool, event);
        }
        int diamondDrop = tool.getEnchantmentLevel(YEEnchants.diamondDrop.get());
        int fortune = tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
        if (diamondDrop > 0 && (state.getBlock() == Blocks.COAL_ORE || state.getBlock() == Blocks.DEEPSLATE_COAL_ORE) && Config.SERVER.isDiamondDrop.get()){
            DiamondDrop.diamondDrop(diamondDrop, world, fortune, pos);
        }
    }

    //脆弱 弓 弩 三叉戟 停止使用物品
    @SubscribeEvent
    public static void stopUseItem(Stop event) {
        LivingEntity entityLiving = event.getEntity();
        if (entityLiving instanceof Player player) {
            ItemStack stack = event.getItem();
            Item item = stack.getItem();
            if (item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem) {
                int unDurable = stack.getEnchantmentLevel(YEEnchants.unDurable.get());
                if (unDurable > 0  && Config.SERVER.isUnDurable.get())
                    stack.hurtAndBreak(RANDOM.nextInt(unDurable) + 1, player, e -> e.broadcastBreakEvent(event.getEntity().getUsedItemHand()));
            }
        }
    }

    //脆弱 右键物品 钓鱼竿
    @SubscribeEvent
    public static void rightClick(RightClickItem event) {
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        Item item = stack.getItem();
        if (item instanceof FishingRodItem) {
            int unDurable = stack.getEnchantmentLevel(YEEnchants.unDurable.get());
            if (unDurable > 0  && Config.SERVER.isUnDurable.get())
                stack.hurtAndBreak(RANDOM.nextInt(unDurable) + 1, player, e -> e.broadcastBreakEvent(event.getHand()));
        }
    }

    //农夫
    @SubscribeEvent
    public static void rightClickBlock(RightClickBlock event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        int farmer = EnchantmentHelper.getEnchantmentLevel(YEEnchants.farmer.get(), player);
        if (farmer > 0 && Config.SERVER.isFarmer.get()) {
            Farmer.harvestCrop(player, hand, world, pos, farmer);
        }
    }

    //脆弱 使用锄头
    @SubscribeEvent
    public static void toolModification(BlockToolModificationEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        ItemStack item = event.getHeldItemStack();
        int unDurable = item.getEnchantmentLevel(YEEnchants.unDurable.get());
        if (unDurable > 0  && Config.SERVER.isUnDurable.get()) {
            item.hurtAndBreak(RANDOM.nextInt(unDurable) + 1, player, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        }
    }

    //脆弱 洞察 海之嫌弃 钓鱼竿钓起鱼
    @SubscribeEvent
    public static void itemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        if (player != null) {
            ItemStack stack = player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem();
            if (stack.getItem() instanceof FishingRodItem) {
                Level world = player.level();
                int unDurable = stack.getEnchantmentLevel(YEEnchants.unDurable.get());
                int insight = stack.getEnchantmentLevel(YEEnchants.insight.get());
                int badLuckOfTheSea = stack.getEnchantmentLevel(YEEnchants.badLuckOfTheSea.get());
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
    public static void breakSpeed(BreakSpeed event) {
        Player player = event.getEntity();
        if (player == null) return;
        int slow = player.getItemInHand(player.getUsedItemHand()).getEnchantmentLevel(YEEnchants.slow.get());
        if (slow > 0 && Config.SERVER.isSlow.get()) {
            event.setNewSpeed(event.getOriginalSpeed() * (1 - slow * 0.2f)); //挖掘速度变慢
        }
    }

    //万箭
    @SubscribeEvent
    public static void arrowLoose(ArrowLooseEvent event) {
        Level world = event.getLevel();
        ItemStack bow = event.getBow();
        if (!bow.isEmpty() && bow.getItem() instanceof BowItem){
            int manyArrow = bow.getEnchantmentLevel(YEEnchants.manyArrow.get());
            if (manyArrow > 0  && Config.SERVER.isManyArrow.get()) {
                ManyArrow.manyArrow(event.getCharge(), event.getEntity(), bow, manyArrow, world);
            }
        }
    }

    //快速拉弓 失稳
    @SubscribeEvent
    public static void  useItem(LivingEntityUseItemEvent event){
        ItemStack item = event.getItem();
        int fastBow = item.getEnchantmentLevel(YEEnchants.fastBow.get());
        int duration = event.getDuration();
        if (fastBow > 0 && Config.SERVER.isFastBow.get() && duration > fastBow){
            event.setDuration(duration - fastBow);
        }
        LivingEntity living = event.getEntity();
        if (living instanceof Player){
            int instability = living.getMainHandItem().getEnchantmentLevel(YEEnchants.instability.get());
            EventHelper.dropItem((Player) living, instability);
        }
    }

    //经验腐蚀 获取经验
    @SubscribeEvent
    public static void pickupXp(PickupXp event) {
        Player player = event.getEntity();
        if (player.takeXpDelay != 0) return;
        int xpValue = event.getOrb().getValue(); //经验值
        player.takeXpDelay = 2;
        player.take(event.getOrb(), 1); //捡起经验
        //获取含有此附魔的装备map
        Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(YEEnchants.expCorrode.get(), player);
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
    public static void livingUpdate(LivingTickEvent event) {
        LivingEntity entityLiving = event.getEntity();
        if (entityLiving instanceof Player player) {
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            int lavaWalker = feet.getEnchantmentLevel(YEEnchants.lavaWalker.get());
            if (lavaWalker > 0  && Config.SERVER.isLavaWalker.get()) {
                LavaWalker.freezingNearby(player, player.level(), player.getOnPos(), lavaWalker);
            }
            int magnet = player.getItemBySlot(EquipmentSlot.LEGS).getEnchantmentLevel(YEEnchants.magnet.get());
            if (magnet > 0 && player.isCrouching() && Config.SERVER.isMagnet.get()) {
                Magnet.moveEntityItemsInRegion(player.level(), player.getOnPos(), 3 + magnet * 2, magnet);
            }
            int waterWalk = feet.getEnchantmentLevel(YEEnchants.waterWalk.get());
            if (waterWalk > 0 && Config.SERVER.isWaterWalk.get()) {
                WaterWalk.walk(player);
            }
            if (!player.level().isClientSide){
                EventHelper.changeMaxHealth(player);
                EventHelper.changeHandRange(player);
                EventHelper.changeSwimSpeed(player);
            }
        }
    }

    //玩家登入
    @SubscribeEvent
    public static void playerLogin(PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.getPersistentData().getBoolean("yuoenchants:login")){
            player.getPersistentData().putBoolean("yuoenchants:login", true);
            //发送消息
            player.sendSystemMessage(Component.translatable("yuoenchants.message.login")
                    .setStyle(Style.EMPTY.withHoverEvent(Action.SHOW_TEXT.deserializeFromLegacy(Component.translatable("yuoenchants.message.login0")))
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))));
        }
    }

    //雷击 真荆棘
    @SubscribeEvent
    public static void playerTick(PlayerTickEvent event) {
        Player player = event.player;
        if (player == null || player.level().isClientSide) return;
        ItemStack stackLegs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack stackChest = player.getItemBySlot(EquipmentSlot.CHEST);
        int lightningDamage = stackLegs.getEnchantmentLevel(YEEnchants.lightningDamage.get());
        if (lightningDamage > 0   && Config.SERVER.isLightningDamage.get()) { //在雨天生效
            LightningDamage.lighting(player, stackLegs);
        }
        int thorns = stackChest.getEnchantmentLevel(YEEnchants.thorns.get());
        if (thorns > 0   && Config.SERVER.isThorns.get()) {
            Thorns.thorns(player, stackChest, thorns);
        }
    }

    //洞察 生物掉落经验
    @SubscribeEvent
    public static void expDrop(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (player != null) {
            int insight = player.getUseItem().getEnchantmentLevel(YEEnchants.insight.get());
            if (insight > 0  && Config.SERVER.isInsight.get()) {
                double exp = event.getOriginalExperience() * (100 + insight * 30) / 100.0;
                event.setDroppedExperience((int) Math.ceil(exp));
            }
            int unLooting = player.getUseItem().getEnchantmentLevel(YEEnchants.unLooting.get());
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
            ItemStack mainHand = player.getItemBySlot(EquipmentSlot.MAINHAND);
            int leech = mainHand.getEnchantmentLevel(YEEnchants.leech.get());
            if (leech > 0  && Config.SERVER.isLeech.get()) {
                player.heal(leech / 2.0f); //回血
            }
            int healthToS = mainHand.getEnchantmentLevel(YEEnchants.healthToSacrifice.get());
            if (healthToS > 0 && Config.SERVER.isHealthToSacrifice.get()) {
                LivingEntity entityLiving = event.getEntity();
                Level world = entityLiving.level();
                int luck = 0;
                MobEffectInstance effect = player.getEffect(MobEffects.LUCK);
                if (effect != null)
                    luck = effect.getAmplifier();
                int looting = mainHand.getEnchantmentLevel(Enchantments.MOB_LOOTING);
                HealthToSacrifice.dropExpDrip(world, healthToS, looting, entityLiving.getOnPos(), entityLiving.getMaxHealth(), luck);
            }
        }
    }

    //斩首 抢劫 生物掉落
    @SubscribeEvent
    public static void livingDrop(LivingDropsEvent event) {
        Entity trueSource = event.getSource().getDirectEntity();
        if (trueSource instanceof Player player) {
            ItemStack useItem = player.getItemBySlot(EquipmentSlot.MAINHAND);
            int beHead = useItem.getEnchantmentLevel(YEEnchants.beHead.get());
            if (beHead > 0  && Config.SERVER.isBehead.get()) {
                event.getDrops().add(BeHead.dropHead(beHead, event.getEntity()));
            }
            int unLooting = useItem.getEnchantmentLevel(YEEnchants.unLooting.get());
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
            int robbery = player.getUseItem().getEnchantmentLevel(YEEnchants.robbery.get());
            if (robbery > 0 && Config.SERVER.isRobbery.get()){
                event.setLootingLevel(Robbery.getLootingLevel(event.getLootingLevel(), robbery));
            }
        }
    }

    //快速恢复
    @SubscribeEvent
    public static void livingHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof Player player) {
            int fastHeal = player.getItemBySlot(EquipmentSlot.CHEST).getEnchantmentLevel(YEEnchants.fastHeal.get());
            if (fastHeal > 0 && Config.SERVER.isFastHeal.get()) {
                event.setAmount(FastHeal.fastHeal(fastHeal, event.getAmount()));
            }
        }
    }

    //斥力
    @SubscribeEvent
    public static void useItemTick(Tick event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getItem().getItem() instanceof ProjectileWeaponItem) {
                int repulsion = event.getItem().getEnchantmentLevel(YEEnchants.repulsion.get());
                if (repulsion > 0 && Config.SERVER.isRepulsion.get()) {
                    Repulsion.moveLivingEntityInRegion(player.level(), player.getOnPos(), 1 + repulsion, repulsion);
                }
            }
        }
    }

    //弹反 火焰盾 火焰免疫 熔岩行者
    @SubscribeEvent
    public static void livingAttack(LivingAttackEvent event) {
        LivingEntity living = event.getEntity();
        if (living instanceof Player player) {
            ItemStack shield = player.getItemInHand(player.getUsedItemHand());
            DamageSource source = event.getSource();
            if (!shield.isEmpty() && player.isBlocking() && player.getUseItem() == shield) {
                int rebound = shield.getEnchantmentLevel(YEEnchants.rebound.get());
                if (rebound > 0 && Config.SERVER.isRebound.get()) {
                    Rebound.rebound(event, rebound, player, shield);
                }
                int fireShield = shield.getEnchantmentLevel(YEEnchants.fireShield.get());
                if (fireShield > 0 && Config.SERVER.isFireShield.get()) {
                    FireShield.fireShield(source, fireShield, player);
                }
            }
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            int fireImmune = legs.getEnchantmentLevel(YEEnchants.fireImmune.get());
            if (fireImmune > 0 && Config.SERVER.isFireImmune.get() && source.is(DamageTypeTags.IS_FIRE)) {
                FireImmune.fireImmune(event, legs, player);
            }
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            int lavaWalker = feet.getEnchantmentLevel(YEEnchants.lavaWalker.get());
            if (lavaWalker > 0 && event.getSource().is(DamageTypeTags.IS_FIRE)  && Config.SERVER.isLavaWalker.get()){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void leftClickBlock(LeftClickBlock event){
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        int instability = stack.getEnchantmentLevel(YEEnchants.instability.get());
        if (!player.level().isClientSide && Config.SERVER.isInstability.get()){
            EventHelper.dropItem(player, instability);
        }
    }
}

