package journeymap.common.network.waypoints;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class WaypointSyncRequestPacket implements IMessage {
    public static final String CHANNEL_NAME = "jm_wypoints";

    public WaypointSyncRequestPacket()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    }
}
