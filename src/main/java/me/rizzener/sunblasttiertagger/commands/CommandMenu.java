package me.rizzener.sunblasttiertagger.commands;

import me.rizzener.sunblasttiertagger.menu.Menu;
import me.rizzener.sunblasttiertagger.SunBlastTierTagger;
import me.rizzener.sunblasttiertagger.tools.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandMenu implements CommandSub {
    private final SunBlastTierTagger plugin;

    public CommandMenu(SunBlastTierTagger plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length >= 1) {
            return false;
        } else if (!(sender instanceof Player)) {
            Utils.sendMessage(sender, Utils.getMessage("messages.only-players"));
            return true;
        } else {
            Player player = (Player) sender;
            if (!Utils.has(player, "SunBlastTierTagger.menu")) {
                return true;
            } else {
                if (args.length == 0) {
                    Menu menu = new Menu(plugin);
                    menu.openMenu(player);
                    return true;
                } else {
                    Utils.sendMessage(player, Utils.getMessage("messages.commands.menu.error"));
                }
                return true;
            }
        }
    }

    @Override
    public List<String> tab(CommandSender var1, String[] var2) {
        return null;
    }

    @Override
    public String command() {
        return "menu";
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public String permission() {
        return "SunBlastTierTagger.menu";
    }

    @Override
    public String description() {
        return Utils.getMessage("messages.commands.menu.usage");
    }
}