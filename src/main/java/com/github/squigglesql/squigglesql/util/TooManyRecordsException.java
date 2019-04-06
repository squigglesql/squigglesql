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
package com.github.squigglesql.squigglesql.util;

import com.github.squigglesql.squigglesql.ResultMapper;
import com.github.squigglesql.squigglesql.query.Query;

import java.sql.Connection;

/**
 * Exception thrown by {@link JdbcUtils#selectOne(Query, Connection, ResultMapper)} on attempt to select two or more
 * rows.
 */
public class TooManyRecordsException extends RuntimeException {

    /**
     * Creates an exception.
     */
    public TooManyRecordsException() {
        super("Expected to retrieve 0 or 1 records from database, got more.");
    }
}
