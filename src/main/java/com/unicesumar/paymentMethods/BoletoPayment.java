package com.unicesumar.paymentMethods;

public class BoletoPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Pagamento de R$" + amount + " gerado via Boleto.");
    }
}
