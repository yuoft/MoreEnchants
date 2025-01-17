package com.yuo.enchants.Event;

import com.yuo.enchants.Config;
import com.yuo.enchants.Enchants.*;
import com.yuo.enchants.Proxy.CommonProxy;
import com.yuo.enchants.YuoEnchants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PotatoBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
@Mod.EventBusSubscriber(modid = YuoEnchants.MOD_ID)
public class EventHandler {
    private static final Random RANDOM = new Random(); //随机数

    //附魔，火焰免疫 屹立不倒 受到伤害
    @SubscribeEvent
    public static void fireImmune(LivingHurtEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity) { //只对玩家生效
            PlayerEntity player = (PlayerEntity) entityLiving;
            ItemStack stackLegs = player.getItemStackFromSlot(EquipmentSlotType.LEGS);
            ItemStack stackFeet = player.getItemStackFromSlot(EquipmentSlotType.FEET);
            int lavaWalker = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.lavaWalker.get(), stackFeet);
            if (lavaWalker > 0 && event.getSource() == DamageSource.HOT_FLOOR  && Config.SERVER.isLavaWalker.get()){
                event.setCanceled(true);
            }
            int fireImmune_legs = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.fireImmune.get(), stackLegs);
            if (fireImmune_legs > 0 && Config.SERVER.isFireImmune.get()) {
                FireImmune.fireImmune(event, stackLegs, player);
            }
            int lastStand = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.lastStand.get(), stackFeet);
            if (lastStand > 0   && Config.SERVER.isLastStand.get()) {
                LastStand.lastStand(player, event, stackFeet);
            }
            int superFire = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.superFire.get(), stackLegs);
            if (superFire > 0 && event.getSource().isFireDamage() && Config.SERVER.isSuperFire.get()) {
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superFire));
            }
            int superBlast = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.superBlast.get(), player.getItemStackFromSlot(EquipmentSlotType.CHEST));
            if (superBlast > 0 && event.getSource().isExplosion() && Config.SERVER.isSuperBlast.get()) {
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superBlast));
            }
            int superArrow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.superArrow.get(), player.getItemStackFromSlot(EquipmentSlotType.HEAD));
            if (superArrow > 0 && event.getSource().isProjectile() && Config.SERVER.isSuperArrow.get()) {
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superArrow));
            }
        }
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) trueSource;
            ItemStack mainHand = EventHelper.getUseItem(player);
            int beHead = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.beHead.get(), mainHand);
            if (beHead > 0  && Config.SERVER.isBehead.get()) {
                BeHead.addDamage(beHead, event, player, entityLiving);
            }
        }
    }

    //高级摔落保护
    @SubscribeEvent
    public static void fallProtect(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            int superFall = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.superFall.get(), player.getItemStackFromSlot(EquipmentSlotType.FEET));
            if (superFall > 0 && Config.SERVER.isSuperFall.get()) {
                event.setDamageMultiplier(SuperProtect.getDamage(event.getDamageMultiplier(), superFall));
            }
        }
    }

    //附魔，以战养战 脆弱 生命献祭 攻击生物
    @SubscribeEvent
    public static void warToWar(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player == null) return;
        ItemStack stack = EventHelper.getUseItem(player);
        int warToWar = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.warToWar.get(), stack);
        if (warToWar > 0 && Config.SERVER.isWarToWar.get()) { //有附魔
            WarToWar.heal(warToWar, player);
        }
        int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
        if (unDurable > 0  && Config.SERVER.isUnDurable.get()) {
            UnDurable.unDurable(stack, unDurable, player);
        }

        int instability = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.instability.get(), player.getHeldItemMainhand());
        EventHelper.dropItem(player, instability);
    }

    //爆炸箭 箭碰撞方块或实体
    @SubscribeEvent
    public static void blastArrow(ProjectileImpactEvent.Arrow event) {
        AbstractArrowEntity arrow = event.getArrow();
        if (arrow.getShooter() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) arrow.getShooter();
            if (player != null) {
                ItemStack itemStack = EventHelper.getUseItem(player);
                int blastArrow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.blastArrow.get(), itemStack);
                if (blastArrow > 0  && Config.SERVER.isBlastArrow.get()) {
                    BlastArrow.boom(arrow, blastArrow);
                }
            }
        }
    }

    //脆弱 洞察 熔炼 粉碎 范围挖掘 强运  --破坏方块
    @SubscribeEvent
    public static void unDurableTool(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player == null) return;
        ItemStack tool = EventHelper.getUseItem(player);
        if (tool.isEmpty()) return;
        Item item = tool.getItem();
        if (item instanceof ToolItem || item instanceof ShearsItem) {
            int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), tool);
            if (unDurable > 0  && Config.SERVER.isUnDurable.get()) {
                tool.damageItem(RANDOM.nextInt(unDurable) + 1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND)); //破坏方块时消耗更多耐久
            }
        }
        if (event.getExpToDrop() > 0) {
            int insight = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.insight.get(), tool);
            if (insight > 0  && Config.SERVER.isInsight.get()) {
                Insight.addDropExp(event, insight);
            }
        }
        World world = (World) event.getWorld();
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        BlockState state = event.getState();

        int strengthLuck = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.strengthLuck.get(), tool);
        int unLuck = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unLuck.get(), tool);
        if (strengthLuck > 0 && Config.SERVER.isStrengthLuck.get()) {
            StrengthLuck.strengthLuck(block, state, world, pos, strengthLuck, player);
            event.setCanceled(true);
            return;
        }
        if (unLuck > 0 && Config.SERVER.isUnLuck.get() &&  RANDOM.nextDouble() < unLuck * 0.2){
            if (event.getExpToDrop() > 0){
                event.setExpToDrop(0);
            }
            if (block == Blocks.POTATOES){ //破坏成熟马铃薯，掉落毒马铃薯
                Integer cropAge = state.get(PotatoBlock.AGE);
                if (cropAge >= 7) world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                        new ItemStack(Items.POISONOUS_POTATO)));
            }
            world.setBlockState(event.getPos(), Blocks.AIR.getDefaultState());
        }
        int rangBreak = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.rangBreak.get(), tool);

        if (player.isSneaking() && rangBreak > 0  && Config.SERVER.isRangBreak.get()) {
            EventHelper.breakBlocks(tool, world, pos, state, player, Math.min(rangBreak, 5)); //最大等级5
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            event.setCanceled(true);
            return;
        }
        int melting = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.melting.get(), tool); //熔炼
        if (melting > 0  && Config.SERVER.isMelting.get()) {
            Melting.melting(block, state, world, pos, player, tool, event);
        }
        int diamondDrop = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.diamondDrop.get(), tool);
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
        if (diamondDrop > 0 && state.getBlock() == Blocks.COAL_ORE && Config.SERVER.isDiamondDrop.get()){
            DiamondDrop.diamondDrop(diamondDrop, world, fortune, pos);
        }
    }

    //脆弱 盔甲
