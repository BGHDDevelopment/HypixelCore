package net.bghd.hypixel.core.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.bghd.hypixel.core.Core;
import net.bghd.hypixel.core.Manager;
import net.bghd.hypixel.core.util.Color;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLManager extends Manager {

    private HikariDataSource hikariDataSource;


    public MySQLManager(Core plugin) {
        super(plugin);
        connect();
        Color.log("Enabled MySQLManager!");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> createTables());
    }

    public void shutdown() {
        close();
        Color.log("MySQLManager has been disabled!");
    }

    public void createTables() {
        Color.log("Creating tables...");
        createTable("player", "uuid VARCHAR(36) NOT NULL PRIMARY KEY, name VARCHAR(16), gold INT(11), network_level INT(11), exp INT(11), rank VARCHAR(20)");
        Color.log("Tables created.");
    }

    public Exception connect() {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:mysql://" + "localhost" + ":" + "3306" + "/" + "HypixelServer");
            hikariConfig.setUsername("Server");
            hikariConfig.setPassword("HypixelServer");
            hikariConfig.setMinimumIdle(5);
            hikariConfig.setMaximumPoolSize(50);
            hikariConfig.setConnectionTimeout(30000);
            hikariConfig.setLeakDetectionThreshold(60 * 1000);
            hikariConfig.setIdleTimeout(600000);
            hikariConfig.setMaxLifetime(1800000);
            hikariDataSource = new HikariDataSource(hikariConfig);
        }  catch (Exception exception) {
            hikariDataSource = null;
            return exception;
        }
        return null;
    }


    public boolean isInitiated() {
        return hikariDataSource != null;
    }

    public void close() {
        this.hikariDataSource.close();
    }

    /**
     * @return A new database connecting, provided by the Hikari pool.
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    /**
     * Create a new table in the database.
     *
     * @param name The name of the table.
     * @param info The table info between the round VALUES() brackets.
     */
    public void createTable(String name, String info) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + "(" + info + ");")) {
                statement.execute();
            } catch (SQLException exception) {
                Color.log("An error occurred while creating database table " + name + ".");
                exception.printStackTrace();
            }
        }).start();
    }

    /**
     * Execute an update to the database.
     *
     * @param query  The statement to the database.
     * @param values The values to be inserted into the statement.
     */
    public void execute(String query, Object... values) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i + 1), values[i]);
                }
                statement.execute();
            } catch (SQLException exception) {
                Color.log("An error occurred while executing an update on the database.");
                Color.log("MySQL#execute : " + query);
                exception.printStackTrace();
            }
        }).start();
    }

    /**
     * Execute a query to the database.
     *
     * @param query    The statement to the database.
     * @param callback The data callback (Async).
     * @param values   The values to be inserted into the statement.
     */
    public void select(String query, SelectCall callback, Object... values) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i + 1), values[i]);
                }
                callback.call(statement.executeQuery());
            } catch (SQLException exception) {
                Color.log("An error occurred while executing a query on the database.");
                Color.log("MySQL#select : " + query);
                exception.printStackTrace();
            }
        }).start();
    }


}
