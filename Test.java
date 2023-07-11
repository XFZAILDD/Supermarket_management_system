import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;

public class Test {
    public static void main(String[] args) {
        FlatIntelliJLaf.install();
        LoginUi login = new LoginUi();
        login.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        login.pack();
        login.setIconImage(null);

        String hello = "hello";
    }
}
