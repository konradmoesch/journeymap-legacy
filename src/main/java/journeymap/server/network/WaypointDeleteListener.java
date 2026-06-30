package journeymap.server.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import journeymap.common.Journeymap;
import journeymap.common.network.waypoints.WaypointDeletePacket;
import journeymap.server.waypoint.ServerWaypointStore;
import net.minecraft.entity.player.EntityPlayerMP;

public class WaypointDeleteListener implements IMessageHandler<WaypointDeletePacket, IMessage> {

    @Override
    public IMessage onMessage(WaypointDeletePacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        String playerName = player.getCommandSenderName();

        Journeymap.getLogger().info("Got waypoint delete packet from user: {}, deleting waypoint {} on server", playerName, message.getWaypointId());
        ServerWaypointStore.instance().delete(message.getWaypointId());

        return null;
    }
}