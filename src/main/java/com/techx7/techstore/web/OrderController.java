package com.techx7.techstore.web;

import com.techx7.techstore.model.dto.cart.CartItemDTO;
import com.techx7.techstore.model.dto.country.CountryDTO;
import com.techx7.techstore.model.dto.order.OrderDTO;
import com.techx7.techstore.model.session.TechStoreUserDetails;
import com.techx7.techstore.service.CartService;
import com.techx7.techstore.service.CountryService;
import com.techx7.techstore.service.OrderService;
import com.techx7.techstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final UserService userService;
    private final CountryService countryService;
    private final CartService cartService;
    private final OrderService orderService;

    @Autowired
    public OrderController(UserService userService,
                           CountryService countryService,
                           CartService cartService,
                           OrderService orderService) {
        this.userService = userService;
        this.countryService = countryService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model,
                           @AuthenticationPrincipal TechStoreUserDetails loggedUser,
                           Principal principal) {
        List<CountryDTO> countryDTOs = countryService.getAllCountries();

        model.addAttribute("countries", countryDTOs);

        List<CartItemDTO> cartItems = cartService.getCartItems(principal);

        model.addAttribute("cartItems", cartItems);

        OrderDTO orderDTO = orderService.getCheckout(loggedUser, principal);

        model.addAttribute("orderDTO", orderDTO);

        return "checkout";
    }

}
