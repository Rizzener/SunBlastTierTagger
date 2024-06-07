package me.rizzener.sunblasttiertagger.tools;

import java.io.File;
import java.io.IOException;

import me.rizzener.sunblasttiertagger.SunBlastTierTagger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    public static FileConfiguration getFile(String s) {
        File file = new File(SunBlastTierTagger.SunBlastPlugin().getDataFolder(), s);
        if (SunBlastTierTagger.SunBlastPlugin().getResource(s) == null) {
            return save(YamlConfiguration.loadConfiguration(file), s);
        } else {
            if (!file.exists()) {
                SunBlastTierTagger.SunBlastPlugin().saveResource(s, false);
            }

            return YamlConfiguration.loadConfiguration(file);
        }
    }

    public static FileConfiguration save(FileConfiguration fileConfiguration, String s) {
        try {
            fileConfiguration.save(new File(SunBlastTierTagger.SunBlastPlugin().getDataFolder(), s));
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        return fileConfiguration;
    }
}
