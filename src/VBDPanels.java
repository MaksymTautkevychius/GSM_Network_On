import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public  class VBDPanels extends JPanel {
    private Graphics g;
    MainLogic ml;
    JComboBox<Object> jb = new JComboBox<>();

    private int nomer;

    public VBDPanels(MainLogic ml, int nomer) {
        this.ml = ml;
        this.nomer = nomer;
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        this.setBorder(border);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(100, 100));
        this.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel FR = new JLabel("Frequency");
        jb.addItem("ACTIVE");
        jb.addItem("WAITING");

        JSlider jSlider = new JSlider();
        JButton jButton = new JButton("Terminate");
        panel.add(new JLabel(String.valueOf(nomer)), BorderLayout.CENTER);
        panel.add(FR, BorderLayout.CENTER);
        panel.add(jSlider);
        this.add(jb, BorderLayout.NORTH);
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

        jb.addActionListener(b -> {
            ml.ClearTheQueueOf();
            this.remove(this);
        });
    }


}
