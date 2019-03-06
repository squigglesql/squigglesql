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
package com.github.squigglesql.squigglesql.mock;

import com.github.squigglesql.squigglesql.statement.StatementBuilder;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Builder for itself...
public class MockStatement implements StatementBuilder<MockStatement> {

    private final String query;
    private final List<Object> parameters = new ArrayList<>();

    MockStatement(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public MockStatement buildStatement() {
        return this;
    }

    @Override
    public void addNull(int sqlType) throws SQLException {
        parameters.add(new MockNull(sqlType));
    }

    @Override
    public void addBoolean(boolean value) throws SQLException {
        parameters.add(value);
    }

    @Override
    public void addByte(byte value) throws SQLException {
        parameters.add(value);
    }

    @Override
    public void addShort(short value) throws SQLException {
        parameters.add(value);
    }

    @Override
    public void addInteger(int value) throws SQLException {
        parameters.add(value);
    }

    @Override
    public void addLong(long value) throws SQLException {
        parameters.add(value);
    }

    @Override
    public void addFloat(float value) throws SQLException {
        parameters.add(value);
    }

    @Override
    public void addDouble(double value) throws SQLException {
        parameters.add(value);
    }

    @Override
    public void addBigDecimal(BigDecimal value) throws SQLException {
        parameters.add(value);
    }

    @Override
    public void addString(String value) throws SQLException {
        parameters.add(value);
    }

    @Override
    public void addTimestamp(Timestamp value, Calendar calendar) throws SQLException {
        parameters.add(new MockWithCalendar(value, calendar));
    }

    @Override
    public void addTime(Time value, Calendar calendar) throws SQLException {
        parameters.add(new MockWithCalendar(value, calendar));
    }

    @Override
    public void addDate(Date value, Calendar calendar) throws SQLException {
        parameters.add(new MockWithCalendar(value, calendar));
    }

    @Override
    public void addArray(Array value) throws SQLException {
        parameters.add(value);
    }

    @Override
    public void addBytes(byte[] value) throws SQLException {
        parameters.add(value);
    }
}
