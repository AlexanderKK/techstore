package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.PrincipalNotFoundException;
import com.techx7.techstore.model.dto.cart.CartItemDTO;
import com.techx7.techstore.model.entity.*;
import com.techx7.techstore.repository.*;
import com.techx7.techstore.service.CartService;
import com.techx7.techstore.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CartServiceImplTestIT {

    @Autowired
    private CartService cartService;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestData testData;

    @Autowired
    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        cartItemRepository.deleteAll();

        productRepository.deleteAll();

        categoryRepository.deleteAll();

        modelRepository.deleteAll();

        manufacturerRepository.deleteAll();

        testData.cleanAllTestData();
    }

    @AfterEach
    void tearDown() {
        cartItemRepository.deleteAll();

        productRepository.deleteAll();

        categoryRepository.deleteAll();

        modelRepository.deleteAll();

        manufacturerRepository.deleteAll();

        testData.cleanAllTestData();
    }

    @Test
    void testGetCartItemsPrincipalNotFound() {
        assertNull(cartService.getCartItems(null));
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void testGetCartItemsUserNotFound() {
        Principal principal = () -> "TestUser";

        assertThrows(
                EntityNotFoundException.class,
                () -> cartService.getCartItems(principal)
        );
    }

    @Test
    @WithMockUser(value = "TestUser")
    void testGetCartItems() {
        assertEquals(0, cartItemRepository.count());

        Principal principal = () -> "TestUser";

        createAndSaveCartItem();

        List<CartItemDTO> cartItems = cartService.getCartItems(principal);

        assertFalse(cartItems.isEmpty());

        assertEquals(1, cartItemRepository.count());
    }

    @Test
    void testAddProductToCartPrincipalNotFound() {
        Product product = createAndSaveProduct();

        assertThrows(
                PrincipalNotFoundException.class,
                () -> cartService.addProductToCart(1, product.getUuid(), null)
        );
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void testAddProductToCartUserNotFound() {
        Product product = createAndSaveProduct();

        Principal principal = () -> "TestUser";

        assertThrows(
                EntityNotFoundException.class,
                () -> cartService.addProductToCart(1, product.getUuid(), principal)
        );
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void testAddProductToCart() {
        Principal principal = () -> "TestUser";

        assertEquals(0, cartItemRepository.count());

        CartItem cartItem = createAndSaveCartItem();
        UUID productUuid = cartItem.getProduct().getUuid();

        CartItemDTO cartItemDTO = cartService.addProductToCart(1, productUuid, principal);

        assertEquals(1, cartItemRepository.count());

        assertEquals(cartItem.getProduct().getUuid(), cartItemDTO.getProductDTO().getUuid());
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void testUpdateQuantityPrincipalNotFound() {
        Product product = createAndSaveProduct();

        assertThrows(
                PrincipalNotFoundException.class,
                () -> cartService.updateQuantity(1, product.getUuid(), null)
        );
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void testUpdateQuantityUserNotFound() {
        Product product = createAndSaveProduct();

        Principal principal = () -> "TestUser";

        assertThrows(
                EntityNotFoundException.class,
                () -> cartService.updateQuantity(1, product.getUuid(), principal)
        );
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void testUpdateQuantity() {
        Principal principal = () -> "TestUser";

        assertEquals(0, cartItemRepository.count());

        CartItem cartItem = createAndSaveCartItem();
        UUID productUuid = cartItem.getProduct().getUuid();

        BigDecimal subtotal = cartService.updateQuantity(1, productUuid, principal);

        assertEquals(1, cartItemRepository.count());

        assertTrue(subtotal.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void testRemoveProductPrincipalNotFound() {
        Product product = createAndSaveProduct();

        assertThrows(
                PrincipalNotFoundException.class,
                () -> cartService.removeProduct(product.getUuid(), null)
        );
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void testRemoveProductUserNotFound() {
        Product product = createAndSaveProduct();

        Principal principal = () -> "TestUser";

        assertThrows(
                EntityNotFoundException.class,
                () -> cartService.removeProduct(product.getUuid(), principal)
        );
    }

    @Test
    @WithMockUser(username = "TestUser", roles = "USER")
    void testRemoveProduct() {
        Principal principal = () -> "TestUser";

        CartItem cartItem = createAndSaveCartItem();

        UUID productUuid = cartItem.getProduct().getUuid();

        assertEquals(1, cartItemRepository.count());

        cartService.removeProduct(productUuid, principal);

        assertEquals(0, cartItemRepository.count());
    }

    private CartItem createAndSaveCartItem() {
        User user = testData.createAndSaveUser();

        Product product = createAndSaveProduct();
        Integer quantity = 1;

        CartItem cartItem = new CartItem();

        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    @Test
    void handleProductQuantityError() {

    }

    @Test
    void handlePrincipalError() {

    }

    @Test
    void testHandlePrincipalError() {

    }

    private Product createAndSaveProduct() {
        Product product = new Product();

        // Set model
        Model model = createAndSaveModel();
        product.setModel(model);

        // Set categories
        Set<Category> categories = createAndSaveCategories();
        product.setCategories(categories);

        // Set other product characteristics
        product.setImageUrl("test.png");

        product.setPrice(BigDecimal.TEN);

        product.setInitialQuantity(15);

        product.setAvailableQuantity(15);

        product.setSpecification("Test Specification");

        return productRepository.save(product);
    }

    private Set<Category> createAndSaveCategories() {
        Category category1 = new Category();
        category1.setImageUrl("category1.png");
        category1.setName("Category1");

        Category category2 = new Category();
        category2.setImageUrl("category2.png");
        category2.setName("Category2");

        Set<Category> categories = Set.of(category1, category2);

        return new HashSet<>(categoryRepository.saveAll(categories));
    }

    private Model createAndSaveModel() {
        Model model = new Model();

        model.setName("TestModel");
        model.setManufacturer(createAndSaveManufacturer());

        return modelRepository.save(model);
    }

    private Manufacturer createAndSaveManufacturer() {
        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setImageUrl("manufacturer.png");
        manufacturer.setName("TestManufacturer");

        return manufacturerRepository.save(manufacturer);
    }

}
