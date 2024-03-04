package org.wenant;


import org.wenant.database.MigrationRunner;

public class Migration {
    public static void main(String[] args) {
        MigrationRunner.runMigrations();

    }
}