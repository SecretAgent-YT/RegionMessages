package me.secretagent.regionmessages.region;

import com.google.common.collect.Range;
import me.secretagent.regionmessages.util.Util;
import org.bson.Document;
import org.bukkit.Location;

public class Region {

    private final String name;

    private final Location location1;
    private final Location location2;

    public Region(String name, Location location1, Location location2) {
        this.name = name;
        this.location1 = location1;
        this.location2 = location2;
    }

    public Region(Document document) {
        this.name = document.getString("name");
        this.location1 = Util.locationFromDocument((Document) document.get("location1"));
        this.location2 = Util.locationFromDocument((Document) document.get("location2"));
    }

    public String getName() {
        return name;
    }

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public boolean insideRegion(Location location) {
        if (inRange(location1.getX(), location2.getX(), location.getX())) {
            if (inRange(location1.getY(), location2.getY(), location.getY())) {
                if (inRange(location1.getZ(), location2.getZ(), location.getZ())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Document getDocument() {
        return new Document()
                .append("name", name)
                .append("location1", Util.locationToDocument(location1))
                .append("location2", Util.locationToDocument(location2));
    }

    private static boolean inRange(double min, double max, double candidate) {
        Range<Double> range;
        if (min < max) {
            range = Range.closed(min, max);
        } else {
            range = Range.closed(max, min);
        }
        return range.contains(candidate);
    }

}
