package journeymap.server.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import journeymap.common.Journeymap;
import journeymap.common.network.PacketHandler;
import journeymap.common.network.permissions.PermissionsRequestPacket;
import net.minecraft.entity.player.EntityPlayerMP;

public class PermissionsRequestListener implements IMessageHandler<PermissionsRequestPacket, IMessage> {
    @Override
    public IMessage onMessage(PermissionsRequestPacket message, MessageContext ctx) {

        String worldName = ctx.getServerHandler().playerEntity.getEntityWorld().getWorldInfo().getWorldName();
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        Journeymap.getLogger().info("Got permissions request from {}", String.valueOf(player.getCommandSenderName()));
        PacketHandler.sendPerms(worldName, player);
        return null;
    }
}