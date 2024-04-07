package com.ocado.basket;


import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class BasketSplitterTest {


    @Test
    public void emptyBasketShouldReturnEmptyMap(){

        //given
        String absolutePathToConfigFile = "src/test/resources/config.json";
        BasketSplitter basketSplitter = new BasketSplitter(absolutePathToConfigFile);
        List<String> basket = new ArrayList<>();

        //when
        Map<String, List<String>> output = basketSplitter.split(basket);

        //then
        assertTrue(output.isEmpty());
    }

    @Test
    public void wrongBasketShouldReturnEmptyMap(){
        //given
        String absolutePathToConfigFile = "src/test/resources/config.json";
        BasketSplitter basketSplitter = new BasketSplitter(absolutePathToConfigFile);

        List<String> basket = Arrays.asList("THIS IS NOT A PRODUCT 1", "THIS IS NOT A PRODUCT 2", "THIS IS NOT A PRODUCT 3");

        //when
        Map<String, List<String>> output = basketSplitter.split(basket);

        //then
        assertTrue(output.isEmpty());
    }

    @Test
    public void partiallyWrongBasketShouldIgnoreWrongProducts(){
        //given
        String absolutePathToConfigFile = "src/test/resources/config.json";
        BasketSplitter basketSplitter = new BasketSplitter(absolutePathToConfigFile);
        Map<String, List<String>> expected = new HashMap<>();
        List<String> basket = Arrays.asList("Cheese - St. Andre", "THIS IS NOT A PRODUCT", "Flour - Buckwheat, Dark","THIS IS NOT A PRODUCT ALSO");

        expected.put("Pick-up point", Arrays.asList("Cheese - St. Andre", "Flour - Buckwheat, Dark"));

        //when
        Map<String, List<String>> output = basketSplitter.split(basket);

        //then
        assertEquals(expected.size(), output.size());
        for (Map.Entry<String, List<String>> entry : expected.entrySet()) {
            assertTrue(output.containsKey(entry.getKey()));
            assertEquals(entry.getValue().size(), output.get(entry.getKey()).size());
            assertTrue(output.get(entry.getKey()).containsAll(entry.getValue()));
        }

    }

    @Test
    public void basketShouldReturnOneDeliveryType(){
        //given
        String absolutePathToConfigFile = "src/test/resources/config.json";
        BasketSplitter basketSplitter = new BasketSplitter(absolutePathToConfigFile);
        List<String> basket = Arrays.asList("Peach - Fresh", "Beer - Muskoka Cream Ale","Sugar - Cubes", "Fish - Soup Base, Bouillon", "Gingerale - Diet - Schweppes");
        Map<String, List<String>> expected = new HashMap<>();

        expected.put("Same day delivery", Arrays.asList("Peach - Fresh", "Beer - Muskoka Cream Ale","Sugar - Cubes", "Fish - Soup Base, Bouillon", "Gingerale - Diet - Schweppes"));

        //when
        Map<String, List<String>> output = basketSplitter.split(basket);

        //then
        assertEquals(expected.size(), output.size());
        for (Map.Entry<String, List<String>> entry : expected.entrySet()) {
            assertTrue(output.containsKey(entry.getKey()));
            assertEquals(entry.getValue().size(), output.get(entry.getKey()).size());
            assertTrue(output.get(entry.getKey()).containsAll(entry.getValue()));
        }
    }

    @Test
    public void eachProductRequireDifferentDelivery(){
        //given
        String absolutePathToConfigFile = "src/test/resources/config.json";
        BasketSplitter basketSplitter = new BasketSplitter(absolutePathToConfigFile);
        List<String> basket = Arrays.asList("Sole - Dover, Whole, Fresh","Garlic - Peeled","Cake - Miini Cheesecake Cherry", "Juice - Ocean Spray Cranberry");
        Map<String, List<String>> expected = new HashMap<>();

        expected.put("In-store pick-up", Arrays.asList("Sole - Dover, Whole, Fresh"));
        expected.put("Same day delivery", Arrays.asList("Garlic - Peeled"));
        expected.put( "Courier", Arrays.asList("Cake - Miini Cheesecake Cherry"));
        expected.put( "Pick-up point", Arrays.asList( "Juice - Ocean Spray Cranberry"));
        //when
        Map<String, List<String>> output = basketSplitter.split(basket);

        //then
        assertEquals(expected.size(), output.size());
        for (Map.Entry<String, List<String>> entry : expected.entrySet()) {
            assertTrue(output.containsKey(entry.getKey()));
            assertEquals(entry.getValue().size(), output.get(entry.getKey()).size());
            assertTrue(output.get(entry.getKey()).containsAll(entry.getValue()));
        }
    }

    @Test
    public void testBasket1() {
        //given
        String absolutePathToConfigFile = "src/test/resources/config.json";
        BasketSplitter basketSplitter = new BasketSplitter(absolutePathToConfigFile);
        List<String> basket = Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Fond - Chocolate", "Cookies - Englishbay Wht");
        Map<String, List<String>> expected = new HashMap<>();

        expected.put("Courier", Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies", "Cookies - Englishbay Wht"));
        expected.put("Pick-up point", Arrays.asList("Fond - Chocolate"));

        //when
        Map<String, List<String>> output = basketSplitter.split(basket);

        //then
        assertEquals(expected.size(), output.size());
        for (Map.Entry<String, List<String>> entry : expected.entrySet()) {
            assertTrue(output.containsKey(entry.getKey()));
            assertEquals(entry.getValue().size(), output.get(entry.getKey()).size());
            assertTrue(output.get(entry.getKey()).containsAll(entry.getValue()));
        }
    }


    @Test
    public void testBasket2() {
        //given

        String absolutePathToConfigFile = "src/test/resources/config.json";
        BasketSplitter basketSplitter = new BasketSplitter(absolutePathToConfigFile);
        List<String> basket = Arrays.asList("Fond - Chocolate", "Chocolate - Unsweetened", "Nut - Almond, Blanched, Whole", "Haggis", "Mushroom - Porcini Frozen", "Cake - Miini Cheesecake Cherry", "Sauce - Mint", "Longan", "Bag Clear 10 Lb", "Nantucket - Pomegranate Pear", "Puree - Strawberry", "Numi - Assorted Teas", "Apples - Spartan", "Garlic - Peeled", "Cabbage - Nappa", "Bagel - Whole White Sesame", "Tea - Apple Green Tea");
        Map<String, List<String>> expected = new HashMap<>();

        expected.put("Same day delivery", Arrays.asList("Sauce - Mint", "Numi - Assorted Teas", "Garlic - Peeled"));
        expected.put("Courier", Arrays.asList("Cake - Miini Cheesecake Cherry"));
        expected.put("Express Collection", Arrays.asList("Fond - Chocolate", "Chocolate - Unsweetened", "Nut - Almond, Blanched, Whole", "Haggis", "Mushroom - Porcini Frozen", "Longan", "Bag Clear 10 Lb", "Nantucket - Pomegranate Pear", "Puree - Strawberry", "Apples - Spartan", "Cabbage - Nappa", "Bagel - Whole White Sesame", "Tea - Apple Green Tea"));

        //when
        Map<String, List<String>> output = basketSplitter.split(basket);

        //then
        assertEquals(expected.size(), output.size());
        for (Map.Entry<String, List<String>> entry : expected.entrySet()) {
            assertTrue(output.containsKey(entry.getKey()));
            assertEquals(entry.getValue().size(), output.get(entry.getKey()).size());
            assertTrue(output.get(entry.getKey()).containsAll(entry.getValue()));
        }
    }

}