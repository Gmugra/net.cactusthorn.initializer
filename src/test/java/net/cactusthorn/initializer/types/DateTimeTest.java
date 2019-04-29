/*******************************************************************************
 * Copyright (C) 2017, Alexei Khatskevich
 * All rights reserved.
 * 
 * Licensed under the BSD 2-clause (Simplified) License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/BSD-2-Clause
 ******************************************************************************/
package net.cactusthorn.initializer.types;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static net.cactusthorn.initializer.annotations.InitPropertyPolicy.OPTIONAL;
import net.cactusthorn.initializer.Initializer;
import net.cactusthorn.initializer.InitializerException;
import net.cactusthorn.initializer.annotations.*;
import net.cactusthorn.initializer.properties.InitPropertiesBuilder;

public class DateTimeTest {

    static Locale LOCALE = Locale.forLanguageTag("us-US");
    static java.util.Calendar CURRENT_CALENDAR = Calendar.getInstance();
    static java.util.Date CURRENT_UTIL_DATE = CURRENT_CALENDAR.getTime();
    static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX", LOCALE);

    Initializer initializer = new Initializer();
    InitPropertiesBuilder pb = new InitPropertiesBuilder();

    @InitProperty(OPTIONAL)
    java.util.Date date;

    @InitProperty(OPTIONAL)
    java.sql.Date sqldate;

    @InitProperty(OPTIONAL)
    java.util.Calendar calendar;

    @InitProperty(OPTIONAL)
    Instant instant;

    @InitProperty
    LocalDateTime local;

    @InitProperty
    ZonedDateTime zoned;

    @InitProperty
    LocalDate localDate;

    @BeforeClass
    public static void initTimeZone() {
        System.setProperty("user.timezone", "US/Alaska");
        TimeZone.setDefault(null);
    }

    @Test(expected = InitializerException.class)
    public void testZonedDateTimeException() {

        new Initializer().initialize(pb.put("zoned", "2017-09-17T11:16:50").build(), this);
    }

    @Test
    public void testLocalDate() {

        new Initializer().initialize(pb.put("localDate", "2017-09-17T11:16:50").build(), this);
        assertEquals("2017-09-17", localDate.toString());

        new Initializer().initialize(pb.put("local", "2017-09-17T23:16:50-12:00").build(), this);
        assertEquals("2017-09-18T03:16:50", local.toString());
    }

    @Test
    public void testInstand() {

        new Initializer().initialize(pb.put("instant", "2017-09-17T11:16:50Z").build(), this);
        assertEquals("2017-09-17T11:16:50Z", instant.toString());

        new Initializer().initialize(pb.put("instant", "2017-09-17T11:16:50+02:00").build(), this);
        assertEquals("2017-09-17T09:16:50Z", instant.toString());

        new Initializer().initialize(pb.put("instant", "2017-09-17T11:16:50").build(), this);
        assertEquals("2017-09-17T11:16:50Z", instant.toString());

        new Initializer().initialize(pb.put("instant", "2017-09-17").build(), this);
        assertEquals("2017-09-17T00:00:00Z", instant.toString());
    }

    @Test
    public void testZonedDateTime() {

        new Initializer().initialize(pb.put("zoned", "2017-09-17T11:16:50+01:00").build(), this);
        assertEquals("2017-09-17T11:16:50+01:00", zoned.toString());
    }

    @Test(expected = InitializerException.class)
    public void testLocalDateTimeException() {

        TimeZone.setDefault(null);

        new Initializer().initialize(pb.put("local", "21.12.1976").build(), this);
    }

    @Test
    public void testLocalDateTime() {

        new Initializer().initialize(pb.put("local", "2017-09-17T11:16:50").build(), this);
        assertEquals("2017-09-17T11:16:50", local.toString());

        new Initializer().initialize(pb.put("local", "2017-09-17T11:16:50+02:00").build(), this);
        assertEquals("2017-09-17T01:16:50", local.toString());
    }

    @Test
    public void testCustomFormat() {

        new Initializer().initialize(pb.addDateTimeFormatPattern("dd.MM.yyyy").put("date", "21.12.1976").build(), this);
        assertNotNull(date);
    }

    @Test(expected = InitializerException.class)
    public void testUtilDateException() {

        TimeZone.setDefault(null);

        initializer.initialize(pb.put("date", "21.12.1976").build(), this);
    }

    @Test
    public void testCalendar() {

        initializer.initialize(pb.put("calendar", "2017-09-17T11:16:50+01:00").build(), this);
        assertNotNull(calendar);

        calendar = null;
        initializer.initialize(pb.put("calendar", "2017-09-17T11:16:50").build(), this);
        assertNotNull(calendar);

        calendar = null;
        initializer.initialize(pb.put("calendar", "2017-10-05").build(), this);
        assertNotNull(calendar);

        calendar = CURRENT_CALENDAR;

        initializer.initialize(pb.put("calendar", "").build(), this);
        assertNull(calendar);
    }

    @Test
    public void testCalendarTZ() {

        initializer.initialize(pb.put("calendar", "2017-09-17T11:16:50+03:00").build(), this);

        assertEquals("GMT+03:00", calendar.getTimeZone().getID());

        ZonedDateTime zdt = ZonedDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());

        assertEquals("2017-09-17T11:16:50+03:00", FORMATTER.format(zdt));
    }

    @Test
    public void testUtilDate() {

        initializer.initialize(pb.put("date", "2017-09-17T11:16:50+01:00").build(), this);

        assertEquals("Sun Sep 17 02:16:50 AKDT 2017", date.toString());

        initializer.initialize(pb.put("date", "2017-09-17T11:16:50").build(), this);
        assertEquals("Sun Sep 17 11:16:50 AKDT 2017", date.toString());

        initializer.initialize(pb.put("date", "2017-10-05").build(), this);
        assertEquals("Thu Oct 05 00:00:00 AKDT 2017", date.toString());

        initializer.initialize(pb.put("date", "").build(), this);
        assertNull(date);
    }
}
