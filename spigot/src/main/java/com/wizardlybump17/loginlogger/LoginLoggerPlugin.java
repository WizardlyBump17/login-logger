package com.wizardlybump17.loginlogger;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wizardlybump17.loginlogger.api.LoginSessionAPI;
import com.wizardlybump17.loginlogger.api.persister.InstantType;
import com.wizardlybump17.loginlogger.api.session.LoginSession;
import com.wizardlybump17.loginlogger.api.storage.sql.LoginSessionDAO;
import com.wizardlybump17.loginlogger.config.YamlLoginLoggerConfig;
import com.wizardlybump17.loginlogger.listener.LoginListener;
import com.wizardlybump17.loginlogger.task.BukkitLoginSessionEndFallbackTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

public final class LoginLoggerPlugin extends JavaPlugin {

    private ConnectionSource connectionSource;

    @Override
    public void onEnable() {
        try {
            initDatabase();
        } catch (IOException | InvalidConfigurationException | SQLException e) {
            getLogger().log(Level.SEVERE, "Error while initializing the database.", e);
            disable();
            return;
        }

        registerListeners();

        try {
            initConfig();
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().log(Level.SEVERE, "Error while loading the configuration.", e);
            disable();
            return;
        }

        initTasks();
    }

    private void initDatabase() throws IOException, InvalidConfigurationException, SQLException {
        YamlConfiguration configuration = new YamlConfiguration();
        saveResource("database.yml", false);
        configuration.load(new File(getDataFolder(), "database.yml"));

        connectionSource = new JdbcPooledConnectionSource(
                configuration.getString("url"),
                configuration.getString("username"),
                configuration.getString("password")
        );
        TableUtils.createTableIfNotExists(connectionSource, LoginSession.class);
        DataPersisterManager.registerDataPersisters(InstantType.getSingleton());

        LoginSessionDAO dao = DaoManager.lookupDao(connectionSource, LoginSession.class);
        LoginSessionAPI.setLoginSessionStorage(dao.getStorage());
    }

    private void registerListeners() {
        new LoginListener(this).register();
    }

    private void initConfig() throws IOException, InvalidConfigurationException {
        YamlConfiguration configuration = new YamlConfiguration();
        File file = new File(getDataFolder(), "config.yml");
        saveResource("config.yml", false);
        configuration.load(file);
        LoginSessionAPI.setConfig(new YamlLoginLoggerConfig(configuration, file));
    }

    private void initTasks() {
        LoginSessionAPI.setEndFallbackTask(new BukkitLoginSessionEndFallbackTask(getLogger()));
        int delay = LoginSessionAPI.getConfig().getEndFallbackTaskDelay();
        Bukkit.getScheduler().runTaskTimer(this, () -> LoginSessionAPI.getEndFallbackTask().run(), delay, delay);
    }

    private void disable() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Error while closing the connection.", e);
            }
        }

        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
    }

    public static @NotNull LoginLoggerPlugin getInstance() {
        return getPlugin(LoginLoggerPlugin.class);
    }
}
