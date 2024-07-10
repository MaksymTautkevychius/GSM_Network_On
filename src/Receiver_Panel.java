import javax.swing.*;
import java.awt.*;

public  class Receiver_Panel extends JPanel {
    MainLogic mainLogic;

    Receiver_Panel(MainLogic ml) {
        mainLogic = ml;
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(200, 300));
        this.setLayout(new BorderLayout());

        ReceiverPane rp = new ReceiverPane(ml);
        this.add(rp, BorderLayout.CENTER);

        JButton jButton = new JButton("Add");
        this.add(jButton, BorderLayout.SOUTH);
        jButton.addActionListener(e -> {
            rp.addNewContainer();
        });
    }
}
