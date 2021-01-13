import org.jdesktop.swingx.JXFormattedTextField;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserCreation
{
    public JPanel BuildUserCreationPanel()
    {
        JPanel userCreationPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel newUserDisplayName_label = new JLabel("Set User's Display Name:");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 4;
        userCreationPanel.add(newUserDisplayName_label, gridConstraints);

        JLabel newUserNameEntry_label = new JLabel("Set User's Account Name:");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 5;
        userCreationPanel.add(newUserNameEntry_label, gridConstraints);


        JLabel newPasswordEntry_label = new JLabel("Set User's Initial Password:");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 6;
        userCreationPanel.add(newPasswordEntry_label, gridConstraints);

        JLabel newPasswordConfirmation_label = new JLabel("Confirm User's Initial Password: ");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 7;
        userCreationPanel.add(newPasswordConfirmation_label, gridConstraints);

        JLabel newUserRole_label = new JLabel("Set User's Role:");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 8;
        userCreationPanel.add(newUserRole_label, gridConstraints);

        JXFormattedTextField newUserDisplayName = new JXFormattedTextField();
        newUserDisplayName.setPreferredSize(new Dimension(100,20));
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 4;
        userCreationPanel.add(newUserDisplayName, gridConstraints);

        JXFormattedTextField newUserNameEntry = new JXFormattedTextField();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 5;
        userCreationPanel.add(newUserNameEntry, gridConstraints);


        JPasswordField newPasswordEntry = new JPasswordField();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 6;
        userCreationPanel.add(newPasswordEntry, gridConstraints);

        JPasswordField newPasswordConfirmation = new JPasswordField();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 7;
        userCreationPanel.add(newPasswordConfirmation, gridConstraints);

        String[] userRoles = new String[] {"A","I","H","S"};
        JComboBox<String> newUserRole = new JComboBox<>(userRoles);
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 8;
        userCreationPanel.add(newUserRole, gridConstraints);

        try
        {
            byte[] responseBytes = COM.COM("GetCurrentAccounts;" + ApplicationGUI.GetSessionInformation()[0] + ';' + ApplicationGUI.GetSessionInformation()[1]);
            String[] response = (new String(responseBytes, "UTF-8")).split(";") ;

            JList<String> existingUsers = new JList<>(response);
            gridConstraints.gridx = 1;
            gridConstraints.gridy = 1;
            gridConstraints.gridwidth = 2;

            userCreationPanel.add(existingUsers, gridConstraints);
        }
        catch (Exception e)
        {
            //
        }

        JButton submitChanges = new JButton("Create New User");
        submitChanges.addActionListener((ActionEvent e) ->
        {
            if(newUserDisplayName.isValid() && newUserNameEntry.isValid() && (new String(newPasswordEntry.getPassword())).contains((new String(newPasswordEntry.getPassword()))) && (newUserRole.getSelectedIndex() > -1))
            {
                new Thread(() ->
                {
                    CreateUser(newUserDisplayName.getText(), newUserNameEntry.getText(), new String(newPasswordEntry.getPassword()), newUserRole.getSelectedItem().toString());
                }
                ).start();
            }
        });
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 9;
        gridConstraints.gridwidth = 3;
        userCreationPanel.add(submitChanges, gridConstraints);

        JButton returnToPrevious = new JButton("Back");
        returnToPrevious.addActionListener((ActionEvent e) ->
        {
            userCreationPanel.removeAll();
            ApplicationGUI.ChangePanel("homePanel");
        });
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 12;
        gridConstraints.gridwidth = 3;
        userCreationPanel.add(returnToPrevious, gridConstraints);

        return userCreationPanel;
    }

    public void CreateUser(String newUser_DisplayName, String newUser_UserName, String newPassword, String newUser_Role)
    {
        String salt = org.mindrot.jbcrypt.BCrypt.gensalt();
        String hash = org.mindrot.jbcrypt.BCrypt.hashpw(newPassword, salt);

        String request = "CreateUser;" + ApplicationGUI.GetSessionInformation()[0] + ";" + ApplicationGUI.GetSessionInformation()[1] + ";" + newUser_DisplayName + ";" + newUser_UserName + ";" + newUser_Role + ";;;" + hash + ";;;" + salt;

        try
        {
            byte[] result = COM.COM(request);
        }
        catch (Exception e)
        {
            //
        }
    }
}
