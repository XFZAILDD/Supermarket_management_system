import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Sat Jun 24 16:10:18 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class ProductModification_sort_Ui extends JDialog {
    int productID;
    MySQLDatabase mySQLDatabase;

    public ProductModification_sort_Ui(JFrame jFrame, MySQLDatabase mySQLDatabase, int productID) {
        super(jFrame, true);
        initComponents();
        init(productID, mySQLDatabase);
    }

    public void init(int productID, MySQLDatabase mySQLDatabase) {
        this.productID = productID;
        this.mySQLDatabase = mySQLDatabase;

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
    }

    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
        dispose();//关闭窗口
    }

    private void button2MouseClicked(MouseEvent e) {
        // TODO add your code here
        int selectedItem = (Integer) comboBox1.getSelectedItem();
        System.out.println("selectedItem:" + selectedItem);
        String sql = "update tbl_product set category_id = ? where product_id = ?";
        boolean resultsOfThe = mySQLDatabase.precompileImplementSQL(sql, new Object[]{selectedItem, productID});
        if (resultsOfThe) {
            JOptionPane.showMessageDialog(null, "商品类别更改成功");
            dispose();//关闭窗口
        } else {
            JOptionPane.showMessageDialog(null, "商品类别更改失败", "警告",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        comboBox1 = new JComboBox();
        comboBox2 = new JComboBox();
        label1 = new JLabel();
        label2 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));

            //---- label1 ----
            label1.setText("\u5206\u7c7bID");

            //---- label2 ----
            label2.setText("\u5206\u7c7b\u540d");

            //---- button1 ----
            button1.setText("\u9000\u51fa");
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
                        .addContainerGap()
                        .addComponent(label1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(label2))
                            .addComponent(button1))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addComponent(button2)
                            .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(23, Short.MAX_VALUE))
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label1)
                            .addComponent(label2))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(button1)
                            .addComponent(button2))
                        .addGap(18, 18, 18))
            );
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    protected JPanel panel1;
    protected JComboBox comboBox1;
    protected JComboBox comboBox2;
    protected JLabel label1;
    protected JLabel label2;
    protected JButton button1;
    protected JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
