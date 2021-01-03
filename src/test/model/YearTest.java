package model;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YearTest {

    @Test
    void getYearCode20Test() {
        Year year = new Year(2021);
        int yy = 21;
        assertEquals(21, year.getYearCode());
    }

    @Test
    void getYearCode27Test() {
        Year year = new Year(2027);
        int yy = 27;
        assertEquals(27, year.getYearCode());
    }

    @Test
    void getLeapYearCode1992Test() {
        Year year = new Year(1992);
        assertEquals(1, year.getLeapYearCode());
    }

    @Test
    void getLeapYearCode2000Test() {
        Year year = new Year(2000);
        assertEquals(1, year.getLeapYearCode());
    }

    @Test
    void getLeapYearCode1900Test() {
        Year year = new Year(1900);
        assertEquals(0, year.getLeapYearCode());
    }

    @Test
    void getLeapYearCode1Test() {
        Year year = new Year(2020);
        assertEquals(1, year.getLeapYearCode());
    }

    @Test
    void getLeapYearCode0Test() {
        Year year = new Year(2021);
        assertEquals(0, year.getLeapYearCode());
    }

    @Test
    void setYear2021Test() {
        Year yr = new Year(2020);
        assertEquals(1, yr.getLeapYearCode());
        yr.setYear(2021);
        assertEquals(2021, yr.getYear());
        assertEquals(0, yr.getLeapYearCode());
        assertEquals(12, yr.getMonths().size());
    }

    @Test
    void setYear2024Test() {
        Year yr = new Year(2020);
        assertEquals(1, yr.getLeapYearCode());
        yr.setYear(2024);
        assertEquals(2024, yr.getYear());
        assertEquals(1, yr.getLeapYearCode());
        assertEquals(12, yr.getMonths().size());
    }

    @Test
    void yearToJson2020Test() {
        Year yr = new Year(2020);
        JSONObject jsonObject = yr.yearToJson();
        assertEquals(2020, jsonObject.get("year"));
        assertEquals(12, jsonObject.getJSONArray("months").length());
    }

    @Test
    void yearToJson2550Test() {
        Year yr = new Year(2550);
        JSONObject jsonObject = yr.yearToJson();
        assertEquals(2550, jsonObject.get("year"));
        assertEquals(12, jsonObject.getJSONArray("months").length());
    }
}