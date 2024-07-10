import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public  class VRDPanels extends JPanel {
    private Graphics g;
    int counter;
    private int num = 1;
    JLabel l = new JLabel(String.valueOf(num));
    MainLogic ml;

    VRDPanels(MainLogic ml) {
        this.ml = ml;
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        this.setBorder(border);

        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(100, 100));
        this.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton jButton = new JButton("Terminate");
        JButton jButton2 = new JButton("Update");
        l.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(l);
        this.add(jButton2, BorderLayout.NORTH);
        jButton2.addActionListener(e -> {
                    updateCounter(ml.getCounter());
                }
        );
        this.add(panel, BorderLayout.CENTER);
        this.add(jButton, BorderLayout.SOUTH);
        jButton.addActionListener(e -> {
            Container parent = this.getParent();
            if (parent instanceof Container) {
                (parent).remove(this);
                parent.revalidate();
                parent.repaint();
            }
        });
    }

    public void updateCounter(int c) {
        l.setText(String.valueOf(c));
    }
}
