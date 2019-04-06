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
package com.github.squigglesql.squigglesql;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class TableTest {

    @Test
    public void testColumnPersistence() {
        Table employee = new Table("employee");
        assertSame(employee.get("name"), employee.get("name"));
    }

    @Test
    public void testColumnReferencePersistence() {
        Table employee = new Table("employee");
        TableColumn employeeName = employee.get("name");
        TableReference e = employee.refer();
        assertSame(e.get(employeeName), e.get(employeeName));
    }

    @Test
    public void testColumnReferenceGetters() {
        Table employee = new Table("employee");
        TableColumn employeeName = employee.get("name");
        TableReference e = employee.refer();
        assertSame(e, e.get(employeeName).getTableReference());
        assertSame(employeeName, e.get(employeeName).getColumn());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTableNullNameException() {
        new Table(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTableEmptyNameException() {
        new Table("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTableColumnNullNameException() {
        Table employee = new Table("employee");
        employee.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTableColumnEmptyNameException() {
        Table employee = new Table("employee");
        employee.get("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTableReferenceNullColumnException() {
        Table employee = new Table("employee");
        TableReference e = employee.refer();
        e.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTableReferenceWrongColumnException() {
        Table employee = new Table("employee");
        Table department = new Table("department");
        TableColumn employeeName = employee.get("name");
        TableReference d = department.refer();
        d.get(employeeName);
    }
}
