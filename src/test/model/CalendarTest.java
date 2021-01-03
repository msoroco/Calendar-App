package model;

import exceptions.UnsupportedYearException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalendarTest {

    private Calendar c;
    private Calendar notEmpty;

    @BeforeEach
    void setUp() {
        c = new Calendar();
        notEmpty = new Calendar();
        try {
            notEmpty.findYearInCalendar(2222);
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2222");
        }
        try {
            notEmpty.findYearInCalendar(2345);
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2345");
        }
    }

    @Test
    void findYearInCalendarMakeNewLowestYearTest() {
        try {
            assertEquals(2000, c.findYearInCalendar(2000).getYear());
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2999");
        }
    }

    @Test
    void findYearInCalendarMakeNewHighestYearTest() {
        try {
            assertEquals(2999, c.findYearInCalendar(2999).getYear());
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2000");
        }
    }

    @Test
    void findYearInCalendarUnsupportedYearTest() {
        try {
            assertEquals(5000, c.findYearInCalendar(5000).getYear());
            fail("UnsupportedYearException expected for 5000");
        } catch (UnsupportedYearException e) {
           //expected
        }
    }

    @Test
    void findYearInCalendarUnsupportedYearLowBoundTest() {
        try {
            assertEquals(1999, c.findYearInCalendar(1999).getYear());
            fail("UnsupportedYearException expected for 1999");
        } catch (UnsupportedYearException e) {
            //expected
        }
    }

    @Test
    void findYearInCalendarUnsupportedYearHighBoundTest() {
        try {
            assertEquals(3000, c.findYearInCalendar(3000).getYear());
            fail("UnsupportedYearException expected for 1999");
        } catch (UnsupportedYearException e) {
            //expected
        }
    }

    @Test
    void findYearInCalendarFindOldAndMakeNewTest() {
        try {
            assertEquals(2222, notEmpty.findYearInCalendar(2222).getYear());
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2222");
        }
        try {
            assertEquals(2222, notEmpty.findYearInCalendar(2222).getYear());
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2222");
        }
        try {
            assertEquals(2345, notEmpty.findYearInCalendar(2345).getYear());
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2345");
        }
        try {
            assertEquals(2001, c.findYearInCalendar(2001).getYear());
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2001");
        }
        try {
            assertEquals(2001, c.findYearInCalendar(2001).getYear());
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2001");
        }
    }

    @Test
    void containsYearMakeNewLowestYearTest() {
        try {
            assertEquals(-1, c.containsYear(2000));
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2999");
        }
    }

    @Test
    void containsYearMakeNewHighestYearTest() {
        try {
            assertEquals(-1,  c.containsYear(2999));
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2000");
        }
    }

    @Test
    void containsYearHasYearTest() {
        try {
            assertEquals(1, notEmpty.containsYear(2345));
        } catch (UnsupportedYearException e) {
            fail("no error expected for 2000");
        }
    }

    @Test
    void containsYearHighUnsupportedYearTest() {
        try {
            assertEquals(-1, notEmpty.containsYear(5000));
            fail("UnsupportedYearException expected for 5000");
        } catch (UnsupportedYearException e) {
            //expected
        }
    }

    @Test
    void containsYearLowUnsupportedYearTest() {
        try {
            assertEquals(-1, notEmpty.containsYear(1000));
            fail("UnsupportedYearException expected for 5000");
        } catch (UnsupportedYearException e) {
            //expected
        }
    }

    @Test
    void getNumYears() {
        assertEquals(2, notEmpty.getNumYears());
        assertEquals(0, c.getNumYears());
    }

    @Test
    void toJsonEmptyCalendarTest() {
        JSONObject jsonObject = c.toJson();
        assertEquals(0, jsonObject.getJSONArray("years").length());
    }

    @Test
    void toJsonNonEmptyCalendarTest() {
        JSONObject jsonObject = notEmpty.toJson();
        assertEquals(2, jsonObject.getJSONArray("years").length());
    }
}