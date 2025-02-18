package it.unicam.cs.ids2425.eshop.controller;

import it.unicam.cs.ids2425.articles.controller.ArticleController;
import it.unicam.cs.ids2425.articles.model.articles.IArticle;
import it.unicam.cs.ids2425.eshop.model.Cart;
import it.unicam.cs.ids2425.users.controller.actors.CustomerController;
import it.unicam.cs.ids2425.users.model.IUser;
import it.unicam.cs.ids2425.users.model.UserRole;
import it.unicam.cs.ids2425.users.model.actors.Customer;
import it.unicam.cs.ids2425.utilities.controllers.IController;
import it.unicam.cs.ids2425.utilities.controllers.SingletonController;
import it.unicam.cs.ids2425.utilities.repositories.SingletonRepository;
import it.unicam.cs.ids2425.utilities.statuses.ArticleStatus;
import it.unicam.cs.ids2425.utilities.statuses.UserStatus;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.NoSuchElementException;

@NoArgsConstructor
public class CartController implements IController {
    private final SingletonRepository<Cart> cartRepository = SingletonRepository.getInstance(Cart.class);

    private final CustomerController customerController = SingletonController.getInstance(new CustomerController() {
    });
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController() {
    });

    private Cart getCustomerCart(@NonNull IUser user) {
        IUser u = customerController.get(user, UserStatus.ACTIVE);
        if (u.getRole() != UserRole.CUSTOMER) {
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
        article = articleController.get(article, ArticleStatus.PUBLISHED);

        cart.addArticle(article);
        cartRepository.save(cart);
        return getCart(cart);
    }

    public Cart removeArticleFromCart(@NonNull IArticle article, @NonNull IUser user) {
        Cart cart = getCustomerCart(user);
        article = articleController.get(article, ArticleStatus.PUBLISHED);

        cart.removeArticle(article);
        cartRepository.save(cart);
        return getCart(cart);
    }

    public Cart get(@NonNull Cart cart) {
        return getCart(cart);
    }

    public Cart create(@NonNull Customer c) {
        Cart cart = new Cart(c);
        if (c.getCart() != null) {
            Cart userCart = cartRepository.findById(c.getCart()).orElse(null);
            if (userCart != null) {
                throw new IllegalArgumentException("Cart already exists");
            }
            cart = c.getCart();
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
