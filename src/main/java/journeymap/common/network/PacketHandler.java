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
import journeymap.client.network.WorldInfoHandler;
import journeymap.common.network.permissions.PermissionsPacket;
import journeymap.common.network.permissions.PermissionsRequestPacket;
import journeymap.common.network.worldid.WorldIDPacket;
import journeymap.common.network.worldid.WorldIDRequestPacket;
import journeymap.server.network.PermissionsRequestListener;
import journeymap.server.network.WorldIdRequestListener;

public class PacketHandler {

    public static final SimpleNetworkWrapper WORLD_INFO_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(WorldIDPacket.CHANNEL_NAME);
    public static final SimpleNetworkWrapper JM_PERMS = NetworkRegistry.INSTANCE.newSimpleChannel("jm_perms");

    public void registerServerHandlers() {
        WORLD_INFO_CHANNEL.registerMessage(WorldIdRequestListener.class, WorldIDRequestPacket.class, 0, Side.SERVER);
        JM_PERMS.registerMessage(PermissionsRequestListener.class, PermissionsRequestPacket.class, 0, Side.SERVER);
    }

    public void registerClientHandlers() {
        WORLD_INFO_CHANNEL.registerMessage(WorldInfoHandler.WorldIdListener.class, WorldIDPacket.class, 0, Side.CLIENT);
        JM_PERMS.registerMessage(PermissionsListener.class, PermissionsPacket.class, 0, Side.CLIENT);
    }

    public void init(Side side) {
        switch (side) {
            case CLIENT:
                registerClientHandlers();
                break;
            case SERVER:
                registerServerHandlers();
                break;
        }
    }
}
