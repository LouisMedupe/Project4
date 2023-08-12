package com.example.demo.ControllerTests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository", itemRepository);
    }

    @Test
    public void getAllItems(){
        List<Item> itemList = new ArrayList<>();
        Item itemA = new Item(1L, "Shirt", BigDecimal.valueOf(5.00), "Plain Shirt");
        Item itemB = new Item(2L, "Jeans", BigDecimal.valueOf(3.00), "Blue Skinny Jeans");
        itemList.add(itemA);
        itemList.add(itemB);

        when(itemRepository.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> ItemResponse = itemController.getItems();
        assertNotNull(ItemResponse);
        assertEquals(200,ItemResponse.getStatusCodeValue());
        assertEquals(itemList, ItemResponse.getBody());
    }

    @Test
    public void VerifyfindItems() {
        Item itemA = new Item(1L, "Shirt", BigDecimal.valueOf(5.00), "Plain Shirt");
        Item itemB = new Item(2L, "Jeans", BigDecimal.valueOf(3.00), "Blue Skinny Jeans");

        // find by id
        when(itemRepository.findById(2L)).thenReturn(Optional.of(itemB));
        ResponseEntity<Item> responseID = itemController.getItemById(2L);
        assertNotNull(responseID);
        assertEquals(200, responseID.getStatusCodeValue());
        assertEquals(itemB, responseID.getBody());

        // find by name
        when(itemRepository.findByName("Shirt")).thenReturn(Arrays.asList(itemA));
        ResponseEntity<List<Item>> responseByName = itemController.getItemsByName("Shirt");
        assertNotNull(responseByName);
        assertEquals(200, responseByName.getStatusCodeValue());
        assertEquals(Arrays.asList(itemA), responseByName.getBody());

    }

    @Test
    public void VerifyfindItemsWithFail() {
        ResponseEntity<Item> ItemResponseID = itemController.getItemById(3L);
        assertNotNull(ItemResponseID);
        assertEquals(404, ItemResponseID.getStatusCodeValue());
        assertNull(ItemResponseID.getBody());

        // find by name
        ResponseEntity<List<Item>> ItemresponseByName = itemController.getItemsByName("Jeans");
        assertNotNull(ItemresponseByName);
        assertEquals(404, ItemresponseByName.getStatusCodeValue());
        assertNull(ItemresponseByName.getBody());
    }
}