package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.CartRepository;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.CartService;
import com.techx7.techstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static com.techx7.techstore.constant.Messages.USER_NOT_LOGGED;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           UserRepository userRepository,
                           ProductService productService,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Override
    public List<CartItem> getCartItems(Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        return cartRepository.findAllByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Cart items")));
    }

    @Override
    public String addProductToCart(Principal principal, UUID uuid, Integer quantity) {
        if(principal == null) {
            return "Login to add this product";
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        Integer addedQuantity = addProduct(user, uuid, quantity);

        return addedQuantity + " item(s) of this product added to your shopping cart";
    }

    private Integer addProduct(User user, UUID productUuid, Integer quantity) {
        Product product = productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));

        CartItem cartItem = cartRepository.findByUserAndProduct(user, product);

        Integer addedQuantity = quantity;

        if(cartItem == null) {
            cartItem = new CartItem(user, product, quantity);
        } else {
            addedQuantity = cartItem.getQuantity() + quantity;

            cartItem.setQuantity(addedQuantity);
        }

        cartRepository.save(cartItem);

        return addedQuantity;
    }

}
