package it.unicam.cs.ids2425.model.eshop.order;

import it.unicam.cs.ids2425.model.eshop.stock.Stock;
import it.unicam.cs.ids2425.model.user.detail.address.Address;
import it.unicam.cs.ids2425.model.user.detail.payment.AbstractPaymentMethod;
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
    @OneToOne(cascade = CascadeType.ALL)
    private Address shippingAddress;
    @OneToOne(cascade = CascadeType.ALL)
    private Address billingAddress;
    @OneToOne(cascade = CascadeType.ALL)
    private AbstractPaymentMethod payment;
    private String trackingNumber;
}
