package persistence;

import exceptions.UnsupportedYearException;
import model.Calendar;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Calendar calendar = new Calendar();
            JsonWriter writer = new JsonWriter("./data/my\\0illegal:fileName.json");
            writer.open();
            fail("IO exception was expected");
        } catch (IOException e) {
            //expected
        }
    }

    @Test
    void testWriterEmptyCalendar() {
        try {
            Calendar calendar = new Calendar();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCalendar.json");
            writer.open();
            writer.write(calendar);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCalendar.json");
            calendar = reader.read();
            assertEquals(0, calendar.getNumYears());
        } catch (IOException | UnsupportedYearException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralCalendar() {
        try {
            Calendar calendar = new Calendar();
            try {
                calendar.findYearInCalendar(2020);
            } catch (UnsupportedYearException e) {
                e.printStackTrace();
            }
            try {
                calendar.findYearInCalendar(2019);
            } catch (UnsupportedYearException e) {
                e.printStackTrace();
            }
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralCalendar.json");
            writer.open();
            writer.write(calendar);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralCalendar.json");
            calendar = reader.read();
            assertEquals(2, calendar.getNumYears());
            assertEquals(12, calendar.findYearInCalendar(2019).getMonths().size());
            assertEquals("March", calendar.findYearInCalendar(2019).getMonth(3).getName());
            assertEquals("Sunday", calendar.findYearInCalendar(2019).getMonth(3).getDay(3).getName());
        } catch (IOException | UnsupportedYearException e) {
            fail("Exception should not have been thrown");
        }
    }

}
