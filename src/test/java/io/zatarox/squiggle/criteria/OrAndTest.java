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
package io.zatarox.squiggle.criteria;

import io.zatarox.squiggle.Table;
import io.zatarox.squiggle.TableColumn;
import io.zatarox.squiggle.TableReference;
import io.zatarox.squiggle.literal.Literal;
import io.zatarox.squiggle.query.SelectQuery;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

        select.addCriteria(new OrCriteria(
                new MatchCriteria(u.get(userName), MatchCriteria.LIKE, Literal.of("a%")),
                new AndCriteria(
                        new MatchCriteria(u.get(userId), MatchCriteria.EQUALS, Literal.of(12345)),
                        new MatchCriteria(u.get(userFeet), MatchCriteria.EQUALS, Literal.of("smelly"))
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
        criterias.add(new MatchCriteria(u.get(userRole), MatchCriteria.EQUALS, Literal.of("ADMIN")));
        criterias.add(new MatchCriteria(u.get(userSuperadmin), MatchCriteria.EQUALS, Literal.of(true)));
        criterias.add(new MatchCriteria(u.get(userEnabled), MatchCriteria.EQUALS, Literal.of(true)));

        select.addCriteria(new AndCriteria(criterias));

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

        List<Criteria> criterias = new ArrayList<Criteria>();
        criterias.add(new MatchCriteria(u.get(userRole), MatchCriteria.EQUALS, Literal.of("ADMIN")));
        criterias.add(new MatchCriteria(u.get(userSuperadmin), MatchCriteria.EQUALS, Literal.of(true)));
        criterias.add(new MatchCriteria(u.get(userEnabled), MatchCriteria.EQUALS, Literal.of(true)));

        select.addCriteria(new OrCriteria(criterias));

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

        select.addCriteria(new OrCriteria());
        select.addCriteria(new AndCriteria());

        assertEquals("SELECT\n"
                + "    u.id\n"
                + "FROM\n"
                + "    user u\n"
                + "WHERE\n"
                + "    1 = 1 AND\n"
                + "    1 = 1", select.toString());
    }
}
