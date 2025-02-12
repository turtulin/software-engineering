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
import it.unicam.cs.ids2425.utilities.wrappers.TypeToken;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@NoArgsConstructor
public class CartController implements IController {
    private final SingletonRepository<List<Cart>, Cart, Cart> cartRepository = SingletonRepository.getInstance(new TypeToken<>() {});

    private final CustomerController customerController = SingletonController.getInstance(new CustomerController() {});
    private final ArticleController articleController = SingletonController.getInstance(new ArticleController() {});

    public Cart addArticleToCart(@NonNull IArticle article, @NonNull IUser user) {
        article = articleController.get(article, ArticleStatus.PUBLISHED);
        IUser u = customerController.get(user, UserStatus.ACTIVE);
        if (u.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("User is not a customer");
        }
        Customer customer = (Customer) user;

        Cart userCart = customer.getCart();
        userCart = cartRepository.get(userCart);

        userCart.addArticle(article);
        userCart = cartRepository.save(userCart, userCart);

        return cartRepository.get(userCart);
    }

    public Cart removeArticleFromCart(@NonNull IArticle article, @NonNull IUser user) {
        article = articleController.get(article, ArticleStatus.PUBLISHED);
        user = customerController.get(user, UserStatus.ACTIVE);

        if (user.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("User is not a customer");
        }
        Customer c = (Customer) user;
        Cart cart = c.getCart();
        cart.removeArticle(article);
        cartRepository.save(cart, cart);
        return cartRepository.get(cart);
    }

    public Cart get(@NonNull Cart cart) {
        return cartRepository.get(cart);
    }

    public Cart create(@NonNull Customer c) {
        Cart cart = new Cart(c);
        if (c.getCart() != null) {
            Cart userCart = cartRepository.get(c.getCart());
            if (userCart != null) {
                throw new IllegalArgumentException("Cart already exists");
            }
            cart = c.getCart();
        }
        cartRepository.create(cart);
        return cartRepository.get(cart);
    }

    public void empty(@NonNull Cart cart) {
        cart = cartRepository.get(cart);
        cart.empty();
        cartRepository.save(cart, cart);
    }
}
