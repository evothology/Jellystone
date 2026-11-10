
import java.awt.*;
import javax.swing.*;

public class SimulationControlPanel extends JPanel implements ForestObserver {

    private JLabel statusLabel;
    private JLabel tickLabel;
    private JLabel disasterLabel;

    private JLabel livingTreesLabel;
    private JLabel burningTreesLabel;
    private JLabel emptyCellsLabel;
    private JLabel burningPercentageLabel;
    private JLabel speciesLabel;

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

        add(Box.createVerticalStrut(20));

        JLabel statisticsHeading = new JLabel("Forest Statistics");
        statisticsHeading.setFont(new Font("Arial", Font.BOLD, 14));
        statisticsHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(statisticsHeading);

        add(Box.createVerticalStrut(10));

        livingTreesLabel = new JLabel(
                "Living Trees: " + forest.countLivingTrees()
        );
        livingTreesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(livingTreesLabel);

        add(Box.createVerticalStrut(10));

        burningTreesLabel = new JLabel(
                "Burning Trees: " + forest.countBurningTrees()
        );
        burningTreesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(burningTreesLabel);

        add(Box.createVerticalStrut(10));

        emptyCellsLabel = new JLabel(
                "Empty Cells: " + forest.countEmptyCells()
        );

        emptyCellsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(emptyCellsLabel);

        add(Box.createVerticalStrut(10));

        burningPercentageLabel = new JLabel(
                String.format("Burning: %.1f%%", forest.getBurningPercentage())
        );
        burningPercentageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(burningPercentageLabel);

        add(Box.createVerticalStrut(15));

        JLabel speciesHeading = new JLabel("Tree Species");
        speciesHeading.setFont(new Font("Arial", Font.BOLD, 14));
        speciesHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(speciesHeading);

        add(Box.createVerticalStrut(10));

        speciesLabel = new JLabel(
                "<html>" + forest.countTreesBySpecies()
                        .toString()
                        .replace("{", "")
                        .replace("}", "")
                        .replace(", ", "<br>")
                + "</html>"
        );
        speciesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(speciesLabel);

        add(Box.createVerticalStrut(25));

        JButton pauseButton = new JButton("Pause");
        JButton resumeButton = new JButton("Resume");
        JButton resetButton = new JButton("Reset Forest");
        JButton summaryButton = new JButton("Simulation Summary");

        Dimension buttonSize = new Dimension(190, 35);

        for (JButton button
                : new JButton[]{pauseButton, resumeButton, resetButton, summaryButton}) {

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

        summaryButton.addActionListener(event -> {

            String summary = "Simulation Summary\n\n"
                    + "Total Ticks: " + ticks + "\n"
                    + "Living Trees: " + forest.countLivingTrees() + "\n"
                    + "Burning Trees: " + forest.countBurningTrees() + "\n"
                    + "Empty Cells: " + forest.countEmptyCells() + "\n"
                    + String.format(
                            "Burning Percentage: %.1f%%",
                            forest.getBurningPercentage()
                    );

            JOptionPane.showMessageDialog(
                    this,
                    summary,
                    "Simulation Summary",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

    }

    @Override
    public void onForestUpdated(Forest forest) {
        ticks++;
        tickLabel.setText("Ticks: " + ticks);

        livingTreesLabel.setText(
                "Living Trees: " + forest.countLivingTrees()
        );

        burningTreesLabel.setText(
                "Burning Trees: " + forest.countBurningTrees()
        );

        emptyCellsLabel.setText(
                "Empty Cells: " + forest.countEmptyCells()
        );

        speciesLabel.setText(
                "<html>" + forest.countTreesBySpecies()
                        .toString()
                        .replace("{", "")
                        .replace("}", "")
                        .replace(", ", "<br>")
                + "</html>"
        );

        burningPercentageLabel.setText(
                String.format("Burning: %.1f%%", forest.getBurningPercentage())
        );

    }

    public void setCurrentDisaster(String disaster) {
        disasterLabel.setText("Current Disaster: " + disaster);
    }

}
