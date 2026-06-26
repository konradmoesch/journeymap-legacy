package journeymap.common.network.waypoints;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import journeymap.common.Journeymap;
import journeymap.common.model.Waypoint;

import java.util.ArrayList;

public class WaypointSyncPacket implements IMessage {
    public static final String CHANNEL_NAME = "jm_wypoints";

    private int status = 0;

    private ArrayList<Waypoint> waypoints = new ArrayList<>();

    public WaypointSyncPacket()
    {
    }

    public WaypointSyncPacket(int status, ArrayList<Waypoint> waypoints)
    {
        Journeymap.getLogger().info("[WaypointSyncPacket] WaypointSyncPacket");
        this.status = status;
        this.waypoints = waypoints;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        try
        {
            int status = ByteBufUtils.readVarShort(buf);

            // server waypoints not loaded or disabled
            //if (status == 0) return;

            int waypointCount = ByteBufUtils.readVarShort(buf);
            for (int i = 0; i < waypointCount; i++) {
                int waypointLength = ByteBufUtils.readVarShort(buf);

                ByteBuf waypointBuffer = Unpooled.buffer(waypointLength);
                buf.readBytes(waypointBuffer, waypointLength);
                Waypoint waypoint = Waypoint.deserializeFromByteBuf(waypointBuffer);
                waypoints.add(waypoint);
            }
        }
        catch (Throwable t)
        {
            Journeymap.getLogger().error("Failed to read message: {}", String.valueOf(t));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        Journeymap.getLogger().info("tobytes WaypointSyncPacket");
        try
        {
            ByteBufUtils.writeVarShort(buf, status);
            int waypointCount = waypoints.size();
            ByteBufUtils.writeVarShort(buf, waypointCount);
            for (int i = 0; i < waypointCount; i++) {
                Waypoint waypoint = waypoints.get(i);

                ByteBuf waypointBytes = waypoint.serializeToByteBuf();
                ByteBufUtils.writeVarShort(buf, waypointBytes.readableBytes());
                buf.writeBytes(waypointBytes);
            }
        } catch (Throwable t)
        {
            Journeymap.getLogger().error("[toBytes]Failed to read message: {}", String.valueOf(t));
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }
}
