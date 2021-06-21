package bezfor.ua.skybattles;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameContent implements Listener, Runnable{
    @Override
    public void run() {
        World world = Bukkit.getWorld("world");
        Location location = new Location(world, -18, 47, 22);
        List<ItemStack> itemList = new ArrayList<>();
        itemList.add(new ItemStack(Material.IRON_LEGGINGS));
        itemList.add(new ItemStack(Material.IRON_BOOTS));
        itemList.add(new ItemStack(Material.GOLDEN_APPLE));
        itemList.add(new ItemStack(Material.IRON_HELMET));
        itemList.add(new ItemStack(Material.DIAMOND_SWORD));
        for(ItemStack item : itemList){
            ItemMeta meta = item.getItemMeta();
            meta.setUnbreakable(true);
            item.setItemMeta(meta);
        }

        int random = new Random().nextInt(itemList.size());
        if(Bukkit.getOnlinePlayers().size() >= 2 ) {
            location.getBlock().setType(Material.GOLD_BLOCK);
            location.getBlock().getWorld().dropItem(location, itemList.get(random));
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.GOLD + "Ресурсы начали падать, скорей иди и лутайся! Там ты можешь получить части брони которые тебе нужны");
            }
        }
    }
}
