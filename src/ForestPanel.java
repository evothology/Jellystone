
import java.awt.*;
import javax.swing.*;

public class ForestPanel extends JPanel implements ForestObserver {

    private Forest forest;

    private Image oakImage;
    private Image birchImage;
    private Image eucalyptusImage;
    private Image jungleImage;
    private Image spruceImage;
    private Image fireImage;

    private Lightning lightning;
    private Image lightningImage;

    private Cell lastLightningCell;
    private long lightningStartTime;

    public ForestPanel(Forest forest, Lightning lightning) {
        this.forest = forest;
        this.lightning = lightning;

        System.out.println(new java.io.File("images/Oak.png").exists());
        System.out.println(new java.io.File("images/Birch.png").exists());
        System.out.println(new java.io.File("images/Eucalyptus.png").exists());
        System.out.println(new java.io.File("images/Jungle.png").exists());
        System.out.println(new java.io.File("images/Spruce.png").exists());
        System.out.println(new java.io.File("images/fire.png").exists());
        System.out.println(new java.io.File("images/lightning.png").exists());

        oakImage = new ImageIcon("images/Oak.png").getImage();
        birchImage = new ImageIcon("images/Birch.png").getImage();
        eucalyptusImage = new ImageIcon("images/Eucalyptus.png").getImage();
        jungleImage = new ImageIcon("images/Jungle.png").getImage();
        spruceImage = new ImageIcon("images/Spruce.png").getImage();

        fireImage = new ImageIcon("images/fire.png").getImage();
        lightningImage = new ImageIcon("images/lightning.png").getImage();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Cell[][] grid = forest.getGrid();

        int cellWidth = getWidth() / grid[0].length;
        int cellHeight = getHeight() / grid.length;

        Cell currentLightningCell = null;

        if (lightning != null) {
            currentLightningCell = lightning.getLastStruckCell();

            if (currentLightningCell != lastLightningCell) {
                lastLightningCell = currentLightningCell;
                lightningStartTime = System.currentTimeMillis();
            }
        }

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];

                g.setColor(Color.BLACK);

                g.fillRect(
                        col * cellWidth,
                        row * cellHeight,
                        cellWidth,
                        cellHeight
                );

                if (lightning != null
                        && cell == currentLightningCell
                        && System.currentTimeMillis() - lightningStartTime < 3000) {

                    g.drawImage(
                            lightningImage,
                            col * cellWidth + 2,
                            row * cellHeight + 2,
                            cellWidth - 4,
                            cellHeight - 4,
                            this
                    );
                } else {
                    if (!cell.hasTree()) {

                        // Empty cell stays black
                    } else if (cell.getTree().isBurning()) {

                        g.drawImage(
                                fireImage,
                                col * cellWidth + 2,
                                row * cellHeight + 2,
                                cellWidth - 4,
                                cellHeight - 4,
                                this
                        );
                    } else {

                        Image selectedImage = spruceImage;

                        switch (cell.getTree().getTreeType()) {
                            case "OAK":
                                selectedImage = oakImage;
                                break;
                            case "BIRCH":
                                selectedImage = birchImage;
                                break;
                            case "EUCALYPTUS":
                                selectedImage = eucalyptusImage;
                                break;
                            case "JUNGLE":
                                selectedImage = jungleImage;
                                break;
                            case "SPRUCE":
                                selectedImage = spruceImage;
                                break;
                        }

                        g.drawImage(
                                selectedImage,
                                col * cellWidth + 2,
                                row * cellHeight + 2,
                                cellWidth - 4,
                                cellHeight - 4,
                                this
                        );

                    }
                }
            }
        }
    }

    @Override
    public void onForestUpdated(Forest forest) {
        repaint();
    }

}
