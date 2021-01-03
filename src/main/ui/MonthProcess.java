package ui;

import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A MonthProcess is a process charge of finding the user requested month in this year.
 * <p>
 * Shows a single field and button which is used for user to input a month in the year.
 * <p>
 * Display all the days of the months. Weekday codes were used to determine where the MonthProcess should print the
 * first day of the month (for example, not all months start on Sunday).
 * <p>
 * All the tags of events saved in the days of this month are printed above the month.
 * <p>
 * The parts of DayProcess that involve prompting user for input (a day in this month, or a string) appears after the
 * MonthProcess is finished (and the tags and month calendar is printed). A new DayProcess is instantiated when the
 * user presses the DayProcess button after month info is finished printing. The user also has the option instead to
 * print a new month by re-entering information for the MonthProcess.
 */

public class MonthProcess extends JDialog implements ActionListener {

    public Calendar calendar;
    private Year year;
    private Month month;

    private JLabel label;
    private JLabel messages;
    private JTextField field;
    private JTextField dayField = new JTextField(10);
    private JTextArea console = new JTextArea();
    private JTextArea tagsConsole = new JTextArea();
    private Box box;
    private JPanel dayInputPanel = new JPanel();
    private JLabel enterDay = new JLabel();

    //EFFECTS: Constructor; displays components and make the JDialogue window modal, visible and resizable.
    public MonthProcess(Year year) {
        this.year = year;
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(YearProcess.WIDTH, YearProcess.HEIGHT));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        setResizable(true);
        initialize("Enter Month", "Enter Month (integer between 0 and 12)");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: adds the enter month JButton along with its JLabel and JField.
    //          Adds JLabel and box to display other components.
    public void initialize(String buttonLabel, String inputCommand) {
        JButton btn = new JButton(buttonLabel);
        btn.setActionCommand("myButton");
        btn.addActionListener(this);
        add(btn);
        label = new JLabel(inputCommand + "\n");
        field = new JTextField(5);
        messages = new JLabel("");
        add(field);
        add(label);
        add(messages);
        box = Box.createVerticalBox();
        JScrollPane p = new JScrollPane(tagsConsole);
        p.setPreferredSize(new Dimension(450, 200));
        box.add(p);
        box.add(console);
        box.add(makeDayPanel());
        console.setVisible(false);
        add(box);
        pack();
    }

