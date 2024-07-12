package com.yuo.Enchants.NetWork;

import com.yuo.Enchants.Proxy.CommonProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KeyPacket {
    private static int key;
    private static boolean flag;
    public KeyPacket(FriendlyByteBuf buffer) {
        key = buffer.readInt();
        flag = buffer.readBoolean();
    }

    public KeyPacket(int code, boolean flag) {
        KeyPacket.key = code;
        KeyPacket.flag = flag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(key);
        buf.writeBoolean(flag);
    }

    public static void handler(KeyPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
//            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> NmCClientPacket.handlePacket(msg, ctx)); //处理服务端发送给客户端的消息
            CommonProxy.setKeyC(flag);
        });
        ctx.get().setPacketHandled(true);
    }

}
