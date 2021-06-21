package bezfor.ua.skybattles;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new MainIsland(), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new GameContent(),0,18000);
    }
}






