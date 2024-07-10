import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public  class BTSS extends JScrollPane implements Runnable {
    private ArrayList<BTSSpanels> arr = new ArrayList<>();
    private JPanel container = new JPanel();
    private MainLogic ml;

    public BTSS(MainLogic ml) {
        this.ml = ml;
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        this.setViewportView(container);
        BTSSpanels a = new BTSSpanels();
        container.add(a);

        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(300, 300));
    }

    public void addNewContainer() {
        BTSSpanels panel = new BTSSpanels();
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
                if (ml.CheckHowManyBTSS() > arr.size() - 1 && ml.CheckHowManyBTSS() > 0) {
                    removeContainer();
                } else if (ml.CheckHowManyBTSS() < arr.size() - 1) {
                    addNewContainer();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
