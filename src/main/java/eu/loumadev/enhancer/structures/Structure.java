package eu.loumadev.enhancer.structures;

import eu.loumadev.enhancer.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Structure {

    private int totalSize;
    private int width;
    private int height;
    private int depth;

    public static void build(Location loc, File file) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject structure = (JSONObject) parser.parse(new FileReader(file));

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        World world = loc.getWorld();

        JSONArray blocks = (JSONArray) structure.get("blocks");
        for(int i = 0; i < blocks.size(); i++){

            JSONObject blk = (JSONObject) blocks.get(i);

            int blkX = x + Integer.parseInt(blk.get("x").toString());
            int blkY = y + Integer.parseInt(blk.get("y").toString());
            int blkZ = z + Integer.parseInt(blk.get("z").toString());

            // TODO: set block data
            world.getBlockAt(blkX, blkY, blkZ).setBlockData(Bukkit.createBlockData(blk.get("blockdata").toString()));
        }
    }

    /**
     * @static
     * @param b1    Primary position
     * @param b2    Secondary position
     * @param file  JSON file to save
     */
    public static void save(Block b1, Block b2, File file) throws WorldMismatchException, IOException {

        if(b1.getWorld() != b2.getWorld()) throw new WorldMismatchException("Positions are not in the same world!");

        HashMap<String, Object> struct = new HashMap<>();
        ArrayList<JSONObject> blocks = new ArrayList<>();

        int startX, startY, startZ;
        int endX, endY, endZ;

        startX = Math.min(b1.getX(), b2.getX());
        startY = Math.min(b1.getY(), b2.getY());
        startZ = Math.min(b1.getZ(), b2.getZ());

        endX = Math.max(b1.getX(), b2.getX());
        endY = Math.max(b1.getY(), b2.getY());
        endZ = Math.max(b1.getZ(), b2.getZ());

        for(int z = startZ; z <= endZ; z++) {
            for(int y = startY; y <= endY; y++) {
                for(int x = startX; x <= endX; x++) {

                    // we can store more block data, but I guess it's fine for now
                    HashMap<String, Object> block = new HashMap<>();

                    // couldn't think of a better way, its fine for now i suppose
                    Block b = b1.getWorld().getBlockAt(x, y, z);

                    block.put("x", x - startX);
                    block.put("y", y - startY);
                    block.put("z", z - startZ);
                    block.put("blockdata", b.getBlockData().getAsString());

                    // now just add them into the array
                    blocks.add(new JSONObject(block));
                }
            }
        }

        int width = endX - startX;
        int height = endY - startY;
        int depth = endZ - startZ;
        int totalSize = width * height * depth;

        struct.put("width", width);
        struct.put("height", height);
        struct.put("depth", depth);
        struct.put("size", totalSize);
        struct.put("blocks", blocks);

        // write into file
        FileWriter writer = new FileWriter(file);
        writer.write(new JSONObject(struct).toJSONString());
        writer.close();
    }

    public int getTotalSize() {
        return totalSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }
}
