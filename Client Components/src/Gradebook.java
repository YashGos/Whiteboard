import org.jdesktop.swingx.JXFormattedTextField;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class Gradebook
{
    public JPanel BuildGradebookPanel(String courseName)
    {
        JPanel gradebookPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;

        if((ApplicationGUI.GetSessionInformation()[2]).contains("S"))
        {
            DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Assignment Name", "Type", "Grade"}, 0);

            JTable gradeTable = new JTable(tableModel);

            try
            {
                byte[] responseBytes = COM.COM("GetGrades;" + ApplicationGUI.GetSessionInformation()[0] + ';' + ApplicationGUI.GetSessionInformation()[1] + ';' + courseName);
                String[] response = (new String(responseBytes, "UTF-8")).split(";");

                for (int i = 0; i < response.length; i++) {
                    DefaultTableModel currentModel = (DefaultTableModel) gradeTable.getModel();
                    currentModel.addRow(new Object[]{response[i * 2], response[(i * 2) + 1], response[(i * 2) + 2]});
                }
            }
            catch (Exception e)
            {
                //
            }

            JScrollPane scrollPane = new JScrollPane(gradeTable);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            scrollPane.setPreferredSize(new Dimension(screenSize.width / 4, screenSize.height / 4));

            gradebookPanel.add(scrollPane);

            gridConstraints.gridx = 0;
            gridConstraints.gridy = 2;
        }
        else if ((ApplicationGUI.GetSessionInformation()[2]).contains("I"))
        {
            try
            {
                byte[] responseBytes = COM.COM("GetAssociatedAssignments;" + ApplicationGUI.GetSessionInformation()[0] + ';' + ApplicationGUI.GetSessionInformation()[1] + ';' + courseName);
                String[] response = (new String(responseBytes, "UTF-8")).split(";");


                DefaultListModel listModel = new DefaultListModel();

                for (int i = 0; i < response.length; i++)
                {
                    if(i*3 < response.length)
                    {
                        listModel.addElement(response[i*3]);
                    }
                }

                JList associatedAssignments = new JList(listModel);

                gridConstraints.gridx = 2;
                gridConstraints.gridy = 1;
                gridConstraints.gridwidth = 2;

                gradebookPanel.add(associatedAssignments, gridConstraints);

                JLabel newAssignmentGraded_label = new JLabel("Select Assignment:");
                gridConstraints.gridx = 0;
                gridConstraints.gridy = 1;
                gradebookPanel.add(newAssignmentGraded_label, gridConstraints);

                JLabel newUserGraded_label = new JLabel("Input username for Grade:");
                gridConstraints.gridx = 0;
                gridConstraints.gridy = 4;
                gradebookPanel.add(newUserGraded_label, gridConstraints);

                JLabel newGrade_label = new JLabel("Input grade (percent):");
                gridConstraints.gridx = 0;
                gridConstraints.gridy = 5;
                gradebookPanel.add(newGrade_label, gridConstraints);

                JXFormattedTextField newUserGraded = new JXFormattedTextField();
                newUserGraded.setPreferredSize(new Dimension(100,20));
                gridConstraints.gridx = 2;
                gridConstraints.gridy = 4;
                gradebookPanel.add(newUserGraded, gridConstraints);

                JXFormattedTextField newGrade = new JXFormattedTextField();
                gridConstraints.gridx = 2;
                gridConstraints.gridy = 5;
                gradebookPanel.add(newGrade, gridConstraints);

                JButton submitChanges = new JButton("Submit Grade");
                submitChanges.addActionListener((ActionEvent e) ->
                {
                    if(newUserGraded.isValid() && newGrade.isValid() && !associatedAssignments.isSelectionEmpty())
                    {
                        new Thread(() ->
                        {
                            //
                        }
                        ).start();
                    }
                });
                gridConstraints.gridx = 2;
                gridConstraints.gridy = 6;
                gradebookPanel.add(submitChanges, gridConstraints);

                gridConstraints.gridx = 0;
                gridConstraints.gridy = 6;
            }
            catch (Exception e)
            {
                //
            }
        }

        JButton returnToPrevious = new JButton("Back");
        returnToPrevious.addActionListener((ActionEvent e) ->
        {
            gradebookPanel.removeAll();
            ApplicationGUI.ChangePanel("coursePanel");
        });

        gradebookPanel.add(returnToPrevious, gridConstraints);

        return gradebookPanel;
    }
}