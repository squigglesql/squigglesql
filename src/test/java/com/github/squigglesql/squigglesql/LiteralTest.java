package com.github.squigglesql.squigglesql;

import com.github.squigglesql.squigglesql.literal.Literal;
import com.github.squigglesql.squigglesql.query.SelectQuery;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class LiteralTest {

    private static final boolean BOOLEAN = true;
    private static final byte BYTE = 10;
    private static final short SHORT = 1000;
    private static final int INT = 1000000;
    private static final long LONG = 1000000000000L;
    private static final float FLOAT = 10.5f;
    private static final double DOUBLE = 20.5d;
    private static final BigDecimal BIG_DECIMAL = new BigDecimal(LONG).pow(4);
    private static final String STRING = "Hello";

    private static final Instant INSTANT = Instant.parse("2018-01-02T03:04:05.006Z");
    private static final LocalDate LOCAL_DATE = LocalDate.of(2018, 1, 2);
    private static final LocalTime LOCAL_TIME = LocalTime.of(3, 4, 5, 6000000);
    private static final LocalDateTime LOCAL_DATE_TIME =
            LocalDateTime.of(2018, 1, 2, 3, 4, 5, 6000000);
    private static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.of(LOCAL_DATE_TIME, ZoneId.of("America/Vancouver"));
    private static final OffsetDateTime OFFSET_DATE_TIME = OffsetDateTime.of(LOCAL_DATE_TIME, ZoneOffset.ofHours(-8));
    private static final OffsetTime OFFSET_TIME = OffsetTime.of(LOCAL_TIME, ZoneOffset.ofHours(-8));

    @Test
    public void testLiteral() {
        SelectQuery select = new SelectQuery();

        select.addToSelection(Literal.ofNull());
        select.addToSelection(Literal.of(BOOLEAN));
        select.addToSelection(Literal.of((Boolean) BOOLEAN));
        select.addToSelection(Literal.of((Boolean) null));
        select.addToSelection(Literal.of(BYTE));
        select.addToSelection(Literal.of((Byte) BYTE));
        select.addToSelection(Literal.of((Byte) null));
        select.addToSelection(Literal.of(SHORT));
        select.addToSelection(Literal.of((Short) SHORT));
        select.addToSelection(Literal.of((Short) null));
        select.addToSelection(Literal.of(INT));
        select.addToSelection(Literal.of((Integer) INT));
        select.addToSelection(Literal.of((Integer) null));
        select.addToSelection(Literal.of(LONG));
        select.addToSelection(Literal.of((Long) LONG));
        select.addToSelection(Literal.of((Long) null));
        select.addToSelection(Literal.of(FLOAT));
        select.addToSelection(Literal.of((Float) FLOAT));
        select.addToSelection(Literal.of(Float.POSITIVE_INFINITY));
        select.addToSelection(Literal.of(Float.NEGATIVE_INFINITY));
        select.addToSelection(Literal.of(Float.NaN));
        select.addToSelection(Literal.of((Float) null));
        select.addToSelection(Literal.of(DOUBLE));
        select.addToSelection(Literal.of((Double) DOUBLE));
        select.addToSelection(Literal.of(Double.POSITIVE_INFINITY));
        select.addToSelection(Literal.of(Double.NEGATIVE_INFINITY));
        select.addToSelection(Literal.of(Double.NaN));
        select.addToSelection(Literal.of((Double) null));
        select.addToSelection(Literal.of(BIG_DECIMAL));
        select.addToSelection(Literal.of((BigDecimal) null));
        select.addToSelection(Literal.of(STRING));
        select.addToSelection(Literal.of((String) null));
        select.addToSelection(Literal.of(INSTANT));
        select.addToSelection(Literal.of((Instant) null));
        select.addToSelection(Literal.of(LOCAL_DATE));
        select.addToSelection(Literal.of((LocalDate) null));
        select.addToSelection(Literal.of(LOCAL_TIME));
        select.addToSelection(Literal.of((LocalTime) null));
        select.addToSelection(Literal.of(LOCAL_DATE_TIME));
        select.addToSelection(Literal.of((LocalDateTime) null));
        select.addToSelection(Literal.of(ZONED_DATE_TIME));
        select.addToSelection(Literal.of((ZonedDateTime) null));
        select.addToSelection(Literal.of(OFFSET_DATE_TIME));
        select.addToSelection(Literal.of((OffsetDateTime) null));
        select.addToSelection(Literal.of(OFFSET_TIME));
        select.addToSelection(Literal.of((OffsetTime) null));
        select.addToSelection(Literal.unsafe("true"));
        select.addToSelection(Literal.unsafe(null));

        assertEquals("SELECT\n"
                + "    null,\n"
                + "    true,\n"
                + "    true,\n"
                + "    null,\n"
                + "    10,\n"
                + "    10,\n"
                + "    null,\n"
                + "    1000,\n"
                + "    1000,\n"
                + "    null,\n"
                + "    1000000,\n"
                + "    1000000,\n"
                + "    null,\n"
                + "    1000000000000,\n"
                + "    1000000000000,\n"
                + "    null,\n"
                + "    10.5,\n"
                + "    10.5,\n"
                + "    Infinity,\n"
                + "    -Infinity,\n"
                + "    NaN,\n"
                + "    null,\n"
                + "    20.5,\n"
                + "    20.5,\n"
                + "    Infinity,\n"
                + "    -Infinity,\n"
                + "    NaN,\n"
                + "    null,\n"
                + "    1000000000000000000000000000000000000000000000000,\n"
                + "    null,\n"
                + "    'Hello',\n"
                + "    null,\n"
                + "    '2018-01-02 03:04:05.006+00',\n"
                + "    null,\n"
                + "    '2018-01-02',\n"
                + "    null,\n"
                + "    '03:04:05.006',\n"
                + "    null,\n"
                + "    '2018-01-02 03:04:05.006',\n"
                + "    null,\n"
                + "    '2018-01-02 03:04:05.006-08',\n"
                + "    null,\n"
                + "    '2018-01-02 03:04:05.006-08',\n"
                + "    null,\n"
                + "    '03:04:05.006-08',\n"
                + "    null,\n"
                + "    true,\n"
                + "    null", select.toString());
    }
}
