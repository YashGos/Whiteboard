import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Settings
{
    public JPanel BuildSettingsPanel()
    {
        JPanel settingsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;

        JButton syncGSuite = new JButton("Link Google Calendar to Account");
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 1;

        syncGSuite.addActionListener((ActionEvent e) ->
            {
                //
                new Thread(() ->
                    {
                    AuthorizeGSuite newAuthorization = new AuthorizeGSuite();
                    }
                ).start();
            }
        );

        settingsPanel.add(syncGSuite, gridConstraints);

        return settingsPanel;
    }
}
