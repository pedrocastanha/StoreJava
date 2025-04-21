package com.unicesumar;

import com.unicesumar.entities.PaymentType;
import com.unicesumar.entities.Product;
import com.unicesumar.entities.Sale;
import com.unicesumar.entities.User;
import com.unicesumar.paymentMethods.PaymentManager;
import com.unicesumar.paymentMethods.PaymentMethod;
import com.unicesumar.paymentMethods.PaymentMethodFactory;
import com.unicesumar.repository.ProductRepository;
import com.unicesumar.repository.SaleRepository;
import com.unicesumar.repository.UserRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Main {
  public static void main(String[] args) {
    ProductRepository listaDeProdutos = null;
    UserRepository listaDeUsuarios = null;
    SaleRepository saleRepository = null;
    Connection conn = null;

    String url = "jdbc:sqlite:database.sqlite";

    try {
      conn = DriverManager.getConnection(url);
      if (conn != null) {
        DBUtils.initDatabase(conn);

        listaDeProdutos = new ProductRepository(conn);
        listaDeUsuarios = new UserRepository(conn);
        saleRepository = new SaleRepository(conn);
      } else {
        System.out.println("Falha na conexão.");
        System.exit(1);
      }
    } catch (SQLException e) {
      System.out.println("Erro ao conectar: " + e.getMessage());
      System.exit(1);
    }

    Scanner scanner = new Scanner(System.in);
    int option;

    do {
      System.out.println("\n---MENU---");
      System.out.println("1 - Cadastrar Produto");
      System.out.println("2 - Listar Produtos");
      System.out.println("3 - Cadastrar Usuário");
      System.out.println("4 - Listar Usuários");
      System.out.println("5 - Registrar Venda");
      System.out.println("6 - Sair");
      System.out.print("Escolha uma opção: ");
      option = scanner.nextInt();

      switch (option) {
        case 1:
          System.out.println("Cadastrar Produto");
          System.out.print("Digite o nome do produto: ");
          scanner.nextLine();
          String nomeProduto = scanner.nextLine();
          System.out.print("Digite o preço do produto: ");
          double precoProduto = scanner.nextDouble();
          Product novoProduto = new Product(nomeProduto, precoProduto);
          listaDeProdutos.save(novoProduto);
          System.out.println("Produto cadastrado com sucesso!");
          break;
        case 2:
          System.out.println("Listar Produtos");
          List<Product> products = listaDeProdutos.findAll();
          products.forEach(System.out::println);
          break;
        case 3:
          System.out.println("Cadastrar Usuário");
          listaDeUsuarios.save(new User("Maria da Silva", "maria@gmail.com", "senha123"));
          listaDeUsuarios.save(new User("João Pereira", "joao@gmail.com", "123456"));
          break;
        case 4:
          System.out.println("Listar Usuários");
          List<User> users = listaDeUsuarios.findAll();
          users.forEach(System.out::println);
          break;
        case 5:
          System.out.println("Registrar Venda");

          System.out.print("Digite o Email do usuário: ");
          String email = scanner.next();

          User user = listaDeUsuarios.findByEmail(email).orElse(null);
          if (user != null) {
            System.out.println("Usuário encontrado: " + user.getName());

            System.out.print("Digite os IDs dos produtos (separados por vírgula): ");
            String productIds = scanner.next();
            List<Product> productsToBuy = new ArrayList<>();

            for (String productId : productIds.split(",")) {
              Product product = listaDeProdutos.findById(UUID.fromString(productId)).orElse(null);
              if (product != null) {
                productsToBuy.add(product);
              }
            }

            System.out.println("Escolha a forma de pagamento: ");
            System.out.println("1 - Cartão de Crédito");
            System.out.println("2 - Boleto");
            System.out.println("3 - PIX");
            System.out.print("Opção: ");
            int paymentOption = scanner.nextInt();

            String paymentType = "";
            switch (paymentOption) {
              case 1:
                paymentType = "CARTAO";
                break;
              case 2:
                paymentType = "BOLETO";
                break;
              case 3:
                paymentType = "PIX";
                break;
              default:
                throw new IllegalArgumentException("Opção inválida");
            }

            double totalAmount = productsToBuy.stream().mapToDouble(Product::getPrice).sum();
            PaymentMethod paymentMethod =
                PaymentMethodFactory.create(PaymentType.valueOf(paymentType));
            PaymentManager paymentManager = new PaymentManager();
            paymentManager.setPaymentMethod(paymentMethod);
            paymentManager.pay(totalAmount);

            Sale sale =
                new Sale(
                    UUID.randomUUID(), user, productsToBuy, paymentType, new Date().toString());
            saleRepository.save(sale);

            System.out.println("=== Resumo da Venda ===");
            System.out.println("ID da Venda: " + sale.getUuid());
            System.out.println("Usuário: " + sale.getUser().getName());
            System.out.println("Produtos Comprados:");
            for (Product product : sale.getProducts()) {
              System.out.println(" - " + product.getName() + " | Preço: R$" + product.getPrice());
            }
            System.out.println("Total Pago: R$" + totalAmount);
            System.out.println("Método de Pagamento: " + paymentType);
            System.out.println("Data da Venda: " + sale.getSaleDate());
            System.out.println("=========================");
          } else {
            System.out.println("Usuário não encontrado.");
          }
          break;
        case 6:
          System.out.println("Saindo...");
          break;
        default:
          System.out.println("Opção inválida. Tente novamente.");
      }

    } while (option != 6);

    scanner.close();
    try {
      conn.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
