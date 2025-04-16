package com.unicesumar.paymentMethods;

public class PaymentManager {
    private PaymentMethod paymentMethod;

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void pay(double amount) {
        if (paymentMethod != null) {
            paymentMethod.pay(amount);
        } else {
            throw new IllegalStateException("Método de pagamento não definido.");
        }
    }
}
