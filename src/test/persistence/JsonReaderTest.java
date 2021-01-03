package persistence;

import exceptions.UnsupportedYearException;
import model.Calendar;
import model.Day;
import model.Year;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    void TestReaderNonExistentFile(){
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Calendar calendar = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        } catch (UnsupportedYearException e) {
            fail("IOException expected");
        }
    }

    @Test
    void testReaderEmptyCalendar(){
        JsonReader reader = new JsonReader("./data/testReaderEmptyCalendar.json");
        try {
            Calendar calendar = reader.read();
            assertEquals(0, calendar.getNumYears());
        } catch (IOException e) {
            fail("Couldn't read from file, No exception expected");
        } catch (UnsupportedYearException e) {
            fail("IOException expected");
        }
    }

    @Test
    void testReaderGeneralCalendar(){
        JsonReader reader = new JsonReader("./data/testReaderGeneralCalendar.json");
        try {
            Calendar calendar = reader.read();
            assertEquals(2, calendar.getNumYears()); //2000 and 2333
            Year year = null;
            try {
                year = calendar.findYearInCalendar(2000);
            } catch (UnsupportedYearException e) {
                fail("no exception expected");
            }
            assertEquals(12, year.getMonths().size());
            assertEquals(31, year.getMonth(10).getDays().size());
            Day day = year.getMonth(4).getDay(6);  //april 6 2000
            assertEquals(day.findTaskWithTitle("testTask1").size(), 1);
            assertEquals(day.findTaskWithTitle("testTask2").size(), 1);
            assertEquals(day.findTaskWithTag("testTag").size(), 2);
        } catch (IOException e) {
            fail("Couldn't read from file, No exception expected");
        } catch (UnsupportedYearException e) {
           fail("No exception expected");
        }
    }
}
