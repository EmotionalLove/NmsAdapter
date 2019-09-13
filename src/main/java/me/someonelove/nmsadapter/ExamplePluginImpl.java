package me.someonelove.nmsadapter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Deprecated // DO NOT ACTUALLY MAKE CALLS TO THIS CLASS, EVIL WILL HAPPEN - THIS CLASS IS PURELY AN EXAMPLE.
public class ExamplePluginImpl /* extends JavaPlugin */ {

    // @Override
    /* public */ void onEnable() {
        NmsAdapter adapter = null;// = new NmsAdapter(this);
        MinecraftClass craftItemStack = adapter.getMinecraftClass("{cb}.inventory.CraftItemStack");
        Object nmsItemStack = craftItemStack.invokeFunction("asNMSCopy", new ItemStack(Material.EMERALD));
        MinecraftClass nmsItemStackClass = adapter.getMinecraftClass("{nms}.ItemStack", nmsItemStack);
        Object s = nmsItemStackClass.invokeFunction("a");
        System.out.println(s);
        // example - this won't actually run on anything above 1.11 because I can't be bothered to look up the actual function names.
        VersionMatcher matcher = // ItemStack.getName()
                new VersionMatcher("a")
                .then(new VersionMatcher.Rule(VersionMatcher.Comparative.EQUAL, 14), "c") // == 1.14.x
                .then(new VersionMatcher.Rule(VersionMatcher.Comparative.GREATER_THAN, 11), "b"); // > 1.11.x
        nmsItemStackClass.invokeFunction(matcher, adapter);
    }
}
