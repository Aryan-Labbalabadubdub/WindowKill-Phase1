package view.containers;

import view.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static controller.constants.DimensionConstants.SCREEN_SIZE;
import static controller.constants.FilePaths.MENU_IMAGEPATH;
import static view.containers.GlassFrame.getGlassFrame;

public class PanelB extends JPanel {
    static final BufferedImage defaultImage;

    static {
        try {
            defaultImage = ImageIO.read(new File(MENU_IMAGEPATH.getValue()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage imageSave;
    public BufferedImage currentImage;
    public GridBagConstraints constraints = new GridBagConstraints();

    public PanelB(Dimension dimension) {
        this(dimension, Utils.toBufferedImage(defaultImage.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH)));
    }

    public PanelB(Dimension dimension, BufferedImage image) {
        this.currentImage = image;
        this.imageSave = image;
        setSize(dimension);
        setBackground(new Color(0, 0, 0, 0));
        setLayout(new GridBagLayout());
        setBorder(null);
        getGlassFrame().add(this);
        setVisible(false);

        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 0;
    }

    public PanelB(int width, int height, BufferedImage image) {
        this(new Dimension(width, height), image);
    }

    public void add(Component component, boolean nextHorizontal, boolean nextVertical) {
        if (nextHorizontal) constraints.gridx++;
        if (nextVertical) {
            constraints.gridy++;
            constraints.gridx = 0;
        }
        add(component, constraints);
    }

    public void bulkAdd(CopyOnWriteArrayList<Component> components, int itemsPerRow) {
        while (!components.isEmpty()) {
            List<Component> rowItems = components.subList(0, Math.min(components.size(), itemsPerRow));
            float preferredAspectRatio = (float) rowItems.get(0).getPreferredSize().height / rowItems.get(0).getPreferredSize().width;
            PanelB temp = new PanelB(new Dimension(getWidth(), (int) (preferredAspectRatio * getWidth()))) {{
                setVisible(true);
            }};
            temp.horizontalBulkAdd(rowItems);
            add(temp, false, false);
            components.removeAll(rowItems);
            constraints.gridx = 0;
            constraints.gridy++;
        }
    }

    public void verticalBulkAdd(Collection<Component> components) {
        constraints.gridy = -1;
        for (Component component : components) add(component, false, true);
    }

    public void horizontalBulkAdd(Collection<Component> components) {
        constraints.gridx = -1;
        for (Component component : components) add(component, true, false);
    }

    public void togglePanel() {
        setVisible(!isVisible());
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        if (imageSave != null) currentImage = Utils.toBufferedImage(imageSave.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        setLocation((SCREEN_SIZE.getValue().width - width) / 2, (SCREEN_SIZE.getValue().height - height) / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage != null) g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), null);
    }

}
