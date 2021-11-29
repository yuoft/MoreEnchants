package com.yuo.enchants.Event;

import com.yuo.enchants.Enchants.EnchantRegistry;
import com.yuo.enchants.Enchants.LavaWalker;
import com.yuo.enchants.Items.ItemRegistry;
import com.yuo.enchants.MoreEnchants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * 事件处理类 附魔实现
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MoreEnchants.MODID)
public class EventHandler {
    private static final Random RANDOM = new Random(); //随机数
    private static int LIGHTNING_DAMAGE_TICK = 0; //雷击计时器
    private static int THORNS_TICK = 0; //真荆棘计时器
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
                //伤害来源：火焰，岩浆，熔岩石，燃烧
                if((event.getSource() == DamageSource.IN_FIRE) || (event.getSource() == DamageSource.ON_FIRE) ||
                        (event.getSource() == DamageSource.LAVA) || (event.getSource() == DamageSource.HOT_FLOOR)) {
                    event.setAmount(0);
                    stackLegs.damageItem(1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
                }
            }
            int lastStand = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.lastStand.get(), stackFeet);
            if (lastStand > 0){
                float amount = event.getAmount(); //伤害值
                float health = player.getHealth();
                if ((health - amount) < 1){ //受到致命伤害
                    int exp = player.experienceTotal; //玩家经验值
                    int ceil = MathHelper.ceil((amount - (health - 1)) * 20); //将玩家血量扣到半颗心时 剩余的伤害值 * 抵消倍率
                    if (exp >= ceil){ //玩家经验值能够抵消伤害
                        player.giveExperiencePoints(-ceil); //扣除经验值
                        player.setHealth(1);
                        event.setAmount(0);
                        stackFeet.damageItem(1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
                    }

                }
            }
        }
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) trueSource;
            ItemStack mainhand = player.getHeldItemMainhand();
            int vorapl = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.vorpal.get(), mainhand);
            if (vorapl > 0){
                int i = RANDOM.nextInt(100);
                if (i < 5 * vorapl){ // 暴击概率 5% * 等级
                    event.setAmount(event.getAmount() * 5); //暴击伤害*5
                    player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    for (int j = 0; j < vorapl * 2; j++) {
                        ((ServerWorld) entityLiving.world).spawnParticle(ParticleTypes.CRIT,
                                entityLiving.getPosX() + entityLiving.world.rand.nextDouble(), entityLiving.getPosY() + 1.5D,
                                entityLiving.getPosZ() + entityLiving.world.rand.nextDouble(), 1, 0, 0, 0, 0);
                    }
                }
            }
        }
    }
    //附魔，以战养战 脆弱 攻击生物
    @SubscribeEvent
    public static void warToWar(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if(player == null) return;
        ItemStack stack = player.getHeldItemMainhand();
        int warToWar = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.warToWar.get(), stack);
        if( warToWar > 0) { //有附魔
            //回血效果和概率与等级相关
            int i = RANDOM.nextInt(100);
            if (i < (20 + warToWar * 15)){
                player.heal(warToWar / 2.0f);
            }
        }
        int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
        if (unDurable > 0){
            Item item = stack.getItem();
            if (item instanceof SwordItem || item instanceof AxeItem || item instanceof ToolItem || item instanceof HoeItem
                    || item instanceof TridentItem){
                stack.damageItem(RANDOM.nextInt(unDurable) + 1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
            }
        }
    }
    //爆炸箭 箭碰撞方块或实体
    @SubscribeEvent
    public static void blastArrow(ProjectileImpactEvent.Arrow event) {
        AbstractArrowEntity arrow = event.getArrow();
        if(arrow.func_234616_v_() instanceof PlayerEntity)
        {
            PlayerEntity player=(PlayerEntity) arrow.func_234616_v_();
            ItemStack itemStack=player.getHeldItemMainhand();
            int blastArrow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.blastArrow.get(), itemStack);
            if (blastArrow > 0){ //产生爆炸
                arrow.world.createExplosion(arrow, arrow.getPosX(), arrow.getPosY(), arrow.getPosZ(), blastArrow * 4.0f, true, Explosion.Mode.NONE);
                arrow.remove(); //删除实体
            }
        }
    }
    //脆弱 洞察 熔炼 粉碎 范围挖掘 破坏方块
    @SubscribeEvent
    public static void unDurableTool(BlockEvent.BreakEvent event){
        PlayerEntity player = event.getPlayer();
        if (player == null) return;
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty()) return;
        Item item = stack.getItem();
        if (item instanceof ToolItem || item instanceof ShearsItem || item instanceof HoeItem){
            int level = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
            if (level > 0){
                stack.damageItem(RANDOM.nextInt(level) + 1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND)); //破坏方块时消耗更多耐久
            }
        }
        if (event.getExpToDrop() > 0){
            int level = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.insight.get(), stack);
            if (level > 0){
                //额外获取 lv + ranmod（lv*3 + 1）经验值
                event.setExpToDrop(event.getExpToDrop() + level + RANDOM.nextInt(3 * level + 1));
            }
        }
        World world = (World) event.getWorld();
        BlockPos pos = event.getPos();
        Block block = event.getState().getBlock();
        BlockState state = event.getState();
        int rangBreak = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.rangBreak.get(), stack);
        if (KeyBindingEvent.isIsKeyC() && rangBreak > 0){
            EventHelper.breakBlocks(stack, world, pos, state, player, rangBreak > 5 ? 5 : rangBreak); //最大等级5
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
//            event.setCanceled(true);
            return;
        }
        int melting = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.melting.get(), stack); //熔炼
        if (melting > 0 && block.canHarvestBlock(event.getState(), world, pos, player)){
            List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);
            if (drops.size() <= 0) return;
            drops.forEach(itemStack -> {
                //获取物品烧炼后产物
                ItemStack dropStack = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(itemStack), world)
                        .map(FurnaceRecipe::getRecipeOutput).filter(e -> !e.isEmpty())
                        .map(e -> ItemHandlerHelper.copyStackWithSize(e, stack.getCount() * e.getCount()))
                        .orElse(itemStack);
                if (!dropStack.equals(itemStack)){
                    EventHelper.meltingAchieve(world, player, pos, event);
                    world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dropStack));
