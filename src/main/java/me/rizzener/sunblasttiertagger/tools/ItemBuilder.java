package me.rizzener.sunblasttiertagger.tools;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;
    private List<String> lore;
    private List<String> formattedLore;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        this.lore = new ArrayList<>();
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        itemMeta.setDisplayName(Utils.colorizeHex(displayName));
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setLore(List<String> lore, Player player) {
        List<String> coloredLore = Utils.colorizeHex(lore);
        List<String> replacedLore = new ArrayList<>();
        for (String line : coloredLore) {
            replacedLore.add(line
                    .replace("{Sword}", Utils.getPlayerSwordTier(player.getName()))
                    .replace("{Vanilla}", Utils.getPlayerVanillaTier(player.getName()))
                    .replace("{Netherite}", Utils.getPlayerNetheriteTier(player.getName())));
        }
        this.lore = replacedLore;
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        this.lore.add(Utils.colorizeHex(line));
        return this;
    }

    public ItemStack build() {
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}