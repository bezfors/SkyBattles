package bezfor.ua.skybattles;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class MainIsland implements Listener {
    public Inventory inv;
    public Inventory inv2;
    public ItemStack chest;
    public ItemStack sword;
    public int updatePay;

    @SuppressWarnings("unused")
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        String nick = event.getPlayer().getName();
        Player playerNick = event.getPlayer();
        Location spawn = playerNick.getWorld().getSpawnLocation();

        event.setJoinMessage(ChatColor.GOLD + "SkyBattles" + ChatColor.GRAY + ">> " + ChatColor.WHITE + nick + ChatColor.GOLD + " присоеденился к игре");
        playerNick.setGameMode(GameMode.ADVENTURE);
        playerNick.setFoodLevel(20000);
        playerNick.teleport(spawn);
        spawn.getWorld().setGameRuleValue("doImmediateRespawn", "true");

        sword = new ItemStack(Material.IRON_SWORD);
        chest = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack clock = new ItemStack(Material.WATCH,1);
        ItemMeta meta = sword.getItemMeta();
        ItemMeta meta2 = chest.getItemMeta();
        ItemMeta meta1 = clock.getItemMeta();

        meta.setUnbreakable(true);
        meta2.setUnbreakable(true);
        meta1.setDisplayName(" ");
        sword.setItemMeta(meta);
        chest.setItemMeta(meta2);
        clock.setItemMeta(meta1);

        if (!playerNick.hasPlayedBefore()) {
            ItemStack[] items = {sword, new ItemStack(Material.GOLDEN_APPLE, 5), clock};
            playerNick.getInventory().addItem(items);
            playerNick.getInventory().setChestplate(chest);
            playerNick.getInventory().getChestplate().getItemMeta().setUnbreakable(true);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setOp(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 2));
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100.0F, 80.0F);
            createScore(player);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        String nick = event.getPlayer().getName();
        event.setQuitMessage(ChatColor.GOLD + "SkyBattles" + ChatColor.GRAY + ">> " + ChatColor.WHITE + nick + ChatColor.GOLD + " вышел с игры");
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void noFall(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
        double SpawnRadius = 30.0;
        Location SpawnLoc = new Location(Bukkit.getWorld("world"), -22, 141, -15);
        Location EntLoc = event.getEntity().getLocation();

        if (EntLoc.getWorld().equals(SpawnLoc.getWorld()) && EntLoc.distance(SpawnLoc) <= SpawnRadius) {
            event.setCancelled(true);
        }
    }
    @SuppressWarnings("unused")
    @EventHandler
    public void weatherOff(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(event.toWeatherState());
        }
    }
    public void createScore(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("SkyBattles", "dummy");
        obj.setDisplayName(ChatColor.DARK_GRAY + "  <<" + ChatColor.GOLD + "  SkyBattles  " + ChatColor.DARK_GRAY + ">>  ");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        int kills = player.getStatistic(Statistic.PLAYER_KILLS);
        int playerLvl = kills / 5;
        Score score5 = obj.getScore(ChatColor.AQUA + " Статистика");
        score5.setScore(7);
        Score score2 = obj.getScore(ChatColor.WHITE + " Уровень " + ChatColor.GRAY + ">>  " + ChatColor.AQUA + playerLvl);
        score2.setScore(6);
        Score score1 = obj.getScore(ChatColor.WHITE + " Убийств " + ChatColor.GRAY + ">>  " + ChatColor.AQUA + player.getStatistic(Statistic.PLAYER_KILLS));
        score1.setScore(4);
        Score score3 = obj.getScore(ChatColor.WHITE + " Смертей " + ChatColor.GRAY + ">>  " + ChatColor.AQUA + player.getStatistic(Statistic.DEATHS));
        score3.setScore(3);
        player.setScoreboard(board);
    }
    @SuppressWarnings("unused")
    @EventHandler
    public void playerKills(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        Entity e = player.getKiller();

        event.setKeepInventory(true);
        event.setKeepLevel(false);
        if(player.getKiller() == killer){
            createScore(player);
            if(e != null){
                event.setDeathMessage(ChatColor.WHITE + "Игрок " + ChatColor.GOLD + event.getEntity().getName() + ChatColor.WHITE + " был убит игроком " + ChatColor.GOLD + killer.getName());
                createScore(killer);
                createScore(player);
                killer.giveExpLevels(1);
            }
        }
    }
    @EventHandler
    @SuppressWarnings("unused")
    public void handleItemDrop(PlayerDropItemEvent event){
        event.setCancelled(true);
    }
    @EventHandler
    @SuppressWarnings("unused")
    public void MenuClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        int paySum = player.getLevel() - updatePay;

        if(event.getInventory().equals(inv)){
            event.setCancelled(true);
            event.getView().getPlayer().openInventory(inv2);
        }
        if(event.getInventory().equals(inv2)){
            event.setCancelled(true);
            if(player.getLevel() >= updatePay){
                if(event.getCurrentItem().getType() == Material.IRON_SWORD){
                    if(event.getCurrentItem().getEnchantmentLevel(Enchantment.DAMAGE_ALL) <= 4) {
                        player.getPlayer().setLevel(paySum);
                        player.closeInventory();
                        event.getCurrentItem().addEnchantment(Enchantment.DAMAGE_ALL, event.getCurrentItem().getEnchantmentLevel(Enchantment.DAMAGE_ALL) + 1);
                    }
                }
                if(event.getCurrentItem().getType() == Material.IRON_CHESTPLATE){
                    if(event.getCurrentItem().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) <= 3) {
                        player.getPlayer().setLevel(paySum);
                        player.closeInventory();
                        event.getCurrentItem().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, event.getCurrentItem().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) + 1);}
                }
            }
        }
    }
    @EventHandler
    @SuppressWarnings("unused")
    public void Menu(PlayerInteractEvent event){
        inv = Bukkit.createInventory(null, InventoryType.DISPENSER, "Меню");
        ItemStack item = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Улучшения");
        item.setItemMeta(meta);
        inv.setItem(4,item);

        inv2 = Bukkit.createInventory(null,9, ChatColor.AQUA + "Улучшения");
        ItemStack item1 = new ItemStack(Material.PAPER);
        ItemMeta metas = item1.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        updatePay = event.getPlayer().getStatistic(Statistic.PLAYER_KILLS) / 5 + 5;
        String upPay = Integer.toString(updatePay);
        lore.add(ChatColor.GOLD + "Стоимость: " + upPay + ChatColor.AQUA + " опыта");
        lore.add(ChatColor.GOLD + "Чтобы улучшить что то клик по предмету!");
        lore.add(ChatColor.GOLD + "Для улучшения брони снемите её!");
        metas.setLore(lore);
        metas.setDisplayName(ChatColor.AQUA + "Информация");
        item1.setItemMeta(metas);
        inv2.setItem(4, item1);

        if(event.getMaterial() == Material.WATCH){
            if(event.getAction() == Action.RIGHT_CLICK_AIR){
                event.getPlayer().openInventory(inv);
            }
        }
    }
}
