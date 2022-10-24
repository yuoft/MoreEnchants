package com.yuo.enchants.Event;

import com.yuo.enchants.Enchants.EnchantRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*;

public class EventHelper {
    private static final Random RANDOM = new Random();

    /**
     * 将玩家主手物品丢出
     * @param player 玩家
     * @param instability 附魔等级
     */
    public static void dropItem(PlayerEntity player, int instability) {
        if (instability > 0 && !player.world.isRemote && !player.isCreative()){ //挖掘时
            EffectInstance instance = player.getActivePotionEffect(Effects.LUCK);
            int luck = -1;
            if (instance != null)
                luck =- instance.getAmplifier();
            if (RANDOM.nextDouble() < 0.15 * instability - (luck + 1) * 0.1){
                player.drop(true);
            }
        }
    }

    /**
     * 获取玩家使用的物品
     * @param player 玩家
     * @return 物品
     */
    public static ItemStack getUseItem(PlayerEntity player){
        ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
        if (!heldItem.isEmpty()) return heldItem;
        ItemStack offHand = player.getHeldItem(Hand.OFF_HAND);
        if (!offHand.isEmpty()) return offHand;
        return ItemStack.EMPTY;
    }

    /**
     * 熔炼附魔的伪实现 通过取消方块破坏事件，同时生成掉落物
     * @param world 世界
     * @param player 玩家
     * @param pos 坐标
     * @param event 事件
     */
    public static void meltingAchieve(World world, PlayerEntity player, BlockPos pos, BlockEvent.BreakEvent event){
        if (!world.isRemote){
            ServerWorld serverWorld = (ServerWorld) world;
            for (int i = 0; i < 10; i++){
                serverWorld.spawnParticle(ParticleTypes.FLAME, pos.getX() + world.rand.nextDouble(), pos.getY() + 1d,
                        pos.getZ() + world.rand.nextDouble(), 1, 0, 0, 0, 0);
            }
        }
        world.playSound(player, pos, SoundEvents.ITEM_FIRECHARGE_USE,  SoundCategory.BLOCKS,1.0f, 1.0f);
        world.setBlockState(event.getPos(), Blocks.AIR.getDefaultState()); //设置此坐标为空气
    }

    public static void breakBlocks(ItemStack tool, World world, BlockPos pos, BlockState state, PlayerEntity player, int lv){
        breakBlock(tool, world, pos, state, player, lv);
    }

