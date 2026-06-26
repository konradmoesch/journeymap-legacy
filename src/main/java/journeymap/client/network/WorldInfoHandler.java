/*
 * JourneyMap Mod <journeymap.info> for Minecraft
 * Copyright (c) 2011-2017  Techbrew Interactive, LLC <techbrew.net>.  All Rights Reserved.
 */

package journeymap.client.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import journeymap.client.forge.helper.ForgeHelper;
import journeymap.common.Journeymap;
import journeymap.common.network.worldid.WorldIDPacket;
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
    Minecraft mc = ForgeHelper.INSTANCE.getClient();

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
                ClientNetworkDispatcher.requestWorldID();
                ClientNetworkDispatcher.requestWaypoints();
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
            ClientNetworkDispatcher.setLastWorldIdResponse(System.currentTimeMillis());
            Journeymap.getLogger().info("Got the World ID from server: {}", message.getWorldID());
            Journeymap.proxy.handleWorldIdMessage(message.getWorldID(), null);
            return null;
        }
    }

}
