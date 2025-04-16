package com.unicesumar.paymentMethods;

public class PixPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Pagamento de R$" + amount + " realizado via PIX.");
    }
}
