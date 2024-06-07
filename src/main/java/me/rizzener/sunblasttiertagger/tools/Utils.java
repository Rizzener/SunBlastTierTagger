package me.rizzener.sunblasttiertagger.tools;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.rizzener.sunblasttiertagger.SunBlastTierTagger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Utils {
    private static FileConfiguration config;
    private static final Map<String, String> swordTiers = new HashMap<>();
    private static final Map<String, String> vanillaTiers = new HashMap<>();
    private static final Map<String, String> netheriteTiers = new HashMap<>();

    public static FileConfiguration getConfig() {
        return config != null ? config : (config = Config.getFile("config.yml"));
    }

    public static int getInt(String path) {
        return getConfig().getInt(path);
    }

    public static double getDouble(String path) {
        return getConfig().getDouble(path);
    }

    public static boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }


    public static void reloadConfig() {
        config = Config.getFile("config.yml");
    }

    public static List<String> colorizeHex(List<String> text) {
        List<String> list = new ArrayList();
        text.forEach((x) -> {
            list.add(colorizeHex(x));
        });
        return list;
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})|<##([a-fA-F0-9]{6})>");
    private static final String COLOR_CHAR = "\u00A7";

    public static String colorizeHex(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            String replaceSharp = group.replace('#', 'x');
            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder(COLOR_CHAR + "x");
            for (char c : ch)
                builder.append(COLOR_CHAR).append(c);
            matcher.appendReplacement(buffer, builder.toString());
        }
        matcher.appendTail(buffer);
        String coloredMessage = buffer.toString();
        coloredMessage = coloredMessage.replace("&", COLOR_CHAR);
        return coloredMessage;
    }
    public static boolean has(CommandSender player, String permission) {
        if (!player.hasPermission(permission)) {
            sendMessage(player, getMessage("no-permission"));
            return false;
        } else {
            return true;
        }
    }

    public static List<String> getStringList(String path) {
        List<String> listMessage = getConfig().getStringList(path);
        return colorizeHex(listMessage);
    }

    public static String getMessage(String s) {
        String message = getConfig().getString(s);
        return colorizeHex(message);
    }
    public static void onEnableMessage() {
        Logger.empty("");
        Logger.empty("&6&l┏ &fПлагин: &eSunBlastTierTagger &8| &fВерсия: &e" + SunBlastTierTagger.getPlugin(SunBlastTierTagger.class).getDescription().getVersion());
        Logger.empty("&6&l⎪ &fПоследнее обновление плагина: &e07.06.2024");
        Logger.empty("&6&l┗ &fСоздатель плагина: &eRizzener (https://t.me/SunBlast_support)");
        Logger.empty("");
    }

    public static void sendMessage(CommandSender player, String text) {
        String[] var2 = text.split(";");
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String line = var2[var4];
            line = line.replace("<player>", player.getName());
            if (line.startsWith("title:")) {
                if (player instanceof Player) {
                    Title.sendTitle((Player)player, line.split("title:")[1]);
                }
            } else if (line.startsWith("actionbar:")) {
                if (player instanceof Player) {
                    ActionBar.sendActionBar((Player)player, line.split("actionbar:")[1]);
                }
            } else {
                player.sendMessage(colorizeHex(getMessage("messages.prefix") + line));
            }
        }
    }
    public static void startUpdater() {
        Bukkit.getScheduler().runTaskTimer(SunBlastTierTagger.SunBlastPlugin(), new Runnable() {
            public void run() {
                updateSwordTiers();
                updateVanillaTiers();
                updateNetheriteTiers();
            };
        }, 1L, (long)getInt("cooldown-update") * 20L);
    }

    //SwordTiers
    public static void updateSwordTiers() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.cistiers.ru/v1/get-table/sword")).GET().build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept((response) -> {
            JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String tier = entry.getKey().toUpperCase();
                for (JsonElement element : entry.getValue().getAsJsonArray()) {
                    swordTiers.put(element.getAsString(), tier);
                }
            }
            Logger.info("Категория Sword обновлена!");
        });
    }

    public static String getPlayerSwordTier(String username) {
        return swordTiers.getOrDefault(username, "Нету");
    }

    //VanillaTiers
    public static void updateVanillaTiers() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.cistiers.ru/v1/get-table/vanilla")).GET().build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept((response) -> {
            JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String tier = entry.getKey().toUpperCase();
                for (JsonElement element : entry.getValue().getAsJsonArray()) {
                    vanillaTiers.put(element.getAsString(), tier);
                }
            }
            Logger.info("Категория Vanilla обновлена!");
        });
    }

    public static String getPlayerVanillaTier(String username) {
        return vanillaTiers.getOrDefault(username, "Нету");
    }

    //NetheriteTiers

    public static void updateNetheriteTiers() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.cistiers.ru/v1/get-table/netherite")).GET().build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept((response) -> {
            JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String tier = entry.getKey().toUpperCase();
                for (JsonElement element : entry.getValue().getAsJsonArray()) {
                    netheriteTiers.put(element.getAsString(), tier);
                }
            }
            Logger.info("Категория Netherite обновлена!");
        });
    }

    public static String getPlayerNetheriteTier(String username) {
        return netheriteTiers.getOrDefault(username, "Нету");
    }

}
