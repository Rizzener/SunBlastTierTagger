package me.rizzener.sunblasttiertagger.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.rizzener.sunblasttiertagger.SunBlastTierTagger;
import me.rizzener.sunblasttiertagger.menu.Menu;
import me.rizzener.sunblasttiertagger.tools.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TierPlaceholder extends PlaceholderExpansion {

    private final SunBlastTierTagger plugin;

    public TierPlaceholder(SunBlastTierTagger plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "sunblasttiertagger";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Rizzener";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("sword_tier")) {
            return Utils.getPlayerSwordTier(player.getName());
        } else if (identifier.equals("vanilla_tier")) {
            return Utils.getPlayerVanillaTier(player.getName());
        } else if (identifier.equals("netherite_tier")) {
            return Utils.getPlayerNetheriteTier(player.getName());
        } else if (identifier.equals("custom_tier")) {
            String playerTier = new Menu(plugin).getPlayerTier(player);
            if (playerTier == null) {
                return "null";
            }
            switch (playerTier) {
                case "Sword":
                    return Utils.getPlayerSwordTier(player.getName());
                case "Vanilla":
                    return Utils.getPlayerVanillaTier(player.getName());
                case "Netherite":
                    return Utils.getPlayerNetheriteTier(player.getName());
                default:
                    return "ERROR";
            }
        }
        return null;
    }
}
