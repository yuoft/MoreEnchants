package com.yuo.Enchants;

import com.yuo.Enchants.Blocks.ServerAnvilRenamePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetWorkHandler {
    public static SimpleChannel INSTANCE;
    private static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }


    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(YuoEnchants.MOD_ID, "network"), //标识符
                () -> VERSION, //数据包版本
                (version) -> version.equals(VERSION), //客户端和服务端可以接收的版本号
                (version) -> version.equals(VERSION)
        );
        INSTANCE.registerMessage(nextID(), //数据包序号
                ServerAnvilRenamePacket.class,  //自定义数据包类
                ServerAnvilRenamePacket::toBytes, //序列化数据包
                ServerAnvilRenamePacket::new, //反序列化
                ServerAnvilRenamePacket::handler); //接收数据后进行操作
    }
}