    //EFFECTS: When JButton is pressed, check if input is integer corresponding to a month. If so, instantiate
    //          DayProcess, else show error popup.
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("myButton")) {
            if (!processCommand(field.getText())) {
                errorMessagePopup();
            }
        }
        if (e.getActionCommand().equals("myButton2")) {
            new DayProcess(year, month, tagsInThisMonth(), dayField.getText());
        }
    }

    //EFFECTS: Creates JFrame and shows error message
    private void errorMessagePopup() {
        JFrame frame = new JFrame("Month Error");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(frame,
                "An invalid month was given.",
                "Month error",
                JOptionPane.ERROR_MESSAGE);
    }

    //MODIFIES: this
    //EFFECTS: finds the month associated with given input, in this year. adds it to this month. returns false if
    //          command is invalid.
    public boolean processCommand(String command) {
        try {
            Integer.parseInt(command);
        } catch (NumberFormatException nfe) {
            return false;
        }
        int month = Integer.parseInt(command);
        if (month <= 12 && month > 0) {
            this.month = year.getMonth(month);
            printConsoles();
            return true;
        } else {
            return false;
        }
    }

    //MODIFIES: this
    //EFFECTS: Displays which month user selected, Format and display console for showing month.
    private void printConsoles() {
        messages.setText("\n You selected: " + this.month.getName());
        Font font = new Font("monospaced", Font.BOLD, 14);
        console.setFont(font);
        console.append("\n\n");
        console.setVisible(true);
        viewMonth();
    }

    //EFFECTS: prints out the month, prints out the tags in this month. Prepares DayProcess by prompting a day input.
    private void viewMonth() {
        displayDays();
        viewTags(tagsInThisMonth());
        displayDayButton();
    }

    //MODIFIES: this
    //EFFECTS: make (temporarily Invisible) JPanel for user to enter a Day in this month via a field and button.
    //          Adds it to box.
    private JPanel makeDayPanel() {
        dayInputPanel.setLayout(null);
        JButton btn = new JButton("Enter");
        btn.setActionCommand("myButton2");
        btn.addActionListener(this);
        dayInputPanel.setLayout(new BoxLayout(dayInputPanel, BoxLayout.X_AXIS));
        dayInputPanel.add(enterDay);
        dayInputPanel.add(dayField);
        dayInputPanel.add(btn);
        box.add(dayInputPanel);
        dayInputPanel.setVisible(false);
        return dayInputPanel;
    }

    //MODIFIES: this
    //EFFECTS: Set panel for Day input to visible for user input. Update message to correspond with inputted month.
    private void displayDayButton() {
        dayInputPanel.setVisible(true);
        enterDay.setText("Enter day (integer between 0 and " + month.getDays().size()
                + ") or enter a tag to see duration of events in this month with that tag.\n" + "\n");
    }

    //EFFECTS: find the tags in the events of this month.
    private Set<String> tagsInThisMonth() {
        Set<String> tags = new HashSet<>();
        for (Day d : month.getDays()) {
            for (Task t : d.getTasks()) {
                tags.addAll(t.getTags());
            }
        }
        return tags;
    }

    //MODIFIES: this
    //EFFECTS: prints tags found in this month.
    private void viewTags(Set<String> tags) {
        StringBuilder str = new StringBuilder();
        Font font = new Font("", Font.BOLD, 14);
        tagsConsole.setFont(font);
        str.append("Tags in this month: \n");
        for (String s : tags) {
            str.append("\t>     ").append(s).append("\n ");
        }
        tagsConsole.setText(str.toString());
        tagsConsole.setCaretPosition(0);
    }

    //MODIFIES: this
    //EFFECTS: prints the list of day names, gets the first day of this month
    private void displayDays() {
        List<Day> days = month.getDays();
        String print = "\n" + this.month.getName() + " " + this.year.getYear() + "\n\n" + printDayNames() + "\n"
                + organizeDays(days.get(0).getName());
        console.setText(print);
    }

    //REQUIRES: A string that is one of the names of the days of the week
    //EFFECTS: generates and index code for that day
    private String organizeDays(String s) {
        switch (s) {
            case "Monday":
                return printDays(1);
            case "Tuesday":
                return printDays(2);
            case "Wednesday":
                return printDays(3);
            case "Thursday":
                return printDays(4);
            case "Friday":
                return printDays(5);
            case "Saturday":
                return printDays(6);
            default:
                return printDays(0);
        }
    }

    //REQUIRES: and index code in 0 - 6 representing a day of the week
    //EFFECTS: prints out all the days of the month with the first day starting under the appropriate day name based
    //          on the index code given (adds appropriate spacing), formatted for console output
    private String printDays(int i) {
        StringBuilder str = new StringBuilder();
        int s = month.getDays().size();
        for (int k = 0; k < i; k++) {
            str.append("           ");
        }
        for (int j = 1; j <= s; j++) {
            if (j < 10) {
                if ((j + i) % 7 == 0) {
                    str.append("     ").append(j).append("     \n");
                } else {
                    str.append("     ").append(j).append("     ");
                }
            } else {
                if ((j + i) % 7 == 0) {
                    str.append("    ").append(j).append("     \n");
                } else {
                    str.append("    ").append(j).append("     ");
                }
            }
        }
        return String.valueOf(str);

    }

    //EFFECTS: Prints out the days of the week in order
    private String printDayNames() {
        return ("   Sunday  " + "   Monday  " + "  Tuesday  " + " Wednesday " + "  Thursday " + "  Friday   "
                + "  Saturday \n");
    }
}