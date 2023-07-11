import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Mon Jun 19 13:39:33 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class LoginUi extends JFrame {
    String backgroundPath;//背景
    MySQLDatabase mySQLDatabase;
    String newUi;

    public LoginUi() {
        initComponents();
        super.setResizable(false);//禁止调整大小
    }

    public void init() {
        Properties properties = new Properties();
        InputStream input = null;
        String configFile = "src/config.properties";// 指定配置文件的路径

        try {
            input = new FileInputStream(configFile);
            // 从输入流中加载配置文件
            properties.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 读取配置文件中的属性值
        String serverHost = properties.getProperty("serverHost");
        String serverPort = properties.getProperty("serverPort");
        String userName = properties.getProperty("userName");
        String password = properties.getProperty("password");
        newUi = properties.getProperty("newUi");
         backgroundPath = properties.getProperty("backgroundPath");

        String url = "jdbc:mysql://" + serverHost + ":" + serverPort + "/supermarketdata?useSSL=false&serverTimezone=GMT%2B8";
        String driver = "com.mysql.cj.jdbc.Driver";//驱动
        mySQLDatabase = new MySQLDatabase(driver, url, userName, password);
    }

    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
        init();
//        String sql = "select role from tbl_user where username='"
//                + textField1.getText() + "' and password='"
//                + passwordField1.getText()+"'";
//        System.out.println(sql);
//        ResultSet resultSet = mySQLDatabase.executeSQLStatements(sql);
        String sql = "select user_id,role from tbl_user where username= ? and password= ? ";
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{textField1.getText(), passwordField1.getText()});
//        String[] values={"string-"+textField1.getText(),"string-"+passwordField1.getText()};
//        ResultSet resultSet = mySQLDatabase.precompiledSQL(sql, values);
        try {
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(null, "登录失败 请检查配置", "警告",
                        JOptionPane.WARNING_MESSAGE);
                System.out.println("登录失败");
                return;
            }
            if (resultSet.getObject(2).equals(comboBox1.getSelectedItem().toString())) {
                JOptionPane.showMessageDialog(null, " 登录成功！");
                setVisible(false);//隐藏登录窗口
                if (newUi.equals("false")){
                    OperationInterfaceUI operationInterfaceUI = new OperationInterfaceUI(comboBox1.getSelectedItem().toString(),
                            (Integer) resultSet.getObject(1));
                    mySQLDatabase.close();//关闭sql
                    operationInterfaceUI.setVisible(true);
                    operationInterfaceUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                }else{
                    OperationInterfaceNewUI newOperationInterfaceUI = new OperationInterfaceNewUI(comboBox1.getSelectedItem().toString(),
                            (Integer) resultSet.getObject(1), backgroundPath);
                    newOperationInterfaceUI.setVisible(true);
                    newOperationInterfaceUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                }




            } else {
                JOptionPane.showMessageDialog(null, "用户不匹配", "警告",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    private void button2MouseClicked(MouseEvent e) {
        // TODO add your code here
        System.exit(0);
    }

    private void button3(ActionEvent e) {
        // TODO add your code here
        new ConfigurationInterface(this).setVisible(true);
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        panel2 = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        textField1 = new JTextField();
        passwordField1 = new JPasswordField();
        label3 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();
        comboBox1 = new JComboBox<>();
        button3 = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(460, 360));
        setTitle("\u767b\u5f55\u754c\u9762");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel1.setBackground(Color.white);

            //======== panel2 ========
            {
                panel2.setBackground(Color.white);
                panel2.setPreferredSize(new Dimension(440, 330));

                //---- label1 ----
                label1.setText("\u7528\u6237\u540d");
                label1.setForeground(Color.black);

                //---- label2 ----
                label2.setText("\u5bc6\u7801");
                label2.setForeground(Color.black);

                //---- textField1 ----
                textField1.setText("admin");

                //---- passwordField1 ----
                passwordField1.setText("admin");

                //---- label3 ----
                label3.setText("\u8d85 \u5e02 \u7ba1 \u7406 \u7cfb \u7edf");
                label3.setHorizontalAlignment(SwingConstants.CENTER);
                label3.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));

                //---- button1 ----
                button1.setText("\u767b\u5f55");
                button1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button1MouseClicked(e);
                    }
                });

                //---- button2 ----
                button2.setText("\u9000\u51fa");
                button2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button2MouseClicked(e);
                    }
                });

                //---- comboBox1 ----
                comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                    "\u7ba1\u7406\u5458",
                    "\u6536\u94f6\u5458",
                    "\u5e93\u7ba1\u5458"
                }));

                //---- button3 ----
                button3.setText("\u914d\u7f6e");
                button3.addActionListener(e -> button3(e));

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addGap(96, 96, 96)
                            .addGroup(panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addGroup(panel2Layout.createParallelGroup()
                                        .addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                                            .addComponent(button3)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                                            .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                                            .addComponent(button1)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                                            .addComponent(button2)))
                                    .addContainerGap(76, Short.MAX_VALUE))
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addGroup(panel2Layout.createParallelGroup()
                                        .addComponent(label2)
                                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
                                    .addGap(61, 61, 61)
                                    .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(textField1, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                        .addComponent(passwordField1, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                                    .addContainerGap(71, Short.MAX_VALUE))))
                        .addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                            .addContainerGap(152, Short.MAX_VALUE)
                            .addComponent(label3, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
                            .addGap(144, 144, 144))
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addGap(30, 30, 30)
                            .addComponent(label3, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                            .addGap(41, 41, 41)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label1))
                            .addGap(36, 36, 36)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label2))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(button3))
                            .addGap(27, 27, 27)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(button2)
                                .addComponent(button1))
                            .addGap(17, 17, 17))
                );
            }

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            );
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    protected JPanel panel1;
    protected JPanel panel2;
    protected JLabel label1;
    protected JLabel label2;
    protected JTextField textField1;
    protected JPasswordField passwordField1;
    protected JLabel label3;
    protected JButton button1;
    protected JButton button2;
    protected JComboBox<String> comboBox1;
    protected JButton button3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
