package de.gilljan.gworld.data.mysql;

import de.gilljan.gworld.GWorld;

import java.sql.*;
import java.util.logging.Level;

public class MySQL {
    private Connection con;
    private final GWorld plugin;
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final int port;

    public MySQL(final GWorld plugin, final String host, final String database, final String username, final String password, final int port) {
        this.plugin = plugin;
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
                plugin.getLogger().info("[" + plugin.getName() + "] MySQL-Connection established!");
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "[" + plugin.getName() + "] Failed to connect to MySQL-Database!", ex);
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                plugin.getLogger().info("[" + plugin.getName() + "] Disconnected MySQL-Database!");
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "[" + plugin.getName() + "] Failure during disconnecting from the MySQL-Database!", ex);
            }
        }
    }

    public boolean isConnected() {
        return con != null;
    }

    public boolean isConnectionClosed() throws SQLException {
        return con.isClosed();
    }

    public boolean update(String query) {
        if (isConnected()) {
            try {
                con.createStatement().executeQuery("SELECT 1");
            } catch (SQLException ignored) {}
            try {
                con.createStatement().executeUpdate(query);
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "[" + plugin.getName() + "] Failed to execute update: " + query, ex);
            }
        }
        return false;
    }

    public ResultSet getResult(String query) {
        if (isConnected()) {
            try {
                con.createStatement().executeQuery("SELECT 1");
            } catch (SQLException ignored) {}
            try {
                return con.createStatement().executeQuery(query);
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "[" + plugin.getName() + "] Failed to execute query: " + query, ex);
            }
        }
        return null;
    }

    public PreparedStatement prepareStatement(String query) {
        if (isConnected()) {
            try {
                con.createStatement().executeQuery("SELECT 1");
            } catch (SQLException ignored) {}
            try {
                return con.prepareStatement(query);
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "[" + plugin.getName() + "] Failed to prepare statement: " + query, ex);
            }
        }
        return null;
    }

    public Connection getCon() {
        return con;
    }

    public GWorld getPlugin() {
        return plugin;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }
}
