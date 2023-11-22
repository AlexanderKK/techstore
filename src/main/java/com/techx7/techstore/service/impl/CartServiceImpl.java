package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PrincipalNotFoundException;
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

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static com.techx7.techstore.constant.Messages.USER_NOT_LOGGED;

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
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        return cartItemRepository.findAllByUser(user).stream()
                .map(cartItem -> mapper.map(cartItem, CartItemDTO.class))
                .toList();
    }

    @Override
    public CartItemDTO addProductToCart(Integer quantity, UUID productUuid, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        return addProduct(user, productUuid, quantity);
    }

    @Override
    @Transactional
    public BigDecimal updateQuantity(Integer quantity, UUID productUuid, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        Product product = productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));

        cartItemRepository.updateQuantity(quantity, user.getUuid(), productUuid);

        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        return subtotal;
    }

    @Override
    @Transactional
    public void removeProduct(UUID productUuid, Principal principal) {
        Product product = productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        cartItemRepository.deleteByProductAndUser(product, user);
    }

    private CartItemDTO addProduct(User user, UUID productUuid, Integer quantity) {
        Integer addedQuantity;

        Product product = productRepository.findByUuid(productUuid)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Product")));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);

        if(cartItem == null) {
            cartItem = new CartItem(user, product, quantity);
        } else {
            addedQuantity = cartItem.getQuantity() + quantity;

            cartItem.setQuantity(addedQuantity);
        }

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        return mapper.map(savedCartItem, CartItemDTO.class);
    }

}
