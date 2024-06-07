package me.rizzener.sunblasttiertagger.commands;

import me.rizzener.sunblasttiertagger.SunBlastTierTagger;
import me.rizzener.sunblasttiertagger.menu.Menu;
import me.rizzener.sunblasttiertagger.tools.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandTiers implements CommandSub {
    private final SunBlastTierTagger plugin;

    public CommandTiers(SunBlastTierTagger plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            return false;
        } else if (!(sender instanceof Player)) {
            Utils.sendMessage(sender, Utils.getMessage("messages.only-players"));
            return true;
        } else {
            Player player = (Player) sender;
            if (!Utils.has(player, "SunBlastTierTagger.info")) {
                return true;
            } else {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        Utils.getStringList("messages.commands.info.other-message") .forEach(message -> {
                            String formattedMessage = message
                                    .replace("{player}", target.getName())
                                    .replace("{sword}", Utils.getPlayerSwordTier(target.getName()))
                                    .replace("{vanilla}", Utils.getPlayerVanillaTier(target.getName()))
                                    .replace("{netherite}", Utils.getPlayerNetheriteTier(target.getName()));
                            player.sendMessage(formattedMessage);
                        });
                    } else {
                        player.sendMessage(Utils.getMessage("messages.unkown-player"));
                    }
                    return true;
                } else if (args.length == 0) {
                    Utils.getStringList("messages.commands.info.me-message").forEach(message -> {
                        String formattedMessage = message
                                .replace("{player}", player.getName())
                                .replace("{sword}", Utils.getPlayerSwordTier(player.getName()))
                                .replace("{vanilla}", Utils.getPlayerVanillaTier(player.getName()))
                                .replace("{netherite}", Utils.getPlayerNetheriteTier(player.getName()))
                                .replace("{selected}", new Menu(plugin).getPlayerTier(player));
                        player.sendMessage(formattedMessage);
                    });
                    return true;
                } else {
                    Utils.sendMessage(player, Utils.getMessage("messages.commands.info.error"));
                    return true;
                }
            }
        }
    }


    public List<String> tab(CommandSender p0, String[] p1) {
        return null;
    }

    public String command() {
        return "info";
    }

    public List<String> aliases() {
        return null;
    }
    public String permission() {
        return "SunBlastTierTagger.info";
    }

    public String description() {
        return Utils.getMessage("messages.commands.info.usage");
    }
}