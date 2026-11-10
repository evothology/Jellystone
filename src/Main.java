
import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Forest forest = new Forest(40, 60);

            Wind wind = new Wind(30, "EAST", 30, 20, 15);
            forest.setWind(wind);

            Random random = new Random();

            IgnitionSource source;
            Lightning lightning = null;

            int choice = random.nextInt(3);

            if (choice == 0) {
                lightning = new Lightning();
                source = lightning;
                System.out.println("Ignition source: Lightning");

            } else if (choice == 1) {
                source = new Arson(20, 20);
                System.out.println("Ignition source: Arson");

            } else {
                source = new Backburning(20);
                System.out.println("Ignition source: Backburning");
            }

            try {
                source.ignite(forest);
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println(
                        "Ignition failed: " + e.getMessage()
                );
            }

            JFrame window = new JFrame("Forest Fire Simulation");

            ForestPanel panel = new ForestPanel(forest, lightning);

            Timer timer = new Timer(500, event -> {
                forest.update();
            });

            Runnable resetAction = () -> {
                forest.reset();
                try {
                    source.ignite(forest);
                } catch (IllegalStateException | IllegalArgumentException e) {
                    System.out.println(
                            "Ignition failed: " + e.getMessage()
                    );
                }
                panel.repaint();
            };

            SimulationControlPanel controls
                    = new SimulationControlPanel(
                            forest, timer, resetAction
                    );

            controls.setCurrentDisaster(
                    source.getClass().getSimpleName()
            );

            forest.addObserver(panel);
            forest.addObserver(controls);

            window.setLayout(new BorderLayout());
            window.add(panel, BorderLayout.CENTER);
            window.add(controls, BorderLayout.EAST);

            window.setSize(1150, 650);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            timer.start();
        });
    }
}
