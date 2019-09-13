package me.someonelove.nmsadapter;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class NmsAdapter {

    private String nmsVersion;

    public NmsAdapter(JavaPlugin plugin) {
        // the package for nms classes has the version in it!
        String[] pkgSplit = plugin.getServer().getClass().getPackage().getName().split("\\.");
        nmsVersion = pkgSplit[pkgSplit.length - 1];
        plugin.getLogger().log(Level.INFO, "NmsAdapter detected Nms version " + nmsVersion);
        plugin.getLogger().log(Level.INFO, "Additionally, the version of Minecraft running is 1." + getMajorVersion() + ".x");
        plugin.getLogger().log(Level.INFO, "Successfully set up NmsAdapter.");
    }

    /**
     * Create a MinecraftClass for accessing static and instance fields and functions
     * @param where The package name + the class name.
     * @param instance An instance of the Class you're trying to find.
     * When accessing NMS and CraftBukkit classes, use these replace keys:
     * {nms} For classes in `net.minecraft.server.[version]`
     * {cb} For classes in `org.bukkit.craftbukkit.[version]`
     *
     * So if you wanted to access the NMS ItemStack class, you'd pass "{nms}.ItemStack"
     * If you wanted to access `CraftItemStack`, you'd pass "{cb}.inventory.CraftItemStack"
     *
     * @return The coressponding MinecraftClass for the given path
     * or null if it was invalid >.>
     */
    public MinecraftClass getMinecraftClass(String where, Object instance) {
        try {
            return new MinecraftClass(Class.forName(where.replace("{nms}", "net.minecraft.server." + nmsVersion).replace("{cb}", "org.bukkit.craftbukkit." + nmsVersion)), instance);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a MinecraftClass for accessing static fields and functions
     * @param where The package name + the class name
     * When accessing NMS and CraftBukkit classes, use these replace keys:
     * {nms} For classes in `net.minecraft.server.[version]`
     * {cb} For classes in `org.bukkit.craftbukkit.[version]`
     *
     * So if you wanted to access the NMS ItemStack class, you'd pass "{nms}.ItemStack"
     * If you wanted to access `CraftItemStack`, you'd pass "{cb}.inventory.CraftItemStack"
     *
     * @return The coressponding MinecraftClass for the given path
     * or null if it was invalid >.>
     */
    public MinecraftClass getMinecraftClass(String where) {
        try {
            return new MinecraftClass(Class.forName(where.replace("{nms}", "net.minecraft.server." + nmsVersion).replace("{cb}", "org.bukkit.craftbukkit." + nmsVersion)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the NMS version
     * @return something like v1_14_1_R1
     */
    public final String getNmsVersion() {
        return nmsVersion;
    }

    /**
     * Get the major version of Minecraft
     * @return The number in the middle
     * So like
     * if you're running 1.14.4 this would return 14
     */
    public final int getMajorVersion() {
        return Integer.parseInt(nmsVersion.split("_")[1]);
    }
}
