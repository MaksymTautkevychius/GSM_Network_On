import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public  class BTSSpanels extends JPanel {
    public BTSSpanels() {
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        this.setBorder(border);
        this.setBackground(Color.darkGray);
        this.setPreferredSize(new Dimension(100, 100));
        this.setLayout(new BorderLayout());

        JLabel l = new JLabel("S26871Project03.BTSS");
        l.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(l);
    }
}
