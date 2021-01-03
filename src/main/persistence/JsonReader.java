package persistence;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import exceptions.UnsupportedYearException;
import model.*;
import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Calendar read() throws IOException, UnsupportedYearException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCalendar(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Calendar from JSON object and returns it
    private Calendar parseCalendar(JSONObject jsonObject) throws UnsupportedYearException {
        Calendar calendar = new Calendar();
        generateCalendar(calendar, jsonObject);
        addTasksToCalendar(calendar, jsonObject);
        return calendar;
    }

    //EFFECTS: parses years from JSON array, adds them to calendar
    private void generateCalendar(Calendar calendar, JSONObject jsonObject) throws UnsupportedYearException {
        JSONArray jsonArray = jsonObject.getJSONArray("years");
        for (Object json : jsonArray) {
            JSONObject nextYear = (JSONObject) json;
            addYearToCalendar(calendar, nextYear);
        }
    }

    //EFFECTS: parses year from JSON object and adds it to calendar
    private void addYearToCalendar(Calendar calendar, JSONObject nextYear) throws UnsupportedYearException {
        int year = nextYear.getInt("year");
        calendar.findYearInCalendar(year);
    }

    //EFFECTS: parses years in JSON array to find tasks
    private void addTasksToCalendar(Calendar calendar, JSONObject jsonObject) throws UnsupportedYearException {
        JSONArray jsonArray = jsonObject.getJSONArray("years");
        for (Object json : jsonArray) {
            JSONObject nextYear = (JSONObject) json;
            int yr = nextYear.getInt("year");
            Year year = calendar.findYearInCalendar(yr);
            searchMonths(year, nextYear);
        }
    }

    //EFFECTS: parses months in JSON array to find tasks
    private void searchMonths(Year year, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("months");
        for (Object json : jsonArray) {
            JSONObject nextMonth = (JSONObject) json;
            int mo = nextMonth.getInt("month");
            Month month = year.getMonth(mo);
            searchDays(month, nextMonth);
        }
    }

    //EFFECTS: parses days in JSON array to find tasks
    private void searchDays(Month month, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("days");
        for (Object json : jsonArray) {
            JSONObject nextDay = (JSONObject) json;
            int num = nextDay.getInt("day");
            Day day = month.getDay(num);
            searchTasks(day, nextDay);
        }
    }

    //EFFECTS: parses tasks in JSON array from day to find tasks
    private void searchTasks(Day day, JSONObject nextDay) {
        JSONArray jsonArray = nextDay.getJSONArray("tasks");
        for (Object json : jsonArray) {
            JSONObject nextTask = (JSONObject) json;
            addTask(day, nextTask);
        }
    }

    //EFFECTS: parses task from JSON object and adds it to day
    private void addTask(Day day, JSONObject nextTask) {
        String title = nextTask.getString("title");
        int sh = nextTask.getInt("startHour");
        int sm = nextTask.getInt("startMinute");
        int eh = nextTask.getInt("endHour");
        int em = nextTask.getInt("endMinute");
        String d = nextTask.getString("description");
        Task t = new Task(title, sh, sm, eh, em, d);
        addTags(t, nextTask);
        day.addTask(t);
    }

    //EFFECTS: parses tags from JSON array and adds it to task
    private void addTags(Task t, JSONObject nextTask) {
        JSONArray jsonArray = nextTask.getJSONArray("tags");
        for (Object json : jsonArray) {
            String tag = json.toString();
            t.addTag(tag);
        }
    }
}