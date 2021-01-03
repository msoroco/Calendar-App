package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private final String TITLE = "call Joe";
    private final int HOUR1 = 13;
    private final int MINUTE1 = 0;
    private final int HOUR2 = 14;
    private final int MINUTE2 = 30;
    private final Year YEAR = new Year(2020);
    private final String DESCRIPTION = "tell Joe about cats";

    Task task;

    @BeforeEach
    void setUp() {
        task = new Task(TITLE, HOUR1, MINUTE1, HOUR2, MINUTE2, DESCRIPTION);
        task.addTag("work");
        assertEquals(HOUR1, task.getStartHour());
        assertEquals(HOUR2, task.getEndHour());
        assertEquals(MINUTE1, task.getStartMinute());
        assertEquals(MINUTE2, task.getEndMinute());
    }

    @Test
    void ConstructorTest() {
        Task task1 = new Task(TITLE, HOUR1, MINUTE1, HOUR2, MINUTE2, DESCRIPTION);
    }

    @Test
    void changeTimeSuccessOneTest() {
        assertTrue(task.changeTime(2, 59, 18, 40));
        assertEquals(2, task.getStartHour());
        assertEquals(18, task.getEndHour());
        assertEquals(59, task.getStartMinute());
        assertEquals(40, task.getEndMinute());
    }

    @Test
    void changeTimeSuccessTwoTest() {
        assertTrue(task.changeTime(6, 54, 12, 45));
        assertEquals(6, task.getStartHour());
        assertEquals(12, task.getEndHour());
        assertEquals(54, task.getStartMinute());
        assertEquals(45, task.getEndMinute());
    }

    @Test
    void changeTimeFailTest() {
        assertFalse(task.changeTime(5, 3, 1, 30));
        assertEquals(HOUR1, task.getStartHour());
        assertEquals(HOUR2, task.getEndHour());
        assertEquals(MINUTE1, task.getStartMinute());
        assertEquals(MINUTE2, task.getEndMinute());
    }

    @Test
    void removeTagTest() {
        assertEquals(1, task.getTags().size());
        task.removeTag("work");
        assertEquals(0, task.getTags().size());
    }

    @Test
    void addTagTest() {
        assertEquals(1, task.getTags().size());
        task.addTag("reminder");
        assertEquals(2, task.getTags().size());
        assertTrue(task.getTags().contains("work"));
        assertTrue(task.getTags().contains("reminder"));
    }

    @Test
    void changeTitleTest() {
        assertEquals(TITLE, task.getTitle());
        task.setTitle("email Joe");
        assertEquals("email Joe", task.getTitle());
    }

    @Test
    void getDuration() {
        assertEquals(90, task.getDuration());
    }

    @Test
    void removeTagTrueTest() {
        assertEquals(1, task.getTags().size());
        assertTrue(task.removeTag("work"));
        assertEquals(0, task.getTags().size());
    }

    @Test
    void removeTagFalseTest() {
        assertEquals(1, task.getTags().size());
        assertFalse(task.removeTag("urgent"));
        assertEquals(1, task.getTags().size());
    }

    @Test
    void addTagUrgentTest() {
        assertEquals(1, task.getTags().size());
        task.addTag("urgent");
        assertEquals(2, task.getTags().size());
        assertTrue(task.getTags().contains("work"));
        assertTrue(task.getTags().contains("urgent"));
    }

    @Test
    void addTagReminderTest() {
        assertEquals(1, task.getTags().size());
        task.addTag("reminder");
        assertEquals(2, task.getTags().size());
        assertTrue(task.getTags().contains("reminder"));
        assertTrue(task.getTags().contains("work"));
    }

    @Test
    void setTitle1Test() {
        assertEquals("call Joe", task.getTitle());
        task.setTitle("email Joe");
        assertEquals("email Joe", task.getTitle());
    }

    @Test
    void setTitle2Test() {
        assertEquals("call Joe", task.getTitle());
        task.setTitle("text Joe");
        assertEquals("text Joe", task.getTitle());
    }

    @Test
    void validTimeFrameInvalidHoursTest() {
        assertFalse(task.validTimeFrame(2, 30, 1, 45));
    }

    @Test
    void validTimeFrameInvalidMinutesTest() {
        assertFalse(task.validTimeFrame(2, 30, 2, 5));
    }

    @Test
    void validTimeFrameInvalidSameMinutesTest() {
        assertFalse(task.validTimeFrame(2, 30, 2, 30));
    }

    @Test
    void validTimeFrameHoursUnderRangeTest() {
        assertFalse(task.validTimeFrame(-1, 30, 2, 30));
    }

    @Test
    void validTimeFrameHoursOverRangeTest() {
        assertFalse(task.validTimeFrame(7, 30, 56, 30));
    }

    @Test
    void validTimeFrameMinuteOneUnderRangeTest() {
        assertFalse(task.validTimeFrame(5, -6, 15, 30));
    }

    @Test
    void validTimeFrameMinutesOneOverRangeTest() {
        assertFalse(task.validTimeFrame(7, 80, 17, 00));
    }

    @Test
    void validTimeFrameMinuteTwoUnderRangeTest() {
        assertFalse(task.validTimeFrame(5, 00, 15, -6));
    }

    @Test
    void validTimeFrameMinuteTwoOverRangeTest() {
        assertFalse(task.validTimeFrame(7, 30, 17, 80));
    }

    @Test
    void validTimeFrameValidHoursTest() {
        assertTrue(task.validTimeFrame(1, 30, 2, 45));
    }

    @Test
    void validTimeFrameValidMinutesTest() {
        assertTrue(task.validTimeFrame(6, 30, 6, 45));
    }

    @Test
    void validTimeFrameInvalidHoursTwoTest() {
        assertFalse(task.validTimeFrame(HOUR1, MINUTE1, 5, MINUTE2));
    }

    @Test
    void validTimeFrameInvalidMinutesTwoTest() {
        assertFalse(task.validTimeFrame(HOUR1, 40, HOUR1, MINUTE2));
    }

    @Test
    void getDescription() {
        assertEquals("tell Joe about cats", task.getDescription());
    }

    @Test
    void setDescriptionTest1() {
        assertEquals("tell Joe about cats", task.getDescription());
        task.setDescription("remind Joe to call Sue");
        assertEquals("remind Joe to call Sue", task.getDescription());
    }

    @Test
    void setDescriptionTest2() {
        assertEquals("tell Joe about cats", task.getDescription());
        task.setDescription("remind Joe to call Susan");
        assertEquals("remind Joe to call Susan", task.getDescription());
    }

    @Test
    void taskToJsonTask1Test(){
        JSONObject jsonObject = task.taskToJson();
        assertEquals(TITLE, jsonObject.get("title"));
        assertEquals(HOUR1, jsonObject.get("startHour"));
        assertEquals(MINUTE1, jsonObject.get("startMinute"));
        assertEquals(HOUR2, jsonObject.get("endHour"));
        assertEquals(MINUTE2, jsonObject.get("endMinute"));
        assertEquals(DESCRIPTION, jsonObject.get("description"));
        assertEquals(1, jsonObject.getJSONArray("tags").length());
    }

    @Test
    void taskToJsonTask2Test(){
        JSONObject jsonObject = makeTask().taskToJson();
        assertEquals("test title", jsonObject.get("title"));
        assertEquals(1, jsonObject.get("startHour"));
        assertEquals(2, jsonObject.get("startMinute"));
        assertEquals(3, jsonObject.get("endHour"));
        assertEquals(4, jsonObject.get("endMinute"));
        assertEquals("test description", jsonObject.get("description"));
        assertEquals(2, jsonObject.getJSONArray("tags").length());
    }

    private Task makeTask() {
        Task task2 = new Task("test title", 1, 2, 3, 4, "test description");
        task2.addTag("test tag1");
        task2.addTag("test tag2");
        return task2;
    }
}