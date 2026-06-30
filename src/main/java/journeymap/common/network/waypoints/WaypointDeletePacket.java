package journeymap.common.network.waypoints;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import journeymap.common.Journeymap;

public class WaypointDeletePacket implements IMessage {
    public static final String CHANNEL_NAME = "jm_wypoints";

    private String waypointId = "";

    public WaypointDeletePacket()
    {
    }

    public WaypointDeletePacket(String waypointId)
    {
        Journeymap.getLogger().info("[WaypointDeletePacket] WaypointDeletePacket");
        this.waypointId = waypointId;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        try
        {
            waypointId = ByteBufUtils.readUTF8String(buf);
        }
        catch (Throwable t)
        {
            Journeymap.getLogger().error("Failed to read message: {}", String.valueOf(t));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        Journeymap.getLogger().info("tobytes WaypointDeletePacket");
        try
        {
            ByteBufUtils.writeUTF8String(buf, waypointId);
        } catch (Throwable t)
        {
            Journeymap.getLogger().error("[toBytes]Failed to read message: {}", String.valueOf(t));
        }
    }

    public String getWaypointId() {
        return waypointId;
    }
}
