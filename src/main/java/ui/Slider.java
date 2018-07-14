package ui;

import model.LineList;
import model.Observer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Slider extends JPanel implements Observer {

    private JSlider slider;
    private LineList lines;
    private ChangeListener changeListener;
    private JButton start;
    private JButton end;
    private JButton play;
    private JButton playback;
    private Timer timer;
    private Timer backTimer;


    Slider(LineList lines) {
        timer = new Timer(10, e -> {
            if (lines.getPercentage() + 10 > lines.getMax()) {
                lines.setPercentage(lines.getMax());
                timer.stop();
                return;
            }
            lines.setPercentage(lines.getPercentage() + 10);
        });
        backTimer = new Timer(10, e -> {
            if (lines.getPercentage() - 10 <= 0) {
                lines.setPercentage(0);
                backTimer.stop();
                return;
            }
            lines.setPercentage(lines.getPercentage() - 10);
        });
        slider = new JSlider();
        start = new JButton(readImage("backward.png"));
        end = new JButton(readImage("forward.png"));
        play = new JButton(readImage("play.png"));
        playback = new JButton((readImage("playback.png")));

        this.lines = lines;
        slider.setMaximum(this.lines.getMax());
        changeListener = e -> this.lines.setPercentage(slider.getValue());
        slider.addChangeListener(changeListener);
        slider.setMajorTickSpacing(1000);
        slider.setPaintTicks(true);


        play.addActionListener(e -> {
            if (lines.getPercentage() == lines.getMax())
                lines.setPercentage(0);
            if (timer.isRunning())
                timer.stop();
            else {
                timer.start();
            }
        });

        playback.addActionListener(e -> {
            if (lines.getPercentage() == 0)
                lines.setPercentage(lines.getMax());

            if (backTimer.isRunning()) {
                backTimer.stop();
            } else {
                backTimer.start();
            }
        });
        start.addActionListener(e -> this.lines.setPercentage(0));
        end.addActionListener(e -> this.lines.setPercentage(this.lines.getMax()));


        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        Insets insets = new Insets(10, 10, 10, 10);
        c.insets = insets;
        add(play, c);
        add(playback, c);
        c.weightx = 1.0;
        add(slider, c);
        c.weightx = 0;
        Insets insets1 = new Insets(10, 10, 10, 5);
        c.insets = insets1;
        add(start, c);

        Insets insets2 = new Insets(10, 5, 10, 10);
        c.insets = insets2;
        add(end, c);
        this.lines.addObserver(this);
    }

    private ImageIcon readImage(String name) {
        try {
            return new ImageIcon(ImageIO.read(new File(name)).getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
            return null;
        }
    }

    @Override
    public void update(Object observable) {
        slider.removeChangeListener(changeListener);
        slider.setMaximum(this.lines.getMax());
        slider.setValue(this.lines.getPercentage());
        slider.addChangeListener(changeListener);

        repaint();
    }

}
