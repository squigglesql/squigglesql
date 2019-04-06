/*
 * Copyright 2019 Egor Nepomnyaschih.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
