package com.unicesumar;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class DBUtils {
    public static void initDatabase(Connection conn) {
        try {
            String initSQL = new String(Files.readAllBytes(Paths.get("scripts/init.sql")));
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(initSQL);
            stmt.close();
            System.out.println("✅ Banco de dados inicializado com sucesso.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar banco: " + e.getMessage(), e);
        }
    }
}
