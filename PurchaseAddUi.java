import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Sun Jun 25 03:25:30 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class PurchaseAddUi extends JDialog {
    MySQLDatabase mySQLDatabase;
    int userId;
    boolean updateIdorName = false;

    public PurchaseAddUi(JFrame frame, MySQLDatabase mySQLDatabase, int userId) {
        super(frame, true);
        initComponents();
        init(mySQLDatabase, userId);
    }

    public void init(MySQLDatabase mySQLDatabase, int userId) {
        this.mySQLDatabase = mySQLDatabase;
        this.userId = userId;
        String sql = "select product_id as '商品ID',product_name as '商品名称' " +
                "from tbl_product ";
        updateIdorName = true;//反正误触发选择事件
        comboBox1.removeAllItems();
        comboBox2.removeAllItems();
        ResultSet resultSet = mySQLDatabase.executeSQLStatements(sql);
        try {
            while (resultSet.next()) {
                comboBox2.addItem(resultSet.getObject(1));
                comboBox1.addItem(resultSet.getObject(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        updateIdorName = false;

        comboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updateIdorName) {
                    comboBox1.setSelectedIndex(comboBox2.getSelectedIndex());
                }
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updateIdorName) {
                    comboBox2.setSelectedIndex(comboBox1.getSelectedIndex());
                }
            }
        });
    }

    /**
     * 添加进货界面点击添加
     *
     * @param e
     */
    private void button2MouseClicked(MouseEvent e) {
        // TODO add your code here
        String purchaseNumber = textField1.getText();//进货号
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner1.getEditor();// 获取 JSpinner 的默认编辑器
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
        Date date = (Date) spinner1.getValue();// 获取日期和时间
        String formattedTime = format.format(date);// 格式化日期和时间
        int productID = (Integer) comboBox2.getSelectedItem();//获取商品ID
        String productName = (String) comboBox1.getSelectedItem();//获取商品名
        int purchaseQuantity = (Integer) spinner2.getValue();//进货数量
        if (purchaseNumber.equals("")) {
            JOptionPane.showMessageDialog(null, "进货号不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "select * from tbl_purchase where purchase_id = ?";
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{purchaseNumber});
        try {
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "进货号重复", "警告",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        int oldStock = 0;
        String query = "select stock from tbl_product where product_id = ?";//查询现有库存
        ResultSet resultSet1 = mySQLDatabase.precompileQuerySQL(query, new Object[]{productID});
        try {
            oldStock = resultSet1.next() ? (Integer) resultSet1.getObject(1) : 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        int newStock = oldStock + purchaseQuantity;
        String update = "update tbl_product set stock = ? where product_id = ?";//设置新库存
        boolean resultsOfThe_Update = mySQLDatabase.precompileImplementSQL(update, new Object[]{newStock, productID});
        if (!resultsOfThe_Update){
            JOptionPane.showMessageDialog(null, "商品更新失败", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }


        sql = "insert into tbl_purchase values (?,?,?,?)";//插入进货表
        Object[] values = new Object[]{purchaseNumber, productID, purchaseQuantity, formattedTime};
        boolean resultsOfThe = mySQLDatabase.precompileImplementSQL(sql, values);

        if (resultsOfThe) {
            JOptionPane.showMessageDialog(null, "添加成功");
            dispose();//关闭窗口
        } else {
            JOptionPane.showMessageDialog(null, "添加失败", "警告",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * 进货ID 自动生成
     *
     * @param e
     */
    private void checkBox1MouseClicked(MouseEvent e) {
        // TODO add your code here
        if (checkBox1.isSelected()) {
            textField1.setEnabled(false);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String dateTimeString = now.format(formatter);//时间字符串
            String purchaseId = dateTimeString + userId;//进货id
            textField1.setText(purchaseId);
        } else {
            textField1.setEnabled(true);
        }

    }

    /**
     * 新增商品
     *
     * @param e
     */
    private void button3MouseClicked(MouseEvent e) {
        // TODO add your code here
        new ProductAdditionUi(this, mySQLDatabase).setVisible(true);
        init(mySQLDatabase, userId);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        label1 = new JLabel();
        textField1 = new JTextField();
        checkBox1 = new JCheckBox();
        spinner1 = new JSpinner();
        label2 = new JLabel();
        label3 = new JLabel();
        comboBox1 = new JComboBox();
        label4 = new JLabel();
        comboBox2 = new JComboBox();
        spinner2 = new JSpinner();
        label5 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();

        //======== this ========
        setTitle("\u6dfb\u52a0\u8fdb\u8d27");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));

            //---- label1 ----
            label1.setText("\u8fdb\u8d27\u53f7");

            //---- checkBox1 ----
            checkBox1.setText("\u81ea\u52a8\u751f\u6210");
            checkBox1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    checkBox1MouseClicked(e);
                }
            });

            //---- spinner1 ----
            spinner1.setModel(new SpinnerDateModel());

            //---- label2 ----
            label2.setText("\u65f6\u95f4");

            //---- label3 ----
            label3.setText("\u5546\u54c1ID");

            //---- label4 ----
            label4.setText("\u5546\u54c1\u540d");

            //---- spinner2 ----
            spinner2.setModel(new SpinnerNumberModel(1, 1, null, 1));

            //---- label5 ----
            label5.setText("\u8fdb\u8d27\u6570\u91cf");

            //---- button1 ----
            button1.setText("\u53d6\u6d88");

            //---- button2 ----
            button2.setText("\u6dfb\u52a0");
            button2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button2MouseClicked(e);
                }
            });

            //---- button3 ----
            button3.setText("\u65b0\u589e\u5546\u54c1");
            button3.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button3MouseClicked(e);
                }
            });

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(label3)
                                .addGap(0, 395, Short.MAX_VALUE))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(label1)
                                    .addComponent(label5))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(checkBox1)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(spinner2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                            .addComponent(textField1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                            .addComponent(comboBox2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                                        .addGap(29, 29, 29)
                                        .addGroup(panel1Layout.createParallelGroup()
                                            .addComponent(label2)
                                            .addComponent(label4)))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(button1)
                                        .addGap(36, 36, 36)
                                        .addComponent(button3)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(spinner1)
                                    .addComponent(comboBox1)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(button2)))
                                .addContainerGap(12, Short.MAX_VALUE))))
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label1)
                            .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2)
                            .addComponent(spinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox1)
                        .addGap(25, 25, 25)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label3)
                            .addComponent(label4)
                            .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label5)
                            .addComponent(spinner2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button2)
                            .addComponent(button1)
                            .addComponent(button3))
                        .addGap(21, 21, 21))
            );
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    protected JPanel panel1;
    protected JLabel label1;
    protected JTextField textField1;
    protected JCheckBox checkBox1;
    protected JSpinner spinner1;
    protected JLabel label2;
    protected JLabel label3;
    protected JComboBox comboBox1;
    protected JLabel label4;
    protected JComboBox comboBox2;
    protected JSpinner spinner2;
    protected JLabel label5;
    protected JButton button1;
    protected JButton button2;
    protected JButton button3;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
