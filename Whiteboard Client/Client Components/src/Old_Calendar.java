import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class Old_Calendar extends JFrame {
    DefaultTableModel model = new DefaultTableModel()
    {
        @Override
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    };
    GregorianCalendar cal = new GregorianCalendar();
    JLabel label;
    JTextField events;
    private String[][] eventsText = new String[12][31];

    public JPanel BuildCalendarPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        events = new JTextField("Event Description");
        events.setHorizontalAlignment(SwingConstants.CENTER);
        events.setEditable(false);
        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JButton b1 = new JButton("<-");
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cal.add(GregorianCalendar.MONTH, -1);
                updateMonth();
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 2;

        panel.add(b1, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        panel.add(label, gbc);

        JButton b2 = new JButton("->");
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cal.add(GregorianCalendar.MONTH, +1);
                updateMonth();
            }
        });

        gbc.gridx = 3;
        gbc.gridy = 2;
        panel.add(b2, gbc);

        String[] columns = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        model = new DefaultTableModel(null, columns);
        JTable table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                int day = (Integer) table.getValueAt(row, col);
                int month = cal.get(GregorianCalendar.MONTH);

                events.setText(eventsText[month][day]);

            }
        });

        gbc.gridx = 2;
        gbc.gridy = 3;

        panel.add(pane, gbc);

        JButton b3 = new JButton("Add Event");
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int m = Integer.parseInt(JOptionPane.showInputDialog("Please enter the month of your event as a number from 1 to 12")) - 1;
                int d = Integer.parseInt(JOptionPane.showInputDialog("Please enter the day of your event"));
                String s = JOptionPane.showInputDialog("Please enter a short description of your event");
                eventsText[m][d] = s;
                //events.setText(eventsText[m][d]);

            }
        });
        gbc.gridx = 2;
        gbc.gridy = 4;

        panel.add(b3, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;

        panel.add(events, gbc);


        //ApplicationGUI gui = new ApplicationGUI();

        JButton home = new JButton("Home");
        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
               // gui.ChangePanel("homePanel", comToken???);
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(home, gbc);

        updateMonth();

        return panel;
    }

    void updateMonth() {
        cal.set(GregorianCalendar.DAY_OF_MONTH, 1);

        String month = cal.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.LONG, Locale.US);
        int year = cal.get(GregorianCalendar.YEAR);
        label.setText(month + " " + year);

        int startDay = cal.get(GregorianCalendar.DAY_OF_WEEK);
        int numberOfDays = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        int weeks = cal.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);

        model.setRowCount(0);
        model.setRowCount(weeks);

        int i = startDay - 1;
        for (int day = 1; day <= numberOfDays; day++) {
            model.setValueAt(day, i / 7, i % 7);
            i = i + 1;
        }
    }

    public static void main(String args[])
    {
        JFrame frame = new JFrame();
        Old_Calendar bills = new Old_Calendar();
        frame.add(bills.BuildCalendarPanel());
        frame.setTitle("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
