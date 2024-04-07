package com.ocado.basket;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class BasketSplitter {
    private Map<String, List<String>> configDeliveryMap;

    public BasketSplitter(String absolutePathToConfigFile){
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.configDeliveryMap = mapper.readValue(new File(absolutePathToConfigFile), new TypeReference<HashMap<String, List<String>>>() {});
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public Map<String, List<String>> split(List<String> items) {
        Map<String, List<String>> deliveryGroups = new HashMap<>();
        Set<String> possibleDeliveryGroup = new HashSet<>();
        Map<String, List<String>> helperMap = new HashMap<>();
        List<String> filtratedItems = new LinkedList<>();

        items.forEach(potentialItem ->{
            if (configDeliveryMap.containsKey(potentialItem)){
                filtratedItems.add(potentialItem);
            }
        });

        if (items.isEmpty()){
            return new HashMap<>();
        }

        filtratedItems.forEach(item ->{
            possibleDeliveryGroup.addAll(configDeliveryMap.get(item));
        });

        possibleDeliveryGroup.forEach(group -> {
            deliveryGroups.put(group, new ArrayList<>());
            helperMap.put(group, new ArrayList<>());
        });


        for (String item: filtratedItems){
                List<String> deliveryPossibilities = configDeliveryMap.get(item);
                for (String possibility : deliveryPossibilities){
                    helperMap.get(possibility).add(item);
                }
        }

        List<String> bestDeliveryTypes = setCoverDeliveryTypes(filtratedItems, helperMap);

        for (String item : filtratedItems){
                String bestDeliveryProductType = null;
                int maximumPoints = Integer.MIN_VALUE;
                for (String deliveryType : bestDeliveryTypes){
                    if(helperMap.get(deliveryType).contains(item) && (maximumPoints < helperMap.get(deliveryType).size())){
                        maximumPoints = helperMap.get(deliveryType).size();
                        bestDeliveryProductType = deliveryType;
                    }
                }
                deliveryGroups.get(bestDeliveryProductType).add(item);
        }

        deliveryGroups.entrySet()
                .removeIf(entry -> entry.getValue().isEmpty());

        return deliveryGroups;
    }

    private static List<String> setCoverDeliveryTypes(List<String> items, Map<String, List<String>> subSets) {
        Set<String> itemsSet = new HashSet<>(items);
        List<String> bestDeliveryTypes = new ArrayList<>();

        while (!itemsSet.isEmpty()) {
            int maxIntersectionSize = 0;
            String selectedSetKey = null;
            for (Map.Entry<String, List<String>> entry : subSets.entrySet()) {
                int intersectionSize = 0;
                for (String sItem: entry.getValue()){
                    if (itemsSet.contains(sItem)){
                        intersectionSize++;
                    }
                }
                if (intersectionSize > maxIntersectionSize) {
                    maxIntersectionSize = intersectionSize;
                    selectedSetKey = entry.getKey();
                }
            }
            if (selectedSetKey == null) {
                break;
            }
            for (String entry : subSets.get(selectedSetKey)){
                itemsSet.remove(entry);
            }
            bestDeliveryTypes.add(selectedSetKey);
        }
        return bestDeliveryTypes;
    }

}