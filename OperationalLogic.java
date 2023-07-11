import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperationalLogic {

    /**
     * 根据resultSet自动设置Jtable组件
     *
     * @param table       被设置的表
     * @param resultSet   resultSet对象
     * @param columnNames 列名为空时自动设置
     */
    public static void autoupdateTable(JTable table, ResultSet resultSet, String[] columnNames, int[] rows) {
        try {
            resultSet.last(); // 将游标移动到最后一行
            int rowCount = resultSet.getRow(); // 获取当前行号，即为总行数
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();//获取列数
            resultSet.beforeFirst(); // 将游标移动到第一行，以便后续操作
            Object[][] data = new Object[rowCount][columnCount];
            int row = 0;
            while (resultSet.next()) {
                for (int i = 0; i < columnCount; i++) {
                    data[row][i] = resultSet.getObject(i + 1);
                }
                row++;
            }
            List<String> columnName_list = columnName(resultSet);
            //创建表格模型并设置数据
            if (columnNames == null) {
                columnNames = columnName_list.toArray(new String[columnName_list.size()]);//设置列名
            }
            DefaultTableModel model = null;
            if (rows == null) {
                model = new DefaultTableModel(data, columnNames);
            } else if (rows.length==0){
                model = new DefaultTableModel(data, columnNames) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
            }
            else {
                model = new DefaultTableModel(data, columnNames) {
                    public boolean isCellEditable(int row, int column) {
                        // 设置第rows[]列可编辑，其它列不可编辑
                        return Arrays.binarySearch(rows, column) >= 0 ? true : false;
                    }
                };
            }
            //使用表格模型
            table.setModel(model);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 自动设置表格 默认所有列不可编辑
     *
     * @param table
     * @param resultSet
     */
    public static void autoupdateTable(JTable table, ResultSet resultSet) {
        autoupdateTable(table, resultSet, null, new int[0]);
    }

    /**
     * 自动设置表格 指定列可编辑
     *
     * @param table
     * @param resultSet
     * @param rows
     */
    public static void autoupdateTable(JTable table, ResultSet resultSet, int[] rows) {
        autoupdateTable(table, resultSet, null, rows);
    }

    /**
     * 解析resultSet对象
     *
     * @param resultSet
     * @return 表数据
     */
    public static Object[][] resultSetanalysis(ResultSet resultSet) {
        try {
            resultSet.last(); // 将游标移动到最后一行
            int rowCount = resultSet.getRow(); // 获取当前行号，即为总行数
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();//获取列数
            resultSet.beforeFirst(); // 将游标移动到第一行，以便后续操作
            Object[][] data = new Object[rowCount][columnCount];
            int row = 0;
            while (resultSet.next()) {
                for (int i = 0; i < columnCount; i++) {
                    data[row][i] = resultSet.getObject(i + 1);
                }
                row++;
            }
            return data;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回查询的列名
     *
     * @param resultSet
     * @return
     */
    public static List<String> columnName(ResultSet resultSet) {
        List<String> columnNames = new ArrayList<>();
        try {
            resultSet.beforeFirst();//将游标移动到第一行
            ResultSetMetaData metaData;
            metaData = resultSet.getMetaData();
            int columnCount = 0;
            columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                columnNames.add(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return columnNames;
    }


    /**
     * 隐藏部分界面
     *
     * @param role       用户
     * @param tabbedPane 组件
     */
    public static void pageDeletionSettings(String role, JTabbedPane tabbedPane) {
        switch (role) {
            case "收银员":
                tabbedPane.removeTabAt(2);
                tabbedPane.removeTabAt(2);
                tabbedPane.removeTabAt(2);
                tabbedPane.removeTabAt(2);
                break;
            case "库管员":
                tabbedPane.removeTabAt(0);
                tabbedPane.removeTabAt(0);
                tabbedPane.removeTabAt(3);
            case "管理员":
                break;
        }
    }


    /**
     * @param role
     * @param tabbedPane
     */
    public void pageDisableSettings(String role, JTabbedPane tabbedPane) {
        int pageIndexToDisable = 1;
        Component componentToDisable = tabbedPane.getComponentAt(pageIndexToDisable);
        setComponentsEnabled(componentToDisable, false);
    }

    /**
     * 递归方式设置组件及其子组件的启用状态
     *
     * @param comp    组件
     * @param enabled 启用状态，true表示启用，false表示禁用
     */
    private void setComponentsEnabled(Component comp, boolean enabled) {
        comp.setEnabled(enabled);
        if (comp instanceof JPanel) {
            Component[] components = ((JPanel) comp).getComponents();
            for (Component childComp : components) {
                setComponentsEnabled(childComp, enabled);
            }
        }
    }

    /**
     * 显示查询的表中的数据
     * @param resultSet
     */
    public static void showTableData(ResultSet resultSet) {
        List<String> columnNames = columnName(resultSet);
        try {
            resultSet.beforeFirst();//将游标移动到第一行
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();//列数
            while (resultSet.next()) {
                for (int column = 0; column < columnCount; column++) {
                    System.out.print(resultSet.getObject(column + 1) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5Encryption(String password) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            result = no.toString(16);
            while (result.length() < 32) {
                result = "0" + result;
            }
            result = result.substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
}
