
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Forest {

    private Cell[][] grid;

    private double temperature;
    private double rainfall;
    private double aridity;

    private Wind wind;

    private Random random = new Random();

    private static final int CLUSTER_SIZE = 5;

    private List<ForestObserver> observers = new ArrayList<>();

    public void addObserver(ForestObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ForestObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (ForestObserver observer : observers) {
            observer.onForestUpdated(this);
        }
    }

    public Forest(int rows, int columns) {

        grid = new Cell[rows][columns];

        temperature = 25.0;
        rainfall = 0.2;
        aridity = 0.5;

        String[][] clusterTypes = new String[(rows + CLUSTER_SIZE - 1) / CLUSTER_SIZE][(columns + CLUSTER_SIZE - 1) / CLUSTER_SIZE];

// Give each cluster a random tree species
        for (int row = 0; row < clusterTypes.length; row++) {
            for (int col = 0; col < clusterTypes[row].length; col++) {
                clusterTypes[row][col] = TreeFactory.randomType();
            }
        }

// Create the cells and plant trees
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {

                grid[row][col] = new Cell(row, col);

                if (random.nextDouble() < 0.8) {

                    double spreadability
                            = 0.3 + random.nextDouble() * 0.7;

                    String species = clusterTypes[row / CLUSTER_SIZE][col / CLUSTER_SIZE];

                    Tree tree = TreeFactory.createTree(
                            species, spreadability
                    );

                    grid[row][col].plantTree(tree);
                }
            }
        }

    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
        wind.applyWind(this);
    }

    public Wind getWind() {
        return wind;
    }

    public void update() {
        spreadFire();
        advanceBurningTrees();
        notifyObservers();
    }

    public void reset() {
        Forest freshForest = new Forest(grid.length, grid[0].length);
        grid = freshForest.getGrid();

        if (wind != null) {
            wind.applyWind(this);
        }

        notifyObservers();
    }

    private void advanceBurningTrees() {

        for (Cell[] row : grid) {

            for (Cell cell : row) {

                if (!cell.hasTree()) {
                    continue;
                }

                Tree tree = cell.getTree();

                if (tree.isBurning()) {
                    tree.advanceBurning();
                }

                if (!tree.isAlive()) {
                    cell.removeTree();
                }
            }
        }
    }

    private void spreadFire() {

        boolean[][] igniteNext
                = new boolean[grid.length][grid[0].length];

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];

                if (cell.hasTree()
                        && cell.getTree().isBurning()) {

                    trySpread(row, col, row - 1, col, igniteNext);
                    trySpread(row, col, row + 1, col, igniteNext);
                    trySpread(row, col, row, col - 1, igniteNext);
                    trySpread(row, col, row, col + 1, igniteNext);
                    trySpread(row, col, row - 1, col - 1, igniteNext);
                    trySpread(row, col, row - 1, col + 1, igniteNext);
                    trySpread(row, col, row + 1, col - 1, igniteNext);
                    trySpread(row, col, row + 1, col + 1, igniteNext);
                }
            }
        }

        // Ignite AFTER checking the entire grid
        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                if (igniteNext[row][col]) {
                    grid[row][col].getTree().ignite();
                }
            }
        }
    }

    private void trySpread(
            int sourceRow,
            int sourceCol,
            int row,
            int col,
            boolean[][] igniteNext) {

        if (row < 0
                || row >= grid.length
                || col < 0
                || col >= grid[0].length) {

            return;
        }

        Cell target = grid[row][col];

        if (!target.hasTree()) {
            return;
        }

        if (target.getTree().isBurning()) {
            return;
        }

        double chance
                = target.getTree().getSpreadability();

        if (wind != null) {

            chance += wind.getSpreadModifier(
                    sourceCol,
                    sourceRow,
                    col,
                    row
            );
        }

        chance += aridity * 0.2;

        chance += (temperature / 100.0) * 0.2;

        chance -= rainfall * 0.2;

        chance = Math.max(0.0, Math.min(1.0, chance));

        if (random.nextDouble() < chance) {
            igniteNext[row][col] = true;
        }
    }

    public void display() {

        for (Cell[] row : grid) {

            for (Cell cell : row) {

                if (!cell.hasTree()) {
                    System.out.print(". ");

                } else if (cell.getTree().isBurning()) {
                    System.out.print("X ");

                } else {
                    System.out.print("T ");
                }
            }

            System.out.println();
        }
    }
}
