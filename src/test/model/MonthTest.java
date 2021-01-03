package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonthTest {

    private Year yr;
    private Task task1;
    private Task task2;
    private Task task3;
    private Day today;
    private Day tomorrow;
    private Day yesterday;

    @BeforeEach
    void setUp() {
        yr = new Year(2020);
    }

    @Test
    void getMonthCodeDecemberTest() {
        Month month = yr.getMonth(12);
        assertEquals(6, month.getMonthCode());
    }

    @Test
    void getMonthCodeJuneTest() {
        Month month = new Month(6, yr);
        assertEquals(5, month.getMonthCode());
    }

    @Test
    void getMonthCodeMarchTest() {
        Month month = new Month(3, yr);
        assertEquals(4, month.getMonthCode());
    }

    @Test
    void getDaysJanuaryTest() {
        Month month = yr.getMonth(1);
        assertEquals(31, month.getDays().size());
    }

    @Test
    void getDaysAprilTest() {
        Month month = yr.getMonth(4);
        assertEquals(30, month.getDays().size());
    }

    @Test
    void getDaysNotLeapFebruaryTest() {
        Year year = new Year(2021);
        Month month = year.getMonth(2);
        assertEquals(28, month.getDays().size());
    }

    @Test
    void getDaysLeapFebruaryTest() {
        Year year = new Year(2024);
        Month month = year.getMonth(2);
        assertEquals(29, month.getDays().size());
    }

    @Test
    void getTotalDurationWithTagTest() {
        Month mo = yr.getMonth(12);
        today = mo.getDay(14); // 14th october 2020
        tomorrow = mo.getDay(15); // 14th october 2020
        yesterday = mo.getDay(15); // 14th october 2020
        task1 = new Task("yoga", 14, 15, 16, 0, "relax");
        task2 = new Task("make food", 12, 0, 12, 30, "leftovers in the fridge");
        task3 = new Task("vacuum", 16, 18, 17, 0, "vacuum the laundry room");
        today.addTask(task1);
        tomorrow.addTask(task2);
        yesterday.addTask(task3);
        task1.addTag("exercise");
        task2.addTag("reminder");
        task3.addTag("chore");
        task3.addTag("exercise");
        assertTrue((146 < mo.getTotalDurationWithTag("exercise")));
        assertTrue((148 > mo.getTotalDurationWithTag("exercise")));
    }

    @Test
    void getNameDecemberTest() {
        assertEquals(yr.getMonth(12).getName(), "December");
    }

    @Test
    void getNameAprilTest() {
        assertEquals(yr.getMonth(4).getName(), "April");
    }

    @Test
    void monthToJsonDecemberTest() {
        Month mo = yr.getMonth(12);
        JSONObject jsonObject = mo.monthToJson();
        assertEquals(12, jsonObject.get("month"));
        assertEquals(31, jsonObject.getJSONArray("days").length());
    }

    @Test
    void monthToJsonAprilTest() {
        Month mo = yr.getMonth(4);
        JSONObject jsonObject = mo.monthToJson();
        assertEquals(4, jsonObject.get("month"));
        assertEquals(30, jsonObject.getJSONArray("days").length());
    }
}