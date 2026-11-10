
import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Forest forest = new Forest(40, 60);

            long sequentialStart = System.nanoTime();
            long sequentialCount = forest.countLivingTrees();
            long sequentialTime = System.nanoTime() - sequentialStart;

            long parallelStart = System.nanoTime();
            long parallelCount = forest.countLivingTreesParallel();
            long parallelTime = System.nanoTime() - parallelStart;

            System.out.println("=== Stream Comparison ===");
            System.out.println("Sequential count: " + sequentialCount);
            System.out.println("Parallel count: " + parallelCount);
            System.out.println("Sequential time: " + sequentialTime + " ns");
            System.out.println("Parallel time: " + parallelTime + " ns");
            System.out.println("Results match: " + (sequentialCount == parallelCount));

            Random random = new Random();

            String[] directions = {"NORTH", "SOUTH", "EAST", "WEST"};
            String randomDirection = directions[random.nextInt(directions.length)];

            Wind wind = new Wind(40, randomDirection, 30, 20, 15);
            forest.setWind(wind);

            System.out.println("Wind direction: " + randomDirection);

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

            JPanel forestContainer = new JPanel() {
                @Override
                public boolean isOptimizedDrawingEnabled() {
                    return false;
                }
            };

            forestContainer.setLayout(new OverlayLayout(forestContainer));

            WindVisualization windOverlay = new WindVisualization(forest);
            forest.addObserver(windOverlay);

            windOverlay.setAlignmentX(0.5f);
            windOverlay.setAlignmentY(0.5f);

            panel.setAlignmentX(0.5f);
            panel.setAlignmentY(0.5f);

            forestContainer.add(windOverlay);
            forestContainer.add(panel);

            window.setLayout(new BorderLayout());
            window.add(forestContainer, BorderLayout.CENTER);
            window.add(controls, BorderLayout.EAST);

            window.setSize(1150, 650);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            timer.start();
        });
    }
}
