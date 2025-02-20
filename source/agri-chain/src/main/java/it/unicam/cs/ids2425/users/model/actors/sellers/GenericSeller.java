package it.unicam.cs.ids2425.users.model.actors.sellers;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.model.stocks.GenericStock;
import it.unicam.cs.ids2425.eshop.model.stocks.SellerStock;
import it.unicam.cs.ids2425.users.model.GenericUser;
import it.unicam.cs.ids2425.users.model.details.addresses.Address;
import it.unicam.cs.ids2425.users.model.details.payments.IPaymentMethod;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"stock", "paymentMethods", "addresses"})
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public sealed abstract class GenericSeller extends GenericUser implements ISeller permits Producer, Transformer, Distributor, EventPlanner {
    protected SellerStock<? extends ISeller, ? extends IArticle> stock;
    private List<IPaymentMethod> paymentMethods;
    private List<Address> addresses;

    public GenericSeller(String username, String password) {
        super(username, password);
        paymentMethods = List.of();
        addresses = List.of();
    }

    @Override
    public void setStock(GenericStock<?, ?> stock) {
        this.stock = (SellerStock<? extends ISeller, ? extends IArticle>) stock;
    }
}


