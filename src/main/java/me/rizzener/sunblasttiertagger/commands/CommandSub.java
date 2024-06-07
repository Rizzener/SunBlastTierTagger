package me.rizzener.sunblasttiertagger.commands;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface CommandSub {
    boolean execute(CommandSender var1, String[] var2);

    List<String> tab(CommandSender var1, String[] var2);

    String command();

    List<String> aliases();

    String permission();

    String description();
}
