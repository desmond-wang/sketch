package ui;

import model.LineList;
import model.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ToolBar extends JPanel implements Observer {
    private static final Color[] colorList = {new Color(0, 0, 0), new Color(128, 128, 128),
            new Color(255, 255, 255), new Color(139, 69, 19),
            new Color(244, 66, 66), new Color(65, 244, 235),
            new Color(255, 204, 0), new Color(51, 204, 51),
            new Color(51, 153, 255), new Color(128, 51, 204),
            new Color(255, 255, 255, 255), new Color(255, 255, 255, 255)};
    private List<JButton> buttonList;
    private JLabel lineThickness;
    private LineList lines;
    private boolean replaceLast;
    // private model.LineList lines;

    ToolBar(LineList lines) {
        replaceLast = false;
        this.lines = lines;
        GridLayout colorSelectLayout = new GridLayout(0, 2);
        JPanel colorSelect = new JPanel();
        buttonList = new ArrayList<>();
        colorSelect.setLayout(colorSelectLayout);
        for (Color color : colorList) {
            JButton button = new JButton();
            buttonList.add(button);
            button.setBackground(color);
            button.addActionListener(e -> lines.setCurColor(button.getBackground()));
            colorSelect.add(button);
        }
        JButton colorSelector = new JButton("Show Color Chooser");
        colorSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        colorSelector.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(
                    this,
                    "Choose line Color",
                    lines.getCurColor());
            if (newColor != null) {
                lines.setCurColor(newColor);
                boolean existColor = false;
                for (Color c : colorList) {
                    if (c.equals(newColor)) {
                        existColor = true;
                    }
                }
                if (!existColor) {
                    if (replaceLast) {
                        buttonList.get(buttonList.size() - 1).setBackground(newColor);
                        replaceLast = false;
                    } else {
                        buttonList.get(buttonList.size() - 2).setBackground(newColor);
                        replaceLast = true;
                    }
                }
            }
        });

        lineThickness = new JLabel(new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                Color color = c.getForeground();
                g2.setColor(color);
                g2.setStroke(new BasicStroke((int) lines.getStrokeThickness() + 4));
                g2.drawLine(x + 10, y + 50, x + 100, y + 50);

            }

            @Override
            public int getIconWidth() {
                return 110;
            }

            @Override
            public int getIconHeight() {
                return 100;
            }
        });

        lineThickness.setForeground(lines.getCurColor());

        JButton addThickness = new JButton("+");
        addThickness.addActionListener(e -> lines.setStrokeThickness(lines.getStrokeThickness() + 1f));
        JButton minusThickness = new JButton("-");
        minusThickness.addActionListener(e -> lines.setStrokeThickness(lines.getStrokeThickness() - 1f));


        JPanel setThickness = new JPanel();
        setThickness.setAlignmentX(Component.CENTER_ALIGNMENT);
        setThickness.setLayout(new BoxLayout(setThickness, BoxLayout.X_AXIS));

        setThickness.add(minusThickness);
        setThickness.add(lineThickness);
        setThickness.add(addThickness);


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(colorSelect);
        this.add(colorSelector);
        this.add(setThickness);
        lines.addObserver(this);

    }

    @Override
    public void update(Object observable) {
        repaint();
        lineThickness.setForeground(lines.getCurColor());
    }
}
