import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public  class BSS_Panel extends JPanel {
    Mainer m = new Mainer();
    ArrayList<BTSRpanels> btsr = new ArrayList<>();
    ArrayList<BTSSpanels> btss = new ArrayList<>();
    ArrayList<BSCpanels> bscp = new ArrayList<>();
    JPanel container = new JPanel();

    BSC bsc = new BSC();
    MainLogic ml;

    public BSS_Panel(MainLogic ml) {
        this.ml = ml;


        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(300, 300));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        BTSS btss = new BTSS(ml);
        this.add(btss, BorderLayout.LINE_START);

        new Thread(btss).start();

        this.add(bsc, BorderLayout.CENTER);

        BTSR btsR = new BTSR(ml);
        this.add(btsR, BorderLayout.LINE_END);

        JButton jButton1 = new JButton("+");
        jButton1.addActionListener(e -> {
            bsc.addNewContainerBSC();
            ml.addBSC();
            revalidate();
            repaint();
        });
        JButton jButton2 = new JButton("-");
        jButton2.addActionListener(e -> {
            ml.stopBSC();
        });

        buttonPanel.add(jButton1);
        buttonPanel.add(jButton2);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }
}
