package me.rizzener.sunblasttiertagger.tools;

import me.rizzener.sunblasttiertagger.SunBlastTierTagger;
import org.bukkit.Bukkit;

public class Logger {
    public static void empty(String text) {
        Bukkit.getConsoleSender().sendMessage(me.rizzener.sunblasttiertagger.tools.Utils.colorizeHex(text));
    }

    public static void info(String s) {
        Bukkit.getConsoleSender().sendMessage(me.rizzener.sunblasttiertagger.tools.Utils.colorizeHex("&e[" + SunBlastTierTagger.SunBlastPlugin().getName() + "] &a" + s));
    }

    public static void warn(String s) {
        Bukkit.getConsoleSender().sendMessage(me.rizzener.sunblasttiertagger.tools.Utils.colorizeHex("&e[" + SunBlastTierTagger.SunBlastPlugin().getName() + "] &6" + s));
    }

    public static void error(String s) {
        Bukkit.getConsoleSender().sendMessage(me.rizzener.sunblasttiertagger.tools.Utils.colorizeHex("&e[" + SunBlastTierTagger.SunBlastPlugin().getName() + "] &c" + s));
    }
}
