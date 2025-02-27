package it.unicam.cs.ids2425.eshop.model.order;

import it.unicam.cs.ids2425.eshop.model.stock.Stock;
import it.unicam.cs.ids2425.user.model.detail.address.Address;
import it.unicam.cs.ids2425.user.model.detail.payment.AbstractPaymentMethod;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Stock stock;
    @OneToOne
    private Address shippingAddress;
    @OneToOne
    private Address billingAddress;
    @OneToOne
    private AbstractPaymentMethod payment;
    private String trackingNumber;
}
