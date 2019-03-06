package com.github.squigglesql.squigglesql.dao;

import java.time.Instant;
import java.util.Objects;

public class Order {

    private final int id;
    private final Instant issuedAt;
    private final Customer customer;

    public Order(int id, Instant issuedAt, Customer customer) {
        this.id = id;
        this.issuedAt = issuedAt;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                issuedAt.equals(order.issuedAt) &&
                customer.equals(order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, issuedAt, customer);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", issuedAt=" + issuedAt +
                ", customer=" + customer +
                '}';
    }
}
