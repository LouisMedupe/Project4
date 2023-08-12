package com.example.demo.ControllerTests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"userRepository", userRepository);
        TestUtils.injectObjects(orderController,"orderRepository", orderRepository);
    }

    @Test
    public void verifysubmitOrder() {
        Cart cart = new Cart();
        User User = new User(1l,"User1","Admin",cart);
        List<Item> itemList = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);

        Item itemA = new Item(1L, "Shirt", BigDecimal.valueOf(5.00), "Plain Shirt");
        Item itemB = new Item(2L, "Jeans", BigDecimal.valueOf(3.00), "Blue Skinny Jeans");
        itemList.add(itemA);
        itemList.add(itemB);
        total.add(itemA.getPrice());
        total.add(itemB.getPrice());

        cart.setItems(itemList);
        cart.setTotal(total);
        cart.setUser(User);

        when(userRepository.findByUsername(User.getUsername())).thenReturn(User);
        ResponseEntity<UserOrder> responseEntity = orderController.submit(User.getUsername());
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        UserOrder userOrder = responseEntity.getBody();
        assertEquals(userOrder.getItems(), itemList);
        assertEquals(userOrder.getUser().getUsername(), User.getUsername());
        assertEquals(userOrder.getTotal(), total);
    }

    @Test
    public void verifySubmitWithFail() {
        when(userRepository.findByUsername("Admin")).thenReturn(null);
        ResponseEntity<UserOrder> responseEntity = orderController.submit("Admin");
        assertNotNull(responseEntity);
        assertEquals(404,responseEntity.getStatusCodeValue());
    }

    @Test
    public void verifygetOrdersForUser() {
        Cart cart = new Cart();
        User User = new User(1l,"User1","Admin",cart);
        List<Item> itemList = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);

        Item itemA = new Item(1L, "Shirt", BigDecimal.valueOf(5.00), "Plain Shirt");
        Item itemB = new Item(2L, "Jeans", BigDecimal.valueOf(3.00), "Blue Skinny Jeans");
        itemList.add(itemA);
        itemList.add(itemB);
        total.add(itemA.getPrice());
        total.add(itemB.getPrice());

        //Adding to the Cart
        cart.setItems(itemList);
        cart.setTotal(total);
        cart.setUser(User);

        UserOrder userOrder = UserOrder.createFromCart(cart);
        List<UserOrder> userOrderList = Arrays.asList(userOrder);

        when(userRepository.findByUsername("Admin")).thenReturn(User);
        when(orderRepository.findByUser(User)).thenReturn(userOrderList);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Admin");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> userOrderListResponse = response.getBody();
        assertEquals(userOrderListResponse.size(), 1);
        UserOrder userOrderResponse = userOrderListResponse.get(0);
        assertEquals(userOrderResponse.getItems(), itemList);
        assertEquals(userOrderResponse.getUser().getUsername(), User.getUsername());
        assertEquals(userOrderResponse.getTotal(), total);

    }

    @Test
    public void verifygetOrdersForUserfail() {
        when(userRepository.findByUsername("Test")).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Test");
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());

    }
}