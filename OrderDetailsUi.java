import java.awt.*;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
/*
 * Created by JFormDesigner on Fri Jun 23 14:46:34 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class OrderDetailsUi extends JFrame {
    public OrderDetailsUi(String orderNumber, ResultSet resultSet) {
        initComponents();
        init(orderNumber, resultSet);
    }

    public void init(String orderNumber, ResultSet resultSet) {
        setTitle("订单:" + orderNumber + " 详情界面");
        table1.getTableHeader().setReorderingAllowed(false);
        OperationalLogic.autoupdateTable(table1, resultSet);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();

        //======== this ========
        setAlwaysOnTop(true);
        setPreferredSize(new Dimension(530, 350));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel1.setLayout(new BorderLayout());

            //======== scrollPane1 ========
            {

                //---- table1 ----
                table1.setModel(new DefaultTableModel(
                    new Object[][] {
                    },
                    new String[] {
                        "\u5546\u54c1\u8be6\u60c5ID", "\u5546\u54c1ID", "\u5546\u54c1\u540d\u79f0", "\u5546\u54c1\u6570\u91cf"
                    }
                ));
                table1.setAutoCreateRowSorter(true);
                scrollPane1.setViewportView(table1);
            }
            panel1.add(scrollPane1, BorderLayout.CENTER);
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    protected JPanel panel1;
    protected JScrollPane scrollPane1;
    protected JTable table1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