//    @SubscribeEvent
//    public static void unDurableDamage(LivingAttackEvent event){
//        LivingEntity entityLiving = event.getEntityLiving();
//        if (entityLiving instanceof PlayerEntity){
//            PlayerEntity player = (PlayerEntity) entityLiving;
//            player.getArmorInventoryList().forEach(e -> {
//                int level = EnchantmentHelper.getEnchantmentLevel(EnchantsRegistry.unDurable.get(), e);
//                if (level > 0){
//                    e.damageItem(level, player, f -> f.sendBreakAnimation(Hand.MAIN_HAND));
//                }
//            });
//        }
//    }
    //脆弱 物品右键使用 弓 弩 三叉戟

    //脆弱 弓 弩 三叉戟 停止使用物品
    @SubscribeEvent
    public static void unDurableRightUse(LivingEntityUseItemEvent.Stop event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            ItemStack stack = event.getItem();
            Item item = stack.getItem();
            if (item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem) {
                int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
                if (unDurable > 0  && Config.SERVER.isUnDurable.get())
                    stack.damageItem(RANDOM.nextInt(unDurable) + 1, player, e -> e.sendBreakAnimation(event.getEntityLiving().getActiveHand()));
            }
        }
    }

    //脆弱 右键物品 钓鱼竿
    @SubscribeEvent
    public static void unDurableRightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        PlayerEntity player = event.getPlayer();
        Item item = stack.getItem();
        if (item instanceof FishingRodItem) {
            int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
            if (unDurable > 0  && Config.SERVER.isUnDurable.get())
                stack.damageItem(RANDOM.nextInt(unDurable) + 1, player, e -> e.sendBreakAnimation(event.getHand()));
        }
    }

    //农夫
    @SubscribeEvent
    public static void farmer(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        int farmer = EnchantmentHelper.getMaxEnchantmentLevel(EnchantRegistry.farmer.get(), player);
        if (farmer > 0 && Config.SERVER.isFarmer.get()) {
            Farmer.harvestCrop(player, hand, world, pos, farmer);
        }
    }

    //脆弱 使用锄头
    @SubscribeEvent
    public static void unDurableHoe(UseHoeEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemUseContext context = event.getContext();
        ItemStack item = context.getItem();
        int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), item);
        if (unDurable > 0  && Config.SERVER.isUnDurable.get()) {
            item.damageItem(RANDOM.nextInt(unDurable) + 1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
        }
    }

    //脆弱 洞察 海之嫌弃 钓鱼竿钓起鱼
    @SubscribeEvent
    public static void unDurableFishing(ItemFishedEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player != null) {
            ItemStack stack = EventHelper.getUseItem(player);
            if (stack.getItem() instanceof FishingRodItem) {
                World world = player.world;
                int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
                int insight = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.insight.get(), stack);
                int badLuckOfTheSea = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.badLuckOfTheSea.get(), stack);
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
    public static void onPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
        PlayerEntity player = event.getPlayer();
        if (player == null) return;
        int slow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.slow.get(), EventHelper.getUseItem(player));
        if (slow > 0   && Config.SERVER.isSlow.get()) {
            event.setNewSpeed(event.getOriginalSpeed() * (1 - slow * 0.2f)); //挖掘速度变慢
        }
    }

    //万箭
    @SubscribeEvent
    public static void manyArrow(ArrowLooseEvent event) {
        World world = event.getWorld();
        ItemStack bow = event.getBow();
        int manyArrow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.manyArrow.get(), bow);
        if (manyArrow > 0  && Config.SERVER.isManyArrow.get()) {
            ManyArrow.manyArrow(event.getCharge(), event.getPlayer(), bow, manyArrow, world);
        }
    }

    //快速拉弓
    @SubscribeEvent
    public static void  fastBow(LivingEntityUseItemEvent event){
        ItemStack item = event.getItem();
        int fastBow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.fastBow.get(), item);
        int duration = event.getDuration();
        if (fastBow > 0 && Config.SERVER.isFastBow.get() && duration > fastBow){
            event.setDuration(duration - fastBow);
        }
    }

    //经验腐蚀 获取经验
    @SubscribeEvent
    public static void expCorrode(PlayerXpEvent.PickupXp event) {
        PlayerEntity player = event.getPlayer();
        if (player.xpCooldown != 0) return;
        int xpValue = event.getOrb().getXpValue(); //经验值
        player.xpCooldown = 2;
        player.onItemPickup(event.getOrb(), 1);
        //获取含有此附魔的装备map
        Map.Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWithEnchantment(EnchantRegistry.expCorrode.get(), player);
        if (entry != null  && Config.SERVER.isExpCorrode.get()) {
            ItemStack itemstack = entry.getValue();
            if (!itemstack.isEmpty() && itemstack.getDamage() > 0) { //物品非空，且有耐久
                int i = (int) (xpValue * 1.0f);  //获取经验值 * 物品经验修复率
                xpValue -= i / 2; //玩家最终获取的经验
                itemstack.damageItem(i, player, e -> e.sendBreakAnimation(entry.getKey())); //腐蚀物品
            }
        }
        if (xpValue > 0) { //如果经验有剩余
            player.giveExperiencePoints(xpValue); //玩家获取经验
        }
    }

    //岩浆行者 生机 距离提升 实体更新
    @SubscribeEvent
    public static void lavaWalker(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            ItemStack feet = player.getItemStackFromSlot(EquipmentSlotType.FEET);
            //重置二段跳次数
            if ((!player.isAirBorne || player.isOnGround()) && !feet.isEmpty() && feet.getOrCreateTag().getInt(DoubleJump.USES) > 0) {
                feet.getOrCreateTag().putInt(DoubleJump.USES, 0);
            }
            int lavaWalker = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.lavaWalker.get(), feet);
            if (lavaWalker > 0  && Config.SERVER.isLavaWalker.get()) {
                LavaWalker.freezingNearby(player, player.world, player.getPosition(), lavaWalker);
            }
            int magnet = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.magnet.get(), player.getItemStackFromSlot(EquipmentSlotType.LEGS));
            if (magnet > 0 && player.isSneaking() && Config.SERVER.isMagnet.get()) {
                Magnet.moveEntityItemsInRegion(player.world, player.getPosition(), 3 + magnet * 2, magnet);
            }
            int waterWalk = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.waterWalk.get(), feet);
            if (waterWalk > 0 && Config.SERVER.isWaterWalk.get()) {
                WaterWalk.walk(player);
            }
            if (!player.world.isRemote){
                EventHelper.changeMaxHealth(player);
                EventHelper.changeHandRange(player);
                EventHelper.changeSwimSpeed(player);
            }
        }
    }

    //玩家登入
    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        if (!player.getPersistentData().getBoolean("yuoenchants:login")){
            player.getPersistentData().putBoolean("yuoenchants:login", true);
            //发送消息
            player.sendMessage(new TranslationTextComponent("yuoenchants.message.login")
                    .setStyle(Style.EMPTY.setHoverEvent(HoverEvent.Action.SHOW_TEXT.deserialize(new TranslationTextComponent("yuoenchants.message.login0")))
                            .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))), UUID.randomUUID());
        }
    }

    //雷击 真荆棘
    @SubscribeEvent
    public static void tickEvent(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player == null || player.world.isRemote) return;
        ItemStack stackLegs = player.getItemStackFromSlot(EquipmentSlotType.LEGS);
        ItemStack stackChest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
        int lightningDamage = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.lightningDamage.get(), stackLegs);
        if (lightningDamage > 0   && Config.SERVER.isLightningDamage.get()) { //在雨天生效
            LightningDamage.lighting(player, stackLegs);
        }
        int thorns = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.thorns.get(), stackChest);
        if (thorns > 0   && Config.SERVER.isThorns.get()) {
            Thorns.thorns(player, stackChest, thorns);
        }
    }

    //洞察 生物掉落经验
    @SubscribeEvent
    public static void expDrops(LivingExperienceDropEvent event) {
        PlayerEntity player = event.getAttackingPlayer();
        if (player != null) {
            int insight = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.insight.get(), EventHelper.getUseItem(player));
            if (insight > 0  && Config.SERVER.isInsight.get()) {
                double exp = event.getOriginalExperience() * (100 + insight * 30) / 100.0;
                event.setDroppedExperience((int) Math.ceil(exp));
            }
            int unLooting = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unLooting.get(), EventHelper.getUseItem(player));
            if (unLooting > 0 && Config.SERVER.isUnLooting.get() && event.getDroppedExperience() > 0){
                int luck = -1;
                EffectInstance instance = player.getActivePotionEffect(Effects.LUCK);
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
    public static void insightLiving(LivingDeathEvent event) {
        Entity trueSource = event.getSource().getTrueSource(); //伤害来源
        if (trueSource instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) trueSource;
            ItemStack mainHand = EventHelper.getUseItem(player);
            int leech = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.leech.get(), mainHand);
            if (leech > 0  && Config.SERVER.isLeech.get()) {
                player.heal(leech / 2.0f); //回血
            }
            int healthToS = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.healthToSacrifice.get(), mainHand);
            if (healthToS > 0 && Config.SERVER.isHealthToSacrifice.get()) {
                LivingEntity entityLiving = event.getEntityLiving();
                World world = entityLiving.world;
                int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, mainHand);
                HealthToSacrifice.dropExpDrip(world, healthToS, looting, entityLiving.getPosition(), entityLiving.getMaxHealth());
            }
        }
    }

    //斩首 抢劫 生物掉落
    @SubscribeEvent
    public static void vorapl(LivingDropsEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) trueSource;
            int beHead = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.beHead.get(), EventHelper.getUseItem(player));
            if (beHead > 0  && Config.SERVER.isBehead.get()) {
                event.getDrops().add(BeHead.dropHead(beHead, event.getEntityLiving()));
            }
            int unLooting = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unLooting.get(), EventHelper.getUseItem(player));
            if (unLooting > 0 && Config.SERVER.isUnLooting.get()){
                int luck = -1;
                EffectInstance instance = player.getActivePotionEffect(Effects.LUCK);
                if (instance != null)
                    luck = instance.getAmplifier();
                int finalLuck = luck;
                event.getDrops().removeIf(e -> RANDOM.nextDouble() < 0.2 * unLooting - (finalLuck + 1) * 0.1);
            }
        }
    }

    //抢夺等级
    @SubscribeEvent
    public static void looting(LootingLevelEvent event) {
        Entity source = event.getDamageSource().getTrueSource();
        if (source instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source;
            int robbery = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.robbery.get(), EventHelper.getUseItem(player));
            if (robbery > 0 && Config.SERVER.isRobbery.get()){
                event.setLootingLevel(Robbery.getLootingLevel(event.getLootingLevel(), robbery));
            }
        }
    }

    //快速恢复
    @SubscribeEvent
    public static void fastHeal(LivingHealEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            int fastHeal = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.fastHeal.get(), player.getItemStackFromSlot(EquipmentSlotType.CHEST));
            if (fastHeal > 0 && Config.SERVER.isFastHeal.get()) {
                event.setAmount(FastHeal.fastHeal(fastHeal, event.getAmount()));
            }
        }
    }

    //斥力
    @SubscribeEvent
    public static void repulsion(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (event.getItem().getItem() instanceof ShootableItem) {
                int repulsion = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.repulsion.get(), event.getItem());
                if (repulsion > 0 && Config.SERVER.isRepulsion.get()) {
                    Repulsion.moveLivingEntityInRegion(player.world, player.getPosition(), 1 + repulsion, repulsion);
                }
            }
        }
    }

    //超级力量
    @SubscribeEvent
    public static void superPower(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ArrowEntity) {
            ArrowEntity arrow = (ArrowEntity) entity;
            Entity shooter = arrow.getShooter();
            if (shooter instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) shooter;
                ItemStack bow = living.getActiveItemStack();
                if (!bow.isEmpty() && bow.getItem() instanceof BowItem) {
                    int superPower = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.superPower.get(), bow);
                    if (superPower > 0 && Config.SERVER.isSuperPower.get()) {
                        arrow.setDamage(arrow.getDamage() + 1.25D + (double) superPower * 0.75D);
                    }
                }
            }
        }
    }

    //弹反 火焰盾
    @SubscribeEvent
    public static void rebound(LivingAttackEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) living;
            ItemStack shield = player.getHeldItemOffhand();
            if (!shield.isEmpty() && shield.isShield(player)) {
                int rebound = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.rebound.get(), shield);
                if (rebound > 0 && Config.SERVER.isRebound.get()) {
                    Rebound.rebound(event, rebound, player, shield);
                }
                int fireShield = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.fireShield.get(), shield);
                if (fireShield > 0 && Config.SERVER.isFireShield.get()) {
                    FireShield.fireShield(event.getSource(), fireShield, player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void instability(PlayerInteractEvent.LeftClickBlock event){
        ItemStack stack = event.getItemStack();
        PlayerEntity player = event.getPlayer();
        int instability = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.instability.get(), stack);
        if (!player.world.isRemote && Config.SERVER.isInstability.get()){
            EventHelper.dropItem(player, instability);
        }
    }

    @SubscribeEvent
    public static void playerUseItem(LivingEntityUseItemEvent.Start event){
        LivingEntity living = event.getEntityLiving();
        if (living instanceof PlayerEntity){
            int instability = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.instability.get(), living.getHeldItemMainhand());
            EventHelper.dropItem((PlayerEntity) living, instability);
        }
    }
}

