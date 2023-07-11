import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.sql.rowset.spi.SyncResolver;
import javax.swing.*;
/*
 * Created by JFormDesigner on Fri Jun 23 15:37:13 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class ProductAdditionUi extends JDialog {
    MySQLDatabase mySQLDatabase;


    /**
     * JFrame 调用
     * @param frame
     * @param mySQLDatabase
     */
    public ProductAdditionUi(JFrame frame, MySQLDatabase mySQLDatabase) {
        super(frame, true);
        initComponents();
        init(mySQLDatabase);
    }

    /**
     * JDialog 调用
     * @param jDialog
     * @param mySQLDatabase
     */
    public ProductAdditionUi(JDialog jDialog, MySQLDatabase mySQLDatabase) {
        super(jDialog, true);
        initComponents();
        init(mySQLDatabase);
    }

    public void init(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
        radioButton2.setSelected(true);//默认设置下架
        String sql = "select category_name from tbl_category";
        ResultSet resultSet = mySQLDatabase.executeSQLStatements(sql);
        try {
            while (resultSet.next()) {
                comboBox1.addItem(resultSet.getString(1));//添加
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 商品添加页面点击 取消
     *
     * @param e
     */
    private void button2MouseClicked(MouseEvent e) {
        // TODO add your code here
        dispose();//关闭窗口
    }

    /**
     * 商品添加页面点击 添加
     *
     * @param e
     */
    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
        int productID = checkBox1.isSelected() ? -1 : (Integer) spinner3.getValue();//商品ID
        String classificationName = (String) comboBox1.getSelectedItem();//分类名
        String productName = textField1.getText();//商品名
        double price = (Double) spinner1.getValue();//价格
        String imageurl = textField2.getText();//图像url
        int stock = (Integer) spinner2.getValue();//价格
        int categoryID = 0;//分类ID
        String status = radioButton1.isSelected() ? "true" : "false";

        //自动设置商品ID
        if (!checkBox1.isSelected()) {
            String sql = "select * from tbl_product where product_id = ?";
            ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{productID});
            try {
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(null, "商品ID重复", "警告",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        //商品名不能为空
        if (productName.equals("")) {
            JOptionPane.showMessageDialog(null, "商品名不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        //获取商品分类ID
        if (!classificationName.equals("无")) {
            String sql = "select category_id from tbl_category where category_name = ?";
            ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{classificationName});
            try {
                categoryID = resultSet.next() ? (Integer) resultSet.getObject(1) : -1;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        String sql = "insert into tbl_product(product_id, product_name, category_id, image_urls, price, stock, is_onsale) " +
                "values (?,?,?,?,?,?,?)";
        Object[] values = null;
        if (checkBox1.isSelected()) {
            values = new Object[]{null, productName, categoryID, imageurl, price, stock, status};
        }else {
            values = new Object[]{productID, productName, categoryID, imageurl, price, stock, status};
        }

        boolean resultsOfThe = mySQLDatabase.precompileImplementSQL(sql, values);
        if (resultsOfThe) {
            JOptionPane.showMessageDialog(null, "商品添加成功");
            dispose();//关闭窗口
        } else {
            JOptionPane.showMessageDialog(null, "商品添加失败", "警告",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * 点击 自动设置商品ID
     *
     * @param e
     */
    private void checkBox1MouseClicked(MouseEvent e) {
        // TODO add your code here
        boolean selected = checkBox1.isSelected();
        if (selected) {
            spinner3.setEnabled(false);
        } else {
            spinner3.setEnabled(true);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        label3 = new JLabel();
        spinner1 = new JSpinner();
        textField2 = new JTextField();
        button1 = new JButton();
        button2 = new JButton();
        label4 = new JLabel();
        label5 = new JLabel();
        comboBox1 = new JComboBox<>();
        spinner2 = new JSpinner();
        label6 = new JLabel();
        spinner3 = new JSpinner();
        checkBox1 = new JCheckBox();
        label7 = new JLabel();
        radioButton1 = new JRadioButton();
        radioButton2 = new JRadioButton();

        //======== this ========
        setTitle("\u5546\u54c1\u6dfb\u52a0");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {

            //---- label1 ----
            label1.setText("\u5546\u54c1\u540d");

            //---- label2 ----
            label2.setText("\u56fe\u50cfuri");

            //---- label3 ----
            label3.setText("\u4ef7\u683c");

            //---- spinner1 ----
            spinner1.setModel(new SpinnerNumberModel(1.0, 0.0, null, 0.1));

            //---- button1 ----
            button1.setText("\u6dfb\u52a0");
            button1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button1MouseClicked(e);
                }
            });

            //---- button2 ----
            button2.setText("\u53d6\u6d88");
            button2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button2MouseClicked(e);
                }
            });

            //---- label4 ----
            label4.setText("\u5546\u54c1ID");

            //---- label5 ----
            label5.setText("\u8bbe\u7f6e\u5206\u7c7b");

            //---- comboBox1 ----
            comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                "\u65e0"
            }));

            //---- spinner2 ----
            spinner2.setModel(new SpinnerNumberModel(0, 0, null, 1));

            //---- label6 ----
            label6.setText("\u5e93\u5b58");

            //---- spinner3 ----
            spinner3.setModel(new SpinnerNumberModel(1, 1, null, 1));

            //---- checkBox1 ----
            checkBox1.setText("\u81ea\u52a8\u8bbe\u7f6e\u5546\u54c1ID");
            checkBox1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    checkBox1MouseClicked(e);
                }
            });

            //---- label7 ----
            label7.setText("\u72b6\u6001");

            //---- radioButton1 ----
            radioButton1.setText("\u4e0a\u67b6");

            //---- radioButton2 ----
            radioButton2.setText("\u4e0b\u67b6");

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(spinner3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                            .addComponent(textField2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                                        .addGap(16, 16, 16))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(radioButton1)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(button2)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE))))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(label5)
                                        .addGap(12, 12, 12)
                                        .addComponent(comboBox1, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(label4)
                                            .addComponent(label2))
                                        .addGap(18, 18, 18)
                                        .addComponent(checkBox1))
                                    .addComponent(label7))
                                .addGap(16, 16, 16)))
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(label3)
                                            .addComponent(label6)))
                                    .addComponent(label1, GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(radioButton2)
                                    .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(textField1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                                        .addComponent(spinner2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                                        .addComponent(spinner1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(button1)
                                .addGap(29, 29, 29)))
                        .addGap(47, 47, 47))
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label4)
                            .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinner3, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label1))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox1)
                        .addGap(24, 24, 24)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label5)
                            .addComponent(spinner1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label3)
                            .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label2)
                            .addComponent(spinner2, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                            .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label6))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label7)
                            .addComponent(radioButton1)
                            .addComponent(radioButton2))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button1)
                            .addComponent(button2))
                        .addGap(15, 15, 15))
            );
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
    protected JLabel label1;
    protected JTextField textField1;
    protected JLabel label2;
    protected JLabel label3;
    protected JSpinner spinner1;
    protected JTextField textField2;
    protected JButton button1;
    protected JButton button2;
    protected JLabel label4;
    protected JLabel label5;
    protected JComboBox<String> comboBox1;
    protected JSpinner spinner2;
    protected JLabel label6;
    protected JSpinner spinner3;
    protected JCheckBox checkBox1;
    protected JLabel label7;
    protected JRadioButton radioButton1;
    protected JRadioButton radioButton2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
