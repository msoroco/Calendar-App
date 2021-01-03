package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DayTest {

    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private Day today;
    Month mo;

    @BeforeEach
    void setUp() {
        Year yr = new Year(2020);
        mo = yr.getMonth(10);  //october 2020
        today = mo.getDay(14); // 14th october 2020
        task1 = new Task("yoga", 14, 15, 16, 0, "relax");
        task2 = new Task("make food", 12, 0, 12, 30, "leftovers in the fridge");
        task3 = new Task("vacuum", 16, 18, 17, 0, "vacuum the laundry room");
        task4 = new Task("yoga", 2, 15, 3, 0, "pigeon pose");

    }

    @Test
    void addTaskTest() {
        assertEquals(today.getTasks().size(), 0);
        today.addTask(task1);
        assertEquals(today.getTasks().size(), 1);
        assertTrue(today.getTasks().contains(task1));
    }

    @Test
    void removeTaskTest() {
        assertEquals(today.getTasks().size(), 0);
        today.addTask(task1);
        assertEquals(today.getTasks().size(), 1);
        assertTrue(today.getTasks().contains(task1));
        today.removeTask(task1);
        assertEquals(today.getTasks().size(), 0);
        assertFalse(today.getTasks().contains(task1));
    }

    @Test
    void getDayNameSundayTest() {
        //make tomorrow be the day 2020/10/04
        Year yr = new Year(2020);
        Month mo = new Month(10, yr);
        Day tomorrow = new Day(4, mo, yr);
        assertEquals(tomorrow.getDayName(), "Sunday");
    }

    @Test
    void getDayNameTuesdayTest() {
        //make tomorrow be the day 2020/10/22
        Year yr = new Year(2020);
        Month mo = yr.getMonth(10);
        Day tomorrow = mo.getDay(22);
        assertEquals(tomorrow.getDayName(), "Thursday");
    }

    @Test
    void getDayNameFridayTest() {
        //make tomorrow be the day 2020/10/22
        Year yr = new Year(2020);
        yr.setYear(2021);
        Month mo = yr.getMonth(10);
        Day tomorrow = mo.getDay(22);
        assertEquals(tomorrow.getDayName(), "Friday");
    }

    @Test
    void getDayNameWednesdayTest() {
        //make tomorrow be the day 2023/04/19
        Year yr = new Year(2023);
        Month mo = yr.getMonth(4);
        Day tomorrow = mo.getDay(19);
        assertEquals(tomorrow.getDayName(), "Wednesday");
    }

    @Test
    void getDayNameThursdayTest() {
        //make tomorrow be the day 2020/10/01
        Year yr = new Year(2020);
        Month mo = yr.getMonth(10);
        Day tomorrow = mo.getDay(1);
        assertEquals(tomorrow.getDayName(), "Thursday");
    }

    @Test
    void getTasksTest() {
        today.addTask(task1);
        assertEquals(today.getTasks().size(), 1);
    }

    @Test
    void findTaskWithTagTest(){
        today.addTask(task1);
        today.addTask(task2);
        today.addTask(task3);
        task1.addTag("exercise");
        task2.addTag("reminder");
        task3.addTag("chore");
        task3.addTag("exercise");
        assertEquals(2, today.findTaskWithTag("exercise").size());
        assertEquals(1, today.findTaskWithTag("chore").size());
        assertTrue(today.findTaskWithTag("chore").contains(task3));
        assertFalse(today.findTaskWithTag("chore").contains(task1));
    }

    @Test
    void getTotalDurationWithTagTest(){
        today.addTask(task1);
        today.addTask(task2);
        today.addTask(task3);
        task1.addTag("exercise");
        task2.addTag("reminder");
        task3.addTag("chore");
        task3.addTag("exercise");
        assertTrue((146 < today.getTotalDurationWithTag("exercise")));
        assertTrue((148 > today.getTotalDurationWithTag("exercise")));
        assertTrue(29 < today.getTotalDurationWithTag("reminder"));
        assertTrue(31 > today.getTotalDurationWithTag("reminder"));
    }

    @Test
    void findTaskWithTitleOneTest() {
        List<Task> lot3 = new ArrayList<>();
        lot3.add(task3);
        today.addTask(task1);
        today.addTask(task2);
        today.addTask(task3);
        today.addTask(task4);
        assertEquals(1, today.findTaskWithTitle("make food").size());
        assertEquals(lot3, today.findTaskWithTitle("vacuum"));
    }

    @Test
    void findTaskWithTitleManyTest() {
        List<Task> lot14 = new ArrayList<>();
        lot14.add(task1);
        lot14.add(task4);
        today.addTask(task1);
        today.addTask(task2);
        today.addTask(task3);
        today.addTask(task4);
        assertEquals(2, today.findTaskWithTitle("yoga").size());
        assertEquals(lot14, today.findTaskWithTitle("yoga"));
    }

    @Test
    void getNumberFourteenTest() {
        assertEquals(today.getNumber(), 14);
    }

    @Test
    void getNumberThreeTest() {
        assertEquals(mo.getDay(3).getNumber(), 3);
    }

    @Test
    void dayToJsonTodayThreeTasksTest() {
        today.addTask(task1);
        today.addTask(task2);
        today.addTask(task3);
        JSONObject jsonObject = today.dayToJson();
        assertEquals(14, jsonObject.get("day"));
        assertEquals(3, jsonObject.getJSONArray("tasks").length());
    }

    @Test
    void dayToJsonOtherDayFourTasksTest() {
        //make otherDay be the day 2020/10/01
        Year yr = new Year(2020);
        Month mo = yr.getMonth(10);
        Day otherDay = mo.getDay(1);
        otherDay.addTask(task1);
        otherDay.addTask(task2);
        otherDay.addTask(task3);
        otherDay.addTask(task4);
        JSONObject jsonObject = otherDay.dayToJson();
        assertEquals(1, jsonObject.get("day"));
        assertEquals(4, jsonObject.getJSONArray("tasks").length());
    }
}