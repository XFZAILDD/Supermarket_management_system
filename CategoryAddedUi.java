import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Sun Jun 25 01:34:32 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class CategoryAddedUi extends JDialog {
    MySQLDatabase mySQLDatabase;

    public CategoryAddedUi(Frame frame, MySQLDatabase mySQLDatabase) {
        super(frame, true);
        initComponents();
        init(mySQLDatabase);
    }

    public void init(MySQLDatabase mySQLDatabase) {
        this.mySQLDatabase = mySQLDatabase;
    }

    /**
     * 点击添加
     *
     * @param e
     */
    private void button2MouseClicked(MouseEvent e) {
        // TODO add your code here
        int categoryID = (Integer) spinner1.getValue();//分类ID
        String categoryName = textField1.getText();//分类名称
        String classificationLegend = textField2.getText();//分类图例
        String status = (String) comboBox1.getSelectedItem();//分类状态
        Object[] values = new Object[]{categoryID, categoryName, classificationLegend, status};//新增数据

        if (!checkBox1.isSelected()) {
            String sql = "select * from tbl_category where category_id = ?";
            ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{categoryID});
            try {
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(null, "分类ID重复", "警告",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            values = new Object[]{null, categoryName, classificationLegend, status};
        }

        if (categoryName.equals("")) {
            JOptionPane.showMessageDialog(null, "分类名称不能为空", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "insert into tbl_category values (?,?,?,?)";
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
     * 点击自动设置
     *
     * @param e
     */
    private void checkBox1MouseClicked(MouseEvent e) {
        // TODO add your code here
        boolean selected = checkBox1.isSelected();
        if (selected) {
            spinner1.setEnabled(false);
        } else {
            spinner1.setEnabled(true);
        }
    }

    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
        dispose();//关闭窗口
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        panel2 = new JPanel();
        label1 = new JLabel();
        spinner1 = new JSpinner();
        checkBox1 = new JCheckBox();
        label2 = new JLabel();
        textField1 = new JTextField();
        label3 = new JLabel();
        textField2 = new JTextField();
        label4 = new JLabel();
        comboBox1 = new JComboBox<>();
        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        setTitle("\u65b0\u589e\u5206\u7c7b");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel1.setLayout(new BorderLayout());

            //======== panel2 ========
            {

                //---- label1 ----
                label1.setText("\u5206\u7c7bID");

                //---- spinner1 ----
                spinner1.setModel(new SpinnerNumberModel(1, 1, null, 1));

                //---- checkBox1 ----
                checkBox1.setText("\u81ea\u52a8\u8bbe\u7f6e");
                checkBox1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        checkBox1MouseClicked(e);
                    }
                });

                //---- label2 ----
                label2.setText("\u5206\u7c7b\u540d\u79f0");

                //---- label3 ----
                label3.setText("\u5206\u7c7b\u56fe\u4f8b");

                //---- label4 ----
                label4.setText("\u5206\u7c7b\u72b6\u6001");

                //---- comboBox1 ----
                comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                    "false",
                    "true"
                }));

                //---- button1 ----
                button1.setText("\u53d6\u6d88");
                button1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button1MouseClicked(e);
                    }
                });

                //---- button2 ----
                button2.setText("\u6dfb\u52a0");
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
                            .addGroup(panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addGap(27, 27, 27)
                                    .addComponent(label1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(panel2Layout.createParallelGroup()
                                        .addGroup(panel2Layout.createSequentialGroup()
                                            .addGap(6, 6, 6)
                                            .addComponent(checkBox1)
                                            .addGap(0, 40, Short.MAX_VALUE))
                                        .addComponent(spinner1)))
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addGap(21, 21, 21)
                                    .addComponent(label3)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panel2Layout.createParallelGroup()
                                        .addGroup(panel2Layout.createSequentialGroup()
                                            .addComponent(button1)
                                            .addGap(0, 41, Short.MAX_VALUE))
                                        .addComponent(textField2, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))))
                            .addGap(28, 28, 28)
                            .addGroup(panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addComponent(label2)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addComponent(label4)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addComponent(button2))
                            .addGap(24, 24, 24))
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addGap(31, 31, 31)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label2)
                                .addComponent(label1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addComponent(spinner1))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(checkBox1)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label3)
                                .addComponent(label4)
                                .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(61, 61, 61)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(button1)
                                .addComponent(button2))
                            .addGap(20, 20, 20))
                );
            }
            panel1.add(panel2, BorderLayout.CENTER);
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
    protected JSpinner spinner1;
    protected JCheckBox checkBox1;
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
