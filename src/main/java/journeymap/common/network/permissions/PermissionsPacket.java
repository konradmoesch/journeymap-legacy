/*
 * JourneyMap Mod <journeymap.info> for Minecraft
 * Copyright (c) 2011-2017  Techbrew Interactive, LLC <techbrew.net>.  All Rights Reserved.
 */

package journeymap.common.network.permissions;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import journeymap.common.Journeymap;

/**
 * Created by Mysticdrew on 10/8/2014.
 */
public class PermissionsPacket implements IMessage {

    private int radarAnimals;
    private int radarPlayers;
    private int radarMobs;
    private int radarVillagers;
    private int mapCaves;
    private int teleport;

    public PermissionsPacket() {
    }

    public PermissionsPacket(int radarAnimals, int radarPlayers, int radarMobs, int radarVillagers, int mapCaves, int teleport) {
        this.radarAnimals = radarAnimals;
        this.radarPlayers = radarPlayers;
        this.radarMobs = radarMobs;
        this.radarVillagers = radarVillagers;
        this.mapCaves = mapCaves;
        this.teleport = teleport;
    }

    public int getRadarAnimals() {
        return radarAnimals;
    }

    public int getRadarPlayers() {
        return radarPlayers;
    }

    public int getRadarMobs() {
        return radarMobs;
    }

    public int getRadarVillagers() {
        return radarVillagers;
    }

    public int getMapCaves() {
        return mapCaves;
    }

    public int getTeleport() {
        return teleport;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            radarAnimals = ByteBufUtils.readVarShort(buf);
            radarPlayers = ByteBufUtils.readVarShort(buf);
            radarMobs = ByteBufUtils.readVarShort(buf);
            radarVillagers = ByteBufUtils.readVarShort(buf);
            mapCaves = ByteBufUtils.readVarShort(buf);
            teleport = ByteBufUtils.readVarShort(buf);
        } catch (Throwable t) {
            Journeymap.getLogger().error("[fromBytes]Failed to read message: {}", String.valueOf(t));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            ByteBufUtils.writeVarShort(buf, radarAnimals);
            ByteBufUtils.writeVarShort(buf, radarPlayers);
            ByteBufUtils.writeVarShort(buf, radarMobs);
            ByteBufUtils.writeVarShort(buf, radarVillagers);
            ByteBufUtils.writeVarShort(buf, mapCaves);
            ByteBufUtils.writeVarShort(buf, teleport);
        } catch (Throwable t) {
            Journeymap.getLogger().error("[toBytes]Failed to read message: {}", String.valueOf(t));
        }
    }
}
