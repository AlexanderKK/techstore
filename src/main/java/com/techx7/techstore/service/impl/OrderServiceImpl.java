package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.model.dto.order.OrderDTO;
import com.techx7.techstore.model.entity.*;
import com.techx7.techstore.model.session.TechStoreUserDetails;
import com.techx7.techstore.repository.*;
import com.techx7.techstore.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final CartItemRepository cartItemRepository;
    private final CountryRepository countryRepository;
    private final OrderRepository orderRepository;
    private final UserInfoRepository userInfoRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceImpl(ModelMapper mapper,
                            UserRepository userRepository,
                            CartItemRepository cartItemRepository,
                            CountryRepository countryRepository,
                            OrderRepository orderRepository,
                            UserInfoRepository userInfoRepository,
                            OrderItemRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.cartItemRepository = cartItemRepository;
        this.countryRepository = countryRepository;
        this.orderRepository = orderRepository;
        this.userInfoRepository = userInfoRepository;
        this.orderItemRepository = orderItemRepository;
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

    @Override
    public void placeOrder(OrderDTO orderDTO, TechStoreUserDetails loggedUser) {
        User user = userRepository.findByUsername(loggedUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        UserInfo userInfo = user.getUserInfo();
        if(userInfo == null) {
            userInfo = getUserInfo(orderDTO);

            userInfoRepository.save(userInfo);
        } else {
            editUserInfo(userInfo, orderDTO);
        }

        user.setUserInfo(userInfo);

        userRepository.save(user);

        List<CartItem> cartItems = getCartItems(orderDTO);

        List<OrderItem> orderItems = getOrderItems(cartItems);

        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setPayment(orderDTO.getPayment());

        orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);
    }

    private List<OrderItem> getOrderItems(List<CartItem> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(
                    cartItem.getUser(), cartItem.getProduct(), cartItem.getQuantity()
            );

            orderItems.add(orderItem);
        }

        return orderItemRepository.saveAll(orderItems);
    }

    private List<CartItem> getCartItems(OrderDTO orderDTO) {
        return Arrays.stream(orderDTO.getCartItems()
                .split(","))
                .mapToLong(Long::parseLong)
                .mapToObj(id ->
                        cartItemRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "Cart item")))
                )
                .toList();
    }

    private void editUserInfo(UserInfo userInfo, OrderDTO orderDTO) {
        changeUserInfo(userInfo, orderDTO);
    }

    private UserInfo getUserInfo(OrderDTO orderDTO) {
        UserInfo userInfo = new UserInfo();

        changeUserInfo(userInfo, orderDTO);

        return userInfo;
    }

    private void changeUserInfo(UserInfo userInfo, OrderDTO orderDTO) {
        userInfo.setFirstName(orderDTO.getFirstName());
        userInfo.setLastName(orderDTO.getLastName());
        userInfo.setPhoneNumber(orderDTO.getPhoneNumber());
        userInfo.setAddress(orderDTO.getAddress());
        userInfo.setCountry(countryRepository.findByName(orderDTO.getCountry()).orElse(null));
        userInfo.setCity(orderDTO.getCity());
        userInfo.setState(orderDTO.getState());
        userInfo.setZipCode(orderDTO.getZipCode());
    }

}
