import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTest {
    @Test
    public void testOrderingOutOfStockItem() {
        // Arrange: Create an item and simulate it being out of stock.
        Item outOfStockItem = new Item("OutOfStockItem", 50.00, 0, "Snack");
        Admin.itemsList.add(outOfStockItem);

        Customer customer = new Customer();
        String expectedErrorMessage = "Item is out of stock and cannot be ordered.";

        // Act: Attempt to place an order for the out-of-stock item.
        String result = customer.placeOrder("OutOfStockItem", 1);

        // Assert: Verify that the order is not processed and the correct error message is displayed.
        assertEquals(expectedErrorMessage, result, "Item is out of stock and cannot be ordered.");
    }
}
