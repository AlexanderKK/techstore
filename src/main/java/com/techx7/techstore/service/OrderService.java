package com.techx7.techstore.service;

import com.techx7.techstore.model.dto.order.OrderDTO;
import com.techx7.techstore.model.session.TechStoreUserDetails;

import java.security.Principal;

public interface OrderService {

    OrderDTO getCheckout(TechStoreUserDetails loggedUser, Principal principal);

    String placeOrder(OrderDTO orderDTO, TechStoreUserDetails loggedUser);

}
