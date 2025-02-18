package it.unicam.cs.ids2425.eshop.controller;

import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.model.Cart;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.actors.Customer;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.NoSuchElementException;

@NoArgsConstructor
public class CartController implements IController {
    private final SingletonRepository<Cart> cartRepository = SingletonRepository.getInstance(Cart.class);

    private Cart getCustomerCart(@NonNull IUser user) {
        if (user.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("User is not a customer");
        }
        Customer customer = (Customer) user;
        return getCart(customer.getCart());
    }

    private Cart getCart(Cart cart) {
        return cartRepository.findById(cart).orElseThrow(() -> new NoSuchElementException("Cart not found"));
    }

    public Cart addArticleToCart(@NonNull IArticle article, @NonNull IUser user) {
        Cart cart = getCustomerCart(user);

        cart.addArticle(article);
        cartRepository.save(cart);
        return getCart(cart);
    }

    public Cart removeArticleFromCart(@NonNull IArticle article, @NonNull IUser user) {
        Cart cart = getCustomerCart(user);

        cart.removeArticle(article);
        cartRepository.save(cart);
        return getCart(cart);
    }

    public Cart get(@NonNull Cart cart) {
        return getCart(cart);
    }

    public Cart create(@NonNull Customer customer) {
        Cart cart = new Cart(customer);
        if (customer.getCart() != null) {
            Cart userCart = cartRepository.findById(customer.getCart()).orElse(null);
            if (userCart != null) {
                throw new IllegalArgumentException("Cart already exists");
            }
            cart = customer.getCart();
        }
        cartRepository.save(cart);
        return getCart(cart);
    }

    public void empty(@NonNull Cart cart) {
        cart = getCart(cart);
        cart.empty();
        cartRepository.save(cart);
    }
}
