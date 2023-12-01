package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.exception.ProductQuantityException;
import com.techx7.techstore.model.dto.cart.CartItemDTO;
import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.CartItemRepository;
import com.techx7.techstore.repository.ProductRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.CartService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static com.techx7.techstore.constant.Messages.*;
import static com.techx7.techstore.utils.PriceUtils.formatPrice;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    @Autowired
    public CartServiceImpl(CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           ModelMapper mapper) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CartItemDTO> getCartItems(Principal principal) {
        if(principal == null) {
            return null;
        }

        User user = userRepository.findByUsername(principal.getName())
                        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        return cartItemRepository.findAllByUser(user).stream()
                .map(cartItem -> mapper.map(cartItem, CartItemDTO.class))
                .toList();
    }

    @Override
    public CartItemDTO addProductToCart(Integer quantity, UUID productUuid, Principal principal) {
        throwOnMissingPrincipal(principal);

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        return addProduct(user, productUuid, quantity);
    }

    @Override
    @Transactional
    public BigDecimal updateQuantity(Integer quantity, UUID productUuid, Principal principal) {
        throwOnMissingPrincipal(principal);

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        Product product = productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        if(cartItem == null) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Cart item"));
        }

        updateAvailableQuantity(product, quantity);

        cartItemRepository.updateQuantity(quantity, user.getUuid(), productUuid);

        // Calculate subtotal with price or discountPrice
        return getSubtotal(quantity, product);
    }

    private static BigDecimal getSubtotal(Integer quantity, Product product) {
        BigDecimal subtotal;

        BigDecimal bigDecimalQuantity = BigDecimal.valueOf(quantity);

        if(product.getDiscountPrice() != null && product.getDiscountPrice().compareTo(BigDecimal.ZERO) >= 0) {
            subtotal = product.getDiscountPrice().multiply(bigDecimalQuantity);
        } else {
            subtotal = product.getPrice().multiply(bigDecimalQuantity);
        }

        return formatPrice(subtotal);
    }

    @Override
    @Transactional
    public void removeProduct(UUID productUuid, Principal principal) {
        throwOnMissingPrincipal(principal);

        Product product = productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        if(cartItem == null) {
            throw new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Cart item"));
        }

        increaseAvailableQuantity(product, cartItem);

        cartItemRepository.deleteByProductAndUser(product, user);
    }

    private static void throwOnMissingPrincipal(Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }
    }

    private CartItemDTO addProduct(User user, UUID productUuid, Integer quantity) {
        Product product = productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);

        Integer addedQuantity;

        if(cartItem == null) {
            cartItem = new CartItem(user, product, quantity);

            addedQuantity = quantity;
        } else {
            addedQuantity = cartItem.getQuantity() + quantity;
        }

        decreaseAvailableQuantity(product, quantity);

        cartItem.setQuantity(addedQuantity);

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        return mapper.map(savedCartItem, CartItemDTO.class);
    }

    private void decreaseAvailableQuantity(Product product, Integer addedQuantity) {
        Integer productAvailableQuantity = product.getAvailableQuantity();

        if(addedQuantity > productAvailableQuantity) {
            throw new ProductQuantityException(QUANTITY_CAPACITY_SURPASSED, productAvailableQuantity);
        }

        productAvailableQuantity -= addedQuantity;

        product.setAvailableQuantity(productAvailableQuantity);

        productRepository.save(product);
    }

    private void updateAvailableQuantity(Product product, Integer quantity) {
        Integer productAvailableQuantity = product.getInitialQuantity();

        if(quantity > productAvailableQuantity) {
            throw new ProductQuantityException(QUANTITY_CAPACITY_SURPASSED, productAvailableQuantity);
        }

        productAvailableQuantity -= quantity;

        product.setAvailableQuantity(productAvailableQuantity);

        productRepository.save(product);
    }

    private void increaseAvailableQuantity(Product product, CartItem cartItem) {
        Integer cartItemQuantity = cartItem.getQuantity();

        Integer productAvailableQuantity = product.getAvailableQuantity();

        productAvailableQuantity += cartItemQuantity;

        product.setAvailableQuantity(productAvailableQuantity);

        productRepository.save(product);
    }

}
