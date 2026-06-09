package journeymap.client.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import journeymap.client.feature.Feature;
import journeymap.client.feature.FeatureManager;
import journeymap.common.Journeymap;
import journeymap.common.network.permissions.PermissionsPacket;

public class PermissionsListener implements IMessageHandler<PermissionsPacket, IMessage> {

    @Override
    public IMessage onMessage(PermissionsPacket message, MessageContext ctx) {
        Journeymap.getLogger().info("Got permissions from server: {}", String.valueOf(message));

        FeatureManager fm = FeatureManager.instance();

        if (message.getRadarAnimals() == 1) fm.disableFeatureInMultiplayer(Feature.RadarAnimals);
        if (message.getRadarPlayers() == 1) fm.disableFeatureInMultiplayer(Feature.RadarPlayers);
        if (message.getRadarMobs() == 1) fm.disableFeatureInMultiplayer(Feature.RadarMobs);
        if (message.getRadarVillagers() == 1) fm.disableFeatureInMultiplayer(Feature.RadarVillagers);
        if (message.getMapCaves() == 1) fm.disableFeatureInMultiplayer(Feature.MapCaves);
        if (message.getTeleport() == 1) fm.disableFeatureInMultiplayer(Feature.Teleport);

        return null;
    }
}