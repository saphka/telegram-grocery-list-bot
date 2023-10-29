package org.saphka.telegram.grocery.bot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Collection;
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

    public GroceryList addOwner(Long ownerId) {
        var owners = new LinkedHashSet<>(this.owners);
        owners.add(ownerId.toString());
        return new GroceryList(this.id, owners, this.products);
    }

    public GroceryList removeOwner(Long ownerId) {
        var owners = new LinkedHashSet<>(this.owners);
        owners.remove(ownerId.toString());
        return new GroceryList(this.id, owners, this.products);
    }

    public GroceryList addProducts(Collection<String> products) {
        var newProducts = new LinkedHashSet<>(this.products);
        newProducts.addAll(products);
        return new GroceryList(this.id, this.owners, newProducts);
    }

    public GroceryList removeProducts(Collection<String> products) {
        var newProducts = new LinkedHashSet<>(this.products);
        newProducts.removeAll(products);
        return new GroceryList(this.id, this.owners, newProducts);
    }

    public GroceryList clearProducts() {
        return new GroceryList(this.id, this.owners, Set.of());
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
