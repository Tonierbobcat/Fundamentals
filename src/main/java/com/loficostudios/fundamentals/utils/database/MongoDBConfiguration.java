package com.loficostudios.fundamentals.utils.database;

import com.mongodb.ConnectionString;

public record MongoDBConfiguration(
        String username,
        String password,
        String database,
        int port,
        String hostname
) {
    public static final int DEFAULT_PORT = 27017;

    public MongoDBConfiguration(String username, String password, String database, int port, String hostname) {
        this.username = username != null ? username : "";
        this.password = password != null ? password : "";
        this.database = database;
        this.port = port;
        this.hostname = hostname;
    }

    public MongoDBConfiguration(String database, int port, String hostname) {
        this("", "", database, port, hostname);
    }

    public MongoDBConfiguration(String database, String hostname) {
        this("", "", database, DEFAULT_PORT, hostname);
    }

    public ConnectionString getConnectionString() {
        if (isNullOrEmpty(database)) {
            throw new IllegalArgumentException("You must specify a database name in the config");
        }
        if (isNullOrEmpty(hostname)) {
            throw new IllegalArgumentException("You must specify a hostname in the config");
        }

        String usernameAndPassword = "";
        if (!isNullOrEmpty(username)) {
            if (!isNullOrEmpty(password)) {
                usernameAndPassword = username + ":" + password + "@";
            } else {
                usernameAndPassword = username + "@";
            }
        }

        var base = !isNullOrEmpty(database)
                ? "/" + database
                : "";
        String string = port > 0
                ? "mongodb://" + usernameAndPassword + hostname + ":" + port + base
                : "mongodb://" + usernameAndPassword + hostname + base;
        return new ConnectionString(string);
    }

    private static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
