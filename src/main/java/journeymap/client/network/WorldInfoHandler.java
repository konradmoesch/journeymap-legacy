/*
 * JourneyMap Mod <journeymap.info> for Minecraft
 * Copyright (c) 2011-2017  Techbrew Interactive, LLC <techbrew.net>.  All Rights Reserved.
 */

package journeymap.client.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import journeymap.client.forge.helper.ForgeHelper;
import journeymap.common.Journeymap;
import journeymap.common.network.worldid.WorldIDPacket;
import journeymap.common.network.worldid.WorldIDRequestPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

/**
 * Sample Forge Client class for handling World Info custom packets.
 *
 * @author techbrew
 */
@Deprecated
public class WorldInfoHandler
{
    // Minimum time in millis that must pass before subsequent requests can be made
    public static final int MIN_DELAY_MS = 1000;

    // Timestamp in millis of the last request by client
    private static long lastRequest;

    // Timestamp in millis of the last response from server
    private static long lastResponse;
    // Network wrapper of the channel for requests/response
    private static SimpleNetworkWrapper channel;
    // Handle to Minecraft client
    Minecraft mc = ForgeHelper.INSTANCE.getClient();

    /**
     * Request a World ID from the server by sending a blank WorldIdMessage.
     */
    public static void requestWorldID()
    {
        if (channel != null)
        {
            long now = System.currentTimeMillis();
            if (lastRequest + MIN_DELAY_MS < now && lastResponse + MIN_DELAY_MS < now)
            {
                Journeymap.getLogger().info("Requesting World ID");
                channel.sendToServer(new WorldIDRequestPacket());
                lastRequest = System.currentTimeMillis();
            }
        }
    }

    /**
     * Use the EntityJoinWorldEvent of the player as a trigger to request the World ID.
     *
     * @param event
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void on(EntityJoinWorldEvent event)
    {
        if (!mc.isSingleplayer() && mc.thePlayer != null && !mc.thePlayer.isDead)
        {
            if (ForgeHelper.INSTANCE.getEntityName(event.entity).equals(ForgeHelper.INSTANCE.getEntityName(mc.thePlayer)))
            {
                requestWorldID();
            }
        }
    }

    /**
     * Simple message listener for WorldIdMessages received from the server.
     */
    public static class WorldIdListener implements IMessageHandler<WorldIDPacket, IMessage>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(WorldIDPacket message, MessageContext ctx)
        {
            lastResponse = System.currentTimeMillis();
            Journeymap.getLogger().info("Got the World ID from server: {}", message.getWorldID());
            Journeymap.proxy.handleWorldIdMessage(message.getWorldID(), null);
            return null;
        }
    }

}