    /**
     * 范围挖掘 破坏方块
     * @param tool 工具
     * @param world 世界
     * @param pos 坐标
     * @param state 方块
     * @param player 玩家
     * @param lv 范围
     */
    private static void breakBlock(ItemStack tool, World world, BlockPos pos, BlockState state, PlayerEntity player, int lv){
        if (!canHarvestBlock(tool, state, player)) return; //工具能够收获方块
        if (!player.isCreative()){
            if (player.getFoodStats().getFoodLevel() <= 2) return; //低饱食度时无法使用
        }
        int count = 1;
        ArrayList<BlockPos> points = getPoints(pos, lv); //坐标
        for (BlockPos blockPos : points){
            BlockState blockState = world.getBlockState(blockPos);
            if (player.getFoodStats().getFoodLevel() <= 2){
                player.sendMessage(new TranslationTextComponent("饱食度不足"), UUID.randomUUID());
                break;
            }
            if (!state.getBlock().equals(blockState.getBlock())) continue; //相同方块才继续
            world.destroyBlock(blockPos, false); //破坏方块
            count++;
            player.getFoodStats().addStats(0, -0.05f);
            if (count % 20 == 0) player.getFoodStats().addStats(-1, 0); //消耗饱食度 20方块消耗一格
        }
        //方块挖掘结束 生成掉落物
        List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null, player, tool);
        if (drops.size() <= 0) return;
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
        int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool);
        int melting = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.melting.get(), tool);
        int finalCount = count;
        drops.forEach(e -> {
            ItemEntity itemEntity = new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ());
            if (melting > 0) { //有熔炼附魔 替换产物
                ItemStack dropStack = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(e), world)
                        .map(FurnaceRecipe::getRecipeOutput).filter(f -> !e.isEmpty())
                        .map(f -> ItemHandlerHelper.copyStackWithSize(f, tool.getCount() * f.getCount()))
                        .orElse(e);
                dropStack.setCount(finalCount);
                itemEntity.setItem(dropStack);
            }else itemEntity.setItem(new ItemStack(e.getItem(), finalCount));
            world.addEntity(itemEntity);
        });
        int expValue = state.getExpDrop(world, pos, fortune, silkTouch) * count;
        if (expValue > 0){
            ExperienceOrbEntity exp = new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), expValue);
            world.addEntity(exp);
        }
        tool.damageItem(count, player, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
    }

    /**
     * 返回当前坐标的周围一圈坐标
     * @param origin 中心坐标
     * @param lv 范围挖掘等级
     * @return 周围方块坐标集合
     */
    private static ArrayList<BlockPos> getPoints(BlockPos origin, int lv) {
        ArrayList<BlockPos> points = new ArrayList<>();
        int[][] dimRange = {
                {-1, 0, 1},
                {-2, -1, 0, 1, 2},
                {-3, -2, -1, 0, 1, 2, 3},
                {-4, -3, -2, -1, 0, 1, 2, 3, 4},
                {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5},
        };
        for(int dx : dimRange[lv-1]) {
            for(int dy : dimRange[lv-1]) {
                for(int dz : dimRange[lv-1]) {
                    if(dx == 0 && dy == 0 && dz == 0) {
                        // If 0, 0, 0
                        continue;
                    }
                    points.add(new BlockPos(origin.getX() + dx, origin.getY() + dy, origin.getZ() + dz));
                }
            }
        }
        Collections.shuffle(points); //随机排序
        return points;
    }

    /**
     * 返回工具是否能够挖掘方块
     * @param tool 工具
     * @param state 方块
     * @param player 玩家
     * @return 使用此工具是否能够挖掘此方块
     */
    private static boolean canHarvestBlock(ItemStack tool, BlockState state, PlayerEntity player){
        int pickaxeLv = tool.getHarvestLevel(ToolType.PICKAXE, player, state);
        int axeLv = tool.getHarvestLevel(ToolType.AXE, player, state);
        int hoeLv = tool.getHarvestLevel(ToolType.HOE, player, state);
        int shovelLv = tool.getHarvestLevel(ToolType.SHOVEL, player, state);
        int level = state.getHarvestLevel();
        //工具有一项工具属性可以挖掘就认为可以挖掘
        return pickaxeLv >= level || axeLv >= level || hoeLv >= level || shovelLv >= level;//工具无法挖掘此块
    }

    /**
     * 更改玩家属性
     * @param player 玩家
     */
    public static void changeAttribute(PlayerEntity player){
        int health = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.health.get(), player.getItemStackFromSlot(EquipmentSlotType.CHEST));
        int handRange = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.handRange.get(), player.getHeldItemMainhand());
        int deepFear = EnchantmentHelper.getEnchantmentLevel(EnchantRegistry.deepFear.get(), player.getItemStackFromSlot(EquipmentSlotType.FEET));
        String key = player.getGameProfile().getName()+":"+player.world.isRemote;
        ModifiableAttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        ModifiableAttributeInstance reachDistance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
        ModifiableAttributeInstance swimSpeed = player.getAttribute(ForgeMod.SWIM_SPEED.get());
        if (health > 0){
            int lv = player.getPersistentData().getInt("yuo:health_level");
            if (lv != health && maxHealth != null){ //换了工具
                maxHealth.applyPersistentModifier(EventHelper.getModifier(ATTR_TYPE.HEALTH, -lv * EventHandler.attrHealth));
                player.getPersistentData().putInt("yuo:health_level", health);
                EventHandler.playerHealth.remove(key);
            }
            if (!EventHandler.playerHealth.contains(key) && maxHealth != null){ //未添加属性
                maxHealth.applyPersistentModifier(EventHelper.getModifier(EventHelper.ATTR_TYPE.HEALTH, health * EventHandler.attrHealth));
                player.getPersistentData().putInt("yuo:health_level", health);
                EventHandler.playerHealth.add(key);
            }
        }else {
            if (EventHandler.playerHealth.contains(key) && maxHealth != null){ //已添加属性
                int level = player.getPersistentData().getInt("yuo:health_level");
                maxHealth.applyPersistentModifier(EventHelper.getModifier(EventHelper.ATTR_TYPE.HEALTH, -level * EventHandler.attrHealth));
                EventHandler.playerHealth.remove(key);
            }
        }
        if (handRange > 0){
            int lv = player.getPersistentData().getInt("yuo:handRange_level");
            if (lv != handRange && reachDistance != null){ //换了工具
                reachDistance.applyPersistentModifier(EventHelper.getModifier(EventHelper.ATTR_TYPE.REACH_DISTANCE, -lv * EventHandler.attrHandRange));
                player.getPersistentData().putInt("yuo:handRange_level", handRange);
                EventHandler.playerHandRange.remove(key);
            }
            if (!EventHandler.playerHandRange.contains(key) && reachDistance != null){
                reachDistance.applyPersistentModifier(EventHelper.getModifier(EventHelper.ATTR_TYPE.REACH_DISTANCE, handRange * EventHandler.attrHandRange));
                player.getPersistentData().putInt("yuo:handRange_level", handRange);
                EventHandler.playerHandRange.add(key);
            }
        }else {
            if (EventHandler.playerHandRange.contains(key) && reachDistance != null){
                int level = player.getPersistentData().getInt("yuo:handRange_level");
                reachDistance.applyPersistentModifier(EventHelper.getModifier(EventHelper.ATTR_TYPE.REACH_DISTANCE, -level * EventHandler.attrHandRange));
                EventHandler.playerHandRange.remove(key);
            }
        }
        if (deepFear > 0){
            int lv = player.getPersistentData().getInt("yuo:deepFear_level");
            if (lv != deepFear && swimSpeed != null){
                swimSpeed.applyPersistentModifier(EventHelper.getModifier(ATTR_TYPE.SWIM_SPEED, lv * EventHandler.attrSwimSpeed));
                player.getPersistentData().putInt("yuo:deepFear_level", deepFear);
                EventHandler.playerSwimSpeed.remove(key);
            }
            if (!EventHandler.playerSwimSpeed.contains(key) && swimSpeed != null){
                swimSpeed.applyPersistentModifier(EventHelper.getModifier(EventHelper.ATTR_TYPE.SWIM_SPEED, -deepFear * EventHandler.attrSwimSpeed));
                player.getPersistentData().putInt("yuo:deepFear_level", deepFear);
                EventHandler.playerSwimSpeed.add(key);
            }
        }else {
            if (EventHandler.playerSwimSpeed.contains(key) && swimSpeed != null){
                int level = player.getPersistentData().getInt("yuo:deepFear_level");
                swimSpeed.applyPersistentModifier(EventHelper.getModifier(EventHelper.ATTR_TYPE.SWIM_SPEED, level * EventHandler.attrSwimSpeed));
                EventHandler.playerSwimSpeed.remove(key);
            }
        }
    }

    public static final UUID HEALTH_UUID = UUID.fromString("91d60b13-c2a9-4883-9b3f-5b0c0bf09c0e");
    public static final UUID RANGE_UUID = UUID.fromString("aa8d5d13-f55e-4216-b1c9-724156377743");
    public static final UUID SWIM_UUID = UUID.fromString("39535dd0-fd27-430b-86ad-c12dd985529f");
    //获取属性修饰器
    public static AttributeModifier getModifier(ATTR_TYPE type, float value){
        return new AttributeModifier(type.getName(), value, type.getType());
    }

    enum ATTR_TYPE{
        HEALTH("generic.maxHealth", HEALTH_UUID, AttributeModifier.Operation.ADDITION),
//        KNOCK("generic.knockbackResistance",AttributeModifier.Operation.ADDITION),
//        MOVE_SPEED("generic.movementSpeed",AttributeModifier.Operation.ADDITION),
//        DAMAGE("generic.attackDamage",AttributeModifier.Operation.ADDITION),
//        ATTACK_SPEED("generic.attackSpeed",AttributeModifier.Operation.ADDITION),
        REACH_DISTANCE("forge.reach_distance", RANGE_UUID, AttributeModifier.Operation.ADDITION),
        SWIM_SPEED("forge.swim_speed", SWIM_UUID, AttributeModifier.Operation.MULTIPLY_BASE);
//        ARMOR("generic.armor",AttributeModifier.Operation.ADDITION),

        private final String name;
        private final UUID uuid;
        private final AttributeModifier.Operation type;
        ATTR_TYPE(String attrName, UUID uuidIn, AttributeModifier.Operation operation){
            this.name = attrName;
            this.type = operation;
            this.uuid = uuidIn;
        }

        public String getName() {
            return name;
        }

        public AttributeModifier.Operation getType() {
            return type;
        }

        public UUID getUuid() {
            return uuid;
        }
    }

}
