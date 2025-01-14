package com.yuo.Enchants.Blocks;

import net.minecraft.SharedConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class ServerAnvilRenamePacket {
    private final String name;
    public ServerAnvilRenamePacket(FriendlyByteBuf buffer) {
        name = buffer.readUtf();
    }

    public ServerAnvilRenamePacket(String s) {
        this.name = s;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(name);
    }

    public static void handler(ServerAnvilRenamePacket msg, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.containerMenu instanceof DiamondAnvilMenu anvilmenu) {
                String s = SharedConstants.filterText(msg.getName());
                if (s.length() <= 50) {
                    anvilmenu.setItemName(s);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public String getName() {
        return name;
    }
}
