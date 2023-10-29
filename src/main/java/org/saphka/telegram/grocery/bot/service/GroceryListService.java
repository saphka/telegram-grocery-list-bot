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
        var finalList = getOrCreateList(ownerId).addOwner(newOwnerId);
        var oldList = repository.findFirstByOwners(newOwnerId.toString());
        if (oldList.isPresent()) {
            finalList = finalList.addProducts(oldList.get().products());
            repository.delete(oldList.get());
        }
        repository.save(finalList);
    }

    @Transactional
    public void addProducts(Long ownerId, List<String> products) {
        repository.save(getOrCreateList(ownerId).addProducts(products));
    }

    @Transactional
    public void removeProducts(Long ownerId, List<String> products) {
        repository.save(getOrCreateList(ownerId).removeProducts(products));
    }

    @Transactional
    public void clearList(Long ownerId) {
        repository.save(getOrCreateList(ownerId).clearProducts());
    }

    @Transactional
    public void removeOwner(Long ownerId, Long removedOwner) {
        repository.save(getOrCreateList(ownerId).removeOwner(removedOwner));
    }
}
