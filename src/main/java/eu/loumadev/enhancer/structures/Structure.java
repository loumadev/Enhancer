package eu.loumadev.enhancer.structures;

import org.bukkit.block.Block;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;

public class Structure {

    private Block posA;
    private Block posB;
    private String name;
    private int totalSize;
    private int width;
    private int height;
    private int depth;

    public static Structure load(File file) {
        return null;
    }

    public static void save(Structure structure, File file) throws Exception {

        Block b1 = structure.getPosA();
        Block b2 = structure.getPosB();
        if (b1.getWorld() != b2.getWorld()) throw new Exception("Positions are not in the same world!");

        JSONObject struct = new JSONObject();
        JSONArray blocks = new JSONArray();
        int startX, startY, startZ;
        int endX, endY, endZ;

        startX = Math.min(b1.getX(), b2.getX());
        startY = Math.min(b1.getY(), b2.getY());
        startZ = Math.min(b1.getZ(), b2.getZ());

        endX = Math.max(b1.getX(), b2.getX());
        endY = Math.max(b1.getY(), b2.getY());
        endZ = Math.max(b1.getZ(), b2.getZ());

        for (int z = 0; z < endZ - startZ; z++) {
            for (int y = 0; y < endY - startY; y++) {
                for (int x = 0; x < endX - startX; x++) {

                    Block b = b1.getWorld().getBlockAt(x, y, z);    // couldn't think of a better way, its fine for now i suppose

                    // we can store more block data, but I guess it's fine for now
                    JSONObject block = new JSONObject();
                    block.put("x", x);
                    block.put("y", y);
                    block.put("z", z);
                    block.put("blockdata", b.getBlockData().getAsString());

                    // now just add them into the array
                    blocks.add(block);
                }
            }
        }

        struct.put("name", structure.getName());
        struct.put("size", structure.getTotalSize());
        struct.put("width", structure.getWidth());
        struct.put("height", structure.getHeight());
        struct.put("depth", structure.getDepth());
        struct.put("blocks", blocks);

        // write into file
        FileWriter writer = new FileWriter(file);
        writer.write(struct.toJSONString());
        writer.close();
    }

    public Block getPosA() {
        return posA;
    }

    public void setPosA(Block posA) {
        this.posA = posA;
    }

    public Block getPosB() {
        return posB;
    }

    public void setPosB(Block posB) {
        this.posB = posB;
    }

    public String getName() {
        return name;
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
