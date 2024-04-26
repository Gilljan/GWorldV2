package de.gilljan.gworld.data;

import de.gilljan.gworld.data.mysql.MySQL;

public class Database implements DataHandler {
    private final MySQL mySQL;

    public Database(MySQL mySQL) {
        this.mySQL = mySQL;
    }

}
