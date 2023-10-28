package org.saphka.telegram.grocery.bot.service;

import org.saphka.telegram.grocery.bot.model.GroceryList;
import org.saphka.telegram.grocery.bot.repository.GroceryListRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class GroceryListService {

    private final GroceryListRepository repository;

    public GroceryListService(GroceryListRepository repository) {
        this.repository = repository;
    }

    public GroceryList getOrCreateList(Long ownerId) {
        return repository.findFirstByOwners(ownerId.toString()).orElseGet(() ->
                repository.save(new GroceryList(
                        UUID.randomUUID().toString(),
                        Set.of(ownerId.toString()),
                        Set.of()
                )));
    }

    public void addOwner(Long ownerId, Long newOwnerId) {
        repository.save(getOrCreateList(ownerId).owner(newOwnerId));
    }

    public void addProduct(Long ownerId, String product) {
        repository.save(getOrCreateList(ownerId).product(product));
    }
}
