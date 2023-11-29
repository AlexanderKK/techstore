package com.techx7.techstore.repository;

import com.techx7.techstore.model.entity.CartItem;
import com.techx7.techstore.model.entity.Product;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.techx7.techstore.testUtils.TestData.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {CartItemRepository.class, UserRepository.class, ProductRepository.class, RoleRepository.class, TestData.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.techx7.techstore.model.entity"})
@DataJpaTest
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestData testData;

    @Autowired
    private RoleRepository roleRepository;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        cartItemRepository.deleteAll();

        userRepository.deleteAll();

        productRepository.deleteAll();

        user = createUser();

        roleRepository.saveAll(user.getRoles());

        userRepository.save(user);

        product = testData.createProduct();

        productRepository.save(product);
    }

    @AfterEach
    void tearDown() {
        cartItemRepository.deleteAll();

        userRepository.deleteAll();

        productRepository.deleteAll();
    }

    @Test
    public void testFindAllByUserWhenCartItemsExist() {
        // Arrange
        CartItem cartItem = new CartItem(user, product, 2);

        cartItemRepository.save(cartItem);

        // Act
        List<CartItem> result = cartItemRepository.findAllByUser(user);

        // Assert
        assertThat(result).contains(cartItem);
    }

    @Test
    public void testFindAllByUserWhenNoCartItemsExist() {
        // Act
        List<CartItem> result = cartItemRepository.findAllByUser(user);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByUserAndProductWhenCartItemExists() {
        // Arrange
        CartItem cartItem = new CartItem(user, product, 2);

        cartItemRepository.save(cartItem);

        // Act
        CartItem result = cartItemRepository.findByUserAndProduct(user, product);

        // Assert
        assertThat(result).isEqualTo(cartItem);
    }

    @Test
    public void testFindByUserAndProductWhenNoCartItemExists() {
        // Act
        CartItem result = cartItemRepository.findByUserAndProduct(user, product);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    public void testUpdateQuantity() {
        // Arrange
        CartItem cartItem = new CartItem(user, product, 2);

        cartItemRepository.save(cartItem);

        Integer newQuantity = 5;

        // Act
        cartItemRepository.updateQuantity(newQuantity, cartItem.getUser().getUuid(), cartItem.getProduct().getUuid());

        CartItem updatedCartItem = cartItemRepository.findByUserAndProduct(user, product);

        // Assert
        assertThat(updatedCartItem.getQuantity()).isEqualTo(newQuantity);
    }

    @Test
    public void testDeleteByProductAndUser() {
        // Arrange
        CartItem cartItem = new CartItem(user, product, 2);
        cartItemRepository.save(cartItem);

        // Act and Assert
        assertEquals(1, cartItemRepository.count());

        cartItemRepository.deleteByProductAndUser(product, user);

        assertEquals(0, cartItemRepository.count());
    }

}
