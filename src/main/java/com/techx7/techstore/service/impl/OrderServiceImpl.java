package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.order.OrderDTO;
import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserInfo;
import com.techx7.techstore.model.session.TechStoreUserDetails;
import com.techx7.techstore.repository.CartItemRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.stream.Collectors;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final CartItemRepository cartItemRepository;

    public OrderServiceImpl(ModelMapper mapper,
                            UserRepository userRepository,
                            CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public OrderDTO getCheckout(TechStoreUserDetails loggedUser, Principal principal) {
        User user = userRepository.findByUsername(loggedUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        UserInfo userInfo = user.getUserInfo();

        OrderDTO orderDTO = new OrderDTO();
        if(userInfo != null) {
            orderDTO = mapper.map(userInfo, OrderDTO.class);
        }

        String cartItemsIds = cartItemRepository.findAllByUser(user)
                .stream()
                .mapToLong(CartItem::getId)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        orderDTO.setCartItems(cartItemsIds);

        return orderDTO;
    }

}
