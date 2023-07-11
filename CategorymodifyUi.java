import java.awt.*;
import java.awt.event.*;
import java.net.IDN;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Thu Jun 29 11:08:45 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class CategorymodifyUi extends JDialog {
    MySQLDatabase mySQLDatabase;
    int categoryID;

    public CategorymodifyUi(JFrame frame, MySQLDatabase mySQLDatabase, int categoryID) {
        super(frame, true);
        initComponents();
        this.mySQLDatabase = mySQLDatabase;
        this.categoryID = categoryID;
        init();
    }

    public void init() {
        spinner1.setEnabled(false);
        String sql = "select category_id, category_name, image_url, status " +
                "from tbl_category " +
                "where category_id = ? ";
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{categoryID});
        String categoryName="";
        String classificationLegend="";
        String classificationStatus="";
        try {
            if (resultSet.next()){
                categoryName= (String) resultSet.getObject(2);
                classificationLegend= (String) resultSet.getObject(3);
                classificationStatus= (String) resultSet.getObject(4);
//                System.out.println(categoryName+classificationLegend+classificationStatus);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        spinner1.setValue(categoryID);
        textField1.setText(categoryName);
        textField2.setText(classificationLegend);
        comboBox1.setSelectedItem(classificationStatus);
    }

    /**
     * 取消
     * @param e
     */
    private void button1(ActionEvent e) {
        // TODO add your code here
        dispose();//关闭窗口
    }

    /**
     * 保存
     * @param e
     */
    private void button2(ActionEvent e) {
        // TODO add your code here
        String categoryName= textField1.getText();
        String classificationLegend=textField2.getText();
        String classificationStatus= (String) comboBox1.getSelectedItem();

        if (categoryName.equals("")){
            JOptionPane.showMessageDialog(null, "分类名不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "update tbl_category set category_name = ?,image_url = ?,status = ? where category_id = ?";
        boolean resultsOfThe = mySQLDatabase.precompileImplementSQL(sql, new Object[]{categoryName, classificationLegend,
                classificationStatus, categoryID});
        if (resultsOfThe){
            JOptionPane.showMessageDialog(null, "修改成功");
            dispose();//关闭窗口
        }else {
            JOptionPane.showMessageDialog(null, "修改失败", "警告",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        label1 = new JLabel();
        spinner1 = new JSpinner();
        label2 = new JLabel();
        textField1 = new JTextField();
        label3 = new JLabel();
        textField2 = new JTextField();
        label4 = new JLabel();
        comboBox1 = new JComboBox<>();
        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new MigLayout(
                "hidemode 3",
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
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- label1 ----
            label1.setText("\u5206\u7c7bID");
            panel1.add(label1, "cell 2 2");
            panel1.add(spinner1, "cell 4 2 3 1");

            //---- label2 ----
            label2.setText("\u5206\u7c7b\u540d\u79f0");
            panel1.add(label2, "cell 8 2");
            panel1.add(textField1, "cell 10 2 3 1");

            //---- label3 ----
            label3.setText("\u5206\u7c7b\u56fe\u4f8b");
            panel1.add(label3, "cell 2 4");
            panel1.add(textField2, "cell 4 4 3 1");

            //---- label4 ----
            label4.setText("\u5206\u7c7b\u72b6\u6001");
            panel1.add(label4, "cell 8 4");

            //---- comboBox1 ----
            comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                "true",
                "false"
            }));
            panel1.add(comboBox1, "cell 10 4 3 1");

            //---- button1 ----
            button1.setText("\u9000\u51fa");
            button1.addActionListener(e -> button1(e));
            panel1.add(button1, "cell 4 6");

            //---- button2 ----
            button2.setText("\u4fdd\u5b58");
            button2.addActionListener(e -> button2(e));
            panel1.add(button2, "cell 8 6");
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    protected JPanel panel1;
    protected JLabel label1;
    protected JSpinner spinner1;
    protected JLabel label2;
    protected JTextField textField1;
    protected JLabel label3;
    protected JTextField textField2;
    protected JLabel label4;
    protected JComboBox<String> comboBox1;
    protected JButton button1;
    protected JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
