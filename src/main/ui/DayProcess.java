package ui;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

import static javax.swing.BorderFactory.createEmptyBorder;

/**
 * A DayProcess carries over the input given in MonthProcess. Focuses on a single day, or a tag in this month.
 * <p>
 * The user is prompted for a day or a tag. If input corresponds to one of the days in the month,
 * the titles of the tasks are printed if there any or it prints that no tasks were found.
 * If the input corresponds with a tag, the total sum of durations of events with that tag will be printed.
 * If the input isn't valid, an error will appear.
 * The user can start a new DayProcess simply by entering new input in the existing JTextField and using the
 * corresponding button.
 * <p>
 * A new TaskListProcess is instantiated when all the printing is complete. This is displayed as a JPanel in this
 * JDialogue.
 */

public class DayProcess extends JDialog implements ActionListener {

    public Calendar calendar;
    Year year;
    Month month;
    Day day;
    Set<String> tags;

    private JLabel label;
    private JTextField field;
    private JLabel chosenDate = new JLabel();
    private JLabel youSelected = new JLabel();
    private JTextArea printTasks = new JTextArea();
    private JLabel monthDuration = new JLabel();
    private JPanel taskListProcess = new JPanel();
    private Box box;

    private String commandFromMonthProcess;

    //EFFECTS: Constructor; displays components and make the window visible and resizable.
    public DayProcess(Year year, Month month, Set<String> tags, String monthProcess) {
        this.month = month;
        this.year = year;
        this.tags = tags;
        this.commandFromMonthProcess = monthProcess;
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(YearProcess.WIDTH, YearProcess.HEIGHT));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());
        setResizable(true);
        initialize("Enter", "Enter day (integer between 0 and " + month.getDays().size()
                + ") or enter a tag to see duration of events in this month with that tag.\n");
        pack();
        setLocationRelativeTo(null);
        processMonthProcessInput();
        setVisible(true);
    }

    //EFFECTS: Processes the input from MonthProcess to start DayProcess, error popup if input is not a day nor a tag
    //          in this month.
    private void processMonthProcessInput() {
        if (!processCommand(commandFromMonthProcess)) {
            errorMessagePopup();
        }
    }

    //MODIFIES: this
    //EFFECTS: Add button to re-enter days or tags. display selected dates, prints tasks in this day, and/or duration of
    //          tasks with input tag in this month
    public void initialize(String buttonLabel, String inputCommand) {
        JButton btn = new JButton(buttonLabel);
        btn.setActionCommand("myButton");
        btn.addActionListener(this);
        add(btn);
        label = new JLabel(inputCommand + "\n");
        field = new JTextField(5);
        chosenDate.setFont(new Font("ALGERIAN", Font.PLAIN, 20));
        box = Box.createVerticalBox();
        box.setBorder(createEmptyBorder(13, 13, 13, 13));
        add(field);
        field.setText(commandFromMonthProcess);
        add(label);
        add(box);
        youSelected.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        box.add(youSelected);
        youSelected.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(chosenDate);
        chosenDate.setAlignmentX(Component.CENTER_ALIGNMENT);
        JScrollPane p = new JScrollPane(printTasks);
        p.setPreferredSize(new Dimension(200, 100));
        box.add(p);
        box.add(monthDuration);
        monthDuration.setBorder(createEmptyBorder(13, 13, 13, 13));
    }

    //EFFECTS: gets day in month inputted by user. Returns true if successful, else false. Displays selected date, and
    //          tasks in this day.
    //MODIFIES: this
    protected boolean processCommand(String command) {
        if (checkTags(command)) {
            return true;
        } else {
            try {
                int day = Integer.parseInt(command);
                if (day <= month.getDays().size() && day > 0) {
                    this.day = month.getDay(day);
                    youSelected.setText("You selected: \n");
                    chosenDate.setText(this.day.getName() + " " + this.month.getName() + " "
                            + this.day.getNumber() + ", " + this.year.getYear());
                    displayTasks();
                    return true;
                } else {
                    return false;
                }
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
    }

    //REQUIRES: tags not null
    //EFFECTS: finds the given tag in this month, gets duration of tasks with that tag in month, returns true if
    //          successful, else false
    private boolean checkTags(String command) {
        for (String s : tags) {
            if (s.equals(command)) {
                getDurationWithTag(command);
                return true;
            }
        }
        return false;
    }

    //REQUIRES: A tag in this month
    //EFFECTS: prints the duration of all tasks with given tag
    private void getDurationWithTag(String command) {
        monthDuration.setText("\nTotal duration of time with tag " + command + " in this month:   "
                + month.getTotalDurationWithTag(command) + " minutes\n");

    }

    //MODIFIES: this
    //EFFECTS: prints the titles of tasks in this day. Instantiate TaskListProcess.
    private void displayTasks() {
        StringBuilder toPrint = new StringBuilder("Tasks for this day: \n\n");
        List<Task> tasks = day.getTasks();
        if (tasks.isEmpty()) {
            toPrint.append("\n\t none.");
        }
        for (Task t : tasks) {
            toPrint.append("\n\t> " + t.getTitle());
        }
        printTasks.setText(toPrint.toString());
        printTasks.setCaretPosition(0);
        add(newTaskListProcess());
        revalidate();
        repaint();
    }

    //MODIFIES: this
    //EFFECTS: clear taskListProcess panel (in case this is is a new input day in this DayProcess,
    //          deletes old duplicate info). Starts TaskListProcess (new panel).
    public JPanel newTaskListProcess() {
        remove(taskListProcess);
        taskListProcess = new JPanel();
        taskListProcess.add(new TaskListProcess(year, month, day, tags));
        return taskListProcess;
    }

    //EFFECTS: Day input button. If input is a valid day or tag in this month, then process command, else,
    //         display error popup.
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("myButton")) {
            if (!processCommand(field.getText())) {
                errorMessagePopup();
            }
        }
    }

    //EFFECTS: Creates JFrame and shows error message
    private void errorMessagePopup() {
        JFrame frame = new JFrame("Date Error");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame,
                "An invalid date was given or tag could not be found.", "Date error",
                JOptionPane.ERROR_MESSAGE);
    }
}
