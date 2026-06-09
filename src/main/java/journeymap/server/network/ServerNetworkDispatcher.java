package journeymap.server.network;

import journeymap.common.Journeymap;
import journeymap.common.network.PacketHandler;
import journeymap.common.network.permissions.PermissionsPacket;
import journeymap.common.network.worldid.WorldIDPacket;
import journeymap.server.oldservercode.mapcontrol.MappingOptionsHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerNetworkDispatcher {
    public static void sendAllPlayersWorldID(String worldID)
    {
        PacketHandler.WORLD_INFO_CHANNEL.sendToAll(new WorldIDPacket(worldID));
    }

    public static void sendPlayerWorldID(String worldID, EntityPlayerMP player)
    {

        if (player != null)
        {
            String playerName = player.getCommandSenderName();

            try
            {
                PacketHandler.WORLD_INFO_CHANNEL.sendTo(new WorldIDPacket(worldID), player);
            }
            catch (RuntimeException rte)
            {
                Journeymap.getLogger().error("{} is not a real player. WorldID:{} Error: {}", playerName, worldID, rte);
            }
            catch (Exception e)
            {
                Journeymap.getLogger().error("Unknown Exception - PlayerName:{} WorldID:{} Exception {}", playerName, worldID, e);
            }
        }

    }

    private static int toInt(boolean val) {
        return val ? 1 : 0;
    }

    public static void sendPerms(String worldName, EntityPlayerMP player) {
        Journeymap.getLogger().info("Sending permissions to player {}", player.getCommandSenderName());
        MappingOptionsHandler options = new MappingOptionsHandler(worldName);
        PacketHandler.JM_PERMS.sendTo(new PermissionsPacket(toInt(options.disableRadar(player.getCommandSenderName())), toInt(options.disableRadar(player.getCommandSenderName())), toInt(options.disableRadar(player.getCommandSenderName())), toInt(options.disableRadar(player.getCommandSenderName())), toInt(options.disableCaveMapping(player.getCommandSenderName())), toInt(options.disableTeleport(player.getCommandSenderName()))), player);
    }
}
