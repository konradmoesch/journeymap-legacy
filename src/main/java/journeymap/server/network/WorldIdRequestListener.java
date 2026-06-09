package journeymap.server.network;

import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import journeymap.common.network.worldid.WorldIDPacket;
import journeymap.common.network.worldid.WorldIDRequestPacket;
import journeymap.server.oldservercode.config.ConfigHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class WorldIdRequestListener implements IMessageHandler<WorldIDRequestPacket, WorldIDPacket> {
    @Override
    public WorldIDPacket onMessage(WorldIDRequestPacket requestMessage, MessageContext ctx) {

        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        String worldName = player.getEntityWorld().getWorldInfo().getWorldName();
        if (ConfigHandler.getConfigByWorldName(worldName).isUsingWorldID()) {
            String worldID = ConfigHandler.getConfigByWorldName(worldName).getWorldID();
            return new WorldIDPacket(worldID);
        }
        return null;
    }
}