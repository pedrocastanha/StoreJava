package com.unicesumar.paymentMethods;

public class PaymentMethodFactory {
    public static PaymentMethod create(PaymentType type) {
        switch (type) {
            case CARTAO:
                return new CartaoPayment();
            case BOLETO:
                return new BoletoPayment();
            case PIX:
                return new PixPayment();
            default:
                throw new IllegalArgumentException("Tipo de pagamento inválido");
        }
    }
}
