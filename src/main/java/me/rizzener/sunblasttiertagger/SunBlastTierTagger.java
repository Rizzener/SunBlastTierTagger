package me.rizzener.sunblasttiertagger;

import me.rizzener.sunblasttiertagger.commands.CommandManager;
import me.rizzener.sunblasttiertagger.commands.CommandMenu;
import me.rizzener.sunblasttiertagger.commands.CommandReload;
import me.rizzener.sunblasttiertagger.commands.CommandTiers;
import me.rizzener.sunblasttiertagger.menu.Menu;
import me.rizzener.sunblasttiertagger.placeholders.TierPlaceholder;
import me.rizzener.sunblasttiertagger.tools.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class SunBlastTierTagger extends JavaPlugin {
    public static me.rizzener.sunblasttiertagger.SunBlastTierTagger SunBlastPlugin;
    private static CommandManager commandManager;
    private File dataFile;
    private FileConfiguration dataConfig;
    public static me.rizzener.sunblasttiertagger.SunBlastTierTagger SunBlastPlugin() {
        return SunBlastPlugin;
    }

    @Override
    public void onEnable() {
        createDataFile();
        this.saveDefaultConfig();
        new TierPlaceholder(this).register();
        SunBlastPlugin = this;
        commandManager = new CommandManager();
        commandManager.register(new CommandTiers(this));
        commandManager.register(new CommandMenu(this));
        commandManager.register(new CommandReload(this));
        this.getCommand("tiers").setExecutor(commandManager);
        Bukkit.getPluginManager().registerEvents(new Menu(this), this);
        Utils.onEnableMessage();
        Utils.startUpdater();
    }



    private void createDataFile() {
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public FileConfiguration getDataConfig() {
        return dataConfig;
    }

    public void saveDataConfig() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
