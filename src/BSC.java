import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public  class BSC extends JScrollPane {
    ArrayList<BSCpanels> bscp = new ArrayList<>();
    JPanel container = new JPanel();

    public BSC() {
        this.setViewportView(container);
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(300, 300));
        BSCpanels a = new BSCpanels();
        container.add(a);

    }

    public void addNewContainerBSC() {
        BSCpanels a = new BSCpanels();
        bscp.add(a);
        container.add(a);
    }
}
