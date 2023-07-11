import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Mon Jun 26 19:36:10 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class ProductModificationUi extends JDialog {

    MySQLDatabase mySQLDatabase;
    int productID;

    public ProductModificationUi(JFrame jFrame, MySQLDatabase mySQLDatabase, int productID) {
        super(jFrame, true);
        initComponents();
        init(mySQLDatabase, productID);
    }

    public void init(MySQLDatabase mySQLDatabase, int productID) {
        this.mySQLDatabase = mySQLDatabase;
        this.productID = productID;

        String sql = "select category_id, category_name " +
                "from tbl_category";
        ResultSet resultSet = mySQLDatabase.executeSQLStatements(sql);

        comboBox1.removeAllItems();
        comboBox2.removeAllItems();
        try {
            while (resultSet.next()) {
                comboBox2.addItem(resultSet.getObject(2));
                comboBox1.addItem(resultSet.getObject(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        comboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBox1.setSelectedIndex(comboBox2.getSelectedIndex());
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBox2.setSelectedIndex(comboBox1.getSelectedIndex());
            }
        });
        sql = "select product_name as '商品名', category_id as '分类ID', image_urls as '图像url', price as '价格', stock as '库存', is_onsale as '状态' " +
                "from tbl_product " +
                "where product_id = ?";
        resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{productID});
        try {
            if (resultSet.next()) {

                String productName = (String) resultSet.getObject(1);//商品名
                int categoryID = (Integer) resultSet.getObject(2);//分类ID
                String imageurl = (String) resultSet.getObject(3);//图像url
                double price = ((BigDecimal) resultSet.getObject(4)).doubleValue();//价格
                int stock = (Integer) resultSet.getObject(5);//库存
                String status = (String) resultSet.getObject(6);//上下架

                textField1.setText(productName);//设置商品名
                textField2.setText(imageurl);//设置图像url
                comboBox1.setSelectedItem(categoryID);//设置分类ID
                spinner1.setValue(price);//设置价格
                spinner2.setValue(stock);//设置库存
                comboBox4.setSelectedIndex(status.equals("true") ? 0 : 1);//设置上下架
            } else {
                JOptionPane.showMessageDialog(null, "未查询到该商品信息", "警告",
                        JOptionPane.WARNING_MESSAGE);
                dispose();//关闭窗口
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void button2MouseClicked(MouseEvent e) {
        // TODO add your code here
        String productName = textField1.getText();//商品名
        String imageurl = textField2.getText();//图像url
        int categoryID = (Integer) comboBox1.getSelectedItem();//分类ID
        double price = (Double) spinner1.getValue();//价格
        int stock = (Integer) spinner2.getValue();//库存
        String status = (String) comboBox4.getSelectedItem();//上下架

        if (productName.equals("")) {
            JOptionPane.showMessageDialog(null, "商品名不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "update tbl_product set product_name= ? ,category_id = ?,image_urls = ?,price = ?,stock = ?,is_onsale = ? " +
                "where product_id = ?";
        Object[] values = new Object[]{productName, categoryID, imageurl, price, stock, status, productID};
        boolean resultsOfThe = mySQLDatabase.precompileImplementSQL(sql, values);
        if (resultsOfThe) {
            JOptionPane.showMessageDialog(null, "商品更改成功");
            dispose();//关闭窗口
        } else {
            JOptionPane.showMessageDialog(null, "商品更改失败", "警告",
                    JOptionPane.WARNING_MESSAGE);
        }

    }

    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
        dispose();//关闭窗口
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        textField2 = new JTextField();
        label3 = new JLabel();
        comboBox1 = new JComboBox();
        label4 = new JLabel();
        comboBox2 = new JComboBox();
        label5 = new JLabel();
        spinner1 = new JSpinner();
        label6 = new JLabel();
        label7 = new JLabel();
        comboBox4 = new JComboBox<>();
        spinner2 = new JSpinner();
        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        setTitle("\u4fee\u6539\u5546\u54c1");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));

            //---- label1 ----
            label1.setText("\u5546\u54c1\u540d");

            //---- label2 ----
            label2.setText("\u56fe\u50cfurl");

            //---- label3 ----
            label3.setText("\u5206\u7c7bID");

            //---- label4 ----
            label4.setText("\u5206\u7c7b\u540d");

            //---- label5 ----
            label5.setText("\u4ef7\u683c");

            //---- spinner1 ----
            spinner1.setModel(new SpinnerNumberModel(1.0, 1.0, null, 1.0));

            //---- label6 ----
            label6.setText("\u5e93\u5b58");

            //---- label7 ----
            label7.setText("\u4e0a/\u4e0b\u67b6");

            //---- comboBox4 ----
            comboBox4.setModel(new DefaultComboBoxModel<>(new String[] {
                "true",
                "false"
            }));

            //---- spinner2 ----
            spinner2.setModel(new SpinnerNumberModel(1, 0, null, 1));

            //---- button1 ----
            button1.setText("\u53d6\u6d88");
            button1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button1MouseClicked(e);
                }
            });

            //---- button2 ----
            button2.setText("\u4fdd\u5b58");
            button2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    button2MouseClicked(e);
                }
            });

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(label1)
                                    .addComponent(label3)
                                    .addComponent(label5))
                                .addGap(18, 18, 18)
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textField1, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                    .addComponent(spinner1, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                    .addComponent(comboBox1, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(label7)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(comboBox4, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(button1)
                                        .addGap(0, 35, Short.MAX_VALUE)))))
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(label2, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label4)
                                    .addComponent(label6))
                                .addGap(18, 18, 18)
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textField2, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                    .addComponent(comboBox2, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                    .addComponent(spinner2, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                                .addContainerGap(44, Short.MAX_VALUE))
                            .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                                .addComponent(button2)
                                .addGap(87, 87, 87))))
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label1)
                            .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2)
                            .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addComponent(label3)
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label4)
                                .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addGap(44, 44, 44)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label5)
                            .addComponent(spinner1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label6)
                            .addComponent(spinner2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(label7)
                            .addComponent(comboBox4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button1)
                            .addComponent(button2))
                        .addGap(17, 17, 17))
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
    protected JLabel label2;
    protected JTextField textField2;
    protected JLabel label3;
    protected JComboBox comboBox1;
    protected JLabel label4;
    protected JComboBox comboBox2;
    protected JLabel label5;
    protected JSpinner spinner1;
    protected JLabel label6;
    protected JLabel label7;
    protected JComboBox<String> comboBox4;
    protected JSpinner spinner2;
    protected JButton button1;
    protected JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
