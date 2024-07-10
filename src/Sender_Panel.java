import javax.swing.*;
import java.awt.*;

public  class Sender_Panel extends JPanel {
    private Graphics g;
    MainLogic ml;

    Sender_Panel(MainLogic ml) {
        this.ml = ml;
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(200, 300));
        this.setLayout(new BorderLayout()); // Set BorderLayout as the layout manager

        SenderPane sp = new SenderPane(ml);
        this.add(sp, BorderLayout.CENTER);

        JButton jButton = new JButton("add");
        this.add(jButton, BorderLayout.SOUTH);
        jButton.addActionListener(e -> {
            String message = JOptionPane.showInputDialog(this, "Enter a message:");

            if (message != null && !message.isEmpty()) {
                sp.addNewContainer(message);
                try {
                    ml.addNewQueues(message, 3);
                    System.out.println("Starting Transmission");
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });
    }
}
