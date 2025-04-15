package com.unicesumar.repository;

import com.unicesumar.entities.Sale;
import com.unicesumar.entities.Product;
import com.unicesumar.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SaleRepository implements EntityRepository<Sale> {
    private final Connection connection;

    public SaleRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Sale sale) {
        // Inserir a venda na tabela sales
        String query = "INSERT INTO sales (id, user_id, payment_method, sale_date) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            stmt.setString(1, sale.getUuid().toString());
            stmt.setString(2, sale.getUser().getUuid().toString());
            stmt.setString(3, sale.getPaymentMethod());
            stmt.setString(4, sale.getSaleDate());
            stmt.executeUpdate();

            // Associar os produtos à venda na tabela sale_products
            for (Product product : sale.getProducts()) {
                String productQuery = "INSERT INTO sale_products (sale_id, product_id) VALUES (?, ?)";
                PreparedStatement productStmt = this.connection.prepareStatement(productQuery);
                productStmt.setString(1, sale.getUuid().toString());
                productStmt.setString(2, product.getUuid().toString());
                productStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar a venda: " + e.getMessage());
        }
    }

    @Override
    public Optional<Sale> findById(UUID id) {
        return Optional.empty(); // Implementar consulta se necessário
    }

    @Override
    public List<Sale> findAll() {
        return null; // Implementar consulta se necessário
    }

    @Override
    public void deleteById(UUID id) {
        // Implementar exclusão de vendas se necessário
    }
}
