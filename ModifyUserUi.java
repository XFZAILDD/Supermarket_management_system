import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
/*
 * Created by JFormDesigner on Thu Jun 29 11:43:21 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class ModifyUserUi extends JDialog {
    MySQLDatabase mySQLDatabase;
    int userID;

    public ModifyUserUi(JFrame frame, MySQLDatabase mySQLDatabase, int userID) {
        super(frame, true);
        this.mySQLDatabase = mySQLDatabase;
        this.userID = userID;
        initComponents();
        init();
    }

    public void init() {

        String sql = "select user_id as '用户ID', username as '登录名', password as '密码', name as '姓名', phone as '电话', role as '权限' " +
                "  from tbl_user " +
                "where user_id = ?";
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{userID});
        String username = "";
        String password = "";
        String name = "";
        String phone = "";
        String role = "";

        try {
            if (resultSet.next()) {
                username = (String) resultSet.getObject(2);
                password = (String) resultSet.getObject(3);
                name = (String) resultSet.getObject(4);
                phone = (String) resultSet.getObject(5);
                role = (String) resultSet.getObject(6);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        textField3.setText(name);
        textField4.setText(phone);
        textField2.setText(username);
        passwordField1.setText(password);
        if (role.equals("收银员")) {
            radioButton1.setSelected(true);
        } else {
            radioButton2.setSelected(true);
        }
    }


    private void button2(ActionEvent e) {
        // TODO add your code here
        dispose();
    }

    private void button1(ActionEvent e) {
        // TODO add your code here
        String username = textField2.getText();
        String password = passwordField1.getText();
        String name = textField3.getText();
        String phone = textField4.getText();
        String role = radioButton1.isSelected() ? "收银员" : "库管员";

        if (username.equals("")) {
            JOptionPane.showMessageDialog(null, "登录名不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.equals("")) {
            JOptionPane.showMessageDialog(null, "密码不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "姓名不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "update tbl_user set name = ?,phone = ?,username = ?,password = ?,role = ? " +
                "where user_id = ?";
        Object[] values = new Object[]{name, phone, username, password, role, userID};
        boolean resultsOfThe = mySQLDatabase.precompileImplementSQL(sql, values);
        if (resultsOfThe){
            JOptionPane.showMessageDialog(null, "更改成功");
            dispose();
        }else {
            JOptionPane.showMessageDialog(null, "更改失败", "警告",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel2 = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        textField2 = new JTextField();
        label3 = new JLabel();
        radioButton1 = new JRadioButton();
        radioButton2 = new JRadioButton();
        label4 = new JLabel();
        textField4 = new JTextField();
        label5 = new JLabel();
        passwordField1 = new JPasswordField();
        button1 = new JButton();
        button2 = new JButton();
        textField3 = new JTextField();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel2 ========
        {

            //---- label1 ----
            label1.setText("\u59d3\u540d");
            label1.setHorizontalAlignment(SwingConstants.LEFT);

            //---- label2 ----
            label2.setText("\u767b\u5f55\u540d");

            //---- label3 ----
            label3.setText("\u5bc6\u7801");

            //---- radioButton1 ----
            radioButton1.setText("\u6536\u94f6\u5458");

            //---- radioButton2 ----
            radioButton2.setText("\u5e93\u7ba1\u5458");

            //---- label4 ----
            label4.setText("\u7535\u8bdd");

            //---- label5 ----
            label5.setText("\u6743\u9650");

            //---- button1 ----
            button1.setText("\u4fee\u6539");
            button1.addActionListener(e -> button1(e));

            //---- button2 ----
            button2.setText("\u53d6\u6d88");
            button2.addActionListener(e -> button2(e));

            GroupLayout panel2Layout = new GroupLayout(panel2);
            panel2.setLayout(panel2Layout);
            panel2Layout.setHorizontalGroup(
                panel2Layout.createParallelGroup()
                    .addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addComponent(button2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                                .addComponent(button1))
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addComponent(label5)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(radioButton1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(radioButton2))
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addGroup(panel2Layout.createParallelGroup()
                                    .addComponent(label2)
                                    .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label4)
                                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(label3))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textField4, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                    .addComponent(textField2, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                    .addComponent(passwordField1, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                    .addComponent(textField3, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))))
                        .addGap(60, 60, 60))
            );
            panel2Layout.setVerticalGroup(
                panel2Layout.createParallelGroup()
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label1)
                            .addComponent(textField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label4)
                            .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2))
                        .addGap(18, 18, 18)
                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label3)
                            .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel2Layout.createParallelGroup()
                            .addComponent(radioButton2)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(radioButton1)
                                .addComponent(label5)))
                        .addGap(42, 42, 42)
                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button1)
                            .addComponent(button2))
                        .addContainerGap())
            );
        }
        contentPane.add(panel2, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        var buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(radioButton1);
        buttonGroup1.add(radioButton2);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    protected JPanel panel2;
    protected JLabel label1;
    protected JLabel label2;
    protected JTextField textField2;
    protected JLabel label3;
    protected JRadioButton radioButton1;
    protected JRadioButton radioButton2;
    protected JLabel label4;
    protected JTextField textField4;
    protected JLabel label5;
    protected JPasswordField passwordField1;
    protected JButton button1;
    protected JButton button2;
    protected JTextField textField3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
