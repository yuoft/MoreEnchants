package com.yuo.enchants.Event;

import com.yuo.enchants.Enchants.*;
import com.yuo.enchants.Items.ItemRegistry;
import com.yuo.enchants.Items.ModEnchantBook;
import com.yuo.enchants.Items.OldBook;
import com.yuo.enchants.MoreEnchants;
import jdk.nashorn.internal.ir.annotations.Ignore;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import org.lwjgl.glfw.GLFW;

import java.util.*;

/**
 * 事件处理类 附魔实现
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MoreEnchants.MODID)
public class EventHandler {
    private static final Random RANDOM = new Random(); //随机数
    public static List<String> playerHealth = new ArrayList<>();
    public static List<String> playerHandRange = new ArrayList<>();

    public static final float attrHealth = 2.0f; //属性变更基础系数
    public static final float attrHandRange = 0.5f; //属性变更基础系数
    //附魔，火焰免疫 屹立不倒 受到伤害
    @SubscribeEvent
    public static void fireImmune(LivingHurtEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){ //只对玩家生效
            PlayerEntity player = (PlayerEntity) entityLiving;
            ItemStack stackLegs = player.getItemStackFromSlot(EquipmentSlotType.LEGS);
            ItemStack stackFeet = player.getItemStackFromSlot(EquipmentSlotType.FEET);
            int fireImmune_legs = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.fireImmune.get(), stackLegs);
            if(fireImmune_legs > 0) {
                FireImmune.fireImmune(event, stackLegs, player);
            }
            int lastStand = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.lastStand.get(), stackFeet);
            if (lastStand > 0){
                LastStand.lastStand(player, event, stackFeet);
            }
            int superFire = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.superFire.get(), stackLegs);
            if (superFire > 0 && event.getSource().isFireDamage()){
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superFire));
            }
            int superBlast = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.superBlast.get(), player.getItemStackFromSlot(EquipmentSlotType.CHEST));
            if (superBlast > 0 && event.getSource().isExplosion()){
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superBlast));
            }
            int superArrow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.superArrow.get(), player.getItemStackFromSlot(EquipmentSlotType.HEAD));
            if (superArrow > 0 && event.getSource().isProjectile()){
                event.setAmount(SuperProtect.getDamage(event.getAmount(), superArrow));
            }
        }
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) trueSource;
            ItemStack mainhand = player.getActiveItemStack();
            int beHead = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.beHead.get(), mainhand);
            if (beHead > 0){
                BeHead.addDamage(beHead, event, player, entityLiving);
            }
        }
    }
    //高级摔落保护
    @SubscribeEvent
    public static void fallProtect(LivingFallEvent event){
        if (event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            int superFall = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.superFall.get(), player.getItemStackFromSlot(EquipmentSlotType.FEET));
            if (superFall > 0){
                event.setDamageMultiplier(SuperProtect.getDamage(event.getDamageMultiplier(), superFall));
            }
        }
    }

    //附魔，以战养战 脆弱 攻击生物
    @SubscribeEvent
    public static void warToWar(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if(player == null) return;
        ItemStack stack = player.getActiveItemStack();
        int warToWar = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.warToWar.get(), stack);
        if( warToWar > 0) { //有附魔
            WarToWar.heal(warToWar, player);
        }
        int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
        if (unDurable > 0){
            UnDurable.unDurable(stack, unDurable, player);
        }
    }
    //爆炸箭 箭碰撞方块或实体
    @SubscribeEvent
    public static void blastArrow(ProjectileImpactEvent.Arrow event) {
        AbstractArrowEntity arrow = event.getArrow();
        if(arrow.getShooter() instanceof PlayerEntity) {
            PlayerEntity player=(PlayerEntity) arrow.getShooter();
            ItemStack itemStack=player.getActiveItemStack();
            int blastArrow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.blastArrow.get(), itemStack);
            if (blastArrow > 0){
                BlastArrow.boom(arrow, blastArrow);
            }
        }
    }
    //脆弱 洞察 熔炼 粉碎 范围挖掘 破坏方块
    @SubscribeEvent
    public static void unDurableTool(BlockEvent.BreakEvent event){
        PlayerEntity player = event.getPlayer();
        if (player == null) return;
        ItemStack stack = player.getActiveItemStack();
        if (stack.isEmpty()) return;
        Item item = stack.getItem();
        if (item instanceof ToolItem || item instanceof ShearsItem){
            int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
            if (unDurable > 0){
                stack.damageItem(RANDOM.nextInt(unDurable) + 1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND)); //破坏方块时消耗更多耐久
            }
        }
        if (event.getExpToDrop() > 0){
            int insight = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.insight.get(), stack);
            if (insight > 0){
                Insight.addDropExp(event, insight);
            }
        }
        World world = (World) event.getWorld();
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        BlockState state = event.getState();
        int rangBreak = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.rangBreak.get(), stack);
        if (KeyBindingEvent.isIsKeyC() && rangBreak > 0){
            EventHelper.breakBlocks(stack, world, pos, state, player, Math.min(rangBreak, 5)); //最大等级5
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }
        int melting = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.melting.get(), stack); //熔炼
        if (melting > 0){
            Melting.melting(block, state, world, pos, player, stack, event);
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
    public static void unDurableRightUse(LivingEntityUseItemEvent.Stop event){
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            ItemStack stack = event.getItem();
            Item item = stack.getItem();
            if (item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem) {
                int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
                if (unDurable > 0) stack.damageItem(RANDOM.nextInt(unDurable) + 1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
            }
        }
    }
    //脆弱 右键物品 钓鱼竿
    @SubscribeEvent
    public static void unDurableRightClick(PlayerInteractEvent.RightClickItem event){
        ItemStack stack = event.getItemStack();
        PlayerEntity player = event.getPlayer();
        Item item = stack.getItem();
        if (item instanceof FishingRodItem){
            int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
            if (unDurable > 0) stack.damageItem(RANDOM.nextInt(unDurable) + 1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
        }
    }
    //农夫
    @SubscribeEvent
    public static void farmer(PlayerInteractEvent.RightClickBlock event){
        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        int farmer = EnchantmentHelper.getMaxEnchantmentLevel(EnchantRegistry.farmer.get(), player);
        if (farmer > 0){
            Farmer.harvestCrop(player, hand, world, pos, farmer);
        }
    }
    //脆弱 使用锄头
    @SubscribeEvent
    public static void unDurableHoe(UseHoeEvent event){
        PlayerEntity player = event.getPlayer();
        ItemUseContext context = event.getContext();
        ItemStack item = context.getItem();
        int level = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), item);
        if (level > 0){
            item.damageItem(RANDOM.nextInt(level) + 1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
        }
    }
    //脆弱 洞察 海之嫌弃 钓鱼竿钓起鱼
    @SubscribeEvent
    public static void unDurableFishing(ItemFishedEvent event){
        PlayerEntity player = event.getPlayer();
        if (player != null){
            ItemStack stack = player.getActiveItemStack();
            if (stack.getItem() instanceof FishingRodItem){
                World world = player.world;
                int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
                int insight = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.insight.get(), stack);
                int badLuckOfTheSea = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.badLuckOfTheSea.get(), stack);
                if (unDurable > 0){
                    event.damageRodBy(event.getRodDamage() + RANDOM.nextInt(unDurable) + 1);
                }
                if (insight > 0){
                    Insight.addFishingExp(player,  world, insight);
                }
                if (badLuckOfTheSea > 0){
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
        int slow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.slow.get(), player.getActiveItemStack());
        if (slow > 0){
            event.setNewSpeed(event.getOriginalSpeed() / (slow * 1.5f)); //挖掘速度变慢
        }
    }
    //万箭 斥力
    @SubscribeEvent
    public static void manyArrow(ArrowLooseEvent event){
        World world = event.getWorld();
        ItemStack bow = event.getBow();
        int charge = event.getCharge();
        int fastBow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.fastBow.get(), bow);
        if (fastBow > 0){
            event.setCharge(FastBow.fastDraw(fastBow, charge));
        }
        int manyArrow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.manyArrow.get(), bow);
        if (manyArrow > 0){
            ManyArrow.manyArrow(event.getCharge(), event.getPlayer(), bow, manyArrow, world);
        }
    }
    //经验腐蚀 获取经验
    @SubscribeEvent
    public static void expCorrode(PlayerXpEvent.PickupXp event){
        PlayerEntity player = event.getPlayer();
        if (player.xpCooldown!= 0) return;
        int xpValue = event.getOrb().getXpValue(); //经验值
        player.xpCooldown = 2;
        player.onItemPickup(event.getOrb(), 1);
        //获取含有此附魔的装备map
        Map.Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWithEnchantment(EnchantRegistry.expCorrode.get(), player);
        if (entry != null) {
            ItemStack itemstack = entry.getValue();
            if (!itemstack.isEmpty() && itemstack.getDamage() > 0) { //物品非空，且有耐久
                int i = (int)(xpValue * 1.0f);  //获取经验值 * 物品经验修复率
                xpValue -= i / 2; //玩家最终获取的经验
                itemstack.damageItem(i, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND)); //腐蚀物品
            }
        }
        if (xpValue > 0){ //如果经验有剩余
            player.giveExperiencePoints(xpValue); //玩家获取经验
        }
    }
    //岩浆行者 生机 距离提升 实体更新
    @SubscribeEvent
    public static void lavaWalker(LivingEvent.LivingUpdateEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            ItemStack feet = player.getItemStackFromSlot(EquipmentSlotType.FEET);
            if ((!player.isAirBorne || player.isOnGround()) && !feet.isEmpty() && feet.getOrCreateTag().getInt(DoubleJump.USES) > 0){
                feet.getOrCreateTag().putInt(DoubleJump.USES, 0);
            }
            int lavaWalker = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.lavaWalker.get(), feet);
            if (lavaWalker > 0){
                LavaWalker.freezingNearby(player, player.world, player.getPosition(), lavaWalker);
            }
            int magnet = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.magnet.get(), player.getItemStackFromSlot(EquipmentSlotType.LEGS));
            if (magnet > 0 && player.isSneaking()){
                Magnet.moveEntityItemsInRegion(player.world, player.getPosition(), 3 + magnet * 2, magnet);
            }
            int waterWalk = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.waterWalk.get(), feet);
            if (waterWalk > 0){
                WaterWalk.walk(player);
            }
            if (!player.world.isRemote)
                EventHelper.changeAttribute(player);
        }
    }
    //玩家登入
    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        PlayerEntity player = event.getPlayer();
        //重启游戏时添加key 防止属性叠加
        String key = player.getGameProfile().getName()+":"+player.world.isRemote;
        if (!playerHealth.contains(key) && !player.world.isRemote){
            int health = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.health.get(), player.getItemStackFromSlot(EquipmentSlotType.CHEST));
            ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MAX_HEALTH);
            if (health > 0 && attribute != null){
                playerHealth.add(key);
            }
        }
        if (!playerHandRange.contains(key) && !player.world.isRemote){
            int handRange = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.handRange.get(), player.getHeldItemMainhand());
            ModifiableAttributeInstance attribute = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
            if (handRange > 0 && attribute != null){
                playerHandRange.add(key);
            }
        }
        //发送消息
        player.sendMessage(new TranslationTextComponent("yuoenchants.message.login")
                .setStyle(Style.EMPTY.setHoverEvent(HoverEvent.Action.SHOW_TEXT.deserialize(new TranslationTextComponent("yuoenchants.message.login0")))
                        .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))), UUID.randomUUID());
    }

    //雷击 真荆棘
    @SubscribeEvent
    public static void tickEvent(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if (player == null || player.world.isRemote) return;
        ItemStack stackLegs = player.getItemStackFromSlot(EquipmentSlotType.LEGS);
        ItemStack stackChest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
        int lightningDamage = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.lightningDamage.get(), stackLegs);
        if (lightningDamage > 0){ //在雨天生效
            LightningDamage.lighting(player, stackLegs);
        }
        int thorns = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.thorns.get(), stackChest);
        if (thorns > 0){
            Thorns.thorns(player, stackChest, thorns);
        }
    }
    //洞察 生物掉落经验
    @SubscribeEvent
    public static void expDrops(LivingExperienceDropEvent event){
        PlayerEntity player = event.getAttackingPlayer();
        if (player != null){
            int insight = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.insight.get(), player.getHeldItemMainhand());
            if (insight > 0){
                double exp = event.getOriginalExperience() * (100 + insight * 30) / 100.0;
                event.setDroppedExperience((int) Math.ceil(exp));
            }
        }
    }
    //生命汲取 生物死亡
    @SubscribeEvent
    public static void insightLiving(LivingDeathEvent event){
        Entity trueSource = event.getSource().getTrueSource(); //伤害来源
        if (trueSource instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) trueSource;
            int leech = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.leech.get(), player.getHeldItemMainhand());
            if (leech > 0){
                player.heal(leech / 2.0f); //回血
            }
        }
    }
    //斩首 生物掉落
    @SubscribeEvent
    public static void vorapl(LivingDropsEvent event){
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) trueSource;
            int beHead = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.beHead.get(), player.getHeldItemMainhand());
            if (beHead > 0){
                event.getDrops().add(BeHead.dropHead(beHead, event.getEntityLiving()));
            }
        }
    }

    //快速恢复
    @SubscribeEvent
    public static void fastHeal(LivingHealEvent event){
        if (event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            int fastHeal = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.fastHeal.get(), player.getItemStackFromSlot(EquipmentSlotType.CHEST));
            if (fastHeal > 0){
                event.setAmount(FastHeal.fastHeal(fastHeal, event.getAmount()));
            }
        }
    }

    //斥力
    @SubscribeEvent
    public static void repulsion(LivingEntityUseItemEvent.Tick event){
        if (event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (event.getItem().getItem() instanceof ShootableItem){
                int repulsion = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.repulsion.get(), event.getItem());
                if (repulsion > 0){
                    Repulsion.moveLivingEntityInRegion(player.world, player.getPosition(), 1 + repulsion, repulsion);
                }
            }
        }
    }

    //超级力量
    @SubscribeEvent
    public static void superPower(EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof ArrowEntity){
            ArrowEntity arrow = (ArrowEntity) entity;
            Entity entity1 = arrow.getShooter();
            if (entity1 instanceof LivingEntity){
                LivingEntity living = (LivingEntity) entity1;
                ItemStack bow = living.getActiveItemStack();
                if (!bow.isEmpty() && bow.getItem() instanceof BowItem){
                    int superPower = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.superPower.get(), bow);
                    if (superPower > 0){
                        arrow.setDamage(arrow.getDamage() + 1.25D + (double)superPower * 0.75D);
                    }
                }
            }
        }
    }

    //弹反 火焰盾
    @SubscribeEvent
    public static void rebound(LivingAttackEvent event){
        LivingEntity living = event.getEntityLiving();
        if (living instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) living;
            ItemStack shield = player.getHeldItemOffhand();
            if (!shield.isEmpty() && shield.isShield(player)){
                int rebound = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.rebound.get(), shield);
                if (rebound > 0){
                    Rebound.rebound(event, rebound, player, shield);
                }
                int fireShield = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.fireShield.get(), shield);
                if (fireShield > 0){
                    FireShield.fireShield(event.getSource(), fireShield, player);
                }
            }
        }
    }

    //切换装备时
//    @SubscribeEvent
    public static void chestChange(LivingEquipmentChangeEvent event){
        if (event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            ItemStack from = event.getFrom();
            ItemStack to = event.getTo();
            EquipmentSlotType slot = event.getSlot();
            if (slot == EquipmentSlotType.CHEST){
                int healthFrom = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.health.get(), from);
                int healthTo = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.health.get(), to);
                ModifiableAttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
                if (healthFrom > 0 && healthTo > 0 && maxHealth != null){
                    int level = player.getPersistentData().getInt("yuo:health_level");
                    maxHealth.applyPersistentModifier(EventHelper.getModifier(EventHelper.ATTR_TYPE.HEALTH, -level * EventHandler.attrHealth));
                    maxHealth.applyPersistentModifier(EventHelper.getModifier(EventHelper.ATTR_TYPE.HEALTH, healthTo * EventHandler.attrHealth));
                    player.getPersistentData().putInt("yuo:health_level", healthTo);

                }
            }
        }
    }

    //古卷合成
    @SubscribeEvent
    public static void oldBookUp(AnvilUpdateEvent event){
        PlayerEntity player = event.getPlayer();
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

    }
}

