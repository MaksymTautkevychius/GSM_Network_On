import javax.swing.*;
import java.util.ArrayList;

public  class ReceiverPane extends JScrollPane {
    ArrayList<VRDPanels> rps = new ArrayList<>();
    JPanel container = new JPanel();
    MainLogic ml;

    public ReceiverPane(MainLogic ml) {
        this.ml = ml;
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        VRDPanels rpp = new VRDPanels(ml);

        container.add(rpp);
        rps.add(rpp);

        setViewportView(container);
    }

    public void addNewContainer() {
        VRDPanels a = new VRDPanels(ml);
        rps.add(a);
        container.add(a);
    }
}
