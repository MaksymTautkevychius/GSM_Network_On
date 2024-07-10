import javax.swing.*;
import java.awt.*;

public  class Centre extends JFrame {
    MainLogic m;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Centre(null));
    }

    public Centre(MainLogic m) {
        this.m = m;
        Sender_Panel leftPanel = new Sender_Panel(m);
        this.getContentPane().add(leftPanel, BorderLayout.LINE_START);

        Receiver_Panel rightPanel = new Receiver_Panel(m);
        this.getContentPane().add(rightPanel, BorderLayout.LINE_END);

        BSS_Panel bss = new BSS_Panel(m);
        this.getContentPane().add(bss, BorderLayout.CENTER);

        this.setSize(1280, 720);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
