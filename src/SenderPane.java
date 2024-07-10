import javax.swing.*;
import java.util.ArrayList;

public  class SenderPane extends JScrollPane {
    int indd = 0;
    MainLogic ml;
    ArrayList<VBDPanels> sps = new ArrayList<>();
    JPanel container = new JPanel();

    public SenderPane(MainLogic ml) {
        this.ml = ml;
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        setViewportView(container);
    }

    public void addNewContainer(String message) {
        VBDPanels a = new VBDPanels(ml, indd);
        indd++;
        sps.add(a);
        container.add(a);
    }
}
