package journeymap.common.network.waypoints;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import journeymap.common.Journeymap;
import journeymap.common.model.Waypoint;

public class WaypointAddPacket implements IMessage {
    public static final String CHANNEL_NAME = "jm_wypoints";

    private Waypoint waypoint = Waypoint.newEmptyWaypoint();

    public WaypointAddPacket()
    {
    }

    public WaypointAddPacket(Waypoint waypoint)
    {
        Journeymap.getLogger().info("[WaypointAddPacket] WaypointAddPacket");
        this.waypoint = waypoint;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        try
        {
            int waypointLength = ByteBufUtils.readVarShort(buf);

            ByteBuf waypointBuffer = Unpooled.buffer(waypointLength);
            buf.readBytes(waypointBuffer, waypointLength);
            waypoint = Waypoint.deserializeFromByteBuf(waypointBuffer);
        }
        catch (Throwable t)
        {
            Journeymap.getLogger().error("Failed to read message: {}", String.valueOf(t));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        Journeymap.getLogger().info("tobytes WaypointAddPacket");
        try
        {
            ByteBuf waypointBytes = waypoint.serializeToByteBuf();
            ByteBufUtils.writeVarShort(buf, waypointBytes.readableBytes());
            buf.writeBytes(waypointBytes);
        } catch (Throwable t)
        {
            Journeymap.getLogger().error("[toBytes]Failed to read message: {}", String.valueOf(t));
        }
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }
}
