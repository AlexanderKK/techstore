package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.CartItemRepository;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static com.techx7.techstore.constant.Messages.USER_NOT_LOGGED;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<CartItem> getCartItems(Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        return cartItemRepository.findAllByUser(user)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Cart items")));
    }

    @Override
    public CartItem addProductToCart(Integer quantity, UUID productUuid, Principal principal) {
        if(principal == null) {
            return null;
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        return addProduct(user, productUuid, quantity);
    }

    @Override
    @Transactional
    public BigDecimal updateQuantity(Integer quantity, UUID productId, Principal principal) {
        Product product = productRepository.findByUuid(productId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        cartItemRepository.updateQuantity(quantity, user.getUuid(), productId);

        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        return subtotal;
    }

    private CartItem addProduct(User user, UUID productUuid, Integer quantity) {
        Product product = productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);

        Integer addedQuantity = quantity;

        if(cartItem == null) {
            cartItem = new CartItem(user, product, quantity);
        } else {
            addedQuantity = cartItem.getQuantity() + quantity;

            cartItem.setQuantity(addedQuantity);
        }

        return cartItemRepository.save(cartItem);
    }

}
