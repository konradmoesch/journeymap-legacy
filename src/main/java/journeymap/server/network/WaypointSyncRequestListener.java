package journeymap.server.network;

import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import journeymap.common.Journeymap;
import journeymap.common.model.Waypoint;
import journeymap.common.network.waypoints.WaypointSyncPacket;
import journeymap.common.network.waypoints.WaypointSyncRequestPacket;
import journeymap.server.waypoint.ServerWaypointStore;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.Collection;

public class WaypointSyncRequestListener implements IMessageHandler<WaypointSyncRequestPacket, WaypointSyncPacket> {

    @Override
    public WaypointSyncPacket onMessage(WaypointSyncRequestPacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        String playerName = player.getCommandSenderName();

        Journeymap.getLogger().info("Got waypoint sync request packet from user: {}", playerName);
        Collection<Waypoint> playerWaypoints = ServerWaypointStore.instance().getAllUserWaypoints(playerName);
        ArrayList<Waypoint> playerWaypointsArrayList = new ArrayList<>(playerWaypoints);

        //WaypointSyncPacket packet = new WaypointSyncPacket(ServerWaypointStore.instance().hasLoaded()?1:0, playerWaypointsArrayList);
        WaypointSyncPacket packet = new WaypointSyncPacket(1, playerWaypointsArrayList);

        return packet;
    }
}