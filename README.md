

**BasketSplitter**

In order to read and convert a file into a Map<String, List<String>>, whose path is provided during the initialization of the class, I use the Jackson library.

**split(List<String> items)**

`deliveryGroups`: will store the returned result.

`possibleDeliveryGroup`: is a set containing unique delivery types that may occur in the solution. `possibleDeliveryGroup` is created to narrow down the number of deliveries to be considered, as they are unique elements, thus I use a set.

`helperMap`: is a helper map, essentially an inversion of what is in the configuration file `config.json`, because `helperMap` is a
Map<String, List<String>> where the key is the delivery type and the value is a list of products that can be delivered by the delivery type (which is the key).

`filtratedItems`: is a list of products created from the list passed to the `split()` function, which is created after filtering out products that do not appear in the `configDeliveryMap` (are not defined in the `config.json` file).

To solve the problem, we first need to find the smallest subgroup of the `possibleDeliveryGroup` which can deliver all the products. This is done using the `setCoverDeliveryTypes()` method, which takes a list of items (in the basket) and subSets, i.e., helperMap whose definition is above.

The algorithm works as follows:

It creates an `itemSet` filled with items passed to the method and creates `bestDeliveryTypes`, i.e., the best subgroup of delivery types passed to the method (it will be returned as best subgroup). It selects the delivery set that has the largest number of common products from the basket. This set is considered the best.

This is a greedy algorithm with a computational complexity of O(nm) where n is the number of products and m is the number of delivery types. After calling `bestDeliveryTypes = setCoverDeliveryTypes(filtratedItems, helperMap);`, we get the smallest set of deliveries, knowing that products can be delivered by `bestDeliveryTypes`, and it is the optimal group.

Now we need to allocate products to deliveries to create the largest possible sets of products for the respective deliveries. We do this by iterating over each product `item` in `filtratedItems` and selecting the most optimal group based on `helperMap.get(deliveryType).size()`, i.e., the number of products that can be delivered by a given delivery type.

The delivery that can deliver the most products is the best option for the given product. The product is added to the respective delivery `deliveryGroups.get(bestDeliveryProductType).add(item);`.

Since not all deliveryGroups have been used, we need to remove deliveries that do not contain any products: `deliveryGroups.entrySet().removeIf(entry -> entry.getValue().isEmpty());`
