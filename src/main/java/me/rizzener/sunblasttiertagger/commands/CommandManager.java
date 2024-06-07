package me.rizzener.sunblasttiertagger.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import me.rizzener.sunblasttiertagger.tools.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandManager implements CommandExecutor, TabCompleter {
    private List<CommandSub> commands = new ArrayList();

    public void register(CommandSub commandSub) {
        this.commands.add(commandSub);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 0) {
            CommandSub sub = this.getCommand(args[0]);
            if (sub != null) {
                if (sub.permission() != null && !Utils.has(sender, sub.permission())) {
                    return true;
                }

                List<String> argsList = new ArrayList(Arrays.asList(args));
                argsList.remove(0);

                try {
                    if (!sub.execute(sender, (String[])argsList.toArray(new String[argsList.size()]))) {
                        Utils.sendMessage(sender, sub.description());
                    }
                } catch (Exception var8) {
                    Utils.sendMessage(sender, "Произошла ошибка при выполнении команды, проверьте консоль.");
                    var8.printStackTrace();
                }
            } else {
                Utils.sendMessage(sender, Utils.getMessage("messages.unknown"));
            }

            return true;
        } else {
            List<CommandSub> commands = this.getAllowed(sender);
            if (commands.isEmpty()) {
                Utils.sendMessage(sender, Utils.getMessage("messages.no-allowed"));
                return true;
            } else {
                commands.forEach((x) -> {
                    Utils.sendMessage(sender, x.description());
                });
                return true;
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return this.filter((List)this.getAllowed(sender).stream().map((x) -> {
                return x.command();
            }).collect(Collectors.toList()), args);
        } else {
            if (args.length >= 2) {
                CommandSub command = this.getCommand(args[0]);
                if (command != null) {
                    if (command.permission() != null && !sender.hasPermission(command.permission())) {
                        return null;
                    }

                    List<String> argsList = new ArrayList(Arrays.asList(args));
                    argsList.remove(0);
                    return this.filter(command.tab(sender, (String[])argsList.toArray(new String[argsList.size()])), args);
                }
            }

            return null;
        }
    }

    private List<String> filter(List<String> text, String[] args) {
        if (text == null) {
            return null;
        } else {
            String last = args[args.length - 1].toLowerCase();
            List<String> result = new ArrayList();
            Iterator var5 = text.iterator();

            while(var5.hasNext()) {
                String s = (String)var5.next();
                if (s.startsWith(last)) {
                    result.add(s);
                }
            }

            return result.isEmpty() ? null : result;
        }
    }

    public List<CommandSub> getAllowed(CommandSender sender) {
        return (List)this.commands.stream().filter((x) -> {
            return x.permission() == null ? true : sender.hasPermission(x.permission());
        }).collect(Collectors.toList());
    }

    public CommandSub getCommand(String command) {
        return (CommandSub)this.commands.stream().filter((x) -> {
            return x.command().equalsIgnoreCase(command) || x.aliases() != null && x.aliases().stream().anyMatch((a) -> {
                return a.equalsIgnoreCase(command);
            });
        }).findAny().orElse((CommandSub) null);
    }
}
