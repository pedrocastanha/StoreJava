package com.unicesumar.entities;

import java.util.List;
import java.util.UUID;

public class Sale extends Entity {
    private final User user;
    private final List<Product> products;
    private final String paymentMethod;
    private final String saleDate;

    public Sale(UUID uuid, User user, List<Product> products, String paymentMethod, String saleDate) {
        super(uuid);
        this.user = user;
        this.products = products;
        this.paymentMethod = paymentMethod;
        this.saleDate = saleDate;
    }

    public User getUser() {
        return user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public String toString() {
        StringBuilder productsList = new StringBuilder();
        for (Product product : products) {
            productsList.append("- ").append(product.getName()).append("\n");
        }
        return String.format("Cliente: %s\nProdutos:\n%sPagamento: %s\nData da venda: %s",
                user.getName(), productsList.toString(), paymentMethod, saleDate);
    }
}
