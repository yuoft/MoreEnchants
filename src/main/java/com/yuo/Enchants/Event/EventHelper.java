package com.yuo.Enchants.Event;

import com.yuo.Enchants.Config;
import com.yuo.Enchants.Enchants.EnchantRegistry;
import com.yuo.Enchants.Enchants.Melting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.world.BlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EventHelper {
    private static final Random RANDOM = new Random();

    public static final float attrHealth = 2.0f; //属性变更基础系数
    public static final float attrHandRange = 0.5f;
    public static final float attrSwimSpeed = -0.25f;
    /**
     * 将玩家主手物品丢出
     * @param player 玩家
     * @param instability 附魔等级
     */
    public static void dropItem(Player player, int instability) {
        if (instability > 0 && !player.level.isClientSide && !player.isCreative() && Config.SERVER.isInstability.get()){ //挖掘时
            MobEffectInstance instance = player.getEffect(MobEffects.LUCK);
            int luck = -1;
            if (instance != null)
                luck =- instance.getAmplifier();
            if (RANDOM.nextDouble() < 0.15 * instability - (luck + 1) * 0.1){
                player.drop(player.getMainHandItem(),true);
            }
        }
    }

    /**
     * 熔炼附魔的伪实现 通过取消方块破坏事件，同时生成掉落物
     * @param world 世界
     * @param player 玩家
     * @param pos 坐标
     * @param event 事件
     */
    public static void meltingAchieve(Level world, Player player, BlockPos pos, BlockEvent.BreakEvent event){
        if (!world.isClientSide){
            ServerLevel serverWorld = (ServerLevel) world;
            for (int i = 0; i < 10; i++){
                serverWorld.addParticle(ParticleTypes.FLAME, pos.getX() + world.random.nextDouble(), pos.getY() + 1d,
                        pos.getZ() + world.random.nextDouble(), 1, 0, 0);
            }
        }
        world.playSound(player, pos, SoundEvents.FIRECHARGE_USE,  SoundSource.BLOCKS,1.0f, 1.0f);
        world.setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState()); //设置此坐标为空气
    }

    public static void breakBlocks(ItemStack tool, Level world, BlockPos pos, BlockState state, Player player, int lv){
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
    private static void breakBlock(ItemStack tool, Level world, BlockPos pos, BlockState state, Player player, int lv){
        if (!tool.isCorrectToolForDrops(state)) return; //工具能够收获方块
        if (!player.isCreative()){
            if (player.getFoodData().getFoodLevel() <= 2) return; //低饱食度时无法使用
        }
        int count = 1;
        ArrayList<BlockPos> points = getPoints(player, pos, lv); //坐标
        //方块挖掘前统计掉落物
        List<ItemStack> drops = new ArrayList<>();
        points.forEach(e ->{
            BlockState blockState = world.getBlockState(e);
            if (!world.isEmptyBlock(e) && state.getBlock().equals(blockState.getBlock())){
                List<ItemStack> drop = Block.getDrops(blockState, (ServerLevel) world, e, null, player, tool);
                drops.addAll(drop);
            }
        });
        //清除方块
        for (BlockPos blockPos : points){
            BlockState blockState = world.getBlockState(blockPos);
            if (player.getFoodData().getFoodLevel() <= 2){
                player.sendMessage(new TranslatableComponent("yuoenchants.message.noFood"), UUID.randomUUID());
                break;
            }
            if (!state.getBlock().equals(blockState.getBlock())) continue; //相同方块才继续
            world.destroyBlock(blockPos, false); //破坏方块
            count++;
            player.getFoodData().setExhaustion(-0.05f);
            if (count % 20 == 0) player.getFoodData().setSaturation(-1); //消耗饱食度 20方块消耗一格
        }
        //生成掉落物和经验
        if (drops.size() <= 0) return;
        int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
        int silkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool);
        int melting = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.melting.get(), tool);
        int finalCount = count;
        drops.forEach(e -> {
            ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), ItemStack.EMPTY);
            if (melting > 0  && Config.SERVER.isMelting.get()) { //有熔炼附魔 替换产物
                ItemStack dropStack = Melting.getMeltingItem(world, e, tool, player);
                itemEntity.setItem(dropStack);
            }else itemEntity.setItem(new ItemStack(e.getItem(), finalCount));
            world.addFreshEntity(itemEntity);
        });
        int expValue = state.getExpDrop(world, pos, fortune, silkTouch) * count;
        if (expValue > 0){
            ExperienceOrb exp = new ExperienceOrb(world, pos.getX(), pos.getY(), pos.getZ(), expValue);
            world.addFreshEntity(exp);
        }
        tool.hurtAndBreak(count, player, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND));
    }

    /**
     * 返回当前坐标的周围一圈坐标
     * @param player 玩家
     * @param pos 挖掘坐标
     * @param lv 范围挖掘等级
     * @return 周围方块坐标集合
     */
    private static ArrayList<BlockPos> getPoints(Player player, BlockPos pos, int lv) {
//        Vec3 vec = player.getLookAngle();
        Direction facing = player.getDirection();
        ArrayList<BlockPos> points = new ArrayList<>();
//        Direction facing = Direction.getNearest(vec.x, vec.y, vec.z);
        switch (facing){
            case UP:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY(); y <= pos.getY() + (2 * lv); y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            points.add(new BlockPos(x,  y, z));
                        }
                    }
                }
                break;
            case DOWN:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY(); y >= pos.getY() - (2 * lv); y --){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            points.add(new BlockPos(x,  y, z));
                        }
                    }
                }
                break;
            case EAST:
                for (int x = pos.getX(); x <= pos.getX() + (2 * lv); x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            points.add(new BlockPos(x,  y, z));
                        }
                    }
                }
                break;
            case WEST:
                for (int x = pos.getX(); x >= pos.getX() - (2 * lv); x --){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            points.add(new BlockPos(x,  y, z));
                        }
                    }
                }
                break;
            case NORTH:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ(); z >= pos.getZ() - (2 * lv); z --){
                            points.add(new BlockPos(x,  y, z));
                        }
                    }
                }
                break;
            case SOUTH:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ(); z <= pos.getZ() + (2 * lv); z ++){
                            points.add(new BlockPos(x,  y, z));
                        }
                    }
                }
                break;
        }
        return points;
    }


    /**
     * 更改玩家属性
     * @param player 玩家
     */
    public static void changeMaxHealth(Player player){
        int health = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.health.get(), player.getItemBySlot(EquipmentSlot.CHEST));
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null  && Config.SERVER.isHealth.get()){
            if (health <= 0){
                RemoveModifier(maxHealth, ATTR_TYPE.HEALTH, health, attrHealth);
                return;
            }
            AddModifier(maxHealth, ATTR_TYPE.HEALTH, health, attrHealth);
        }
    }

    public static void changeHandRange(Player player){
        int handRange = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.handRange.get(), player.getMainHandItem());
        AttributeInstance reachDistance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
        if (reachDistance != null  && Config.SERVER.isHandRange.get()){
            if (handRange <= 0){
                RemoveModifier(reachDistance, ATTR_TYPE.REACH_DISTANCE, handRange, attrHandRange);
                return;
            }
            AddModifier(reachDistance, ATTR_TYPE.REACH_DISTANCE, handRange, attrHandRange);
        }
    }

    public static void changeSwimSpeed(Player player){
        int deepFear = EnchantmentHelper.getItemEnchantmentLevel(EnchantRegistry.deepFear.get(), player.getItemBySlot(EquipmentSlot.FEET));
        AttributeInstance swimSpeed = player.getAttribute(ForgeMod.SWIM_SPEED.get());
        if (swimSpeed != null && Config.SERVER.isDeepFear.get()){
            if (deepFear <= 0){
                RemoveModifier(swimSpeed, ATTR_TYPE.SWIM_SPEED, deepFear, attrSwimSpeed);
                return;
            }
            AddModifier(swimSpeed, ATTR_TYPE.SWIM_SPEED, deepFear, attrSwimSpeed);
        }
    }
    /**
     * 添加属性修饰器
     * @param attr 属性实例
     * @param type  属性类型
     * @param value 附魔等级
     * @param attrValue 修改基础数值
     */
    private static void AddModifier(AttributeInstance attr, ATTR_TYPE type, int value, float attrValue){
        AttributeModifier modifier = EventHelper.getModifier(type, value * attrValue);
        attr.removeModifier(modifier.getId());
        attr.addPermanentModifier(modifier);
    }

    /**
     * 清除属性修饰器
     * @param attr 属性实例
     * @param type  属性类型
     * @param value 附魔等级
     * @param attrValue 修改基础数值
     */
    private static void RemoveModifier(AttributeInstance attr, ATTR_TYPE type, int value, float attrValue){
        AttributeModifier modifier = EventHelper.getModifier(type, -value * attrValue);
        if (attr.getModifier(modifier.getId()) == null) return;
        attr.removeModifier(modifier.getId());
    }

    public static final UUID HEALTH_UUID = UUID.fromString("91d60b13-c2a9-4883-9b3f-5b0c0bf09c0e");
    public static final UUID RANGE_UUID = UUID.fromString("aa8d5d13-f55e-4216-b1c9-724156377743");
    public static final UUID SWIM_UUID = UUID.fromString("39535dd0-fd27-430b-86ad-c12dd985529f");

    /**
     * 获取属性修饰器
     * @param type 属性
     * @param value 修改值
     * @return 修饰器
     */
    public static AttributeModifier getModifier(ATTR_TYPE type, float value){
        return new AttributeModifier(type.getUuid(), type.getName(), value, type.getType());
    }

    /**
     * 属性修饰器基础数据枚举
     */
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
        private final AttributeModifier.Operation type; //修改类型
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
