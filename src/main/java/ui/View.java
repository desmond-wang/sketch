package ui;

import model.LineList;
import model.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class View extends JFrame implements Observer {

    private LineList lines;
    private int width;
    private int height;
    private boolean saved = false;

    /**
     * Create a new ui.View.
     */
    public View(LineList lines) {
        // Set up the window.
        width = 800;
        height = 600;
        this.setMinimumSize(new Dimension(200, 150));
        this.setTitle("New Paint");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(width, height);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


        this.lines = lines;
        JPanel toolBarArea = new ToolBar(lines);
        DrawImage drawImage = new DrawImage(lines);
        Slider slider = new Slider(lines);
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = menuBar.add(new JMenu("File"));
        JMenuItem newMenuItem = fileMenu.add(new JMenuItem("New"));
        JMenuItem saveMenuItem = fileMenu.add(new JMenuItem("Save as"));
        JMenuItem loadMenuItem = fileMenu.add(new JMenuItem("Load"));
        JMenuItem exitMenuItem = fileMenu.add(new JMenuItem("Exit"));

        newMenuItem.addActionListener(e -> {
            if (!this.saveDialog(newMenuItem)) {
                return;
            }
            setTitle("New Paint");
            lines.reset();
            saved = false;

        });


        saveMenuItem.addActionListener(e -> this.saveDialog(newMenuItem));

        loadMenuItem.addActionListener(e -> {
            if (!this.saveDialog(loadMenuItem)) {
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(loadMenuItem) == JFileChooser.APPROVE_OPTION) {
                saved = false;
                File file = fileChooser.getSelectedFile();
                lines.Import(file);
                setTitle(file.getName());
            }

        });

        exitMenuItem.addActionListener(e -> {
            if (!this.saveDialog(exitMenuItem)) {
                return;
            }
            System.exit(0);
        });

        lines.notifyObservers();

        toolBarArea.setMaximumSize(new Dimension(width / 6, height)); // size


        // two JPanel for different section
        add(toolBarArea, BorderLayout.WEST);
        add(menuBar, BorderLayout.NORTH);
        add(slider, BorderLayout.SOUTH);
        add(drawImage, BorderLayout.CENTER);

        View self = this;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!saveDialog(self)) {
                    return;
                }
                System.exit(0);
            }
        });


        // Hook up this observer so that it will be notified when the model
        // changes.
//        this.lines = lines;
//        lines.addObserver(this);

        setVisible(true);
    }

    private boolean saveDialog(Component c) {
        if (saved || lines.getLines().size() == 0) return true;
        int optionPaneValue = JOptionPane.showConfirmDialog(this, "Do you want to save?", getTitle(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (optionPaneValue == JOptionPane.YES_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(c) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    lines.Export(file);
                    setTitle(file.getName());
                    saved = true;
                    return true;
                } catch (IOException e1) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return optionPaneValue == JOptionPane.NO_OPTION;
        }

    }


    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
        System.out.println("Model changed!");
    }
}
