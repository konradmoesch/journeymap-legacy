/*
 * JourneyMap Mod <journeymap.info> for Minecraft
 * Copyright (c) 2011-2017  Techbrew Interactive, LLC <techbrew.net>.  All Rights Reserved.
 */

package journeymap.common.network;

/**
 * Created by Mysticdrew on 10/8/2014.
 */

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import journeymap.client.network.PermissionsListener;
import journeymap.client.network.WaypointSyncListener;
import journeymap.client.network.WorldInfoHandler;
import journeymap.common.network.permissions.PermissionsPacket;
import journeymap.common.network.permissions.PermissionsRequestPacket;
import journeymap.common.network.waypoints.WaypointAddPacket;
import journeymap.common.network.waypoints.WaypointDeletePacket;
import journeymap.common.network.waypoints.WaypointSyncPacket;
import journeymap.common.network.waypoints.WaypointSyncRequestPacket;
import journeymap.common.network.worldid.WorldIDPacket;
import journeymap.common.network.worldid.WorldIDRequestPacket;
import journeymap.server.network.*;

public class PacketHandler {

    public static final SimpleNetworkWrapper WORLD_INFO_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(WorldIDPacket.CHANNEL_NAME);
    public static final SimpleNetworkWrapper JM_PERMS = NetworkRegistry.INSTANCE.newSimpleChannel("jm_perms");
    public static final SimpleNetworkWrapper JM_WAYPOINTS = NetworkRegistry.INSTANCE.newSimpleChannel("jm_waypoints");

    public void registerServerHandlers() {
        WORLD_INFO_CHANNEL.registerMessage(WorldIdRequestListener.class, WorldIDRequestPacket.class, 0, Side.SERVER);
        JM_PERMS.registerMessage(PermissionsRequestListener.class, PermissionsRequestPacket.class, 2, Side.SERVER);
        JM_WAYPOINTS.registerMessage(WaypointSyncRequestListener.class, WaypointSyncRequestPacket.class, 4, Side.SERVER);
        JM_WAYPOINTS.registerMessage(WaypointAddListener.class, WaypointAddPacket.class, 6, Side.SERVER);
        JM_WAYPOINTS.registerMessage(WaypointDeleteListener.class, WaypointDeletePacket.class, 7, Side.SERVER);
    }

    public void registerClientHandlers() {
        WORLD_INFO_CHANNEL.registerMessage(WorldInfoHandler.WorldIdListener.class, WorldIDPacket.class, 1, Side.CLIENT);
        JM_PERMS.registerMessage(PermissionsListener.class, PermissionsPacket.class, 3, Side.CLIENT);
        JM_WAYPOINTS.registerMessage(WaypointSyncListener.class, WaypointSyncPacket.class, 5, Side.CLIENT);
    }

    public void init(Side side) {
        registerClientHandlers();
        registerServerHandlers();
    }
}
