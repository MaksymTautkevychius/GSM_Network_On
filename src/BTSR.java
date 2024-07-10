import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public  class BTSR extends JScrollPane implements Runnable {
    private ArrayList<BTSRpanels> arr = new ArrayList<>();
    private JPanel container = new JPanel();
    private MainLogic ml;

    public BTSR(MainLogic ml) {
        this.ml = ml;
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        this.setViewportView(container);
        BTSRpanels a = new BTSRpanels();
        container.add(a);

        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(300, 300));
    }

    public void addNewContainer() {
        BTSRpanels panel = new BTSRpanels();
        container.add(panel);
        arr.add(panel);
        container.revalidate();
        container.repaint();
    }

    public void removeContainer() {
        int componentCount = container.getComponentCount();
        if (componentCount > 0) {
            Component lastComponent = container.getComponent(componentCount - 1);
            container.remove(lastComponent);
            container.revalidate();
            container.repaint();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
                if (ml.CheckHowManyBTSR() > arr.size() - 1) {
                    removeContainer();
                } else if (ml.CheckHowManyBTSR() < arr.size() - 1) {
                    addNewContainer();
                } else {
                    System.out.println("no changes");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
