package journeymap.common.model;

import journeymap.client.render.texture.TextureCache;
import journeymap.client.render.texture.TextureImpl;

public class WaypointHelper {
    public static TextureImpl getTexture(Waypoint waypoint)
    {
        return waypoint.isDeathPoint() ? TextureCache.instance().getDeathpoint() : TextureCache.instance().getWaypoint();
    }
}
