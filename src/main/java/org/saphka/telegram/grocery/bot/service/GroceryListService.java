package org.saphka.telegram.grocery.bot.service;

import org.saphka.telegram.grocery.bot.model.GroceryList;
import org.saphka.telegram.grocery.bot.repository.GroceryListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class GroceryListService {

    private final GroceryListRepository repository;

    public GroceryListService(GroceryListRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public GroceryList getOrCreateList(Long ownerId) {
        return repository.findFirstByOwners(ownerId.toString()).orElseGet(() ->
                repository.save(new GroceryList(
                        UUID.randomUUID().toString(),
                        Set.of(ownerId.toString()),
                        Set.of()
                )));
    }

    @Transactional
    public void addOwner(Long ownerId, Long newOwnerId) {
        var finalList = getOrCreateList(ownerId).owner(newOwnerId);
        var oldList = repository.findFirstByOwners(newOwnerId.toString());
        if (oldList.isPresent()) {
            finalList = finalList.products(oldList.get().products());
            repository.delete(oldList.get());
        }
        repository.save(finalList);
    }

    @Transactional
    public void addProduct(Long ownerId, String product) {
        repository.save(getOrCreateList(ownerId).products(List.of(product)));
    }
}
