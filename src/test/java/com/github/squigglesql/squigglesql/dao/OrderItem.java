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

import java.util.Objects;

public class OrderItem {

    private final int id;
    private final Order order;
    private final Product product;
    private final int quantity;

    public OrderItem(int id, Order order, Product product, int quantity) {
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id &&
                quantity == orderItem.quantity &&
                order.equals(orderItem.order) &&
                product.equals(orderItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, product, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", order=" + order +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
