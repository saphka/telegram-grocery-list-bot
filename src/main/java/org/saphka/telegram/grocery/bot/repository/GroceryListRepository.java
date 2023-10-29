package org.saphka.telegram.grocery.bot.repository;

import org.saphka.telegram.grocery.bot.model.GroceryList;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroceryListRepository extends CrudRepository<GroceryList, String> {

    Optional<GroceryList> findFirstByOwnersContaining(String owner);
}

