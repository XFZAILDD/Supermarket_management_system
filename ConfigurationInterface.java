import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Properties;
import javax.naming.Name;
import javax.swing.*;

import com.formdev.flatlaf.FlatIntelliJLaf;
import net.miginfocom.swing.*;

/*
 * Created by JFormDesigner on Tue Jun 27 00:34:25 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class ConfigurationInterface extends JDialog {
    Properties properties = new Properties();
    InputStream input = null;
    String configFile = "src/config.properties";// 指定配置文件的路径

    String serverHost;
    String serverPort;
    String userName;
    String password;
    String newUi;
    String backgroundPath;

    public ConfigurationInterface(JFrame frame) {
        super(frame, true);
        initComponents();
        init();

    }

    public void init() {

        try {
            input = new FileInputStream(configFile);
            // 从输入流中加载配置文件
            properties.load(input);
            // 读取配置文件中的属性值
            serverHost = properties.getProperty("serverHost");
            serverPort = properties.getProperty("serverPort");
            userName = properties.getProperty("userName");
            password = properties.getProperty("password");
            newUi = properties.getProperty("newUi");
            backgroundPath = properties.getProperty("backgroundPath");

            if (serverPort != null) {
                comboBox1.setSelectedItem(serverHost);
            }
            if (serverPort != null) {
                spinner1.setValue(Integer.parseInt(serverPort));
            }
            if (userName != null) {
                textField1.setText(userName);
            }
            if (password != null) {
                passwordField1.setText(password);
            }
            if (newUi != null && newUi.equals("true")) {
                toggleButton1.setSelected(true);
                textField2.setEnabled(true);
            } else {
                toggleButton1.setSelected(false);
                textField2.setEnabled(false);
            }
            textField2.setText(backgroundPath);


        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "配置文件打开错误", "警告",
                    JOptionPane.WARNING_MESSAGE);

            File file = new File(configFile);
            try {
                if (file.createNewFile()) {
                    System.out.println("文件创建成功！");
                } else {
                    System.out.println("文件已存在。");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void button1(ActionEvent e) {
        // TODO add your code here
        if (!toggleButton1.isSelected()){
            return;
        }

        JFileChooser fileChooser = new JFileChooser();

        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textField2.setText(selectedFile.getAbsolutePath());
        }

        System.out.println("点击了 这个按钮");
    }

//    public static void main(String[] args) {
//        FlatIntelliJLaf.install();
//        ConfigurationInterface configurationInterface = new ConfigurationInterface(null);
//        configurationInterface.setVisible(true);
//
//    }

    private void toggleButton1(ActionEvent e) {
        // TODO add your code here
        if (toggleButton1.isSelected()) {
            textField2.setEnabled(true);
            button1.setEnabled(true);
        } else {
            textField2.setEnabled(false);
            button1.setEnabled(false);
        }
    }

    private void button3(ActionEvent e) {
        // TODO add your code here
        serverHost =  comboBox1.getSelectedItem().toString();
        serverPort =  spinner1.getValue().toString();
        userName = textField1.getText();
        password = passwordField1.getText();
        newUi= String.valueOf(toggleButton1.isSelected());
        backgroundPath= textField2.getText();

        if (userName.equals("")){
            JOptionPane.showMessageDialog(null, "用户名不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.equals("")){
            JOptionPane.showMessageDialog(null, "密码不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Properties properties = new Properties();
        OutputStream output = null;

        try {
            // 设置属性值
            properties.setProperty("serverHost", serverHost);
            properties.setProperty("serverPort", serverPort);
            properties.setProperty("userName", userName);
            properties.setProperty("password", password);
            properties.setProperty("newUi", newUi);
            properties.setProperty("backgroundPath", backgroundPath);
            // 指定输出文件路径
            output = new FileOutputStream(configFile);
            // 将属性值写入输出流
            properties.store(output, "My Configuration");
            System.out.println("配置文件写入成功！");
            dispose();//关闭窗口
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    private void button2(ActionEvent e) {
        // TODO add your code here
        dispose();//关闭窗口
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        label3 = new JLabel();
        comboBox1 = new JComboBox<>();
        label5 = new JLabel();
        spinner1 = new JSpinner();
        label6 = new JLabel();
        label4 = new JLabel();
        textField1 = new JTextField();
        label7 = new JLabel();
        passwordField1 = new JPasswordField();
        label8 = new JLabel();
        toggleButton1 = new JToggleButton();
        label9 = new JLabel();
        textField2 = new JTextField();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "insets 0,hidemode 3",
                // columns
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[fill]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- label3 ----
            label3.setText("\u670d\u52a1\u5668\u5730\u5740");
            panel1.add(label3, "cell 2 2");

            //---- comboBox1 ----
            comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                "localhost"
            }));
            comboBox1.setEditable(true);
            comboBox1.setPreferredSize(new Dimension(100, 27));
            panel1.add(comboBox1, "cell 3 2 5 1");

            //---- label5 ----
            label5.setText("\u670d\u52a1\u5668\u7aef\u53e3\u53f7");
            panel1.add(label5, "cell 10 2");

            //---- spinner1 ----
            spinner1.setModel(new SpinnerNumberModel(1, 1, null, 1));
            spinner1.setPreferredSize(new Dimension(90, 31));
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) spinner1.getEditor();
            DecimalFormat format = editor.getFormat();
            format.applyPattern("#");
            spinner1.setValue(3306);
            panel1.add(spinner1, "cell 13 2 6 1");

            //---- label6 ----
            label6.setText("\u670d\u52a1\u5668\u7528\u6237\u540d");
            panel1.add(label6, "cell 2 4");
            panel1.add(label4, "cell 2 4");
            panel1.add(textField1, "cell 3 4 5 1");

            //---- label7 ----
            label7.setText("\u670d\u52a1\u5668\u5bc6\u7801");
            panel1.add(label7, "cell 10 4");
            panel1.add(passwordField1, "cell 13 4 6 1");

            //---- label8 ----
            label8.setText("\u6478\u9c7c\u6a21\u5f0f");
            panel1.add(label8, "cell 2 6");

            //---- toggleButton1 ----
            toggleButton1.setText("\u5f00/\u5173");
            toggleButton1.addActionListener(e -> toggleButton1(e));
            panel1.add(toggleButton1, "cell 3 6 5 1");

            //---- label9 ----
            label9.setText("\u65b0\u7248UI\u80cc\u666f");
            panel1.add(label9, "cell 10 6");
            panel1.add(textField2, "cell 13 6 6 1");

            //---- button1 ----
            button1.setIcon(UIManager.getIcon("Tree.openIcon"));
            button1.addActionListener(e -> button1(e));
            panel1.add(button1, "cell 19 6");

            //---- button2 ----
            button2.setText("\u53d6\u6d88\u66f4\u6539");
            button2.addActionListener(e -> button2(e));
            panel1.add(button2, "cell 3 10 5 1");

            //---- button3 ----
            button3.setText("\u4fdd\u5b58\u66f4\u6539");
            button3.addActionListener(e -> button3(e));
            panel1.add(button3, "cell 10 10");
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    protected JPanel panel1;
    protected JLabel label3;
    protected JComboBox<String> comboBox1;
    protected JLabel label5;
    protected JSpinner spinner1;
    protected JLabel label6;
    protected JLabel label4;
    protected JTextField textField1;
    protected JLabel label7;
    protected JPasswordField passwordField1;
    protected JLabel label8;
    protected JToggleButton toggleButton1;
    protected JLabel label9;
    protected JTextField textField2;
    protected JButton button1;
    protected JButton button2;
    protected JButton button3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
