package me.rizzener.sunblasttiertagger.menu;

import me.rizzener.sunblasttiertagger.SunBlastTierTagger;
import me.rizzener.sunblasttiertagger.tools.ItemBuilder;
import me.rizzener.sunblasttiertagger.tools.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;

public class Menu implements Listener {
    private final SunBlastTierTagger plugin;

    public Menu(SunBlastTierTagger plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player) {
        String title = plugin.getConfig().getString("menu.title", "Menu");
        Inventory menu = Bukkit.createInventory(null, 45, title);

        ConfigurationSection itemsSection = plugin.getConfig().getConfigurationSection("menu.items");
        if (itemsSection != null) {
            Set<String> itemKeys = itemsSection.getKeys(false);
            for (String key : itemKeys) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    ItemStack item = createItemFromConfig(itemSection, player);
                    if (itemSection.isInt("slot")) {
                        int slot = itemSection.getInt("slot");
                        menu.setItem(slot, item);
                    } else if (itemSection.isList("slots")) {
                        List<Integer> slots = itemSection.getIntegerList("slots");
                        for (int slot : slots) {
                            menu.setItem(slot, item);
                        }
                    }
                }
            }
        }

        player.openInventory(menu);
    }

    private ItemStack createItemFromConfig(ConfigurationSection itemSection, Player player) {
        Material material = Material.valueOf(itemSection.getString("material", "STONE"));
        int amount = itemSection.getInt("amount", 1);
        String displayName = itemSection.getString("displayname", "");
        List<String> lore = itemSection.getStringList("lore");

        // Replace placeholders in lore
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replace("{Sword}", Utils.getPlayerSwordTier(player.getName()))
                    .replace("{Vanilla}", Utils.getPlayerVanillaTier(player.getName()))
                    .replace("{Netherite}", Utils.getPlayerNetheriteTier(player.getName())));
        }

        ItemBuilder itemBuilder = new ItemBuilder(material)
                .setAmount(amount)
                .setDisplayName(displayName)
                .setLore(lore, player);

        if (itemSection.isConfigurationSection("enchantments")) {
            ConfigurationSection enchantmentsSection = itemSection.getConfigurationSection("enchantments");
            for (String enchantmentKey : enchantmentsSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByName(enchantmentKey);
                int level = enchantmentsSection.getInt(enchantmentKey);
                if (enchantment != null) {
                    itemBuilder.addEnchantment(enchantment, level, true);
                }
            }
        }

        if (itemSection.isList("flags")) {
            List<String> flags = itemSection.getStringList("flags");
            for (String flag : flags) {
                itemBuilder.addItemFlags(ItemFlag.valueOf(flag));
            }
        }

        return itemBuilder.build();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(plugin.getConfig().getString("menu.title", "Menu"))) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || !clickedItem.hasItemMeta()) {
                return;
            }

            ItemMeta meta = clickedItem.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) {
                return;
            }

            String displayName = (meta.getDisplayName());
            Player player = (Player) event.getWhoClicked();

            ConfigurationSection itemsSection = plugin.getConfig().getConfigurationSection("menu.items");
            if (itemsSection != null) {
                for (String key : itemsSection.getKeys(false)) {
                    ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                    if (itemSection != null) {
                        String name = displayName.replace("&", "§");
                        String configDisplayName = (itemSection.getString("displayname", "").replace("&", "§"));
                        if (name.equals(configDisplayName)) {
                            String type = itemSection.getString("type", "item").toLowerCase();
                            handleItemClick(player, type);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void handleItemClick(Player player, String type) {
        switch (type) {
            case "sword":
                Utils.sendMessage(player, Utils.getMessage("menu.Sword-selected"));
                setPlayerTier(player, "Sword");
                break;
            case "vanilla":
                Utils.sendMessage(player, Utils.getMessage("menu.Vanilla-selected"));
                setPlayerTier(player, "Vanilla");
                break;
            case "netherite":
                Utils.sendMessage(player, Utils.getMessage("menu.Netherite-selected"));
                setPlayerTier(player, "Netherite");
                break;
            default:
                // Do nothing for "item" type
                break;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getDataConfig().contains("players." + player.getName())) {
            setPlayerTier(player, "Sword");
        }
    }

    private void setPlayerTier(Player player, String tier) {
        plugin.getDataConfig().set("players." + player.getName() + ".value", tier);
        plugin.saveDataConfig();
    }

    public String getPlayerTier(Player player) {
        return plugin.getDataConfig().getString("players." + player.getName() + ".value", "Sword");
    }
}