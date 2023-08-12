package com.example.demo.ControllerTests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.assertj.core.api.Assertions;
import org.hibernate.engine.internal.CascadePoint;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository", userRepository);
        TestUtils.injectObjects(cartController,"cartRepository", cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository", itemRepository);
    }

    @Test
    public void verifyAddToCart() {
        Cart cart = new Cart();
        User CartUser = new User(1l,"User1","Admin",cart);
        Item item = new Item(1L, "Product 1", BigDecimal.valueOf(5.0), "Product 1");

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(CartUser.getUsername());
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(5);

        when(userRepository.findByUsername(CartUser.getUsername())).thenReturn(CartUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        //assertEquals(200, responseEntity.getStatusCodeValue());
        Cart cartAdd = responseEntity.getBody();
        //assertEquals(cartAdd.getItems(),new ArrayList<>());
    }
    @Test
    public void verifyAddToCartWithError() {

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Admin");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(5);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(
                HttpStatus.NOT_FOUND);
    }
    @Test
    public void VerifyremoveCart() {
        Cart cart = new Cart();
        User CartUser = new User(1l,"User1","Admin",cart);
        Item item = new Item(1L, "Product 1", BigDecimal.valueOf(5.0), "Product 1");

        when(userRepository.findByUsername(CartUser.getUsername())).thenReturn(CartUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(CartUser.getUsername());
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        Cart cartRemove = responseEntity.getBody();
        assertEquals(cartRemove.getItems(), new ArrayList<>());
    }

    @Test
    public void VerifyremoveCartWithError() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Admin");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(
                HttpStatus.NOT_FOUND);
    }


}
