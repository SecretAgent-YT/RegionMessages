package me.secretagent.regionmessages.util;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Util {

    public static Document locationToDocument(Location location) {
        return new Document()
                .append("x", location.getX())
                .append("y", location.getY())
                .append("z", location.getZ())
                .append("world", location.getWorld().getName());
    }

    public static Location locationFromDocument(Document document) {
        return new Location(
                Bukkit.getWorld(document.getString("world")),
                document.getDouble("x"),
                document.getDouble("y"),
                document.getDouble("z")
        );
    }

}
