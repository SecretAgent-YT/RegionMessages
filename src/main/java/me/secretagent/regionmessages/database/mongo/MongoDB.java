package me.secretagent.regionmessages.database.mongo;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.secretagent.regionmessages.RegionMessages;
import me.secretagent.regionmessages.database.Database;
import me.secretagent.regionmessages.region.Region;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MongoDB implements Database {

    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    private final RegionMessages plugin;

    public MongoDB(RegionMessages plugin, String url) {
        this.plugin = plugin;
        this.client = MongoClients.create(url);
        this.database = client.getDatabase("RegionMessages");
        this.collection = database.getCollection("Regions");
    }

    @Override
    public void saveRegion(Region region) {
        collection.insertOne(region.getDocument());
        loadRegions();
    }

    @Override
    public Region getRegion(String name) {
        Bson filter = eqIgn("name", name);
        if (collection.countDocuments(filter) != 0) {
            return new Region(collection.find(filter).first());
        } else {
            return null;
        }
    }

    @Override
    public Region getRegion(Location location) {
        for (Region region : plugin.getRegions()) {
            if (region.insideRegion(location)) return region;
        }
        return null;
    }

    @Override
    public void deleteRegion(String name) {
        Bson filter = eq("name", name);
        if (regionExists(name)) collection.deleteOne(filter);
        loadRegions();
    }

    @Override
    public boolean regionExists(String name) {
        return getRegion(name) != null;
    }

    @Override
    public boolean regionExists(Location location) {
        return getRegion(location) != null;
    }

    @Override
    public void loadRegions() {
        List<Region> regions = new ArrayList<>();
        collection.find().forEach(doc -> regions.add(new Region(doc)));
        plugin.getRegions().clear();
        plugin.getRegions().addAll(regions);
    }

    private static Bson eqIgn(String fieldName, String value) {
        String patternString = new StringBuilder("(?i)^").append(value).append("$").toString();
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        return Filters.regex(fieldName, pattern);
    }

}
