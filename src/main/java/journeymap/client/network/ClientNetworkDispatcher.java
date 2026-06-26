package journeymap.client.network;

import journeymap.common.Journeymap;
import journeymap.common.model.Waypoint;
import journeymap.common.network.PacketHandler;
import journeymap.common.network.waypoints.WaypointAddPacket;
import journeymap.common.network.waypoints.WaypointSyncRequestPacket;
import journeymap.common.network.worldid.WorldIDRequestPacket;

public class ClientNetworkDispatcher {
    // Minimum time in millis that must pass before subsequent requests can be made
    public static final int MIN_DELAY_MS = 1000;

    // Timestamp in millis of the last request by client
    private static long lastWorldIdRequest;

    // Timestamp in millis of the last response from server
    private static long lastWorldIdResponse;

    /**
     * Request a World ID from the server by sending a blank WorldIdMessage.
     */
    public static void requestWorldID()
    {
        long now = System.currentTimeMillis();
        if (lastWorldIdRequest + MIN_DELAY_MS < now && lastWorldIdResponse + MIN_DELAY_MS < now)
        {
            Journeymap.getLogger().info("Requesting World ID");
            PacketHandler.WORLD_INFO_CHANNEL.sendToServer(new WorldIDRequestPacket());
            lastWorldIdRequest = System.currentTimeMillis();
        }
    }

    public static void setLastWorldIdResponse(long millis) {
        lastWorldIdResponse = millis;
    }

    public static void requestWaypoints()
    {
        Journeymap.getLogger().info("Requesting Waypoints");
        PacketHandler.JM_WAYPOINTS.sendToServer(new WaypointSyncRequestPacket());
    }

    public static void sendAddWaypoint(Waypoint waypoint) {
        Journeymap.getLogger().info("Sending new waypoint to server");
        PacketHandler.JM_WAYPOINTS.sendToServer(new WaypointAddPacket(waypoint));
    }
}
