import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Sat Jun 24 01:47:57 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class AddUserUi extends JDialog {
    MySQLDatabase mySQLDatabase;

    public AddUserUi(JFrame owner,MySQLDatabase mySQLDatabase) {
        super(owner,true);
        initComponents();
        init(mySQLDatabase);
    }

    public void init(MySQLDatabase mySQLDatabase) {
        radioButton1.setSelected(true);//设置默认收银员
        this.mySQLDatabase = mySQLDatabase;
    }

    /**
     * 添加用户界面 点击添加
     *
     * @param e
     */
    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
        String name = textField3.getText();
        String phone = textField4.getText();
        String logInName = textField2.getText();
        String password = passwordField1.getText();
        String role = radioButton1.isSelected() ? "收银员" : "库管员";

        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "姓名不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (logInName.equals("")) {
            JOptionPane.showMessageDialog(null, "登录名不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (password.equals("")) {
            JOptionPane.showMessageDialog(null, "登录密码不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "insert into tbl_user (username, password, name, phone, role) value (?,?,?,?,?)";
        Object[] values = new Object[]{logInName, password, name, phone, role};
        boolean resultsOfThe = mySQLDatabase.precompileImplementSQL(sql, values);
        if (resultsOfThe){
            JOptionPane.showMessageDialog(null, "创建用户成功");
            dispose();//关闭窗口
        }else{
            JOptionPane.showMessageDialog(null, "创建用户失败");
        }
    }

    /**
     * 添加用户界面点击取消添加
     *
     * @param e
     */
    private void button2MouseClicked(MouseEvent e) {
        // TODO add your code here
        dispose();//关闭窗口
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
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
        setTitle("\u6dfb\u52a0\u7528\u6237");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel1.setLayout(new BorderLayout());

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
                button1.setText("\u6dfb\u52a0");
                button1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button1MouseClicked(e);
                    }
                });

                //---- button2 ----
                button2.setText("\u53d6\u6d88\u6dfb\u52a0");
                button2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button2MouseClicked(e);
                    }
                });

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                            .addGap(69, 69, 69)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addComponent(button2)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
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
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
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
                            .addGap(18, 18, 18)
                            .addGroup(panel2Layout.createParallelGroup()
                                .addComponent(button2)
                                .addComponent(button1))
                            .addGap(30, 30, 30))
                );
            }
            panel1.add(panel2, BorderLayout.CENTER);
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        var buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(radioButton1);
        buttonGroup1.add(radioButton2);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    protected JPanel panel1;
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
