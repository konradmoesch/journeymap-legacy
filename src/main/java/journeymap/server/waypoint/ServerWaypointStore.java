package journeymap.server.waypoint;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import journeymap.client.log.LogFormatter;
import journeymap.client.waypoint.JmReader;
import journeymap.common.Journeymap;
import journeymap.common.model.Waypoint;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

public class ServerWaypointStore {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, Waypoint> waypointsById = new HashMap<String, Waypoint>();
    private final Map<String, Set<String>> ownedWaypointIds = new HashMap<String, Set<String>>();
    private final Set<Integer> dimensions = new HashSet<Integer>();
    private boolean loaded = false;

    private String waypointDirectory = "";

    private ServerWaypointStore() {
    }

    public static ServerWaypointStore instance() {
        return Holder.INSTANCE;
    }

    public void setWaypointDirectory(String newWaypointDirectory)
    {
        waypointDirectory = newWaypointDirectory;
    }

    public Collection<Waypoint> getAll() {
        return waypointsById.values();
    }

    public Waypoint getWaypointById(String id) {
        Journeymap.getLogger().info("All waypoints: {}", waypointsById);
        Journeymap.getLogger().info("Waypoint By ID: {}", waypointsById.get(id));
        return waypointsById.get(id);
    }

    public Collection<Waypoint> getAllUserWaypoints(String username) {
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        Set<String> waypointIds = ownedWaypointIds.get(username);
        if (waypointIds != null) {
            for (String waypointId : waypointIds) {
                waypoints.add(waypointsById.get(waypointId));
            }
        }
        return waypoints;
    }

    public void reset() {
        Journeymap.getLogger().info("Resetting ServerWaypointStore");
        waypointsById.clear();
        ownedWaypointIds.clear();
        dimensions.clear();
        loaded = false;
        // TODO: only load if Waypointsync enabled
        initializeCaches();
    }

    private void initializeCaches() {
        synchronized (waypointsById) {
            synchronized (ownedWaypointIds) {
                Journeymap.getLogger().info("Initializing caches");
                File waypointDir = null;
                try {
                    waypointsById.clear();
                    ownedWaypointIds.clear();

                    waypointDir = new File(getWaypointDirectory());
                    ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>(new JmReader().loadWaypoints(waypointDir));

                    load(waypoints);

                    Journeymap.getLogger().info("Loaded {} waypoints from {}", waypointsById.size(), waypointDir);
                    debugLog();
                } catch (Exception e) {
                    Journeymap.getLogger().error("Error loading waypoints from {}: {}", waypointDir, LogFormatter.toString(e));
                }
            }
        }
    }

    public void load(Collection<Waypoint> waypoints) {
        Journeymap.getLogger().info("Loading {} waypoints into indices", waypoints.size());
        for (Waypoint waypoint : waypoints) {
            indexWaypoint(waypoint);
            dimensions.addAll(waypoint.getDimensions());
        }
        loaded = true;
    }

    public void indexWaypoint(Waypoint waypoint) {
        waypointsById.put(waypoint.getId(), waypoint);

        ownedWaypointIds.computeIfAbsent(waypoint.getOwner(), k -> new HashSet<>()).add(waypoint.getId());
    }

    public void saveAll() {
        waypointsById.values().forEach(waypoint -> {

        });
    }

    public void save(Waypoint waypoint)
    {
        waypointsById.remove(waypoint.getId());
        indexWaypoint(waypoint);
        Journeymap.getLogger().info("Saving waypoint to disk");
        File waypointFile = null;
        try
        {
            // Write to file
            File waypointDir = new File(getWaypointDirectory());
            //waypointFile = new File(FileHandler.getWaypointDir(), waypoint.getFileName());
            waypointFile = new File(waypointDir, waypoint.getFileName());
            Files.write(gson.toJson(waypoint), waypointFile, Charset.forName("UTF-8"));
        }
        catch (Exception e)
        {
            Journeymap.getLogger().error("Can't save waypoint file {}: {}", waypointFile, LogFormatter.toString(e));
        }
    }

    public void delete(String waypointId)
    {
        Journeymap.getLogger().info("Deleting waypoint on disk");
        Waypoint toDelete = getWaypointById(waypointId);

        File waypointFile = null;
        try
        {
            File waypointDir = new File(getWaypointDirectory());

            waypointFile = new File(waypointDir, toDelete.getFileName());
            if (!waypointFile.delete()) {
                Journeymap.getLogger().error("Could not delete waypoint file {}", waypointFile);
            }
            waypointsById.remove(waypointId);

        }
            catch (Exception e)
        {
            Journeymap.getLogger().error("Can't delete waypoint file {}: {}", waypointFile, LogFormatter.toString(e));
        }
    }

    public void debugLog()
    {
        Journeymap.getLogger().info("WaypointStore status");
        Journeymap.getLogger().info("Total Waypoints: {}", waypointsById.size());
        Journeymap.getLogger().info("Waypoints by player:");
        ownedWaypointIds.forEach((key, value) -> Journeymap.getLogger().info("{}: {}", key, value));
    }

    public boolean hasLoaded() {
        return loaded;
    }

    public List<Integer> getLoadedDimensions() {
        return new ArrayList<Integer>(dimensions);
    }

    public String getWaypointDirectory() {
        return waypointDirectory;
    }

    private static class Holder {
        private static final ServerWaypointStore INSTANCE = new ServerWaypointStore();
    }
}