//                    event.setCanceled(true);
                }
            });
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
            if (item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem)
            {
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
    //脆弱 使用锄头
    @SubscribeEvent
    public static void unDurableHoe(UseHoeEvent event){
        PlayerEntity player = event.getPlayer();
        ItemStack item = event.getContext().getItem();
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
            ItemStack stack = player.getHeldItemMainhand();
            if (stack.getItem() instanceof FishingRodItem){
                World world = player.world;
                int unDurable = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.unDurable.get(), stack);
                int insight = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.insight.get(), stack);
                int badLuckOfTheSea = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.badLuckOfTheSea.get(), stack);
                if (unDurable > 0){
                    event.damageRodBy(event.getRodDamage() + RANDOM.nextInt(unDurable) + 1);
                }
                if (insight > 0){
                    ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, player.getPosX(), player.getPosY(),
                            player.getPosZ(), RANDOM.nextInt(insight * 3) + 1);
                    world.addEntity(experienceOrbEntity);
                }
                if (badLuckOfTheSea > 0){
                    BlockPos blockPos = event.getHookEntity().getPosition();
                    int i = RANDOM.nextInt(100);
                    if (i < badLuckOfTheSea * 10 + 30){ //引燃的TNT飞向玩家
                        event.getDrops().clear();
                        TNTEntity tntEntity = new TNTEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), player);
                        tntEntity.setFuse(40); //引燃时间40刻度（2S）
                        BlockPos pos = event.getHookEntity().getPosition(); //获取鱼鳔实体坐标
                        double d0 = player.getPosX() - pos.getX();
                        double d1 = player.getPosY() - pos.getY();
                        double d2 = player.getPosZ() - pos.getZ();
                        //设置tnt运动方向
                        tntEntity.setMotion(d0 * 0.1D, d1 * 0.1D + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08D, d2 * 0.1D);
                        world.addEntity(tntEntity);
                    }
                }
            }
        }
    }
    //拖拉 挖掘速度
    @SubscribeEvent
    public static void onPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
        PlayerEntity player = event.getPlayer();
        if (player == null) return;
        int slow = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.slow.get(), player.getHeldItemMainhand());
        if (slow > 0){
            event.setNewSpeed(event.getOriginalSpeed() / (slow * 1.5f)); //挖掘速度变慢
        }
    }
    //万箭 拉弓
    @SubscribeEvent
    public static void manyArrow(ArrowLooseEvent event){
        World world = event.getWorld();
        ItemStack bow = event.getBow();
        int level = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.manyArrow.get(), bow);
        if (level > 0){
            PlayerEntity player = event.getPlayer();
            float charge = BowItem.getArrowVelocity(event.getCharge()); //弓的状态
            ItemStack itemStack = player.findAmmo(bow);
            int fireArrow = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow);
            float rotationPitch = player.rotationPitch;
            float rotationYaw = player.rotationYaw;
            for (int i = 0; i < level; i++){
                ArrowItem arrowitem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(world, itemStack, player);
                abstractarrowentity.setDamage(bow.getDamage());
                if (fireArrow > 0) abstractarrowentity.setFire(100);
                if (charge == 1.0F) abstractarrowentity.setIsCritical(true);
                abstractarrowentity.func_234612_a_(player, rotationPitch, rotationYaw, 0.0F, charge * 3.0F, 1.0F);
                abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY; //万箭附魔的额外箭不可回收
                world.addEntity(abstractarrowentity);
            }
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
            if (!itemstack.isEmpty() && itemstack.getDamage() > 0) { //物品部位空，且有耐久
                int i = (int)(xpValue * 1.0f);  //获取经验值 * 物品经验修复率
                xpValue -= i / 2; //玩家最终获取的经验
                itemstack.damageItem(i, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND)); //腐蚀物品
            }
        }
        if (xpValue > 0){ //如果经验有剩余
            player.giveExperiencePoints(xpValue); //玩家获取经验
        }
    }
    //岩浆行者 实体更新
    @SubscribeEvent
    public static void lavaWalker(LivingEvent.LivingUpdateEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (!player.world.isRemote){
                int lavaWalker = EnchantmentHelper.getMaxEnchantmentLevel(EnchantRegistry.lavaWalker.get(), player);
                if (lavaWalker > 0){
                    LavaWalker.freezingNearby(player, player.world, player.getPosition(), lavaWalker);
                }

            }
        }
    }
    //雷击
    @SubscribeEvent
    public static void tickEvent(TickEvent.PlayerTickEvent event){
        LIGHTNING_DAMAGE_TICK++;
        THORNS_TICK++;
        PlayerEntity player = event.player;
        if (player == null) return;
        if (player.world.isRemote) return;
        ItemStack stackLegs = player.getItemStackFromSlot(EquipmentSlotType.LEGS);
        ItemStack stackChest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
        int lightningDamage = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.lightningDamage.get(), stackLegs);
        if (lightningDamage > 0 && (player.world.isThundering() || player.world.isRaining())){ //在雨天生效
            AxisAlignedBB axisAlignedBB = player.getBoundingBox().grow(16); //范围
            List<Entity> toAttack = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, axisAlignedBB);//生物列表
            if (toAttack.size() < 1) return;
            if (LIGHTNING_DAMAGE_TICK >= 60 * 3){ //每3秒触发一次
                LIGHTNING_DAMAGE_TICK = 0; //计时器清零
                //随机给予一个生物实体雷击
                Entity entity = toAttack.get(RANDOM.nextInt(toAttack.size()));
                if (entity instanceof LivingEntity){
                    LightningBoltEntity lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(player.world);
                    lightningBoltEntity.moveForced(Vector3d.copyCenteredHorizontally(entity.getPosition())); //设置闪电运动路径 才能生成闪电
                    lightningBoltEntity.setCaster(entity instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity : null);
                    player.world.addEntity(lightningBoltEntity);
                    stackLegs.damageItem(1, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
                }
            }
        }
        int thorns = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.thorns.get(), stackChest);
        if (thorns > 0 && THORNS_TICK >= 60 * 2){
            THORNS_TICK = 0;
            player.attackEntityFrom(DamageSource.GENERIC, thorns / 2.0f);
            stackChest.damageItem(2, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
        }
    }
    //洞察 生命汲取 生物死亡
    @SubscribeEvent
    public static void insightLiving(LivingDeathEvent event){
        Entity trueSource = event.getSource().getTrueSource(); //伤害来源
        if (trueSource instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) trueSource;
            int insight = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.insight.get(), player.getHeldItemMainhand());
            int leech = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.leech.get(), player.getHeldItemMainhand());
            if (insight > 0){
                ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(player.world, player.getPosX(), player.getPosY() + 0.5f,
                        player.getPosZ(), insight + RANDOM.nextInt(insight * 3 + 1));
                player.world.addEntity(experienceOrbEntity);
            }
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
            LivingEntity living = event.getEntityLiving();
            int vorapl = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.vorpal.get(), player.getHeldItemMainhand());
            if (vorapl > 0){
                int i = RANDOM.nextInt(100);
                if (i > 20 * vorapl) return;
                LivingEntity entityLiving = event.getEntityLiving();
                ItemStack skull = ItemStack.EMPTY;
                if (entityLiving instanceof PlayerEntity){
                    skull = new ItemStack(Items.PLAYER_HEAD, 1);
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putString("playerName", entityLiving.getName().getString());
                    skull.setTag(nbt);
                }else if (entityLiving instanceof SkeletonEntity){
                    skull = new ItemStack(Items.SKELETON_SKULL, 1);
                }else if (entityLiving instanceof WitherSkeletonEntity){
                    skull = new ItemStack(Items.WITHER_SKELETON_SKULL, 1);
                }else if (entityLiving instanceof ZombieEntity){
                    skull = new ItemStack(Items.ZOMBIE_HEAD, 1);
                }else if (entityLiving instanceof CreeperEntity){
                    skull = new ItemStack(Items.CREEPER_HEAD, 1);
                }
                if (skull.isEmpty()) return;
                ItemEntity itemEntity = new ItemEntity(living.world, living.getPosX(), living.getPosY(), living.getPosZ(), skull);
                event.getDrops().add(itemEntity);
            }
        }
    }
}

