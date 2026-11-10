
import java.awt.*;
import javax.swing.*;

public class SimulationControlPanel extends JPanel implements ForestObserver {

    private JLabel statusLabel;
    private JLabel tickLabel;
    private JLabel disasterLabel;
    private int ticks = 0;

    public SimulationControlPanel(
            Forest forest,
            Timer timer,
            Runnable resetAction) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(220, 600));
        setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12));

        JLabel heading = new JLabel("Simulation Controls");
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(heading);

        add(Box.createVerticalStrut(20));

        statusLabel = new JLabel("Status: Running");
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(statusLabel);

        add(Box.createVerticalStrut(10));

        tickLabel = new JLabel("Ticks: 0");
        tickLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(tickLabel);

        add(Box.createVerticalStrut(10));

        disasterLabel = new JLabel("Current Disaster: None");
        disasterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(disasterLabel);

        add(Box.createVerticalStrut(25));

        JButton pauseButton = new JButton("Pause");
        JButton resumeButton = new JButton("Resume");
        JButton resetButton = new JButton("Reset Forest");

        Dimension buttonSize = new Dimension(190, 35);

        for (JButton button
                : new JButton[]{pauseButton, resumeButton, resetButton}) {

            button.setMaximumSize(buttonSize);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(button);
            add(Box.createVerticalStrut(10));
        }

        pauseButton.addActionListener(event -> {
            timer.stop();
            statusLabel.setText("Status: Paused");
        });

        resumeButton.addActionListener(event -> {
            timer.start();
            statusLabel.setText("Status: Running");
        });

        resetButton.addActionListener(event -> {
            timer.stop();
            resetAction.run();
            ticks = 0;
            tickLabel.setText("Ticks: 0");
            statusLabel.setText("Status: Paused");
        });
    }

    @Override
    public void onForestUpdated(Forest forest) {
        ticks++;
        tickLabel.setText("Ticks: " + ticks);
    }

    public void setCurrentDisaster(String disaster) {
        disasterLabel.setText("Current Disaster: " + disaster);
    }

}
