// Package Declaration
package me.iffa.bananaspace;

// Java imports
import java.util.logging.Logger;
import java.io.File;

// BananaSpace imports
import me.iffa.bananaspace.config.SpaceConfig;
import me.iffa.bananaspace.config.SpacePlanetConfig;
import me.iffa.bananaspace.wgen.SpaceChunkGenerator;
import me.iffa.bananaspace.wgen.planets.PlanetsChunkGenerator;

// Bukkit imports
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.util.config.Configuration;

/**
 * Main class of BananaSpace
 * 
 * @author iffa
 * 
 */
public class BananaSpace extends JavaPlugin {
    // Variables
    public static String prefix;
    public static String version;
    public static final Logger log = Logger.getLogger("Minecraft");
    SpaceConfig cMgr = new SpaceConfig();
    SpacePlanetConfig cplaMgr = new SpacePlanetConfig();
    /**
     * Called when the plugin is loaded
     */
    @Override
    public void onDisable() {
        log.info(prefix + " Disabled version " + version);
    }

    /**
     * Called when the plugin is loaded
     */
    @Override
    public void onEnable() {
        // Initializing some variables
        version = getDescription().getVersion();
        prefix = "[" + getDescription().getName() + "]";

        // Loading configuration files
        cMgr.loadConfig();
        cplaMgr.loadConfig();
        log.info(prefix + " Enabled version " + version);
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        Configuration myConfig = null;
        File configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("BananaSpace").getDataFolder(), "config.yml");
        if (configFile.exists()) {
            myConfig = new Configuration(configFile);
            myConfig.load();
        } else {
            try {
                Bukkit.getServer().getPluginManager().getPlugin("BananaSpace").getDataFolder().mkdir();
                SpaceConfig.copyFile(getClass().getResourceAsStream("/config.yml"), configFile);
                myConfig = new Configuration(configFile);
                myConfig.load();
                configFile.delete();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        if (myConfig.getBoolean("worlds." + worldName + ".generation.generateplanets", true)) {
            return new PlanetsChunkGenerator(myConfig, this);
        }
        return new SpaceChunkGenerator();
    }
}
