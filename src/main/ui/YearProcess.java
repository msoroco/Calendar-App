package ui;

import exceptions.UnsupportedYearException;
import model.Calendar;
import model.Day;
import model.Month;
import model.Year;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;

/**
 * A YearProcess instantiates a new calendar.
 * <p>
 * This is where the file to be read from is chosen.
 * <p>
 * Prompts user for a year supported by this calendar (2000 to 2999).
 * When user exists the monthProcess (going backwards after making changes) they will be prompted if they want to
 * save their changes to the file.
 */

public class YearProcess extends JFrame implements ActionListener {

    public Calendar calendar;
    public Year year;
    public Month month;
    public Day day;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JLabel label;
    private JTextField field;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    public static final String JSON_STORE = "./data/calendar.json";

    //EFFECTS: Constructor; displays components and make the window visible and resizable.
    public YearProcess() {
        super("Agenda");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());

        calendar = new Calendar();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        initialize("Enter Year", "Enter Year (integer between 2000 and 2999)");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    //EFFECTS: adds JButton, JField, and JLabel for a user year input, and an image.
    public void initialize(String buttonLabel, String inputCommand) {
        JButton btn = new JButton(buttonLabel);
        btn.setActionCommand("myButton");
        btn.addActionListener(this);
        add(btn);
        label = new JLabel(inputCommand);
        field = new JTextField(5);
        add(field);
        add(label);
        add(makeImageComponent());
    }

    //EFFECTS: Creates a JLabel to show an image
    private JLabel makeImageComponent() {
        return new JLabel(new ImageIcon("./data/tobs.jpg"));
    }

    //EFFECTS: Check that user input is valid (integer within range), when user presses button. Display error message
    // if invalid. Start MonthProcess if valid.
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("myButton")) {
            if (!processCommand(field.getText())) {
                errorMessagePopup("Year error", "An invalid year was given.");
            }
        } else {
            processCommand(field.getText());
        }
    }

    //EFFECTS: Creates JFrame and shows error message
    private void errorMessagePopup(String title, String message) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
    }


    //EFFECTS: starts a month process with inputted year if year is within range supported by this app.
    public boolean processCommand(String command) {
        try {
            Integer.parseInt(command);
        } catch (NumberFormatException nfe) {
            return false;
        }
        int c = Integer.parseInt(command);
        if (c >= 2000 && c <= 2999) {
            findYear(c);
            return true;
        } else {
            return false;
        }
    }

    //MODIFIES: this
    //EFFECTS: checks to see if year was already instantiated. Instantiates new MonthProcess with year if found or with
    //          new year if not.
    private void findYear(int c) {
        loadCalendar();
        try {
            this.year = this.calendar.findYearInCalendar(c);
        } catch (UnsupportedYearException e) {
            e.printStackTrace();
        }
        new MonthProcess(year);
        saveCalendar();
    }

    // MODIFIES: this
    // EFFECTS: saves the calendar to file if user input is yes and shows success popup,
    //          otherwise shows changes unsaved popup
    private void saveCalendar() {
        if (0 == inputPopup("Save?", "Do you want to save your changes?")) {
            try {
                jsonWriter.open();
                jsonWriter.write(calendar);
                jsonWriter.close();
                messagePopup("Saved", "Calendar Saved!");
            } catch (FileNotFoundException e) {
                errorMessagePopup("Json Write Error", "Unable to write to file: " + JSON_STORE);
            }
        } else {
            messagePopup("Not Saved", "Changes discarded");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads calendar from file, show popup depending if successful or unsuccessful.
    private void loadCalendar() {
        if (0 == inputPopup("Load Calendar?", "Do you want to load the calendar in " + JSON_STORE + " ?")) {
            try {
                calendar = jsonReader.read();
                messagePopup("Source file", "Loaded calendar from " + JSON_STORE);
            } catch (IOException | UnsupportedYearException e) {
                errorMessagePopup("Json Read Error", "Unable to read from file: " + JSON_STORE);
            }
        }
    }

    //EFFECTS: Creates JFrame and prompts yes or no input
    private int inputPopup(String title, String message) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return JOptionPane.showOptionDialog(frame, message, title, JOptionPane.YES_NO_OPTION, QUESTION_MESSAGE,
                null, null, 0);
    }

    //EFFECTS: Creates JFrame and shows error message
    private void messagePopup(String title, String message) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}