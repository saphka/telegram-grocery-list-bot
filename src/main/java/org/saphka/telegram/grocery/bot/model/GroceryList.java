package org.saphka.telegram.grocery.bot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@RedisHash("grocery")
public record GroceryList(@Id String id, @Indexed Set<String> owners, Set<String> products) {

    public GroceryList(String id, Set<String> owners, Set<String> products) {
        this.id = id;
        this.owners = Set.copyOf(Objects.requireNonNullElse(owners, Set.of()));
        this.products = Set.copyOf(Objects.requireNonNullElse(products, Set.of()));
    }

    public GroceryList owner(Long ownerId) {
        var owners = new LinkedHashSet<>(this.owners);
        owners.add(ownerId.toString());
        return new GroceryList(this.id, owners, this.products);
    }

    public GroceryList product(String product) {
        var products = new LinkedHashSet<>(this.products);
        products.add(product);
        return new GroceryList(this.id, this.owners, products);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroceryList that = (GroceryList) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
