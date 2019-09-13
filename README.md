# NmsAdapter
A library for making NMS calls cross-compatible, the easy way.

# Adding to your project
Use maven or gradle

## Gradle
```
allprojects {
  repositories {
    ...
      maven { url 'https://jitpack.io' }
  }
}
  
dependencies {
  implementation 'com.github.EmotionalLove:NmsAdapter:2cc65a8f83'
}

```
## Maven
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
      <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependencies>
  <dependency>
    <groupId>com.github.EmotionalLove</groupId>
    <artifactId>NmsAdapter</artifactId>
    <version>2cc65a8f83</version>
  </dependency>
</dependencies>
```

# Packaging the library
This library should be packaged with your plugin's JAR file, using shadowjar or something similar.
This library will not load as a plugin.

# Using the library
```Java
public class ExamplePluginImpl extends JavaPlugin {

    @Override
    public void onEnable() {
        NmsAdapter adapter = new NmsAdapter(this); // create the NmsAdapter instance (store this in a field if you'd like)
        MinecraftClass craftItemStack = adapter.getMinecraftClass("{cb}.inventory.CraftItemStack"); 
        Object nmsItemStack = craftItemStack.invokeFunction("asNMSCopy", new ItemStack(Material.EMERALD));
        MinecraftClass nmsItemStackClass = adapter.getMinecraftClass("{nms}.ItemStack", nmsItemStack);
        Object s = nmsItemStackClass.invokeFunction("a");
        System.out.println(s);
        // example - the below won't actually run on anything above 1.11 because I can't be bothered to look up the actual function names.
        VersionMatcher matcher = // ItemStack.getUnlocalizedName()
                new VersionMatcher("a")
                .then(new VersionMatcher.Rule(VersionMatcher.Comparative.EQUAL, 14), "c") // == 1.14.x
                .then(new VersionMatcher.Rule(VersionMatcher.Comparative.GREATER_THAN, 11), "b"); // > 1.11.x
        nmsItemStackClass.invokeFunction(matcher, adapter);
    }
}
```
Your project should have no imports to `net.minecraft.server.*` or `org.bukkit.craftbukkit.*`

You can access the `Class<?>` contained by `MinecraftClass` directly by accessing the field `MinecraftClass#minecraftClass`

The `VersionMatcher` helps your plugin function on versions in which the name of a class member changes between versions. See the documentation [here](https://github.com/EmotionalLove/NmsAdapter/blob/master/src/main/java/me/someonelove/nmsadapter/VersionMatcher.java)
