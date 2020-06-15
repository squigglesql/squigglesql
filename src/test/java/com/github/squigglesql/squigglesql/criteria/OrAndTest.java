/*
 * Copyright 2004-2019 Joe Walnes, Guillaume Chauvet, Egor Nepomnyaschih.
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
package com.github.squigglesql.squigglesql.criteria;

import com.github.squigglesql.squigglesql.Table;
import com.github.squigglesql.squigglesql.TableColumn;
import com.github.squigglesql.squigglesql.TableReference;
import com.github.squigglesql.squigglesql.literal.Literal;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.squigglesql.squigglesql.criteria.Criteria.*;
import static org.junit.Assert.assertEquals;

public class OrAndTest {

    @Test
    public void testOrAnd() {
        Table user = new Table("user");
        TableColumn userId = user.get("id");
        TableColumn userName = user.get("name");
        TableColumn userFeet = user.get("feet");

        TableReference u = user.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(u.get(userId));

        select.addCriteria(or(
                like(u.get(userName), Literal.of("a%")),
                and(
                        equal(u.get(userId), Literal.of(12345)),
                        equal(u.get(userFeet), Literal.of("smelly"))
                )
        ));

        assertEquals("SELECT\n"
                + "    u.id\n"
                + "FROM\n"
                + "    user u\n"
                + "WHERE\n"
                + "    (\n"
                + "        u.name LIKE 'a%' OR\n"
                + "        (\n"
                + "            u.id = 12345 AND\n"
                + "            u.feet = 'smelly'\n"
                + "        )\n"
                + "    )", select.toString());
    }

    @Test
    public void testAndByCollection() {
        Table user = new Table("user");
        TableColumn userId = user.get("id");
        TableColumn userRole = user.get("role");
        TableColumn userSuperadmin = user.get("superadmin");
        TableColumn userEnabled = user.get("enabled");

        TableReference u = user.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(u.get(userId));

        List<Criteria> criterias = new ArrayList<Criteria>();
        criterias.add(equal(u.get(userRole), Literal.of("ADMIN")));
        criterias.add(equal(u.get(userSuperadmin), Literal.of(true)));
        criterias.add(equal(u.get(userEnabled), Literal.of(true)));

        select.addCriteria(and(criterias));

        assertEquals("SELECT\n"
                + "    u.id\n"
                + "FROM\n"
                + "    user u\n"
                + "WHERE\n"
                + "    (\n"
                + "        u.role = 'ADMIN' AND\n"
                + "        u.superadmin = true AND\n"
                + "        u.enabled = true\n"
                + "    )", select.toString());
    }

    @Test
    public void testOrByCollection() {
        Table user = new Table("user");
        TableColumn userId = user.get("id");
        TableColumn userRole = user.get("role");
        TableColumn userSuperadmin = user.get("superadmin");
        TableColumn userEnabled = user.get("enabled");

        TableReference u = user.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(u.get(userId));

        List<Criteria> criterias = new ArrayList<>();
        criterias.add(equal(u.get(userRole), Literal.of("ADMIN")));
        criterias.add(equal(u.get(userSuperadmin), Literal.of(true)));
        criterias.add(equal(u.get(userEnabled), Literal.of(true)));

        select.addCriteria(or(criterias));

        assertEquals("SELECT\n"
                + "    u.id\n"
                + "FROM\n"
                + "    user u\n"
                + "WHERE\n"
                + "    (\n"
                + "        u.role = 'ADMIN' OR\n"
                + "        u.superadmin = true OR\n"
                + "        u.enabled = true\n"
                + "    )", select.toString());
    }

    @Test
    public void testEmpty() {
        Table user = new Table("user");
        TableColumn userId = user.get("id");

        TableReference u = user.refer();

        SelectQuery select = new SelectQuery();

        select.addToSelection(u.get(userId));

        select.addCriteria(or());
        select.addCriteria(and());

        assertEquals("SELECT\n"
                + "    u.id\n"
                + "FROM\n"
                + "    user u\n"
                + "WHERE\n"
                + "    0 = 1 AND\n"
                + "    1 = 1", select.toString());
    }
}
