import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        ServerPanel servidorPanel = new ServerPanel();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        frame.setVisible(true);

        frame.add(servidorPanel);

    }
}