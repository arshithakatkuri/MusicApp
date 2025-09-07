import javazoom.jl.player.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.File;

public class MusicPlayer extends JFrame {

    private JButton playButton, pauseButton, stopButton, chooseButton;
    private JLabel songLabel;
    private File songFile;
    private Player player;
    private Thread playThread;
    private boolean isPlaying = false;
    private boolean isPaused = false;

    public MusicPlayer() {
        setTitle("Simple Music Player");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        songLabel = new JLabel("No song selected");
        chooseButton = new JButton("Choose Song");
        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");

        add(songLabel);
        add(chooseButton);
        add(playButton);
        add(pauseButton);
        add(stopButton);

        // Choose song
        chooseButton.addActionListener(e -> chooseSong());

        // Play button
        playButton.addActionListener(e -> playSong());

        // Pause button
        pauseButton.addActionListener(e -> pauseSong());

        // Stop button
        stopButton.addActionListener(e -> stopSong());

        setVisible(true);
    }

    private void chooseSong() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            songFile = fileChooser.getSelectedFile();
            songLabel.setText("Selected: " + songFile.getName());
        }
    }

    private void playSong() {
        if (songFile == null) {
            JOptionPane.showMessageDialog(this, "Choose a song first!");
            return;
        }

        if (isPaused) {
            // Restart song when paused (simple way)
            stopSong();
        }

        if (!isPlaying) {
            playThread = new Thread(() -> {
                try {
                    FileInputStream fis = new FileInputStream(songFile);
                    player = new Player(fis);
                    isPlaying = true;
                    player.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            playThread.start();
        }
    }

    private void pauseSong() {
        if (isPlaying) {
            stopSong();
            isPaused = true;
            JOptionPane.showMessageDialog(this, "Paused (resume with Play)");
        }
    }

    private void stopSong() {
        if (player != null) {
            player.close();
            isPlaying = false;
            isPaused = false;
        }
    }

    public static void main(String[] args) {
        new MusicPlayer();
    }
}
