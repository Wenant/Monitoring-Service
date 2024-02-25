package org.wenant;


import org.wenant.database.MigrationRunner;

public class App {

    public static void main(String[] args) {
        MigrationRunner.runMigrations();

    }
}