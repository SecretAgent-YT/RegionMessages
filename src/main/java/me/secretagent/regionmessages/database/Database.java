package me.secretagent.regionmessages.database;

import me.secretagent.regionmessages.region.Region;
import org.bukkit.Location;

import java.util.List;

public interface Database {

    void saveRegion(Region region);

    Region getRegion(String name);

    Region getRegion(Location location);

    void deleteRegion(String name);

    boolean regionExists(String name);

    boolean regionExists(Location location);

    void loadRegions();

}
