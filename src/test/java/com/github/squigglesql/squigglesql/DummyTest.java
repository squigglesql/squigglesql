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

import com.github.squigglesql.squigglesql.syntax.SqlSyntax;
import com.github.squigglesql.squigglesql.util.CollectionWriter;
import com.github.squigglesql.squigglesql.util.JdbcUtils;
import com.github.squigglesql.squigglesql.util.SquiggleUtils;
import org.junit.Test;

// Test to force coveralls.io ignore classes of utilities in coverage.
public class DummyTest {

    @Test
    public void testDummy() {
        new SquiggleUtils() {
        };
        new SquiggleConstants() {
        };
        new JdbcUtils() {
        };
        new CollectionWriter() {
        };
        new SqlSyntax() {
        };
        SqlSyntax.DEFAULT_SQL_SYNTAX.compileEmptyInsert(null);
    }
}
