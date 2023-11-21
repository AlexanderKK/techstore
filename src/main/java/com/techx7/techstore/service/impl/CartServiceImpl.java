package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.CartItemRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;
import static com.techx7.techstore.constant.Messages.USER_NOT_LOGGED;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartItemRepository cartItemRepository,
                           UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CartItem> getCartItems(Principal principal) {
        if(principal == null) {
            throw new PrincipalNotFoundException(USER_NOT_LOGGED);
        }

        User user = userRepository.findByUsername(principal.getName())
                        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        return cartItemRepository.findAllByUser(user);
    }

}
