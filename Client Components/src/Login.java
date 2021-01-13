import org.jdesktop.swingx.JXFormattedTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.List;


interface LoginListener
{
    void loginComplete(String currentUser, String comToken, String comLevel);
}

public class Login
{
    private List<LoginListener> listeners = new ArrayList<LoginListener>();

    public void addListener(LoginListener toAdd)
    {
        listeners.add(toAdd);
    }


    public JPanel BuildLoginPanel()
    {

        JPanel loginPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;

        JXFormattedTextField usernameEntry = new JXFormattedTextField("Username");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 1;
        loginPanel.add(usernameEntry, gridConstraints);


        JPasswordField passwordEntry = new JPasswordField();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 2;
        loginPanel.add(passwordEntry, gridConstraints);

        JButton submitButton = new JButton("Submit");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 3;

        submitButton.addActionListener((ActionEvent e) ->
            {
                if(usernameEntry.isEditValid() && (passwordEntry.getPassword().length > 0))
                {
                    try
                    {
                        byte[] salt = COM.COM("InitiateLogin;" + usernameEntry.getText());

                        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(new String(passwordEntry.getPassword()) , new String(salt, StandardCharsets.UTF_8));

                        byte[] responseBytes = COM.COM("AuthenticateLogin;" + usernameEntry.getText() + ";;;" + hashedPassword);

                        String[] response = (new String(responseBytes, "UTF-8")).split(";") ;

                        String accessToken = response[0];
                        String accessLevel = response[1];

                        if(!accessToken.equals("00112233-4455-6677-8899-aabbccddeeff"))
                        {
                            for (LoginListener ll : listeners)
                            {
                                ll.loginComplete(usernameEntry.getText(), accessToken, accessLevel);
                            }
                        }
                    }
                    catch (Exception loginException)
                    {
                        //
                    }
                }
            }
        );

        loginPanel.add(submitButton, gridConstraints);

        return loginPanel;
    }
}
