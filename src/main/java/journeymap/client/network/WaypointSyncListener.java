package journeymap.client.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import journeymap.client.log.ChatLog;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import journeymap.common.model.Waypoint;
import journeymap.common.network.waypoints.WaypointSyncPacket;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;

public class WaypointSyncListener implements IMessageHandler<WaypointSyncPacket, IMessage> {

    @Override
    public IMessage onMessage(WaypointSyncPacket message, MessageContext ctx) {
        Journeymap.getLogger().info("Got waypoint sync packet from server: {}", String.valueOf(message));
        Journeymap.getLogger().info("Server Waypoint status: {}", String.valueOf(message.getStatus()));

        ArrayList<Waypoint> waypoints = message.getWaypoints();
        WaypointStore waypointStore = WaypointStore.instance();

        ChatLog.queueAnnouncement(new ChatComponentText("Loaded " + waypoints.size() + " waypoints from server"));

        for (Waypoint waypoint: waypoints) {
            waypointStore.add(waypoint);
        }


        return null;
    }
}