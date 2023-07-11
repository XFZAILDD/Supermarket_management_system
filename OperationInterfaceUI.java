import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.formdev.flatlaf.ui.*;
/*
 * Created by JFormDesigner on Tue Jun 20 12:38:20 HKT 2023
 */


/**
 * @author XFZAILDD
 */
public class OperationInterfaceUI extends JFrame {
    MySQLDatabase mySQLDatabase = new MySQLDatabase();//数据库
    DefaultTableModel productSettlement = new DefaultTableModel(null, new String[]{"商品ID", "商品名称", "价格", "数量"}) {
        public boolean isCellEditable(int row, int column) {
            // 设置第4列可编辑，其它列不可编辑
            return column == 3;
        }
    }; //商品结算表
    int userID;//用户ID

    boolean productDeletionEvent = true;

    public OperationInterfaceUI(String role, int userID) {
        initComponents();
        init(role, userID);
    }

    /**
     * 对不同用户登录进行不同的初始化操作
     *
     * @param role 角色
     */
    public void init(String role, int userID) {
        setTitle(role + ":操作界面");//操作界面用户信息
        OperationalLogic.pageDeletionSettings(role, tabbedPane1);//根据用户不同隐藏界面
        this.userID = userID;//初始化当前登录用户ID
        table1.getTableHeader().setReorderingAllowed(false);//设置表格列不可拖动
        table2.getTableHeader().setReorderingAllowed(false);
        table3.getTableHeader().setReorderingAllowed(false);
        table4.getTableHeader().setReorderingAllowed(false);
        table5.getTableHeader().setReorderingAllowed(false);
        table6.getTableHeader().setReorderingAllowed(false);
        table7.getTableHeader().setReorderingAllowed(false);
        if (role.equals("收银员") || role.equals("管理员")) {
            table1.setModel(productSettlement);
            productSettlement.addTableModelListener(e -> TableModelListener(e));//表格更新事件
            TableColumn column = table1.getColumnModel().getColumn(1);
            column.setCellEditor(null);   // 设置编辑器为空
            table1.setRowSelectionAllowed(true);//设置行可选中
        }
    }

    /**
     * 商品结算页面 点击查询
     *
     * @param e
     */
    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
        boolean fuzzyQuery = checkBox1.isSelected();//模糊查询
        String queryType = comboBox1.getSelectedItem().toString();//查询类型
        String queryValue = textField1.getText();//查询值
        String sql = "select product_id as '商品ID',product_name as '商品名称' " +
                "from tbl_product left  join tbl_category  on tbl_product.category_id = tbl_category.category_id ";
        List<Object> values = new ArrayList<>();//查询值
        if (fuzzyQuery) {
            System.out.println("模糊查询");
            sql = sql + "where (product_name like ? or product_id like ? or category_name like ?) and is_onsale = 'true'";
            values.add("%" + queryValue + "%");//添加查询值
            values.add("%" + queryValue + "%");
            values.add("%" + queryValue + "%");
        } else {
            values.add("%" + queryValue + "%");
            switch (queryType) {
                case "编号":
                    sql += "where product_id like ? and is_onsale = 'true'";
                    break;
                case "名称":
                    sql += "where product_name like ? and is_onsale = 'true'";
                    break;
                case "类型":
                    sql += "where category_name like ? and is_onsale = 'true'";
                    break;
                default:
                    break;
            }
        }
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, values);
//        new OperationalLogic().autoupdateTable(table2, resultSet, null);//更新表格

        Object[][] data = OperationalLogic.resultSetanalysis(resultSet);
        DefaultTableModel model = new DefaultTableModel(data, new String[]{"商品ID", "商品名称"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table2.setModel(model);

        try {
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String tradeID = resultSet.getString(1);
                String tradeName = resultSet.getString(2);
                System.out.println("查询结果:" + tradeID + "-" + tradeName);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 商品结算页面 点击添加按钮
     *
     * @param e
     */
    private void button2MouseClicked(MouseEvent e) {
        // TODO add your code here
        int selectedRow = table2.getSelectedRow();//获取索引
        if (selectedRow == -1) {
            return;
        }
//        Object[] rowData = new Object[table2.getColumnCount()];
//        for (int i = 0; i < table2.getColumnCount(); i++) {
//            rowData[i] = table2.getValueAt(selectedRow, i);
//        }
//        System.out.println("索引" + selectedRow);
        Object productID_tab2 = table2.getValueAt(selectedRow, 0);//获取商品ID
        String sql = "select product_id as '商品ID',product_name as '商品名称',price as '商品价格',stock as '库存' from tbl_product where product_id = ?";
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{productID_tab2});//根据ID查询商品

        int remainingInventory;//剩余库存
        int addNumber = (Integer) spinner1.getValue();//添加个数
        int numberOfSettlements;//结算个数

        Object[] data = new Object[4];
        try {
            remainingInventory = resultSet.next() ? resultSet.getInt(4) : 0;//设置剩余库存
            if (remainingInventory < 1) {
                JOptionPane.showMessageDialog(null, "库存不足", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
            //遍历tab1 查看是否有存在的项
            for (int row = 0; row < table1.getRowCount(); row++) {
                Object productID_tab1 = table1.getValueAt(row, 0);
//                Class aClass = productID_tab1.getClass();
//                System.out.println(aClass.getName());
                //寻找商品ID相同的单元格
                if (productID_tab1 == productID_tab2) {
                    System.out.println("已添加过该商品");
                    numberOfSettlements = (Integer) table1.getValueAt(row, 3);//结算个数
                    //检测添加的个数是不是大于库存
                    if (addNumber + numberOfSettlements > remainingInventory) {
                        JOptionPane.showMessageDialog(null, "结算商品个数大于库存", "警告", JOptionPane.WARNING_MESSAGE);
                        table1.setValueAt(remainingInventory, row, 3);//修改值为库存个数
                        spinner1.setValue(1);
                        return;
                    }
                    table1.setValueAt(numberOfSettlements + addNumber, row, 3);//修改已有的值
                    spinner1.setValue(1);
                    return;
                }
            }

            resultSet.beforeFirst();
            if (resultSet.next()) {
                for (int i = 0; i < 3; i++) {
                    data[i] = resultSet.getObject(i + 1);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (addNumber > remainingInventory) {
            JOptionPane.showMessageDialog(null, "添加商品个数大于库存", "警告", JOptionPane.WARNING_MESSAGE);
            data[3] = remainingInventory;//最终设置结算个数
        } else {
            data[3] = addNumber;//最终设置结算个数
        }
        productSettlement.addRow(data);//添加新一行
        spinner1.setValue(1);
    }

    /**
     * 商品结算页面 点击删除商品
     *
     * @param e
     */
    private void button3MouseClicked(MouseEvent e) {
        // TODO add your code here
        productDeletionEvent = false;
        int selectedRow = table1.getSelectedRow(); // 获取选中的行
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.removeRow(selectedRow); // 从表格模型中删除行
        }
        productDeletionEvent = true;
    }

    /**
     * 商品结算页面 查询输入框 回车事件
     *
     * @param e
     */
    private void textField1(ActionEvent e) {
        // TODO add your code here
        button1MouseClicked(null);
    }


    /**
     * 商品结算页面 点击结算事件
     *
     * @param e
     */
    private void button4MouseClicked(MouseEvent e) {
        // TODO add your code here
        if (table1.getRowCount() < 1) {
            return;
        }
        productDeletionEvent = false;//防止误触发
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter_1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter formatter_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeString_1 = now.format(formatter_1);//时间字符串
        String dateTimeString_2 = now.format(formatter_2);//时间字符串
        String orderNumber = dateTimeString_1 + userID;//订单号
        double totalPriceOfGoods = 0;//商品总价
        String sql_query = "select stock from tbl_product where product_id = ?";
        String sql_insert_1 = "insert into tbl_order_detail(order_id,product_id,quantity) values (?,?,?)";
        String sql_insert_2 = "insert into tbl_order value (?,?,?,?)";
        String sql_update = "update tbl_product set stock = ? where product_id = ?";


        for (int row = 0; row < table1.getRowCount(); row++) {
            int productID = (Integer) table1.getValueAt(row, 0);//商品ID
            double productprice = ((BigDecimal) table1.getValueAt(row, 2)).doubleValue();//商品价格
            int productQuantity = (Integer) table1.getValueAt(row, 3);//商品数量

            ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql_query, new Object[]{productID});
            try {
                int inventory = (Integer) (resultSet.next() ? resultSet.getObject(1) : 0);//库存
                if ((Integer) inventory >= (Integer) productQuantity) {
                    boolean updateProductTable = mySQLDatabase.precompileImplementSQL(sql_update, new Object[]{inventory
                            - productQuantity, productID});//更新商品详情表
                    if (!updateProductTable){
                        JOptionPane.showMessageDialog(null, "更新商品详情表失败", "警告",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    boolean insertOrderDetails = mySQLDatabase.precompileImplementSQL(sql_insert_1, new Object[]{orderNumber,
                            productID, productQuantity});//插入订单详情表
                    if (!insertOrderDetails){
                        JOptionPane.showMessageDialog(null, "插入订单详情表失败", "警告",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    totalPriceOfGoods = productprice * productQuantity + totalPriceOfGoods;
                    productSettlement.removeRow(row);//删除结算完的商品
                    row--;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        System.out.println("总价" + totalPriceOfGoods);
        mySQLDatabase.precompileImplementSQL(sql_insert_2, new Object[]{orderNumber, userID, dateTimeString_2, totalPriceOfGoods});//插入订单详情表
        JOptionPane.showMessageDialog(null, "订单总价 " + totalPriceOfGoods + " 元", "结算成功",
                JOptionPane.INFORMATION_MESSAGE);
        productDeletionEvent = true;
    }

    /**
     * 商品结算页面 手动设置结算数量
     * 更新表时的调用的方法
     * 检测是否有不合法的数据
     * @param e
     */
    public void TableModelListener(Object e) {
        // TODO add your code here
        int tab2_selectedRow = table2.getSelectedRow();
        //防止在商品结算添加误触发事件
        if (tab2_selectedRow >= 0) {
            return;
        }

        int tab1_selectedRow = table1.getSelectedRow();//获取选中行数
//        //防止删除结算商品时误触发事件
//        if (tab1_selectedRow < 0) {
//            return;
//        }

        //标志 某些时候不触发该事件
        if (!productDeletionEvent) {
            return;
        }

        int ID = (Integer) table1.getValueAt(tab1_selectedRow, 0);//获取选中的商品ID
        String number = table1.getValueAt(tab1_selectedRow, 3).toString();//获取选中行的订单结算数
        //输入的不是纯数字就将个数重置为1
        if (!number.matches("[0-9]+")) {
            table1.setValueAt(1, tab1_selectedRow, 3);
            return;
        }
        int settlementQuantity = Integer.parseInt(number);//订单结算数量

        String sql = "select stock as '库存' from tbl_product where product_id = ?";
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{ID});
        try {
            int inventoryQuantity = (Integer) (resultSet.next() ? resultSet.getObject(1) : 0);//库存数
//            System.out.println("inventoryQuantity:" + inventoryQuantity);
            if (inventoryQuantity < settlementQuantity) {
                JOptionPane.showMessageDialog(null, "结算商品个数大于库存", "警告",
                        JOptionPane.WARNING_MESSAGE);
                productDeletionEvent=false;
                table1.setValueAt(inventoryQuantity, tab1_selectedRow, 3);
                productDeletionEvent=true;
            }else {
                productDeletionEvent=false;
                table1.setValueAt(settlementQuantity, tab1_selectedRow, 3);
                productDeletionEvent=true;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     *  商品结算页面 点击商品查询表 使结算表取消选中
     *
     * @param e
     */
    private void table2MouseClicked(MouseEvent e) {
        // TODO add your code here
        table1.clearSelection();
    }

    /**
     * 商品结算页面 点击结算表 使商品表取消选中
     *
     * @param e
     */
    private void table1MouseClicked(MouseEvent e) {
        // TODO add your code here
        table2.clearSelection();
    }


    /**
     * 订单管理点击查询
     *
     * @param e
     */
    private void button17MouseClicked(MouseEvent e) {
        // TODO add your code here
        boolean fuzzyQuery = checkBox4.isSelected();//模糊查询
        String queryType = comboBox4.getSelectedItem().toString();//查询类型
        String queryValue = textField5.getText();//查询值
        String sql = "select order_id as '订单号',name as '操作用户',create_time as '订单时间',total_price as '订单总价' " +
                "from tbl_order natural join tbl_user ";
        List<Object> values = new ArrayList<>();//查询值
        if (fuzzyQuery) {
            sql = sql + "where order_id like ? or user_id like ? ";
            values.add("%" + queryValue + "%");
            values.add("%" + queryValue + "%");
        } else {
            values.add("%" + queryValue + "%");
            switch (queryType) {
                case "订单号":
                    sql = sql + "where order_id like ?";
                    break;
                case "订单时间":
                    sql = sql + "where user_id like ?";
                    break;
                default:
                    break;
            }
        }
//        System.out.println(sql);
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, values);
        OperationalLogic.autoupdateTable(table5, resultSet);//自动设置表格
    }

    /**
     * 订单管理点击详情
     *
     * @param e
     */
    private void button12MouseClicked(MouseEvent e) {
        // TODO add your code here
        int selectedRow = table5.getSelectedRow();//获取行
        if (selectedRow == -1) {
            return;
        }
        String sql = "select order_detail_id as '订单详情ID',product_id as '商品ID',product_name as '商品名称',quantity as '商品数量' " +
                "from tbl_product natural join tbl_order natural join tbl_order_detail " +
                "where order_id = ?";
        Object orderNumber = table5.getValueAt(selectedRow, 0);//订单号
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, new Object[]{orderNumber});//查询
        new OrderDetailsUi((String) orderNumber, resultSet).setVisible(true);
    }

    /**
     * 商品管理点击查询
     *
     * @param e
     */
    private void button6MouseClicked(MouseEvent e) {
        // TODO add your code here
        boolean fuzzyQuery = checkBox2.isSelected();//模糊查询
        String queryType = comboBox2.getSelectedItem().toString();//查询类型
        String queryValue = textField2.getText();//查询值
        String sql = "select product_id as '商品ID',product_name as '商品名',tbl_category.category_id as '分类ID'," +
                "category_name as '分类名',image_urls as '图像url',price as '价格',stock as '库存',is_onsale as '上/下架' " +
                "from tbl_product left join tbl_category  on tbl_product.category_id = tbl_category.category_id ";
//        Object[] columnName = new String[]{"商品ID","商品名","分类ID","分类名","图像url","价格","库存","上/下架"};

        List<Object> values = new ArrayList<>();//查询值
        if (fuzzyQuery) {
            sql = sql + "where product_id like ? or product_name like ? or category_id like ? or category_name like ?";
            values.add("%" + queryValue + "%");
            values.add("%" + queryValue + "%");
            values.add("%" + queryValue + "%");
            values.add("%" + queryValue + "%");
        } else {
            values.add("%" + queryValue + "%");
            switch (queryType) {
                case "商品编号":
                    sql += "where product_id like ?";
                    break;
                case "商品名":
                    sql += "where product_name like ?";
                    break;
                case "商品类型":
                    sql += "where category_name like ?";
                    break;
                default:
                    break;
            }
        }
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, values);
        OperationalLogic.autoupdateTable(table4, resultSet, new int[]{1, 4, 5, 6, 7});//自动设置表格
    }

    /**
     * 分类管理点击查询
     *
     * @param e
     */
    private void button14MouseClicked(MouseEvent e) {
        // TODO add your code here
        boolean fuzzyQuery = checkBox5.isSelected();//模糊查询
        String queryType = comboBox5.getSelectedItem().toString();//查询类型
        String queryValue = textField6.getText();//查询值
        String sql = "select category_id as '分类ID', category_name as '分类名称', image_url as '分类图例url', status as '分类状态' " +
                "from tbl_category ";
        List<Object> values = new ArrayList<>();//替换SQL查询值
        if (fuzzyQuery) {
            sql += "where category_name like ? or category_id like ?";
            values.add("%" + queryValue + "%");
            values.add("%" + queryValue + "%");
        } else {
            values.add("%" + queryValue + "%");
            switch (queryType) {
                case "分类名称":
                    sql += "where category_name like ?";
                    break;
                case "分类ID":
                    sql += "where category_id like ?";
                    break;
                default:
                    break;
            }
        }
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, values);
        OperationalLogic.autoupdateTable(table6, resultSet);//自动设置表格
    }

    /**
     * 用户管理点击查询
     *
     * @param e
     */
    private void button13MouseClicked(MouseEvent e) {
        // TODO add your code here
        boolean fuzzyQuery = checkBox3.isSelected();//模糊查询
        String queryType = comboBox3.getSelectedItem().toString();//查询类型
        String queryValue = textField4.getText();//查询值
        String sql = "select user_id as '用户ID', username as '登录名', password as '密码', name as '姓名', phone as '电话', role as '权限' " +
                "from tbl_user ";
        List<Object> values = new ArrayList<>();//替换SQL查询值
        if (fuzzyQuery) {
            sql += "where name like ? or user_id like ? or role like ?";
            values.add("%" + queryValue + "%");
            values.add("%" + queryValue + "%");
            values.add("%" + queryValue + "%");
        } else {
            values.add("%" + queryValue + "%");
            switch (queryType) {
                case "姓名":
                    sql += "where name like ?";
                    break;
                case "用户ID":
                    sql += "where user_id like ?";
                    break;
                case "职位":
                    sql += "whree role like ?";
                default:
                    break;
            }
        }
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, values);
        OperationalLogic.autoupdateTable(table3, resultSet);//自动设置表格
    }

    /**
     * 进货管理点击查询
     *
     * @param e
     */
    private void button5MouseClicked(MouseEvent e) {
        // TODO add your code here
        boolean fuzzyQuery = checkBox6.isSelected();//模糊查询
        String queryType = comboBox6.getSelectedItem().toString();//查询类型
        String queryValue = textField3.getText();//查询值
        String sql = "select purchase_id as '进货ID', product_id as '商品ID', product_name as '商品名', quantity as '进货数量'," +
                " purchase_date as '进货时间' " +
                "from tbl_purchase natural join tbl_product ";
        List<Object> values = new ArrayList<>();//替换SQL查询值
        if (fuzzyQuery) {
            sql += "where purchase_id like ? or product_name like ? or product_id like ?";
            values.add("%" + queryValue + "%");
            values.add("%" + queryValue + "%");
            values.add("%" + queryValue + "%");
        } else {
            values.add("%" + queryValue + "%");
            switch (queryType) {
                case "进货号":
                    sql += "where purchase_id like ?";
                    break;
                case "商品名":
                    sql += "where product_name like ?";
                    break;
                case "商品ID":
                    sql += "whree product_id like ?";
                default:
                    break;
            }
        }
        ResultSet resultSet = mySQLDatabase.precompileQuerySQL(sql, values);
        OperationalLogic.autoupdateTable(table7, resultSet);//自动设置表格
    }

    /**
     * 用户管理点击添加
     *
     * @param e
     */
    private void button11MouseClicked(MouseEvent e) {
        // TODO add your code here
        AddUserUi addUserUi = new AddUserUi(this, mySQLDatabase);
        addUserUi.setVisible(true);
        textField4.setText("");
        button13MouseClicked(null);
    }

    /**
     * 用户管理点击删除
     *
     * @param e
     */
    private void button10MouseClicked(MouseEvent e) {
        // TODO add your code here
        int editingRow = table3.getSelectedRow();
        if (editingRow == -1) {
            JOptionPane.showMessageDialog(null, "未选中任何用户", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int userID = (Integer) table3.getValueAt(editingRow, 0);
        String sql = "delete from tbl_user where user_id = ?";
        boolean resultsOfThe = mySQLDatabase.precompileImplementSQL(sql, new Object[]{userID});
        if (resultsOfThe) {
            JOptionPane.showMessageDialog(null, "删除成功");
        } else {
            JOptionPane.showMessageDialog(null, "删除失败", "警告",
                    JOptionPane.ERROR_MESSAGE);
        }
        textField4.setText("");
        button13MouseClicked(null);//更新表
    }

    /**
     * 商品管理界面点击添加
     *
     * @param e
     */
    private void button7MouseClicked(MouseEvent e) {
        // TODO add your code here
        new ProductAdditionUi(this, mySQLDatabase).setVisible(true);
        textField2.setText("");
        button6MouseClicked(null);//更新表
    }

    /**
     * 商品管理点击删除
     *
     * @param e
     */
    private void button8MouseClicked(MouseEvent e) {
        // TODO add your code here
        int editingRow = table4.getSelectedRow();
        if (editingRow == -1) {
            JOptionPane.showMessageDialog(null, "未选中任何商品", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int productID = (Integer) table4.getValueAt(editingRow, 0);
        String sql = "delete from tbl_product where product_id = ?";
        boolean resultsOfThe = mySQLDatabase.precompileImplementSQL(sql, new Object[]{productID});
        if (resultsOfThe) {
            JOptionPane.showMessageDialog(null, "删除成功");
        } else {
            JOptionPane.showMessageDialog(null, "删除失败", "警告",
                    JOptionPane.ERROR_MESSAGE);
        }
        textField2.setText("");
        button6MouseClicked(null);//更新表
    }

    /**
     * 商品分类点击删除
     *
     * @param e
     */
    private void button21MouseClicked(MouseEvent e) {
        // TODO add your code here
        int editingRow = table6.getSelectedRow();
        if (editingRow == -1) {
            JOptionPane.showMessageDialog(null, "未选中任何分类信息", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int productID = (Integer) table6.getValueAt(editingRow, 0);
        String sql = "delete from tbl_category where category_id = ?";
        boolean resultsOfThe = mySQLDatabase.precompileImplementSQL(sql, new Object[]{productID});
        if (resultsOfThe) {
            JOptionPane.showMessageDialog(null, "删除成功");
        } else {
            JOptionPane.showMessageDialog(null, "删除失败", "警告",
                    JOptionPane.ERROR_MESSAGE);
        }
        textField6.setText("");
        button14MouseClicked(null);//更新表
    }

    /**
     * 商品分类点击添加
     *
     * @param e
     */
    private void button15MouseClicked(MouseEvent e) {
        // TODO add your code here
        new CategoryAddedUi(this, mySQLDatabase).setVisible(true);
        textField6.setText("");
        button14MouseClicked(null);
    }

    /**
     * 进货管理点击 新增进货
     *
     * @param e
     */
    private void button19MouseClicked(MouseEvent e) {
        // TODO add your code here
        new PurchaseAddUi(this, mySQLDatabase, userID).setVisible(true);
        textField3.setText("");
        button5MouseClicked(null);
    }

    /**
     * 点击商品管理分类
     *
     * @param e
     */
    private void table4MouseClicked(MouseEvent e) {
        // TODO add your code here
        int row = table4.rowAtPoint(e.getPoint());//行
        int column = table4.columnAtPoint(e.getPoint());//列

        if (column == 2 || column == 3) {
            int productID = (Integer) table4.getValueAt(row, 0);
            System.out.println("productID:"+productID);
            new ProductModification_sort_Ui(this, mySQLDatabase, productID).setVisible(true);
            textField2.setText("");
            button6MouseClicked(null);//更新表
            return;
        }
        if (column==-1||row==-1){
            return;
        }

    }

    /**
     * 商品管理点击修改
     * @param e
     */
    private void button18MouseClicked(MouseEvent e) {
        // TODO add your code here
        int selectedRow = table4.getSelectedRow();
        if (selectedRow<0){
            JOptionPane.showMessageDialog(null, "未选择任何商品信息", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int productID = (Integer) table4.getValueAt(selectedRow, 0);
        new ProductModificationUi(this,mySQLDatabase,productID).setVisible(true);
        textField4.setText("");
        button6MouseClicked(null);
    }

    /**
     * 商品分类界面 点击修改
     * @param e
     */
    private void button16(ActionEvent e) {
        // TODO add your code here
        int selectedRow = table6.getSelectedRow();
        if (selectedRow<0){
            JOptionPane.showMessageDialog(null, "未选中需要修改的分类", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int categoryId = (Integer) table6.getValueAt(selectedRow, 0);
        System.out.println(categoryId);
        new CategorymodifyUi(this,mySQLDatabase,categoryId).setVisible(true);
        textField6.setText("");
        button14MouseClicked(null);
    }

    /**
     * 用户管理界面 点击修改
     * @param e
     */
    private void button9(ActionEvent e) {
        // TODO add your code here
        int selectedRow = table3.getSelectedRow();
        if (selectedRow<0){
            JOptionPane.showMessageDialog(null, "未选中需要修改的用户", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        Integer userID = (Integer) table3.getValueAt(selectedRow, 0);
        if (userID==1){
            JOptionPane.showMessageDialog(null, "管理员不允许修改", "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        new ModifyUserUi(this,mySQLDatabase, userID).setVisible(true);
        textField4.setText("");
        button13MouseClicked(null);
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        tabbedPane1 = new JTabbedPane();
        panel2 = new JPanel();
        panel8 = new JPanel();
        textField1 = new JTextField();
        button1 = new JButton();
        label1 = new JLabel();
        spinner1 = new JSpinner();
        button2 = new JButton();
        button3 = new JButton();
        checkBox1 = new JCheckBox();
        comboBox1 = new JComboBox<>();
        button4 = new JButton();
        panel12 = new JPanel();
        scrollPane1 = new JScrollPane();
        table2 = new JTable();
        panel15 = new JPanel();
        scrollPane2 = new JScrollPane();
        table1 = new JTable();
        panel3 = new JPanel();
        panel10 = new JPanel();
        textField5 = new JTextField();
        comboBox4 = new JComboBox<>();
        button17 = new JButton();
        checkBox4 = new JCheckBox();
        button12 = new JButton();
        scrollPane5 = new JScrollPane();
        table5 = new JTable();
        panel4 = new JPanel();
        panel9 = new JPanel();
        textField2 = new JTextField();
        button6 = new JButton();
        checkBox2 = new JCheckBox();
        button7 = new JButton();
        button8 = new JButton();
        comboBox2 = new JComboBox<>();
        button18 = new JButton();
        scrollPane4 = new JScrollPane();
        table4 = new JTable();
        panel5 = new JPanel();
        scrollPane6 = new JScrollPane();
        table6 = new JTable();
        panel14 = new JPanel();
        textField6 = new JTextField();
        button14 = new JButton();
        checkBox5 = new JCheckBox();
        button15 = new JButton();
        button16 = new JButton();
        comboBox5 = new JComboBox<>();
        button21 = new JButton();
        panel6 = new JPanel();
        panel13 = new JPanel();
        textField3 = new JTextField();
        comboBox6 = new JComboBox<>();
        button5 = new JButton();
        checkBox6 = new JCheckBox();
        button19 = new JButton();
        button20 = new JButton();
        scrollPane7 = new JScrollPane();
        table7 = new JTable();
        panel7 = new JPanel();
        panel11 = new JPanel();
        textField4 = new JTextField();
        comboBox3 = new JComboBox<>();
        checkBox3 = new JCheckBox();
        button11 = new JButton();
        button10 = new JButton();
        button13 = new JButton();
        button9 = new JButton();
        scrollPane3 = new JScrollPane();
        table3 = new JTable();
        panel16 = new JPanel();

        //======== this ========
        setPreferredSize(new Dimension(780, 500));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
            panel1.setLayout(new BorderLayout());

            //======== tabbedPane1 ========
            {

                //======== panel2 ========
                {
                    panel2.setLayout(new BorderLayout());

                    //======== panel8 ========
                    {

                        //---- textField1 ----
                        textField1.addActionListener(e -> textField1(e));

                        //---- button1 ----
                        button1.setText("\u67e5\u8be2");
                        button1.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button1MouseClicked(e);
                            }
                        });

                        //---- label1 ----
                        label1.setText("\u6570\u91cf");
                        label1.setHorizontalAlignment(SwingConstants.CENTER);

                        //---- spinner1 ----
                        spinner1.setModel(new SpinnerNumberModel(1, 1, null, 1));

                        //---- button2 ----
                        button2.setText("\u6dfb\u52a0");
                        button2.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button2MouseClicked(e);
                            }
                        });

                        //---- button3 ----
                        button3.setText("\u5220\u9664");
                        button3.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button3MouseClicked(e);
                            }
                        });

                        //---- checkBox1 ----
                        checkBox1.setText("\u6a21\u7cca\u67e5\u8be2");

                        //---- comboBox1 ----
                        comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                            "\u7f16\u53f7",
                            "\u540d\u79f0",
                            "\u7c7b\u578b"
                        }));

                        GroupLayout panel8Layout = new GroupLayout(panel8);
                        panel8.setLayout(panel8Layout);
                        panel8Layout.setHorizontalGroup(
                            panel8Layout.createParallelGroup()
                                .addGroup(panel8Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button1, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkBox1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(label1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(spinner1, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                                    .addComponent(button2, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button3, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                        );
                        panel8Layout.setVerticalGroup(
                            panel8Layout.createParallelGroup()
                                .addGroup(panel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button3)
                                    .addComponent(button2)
                                    .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button1)
                                    .addComponent(checkBox1)
                                    .addComponent(label1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        );
                    }
                    panel2.add(panel8, BorderLayout.NORTH);

                    //---- button4 ----
                    button4.setText("\u7ed3\u7b97");
                    button4.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button4MouseClicked(e);
                        }
                    });
                    panel2.add(button4, BorderLayout.SOUTH);

                    //======== panel12 ========
                    {
                        panel12.setLayout(new BorderLayout());

                        //======== scrollPane1 ========
                        {
                            scrollPane1.setPreferredSize(new Dimension(150, 158));

                            //---- table2 ----
                            table2.setModel(new DefaultTableModel(
                                new Object[][] {
                                },
                                new String[] {
                                    "\u5546\u54c1ID", "\u5546\u54c1\u540d\u79f0"
                                }
                            ));
                            table2.setAutoCreateRowSorter(true);
                            table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            table2.setPreferredScrollableViewportSize(new Dimension(450, 10));
                            table2.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    table2MouseClicked(e);
                                }
                            });
                            scrollPane1.setViewportView(table2);
                        }
                        panel12.add(scrollPane1, BorderLayout.CENTER);
                    }
                    panel2.add(panel12, BorderLayout.WEST);

                    //======== panel15 ========
                    {
                        panel15.setLayout(new BorderLayout());

                        //======== scrollPane2 ========
                        {
                            scrollPane2.setBorder(new FlatBorder());

                            //---- table1 ----
                            table1.setBorder(new FlatEmptyBorder());
                            table1.setModel(new DefaultTableModel(
                                new Object[][] {
                                },
                                new String[] {
                                    "\u5546\u54c1ID", "\u5546\u54c1\u540d\u79f0", "\u4ef7\u683c", "\u6570\u91cf"
                                }
                            ) {
                                Class<?>[] columnTypes = new Class<?>[] {
                                    Object.class, String.class, Float.class, Integer.class
                                };
                                @Override
                                public Class<?> getColumnClass(int columnIndex) {
                                    return columnTypes[columnIndex];
                                }
                            });
                            table1.setAutoCreateRowSorter(true);
                            table1.setPreferredScrollableViewportSize(new Dimension(450, 10));
                            table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            table1.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    table1MouseClicked(e);
                                }
                            });
                            scrollPane2.setViewportView(table1);
                        }
                        panel15.add(scrollPane2, BorderLayout.CENTER);
                    }
                    panel2.add(panel15, BorderLayout.CENTER);
                }
                tabbedPane1.addTab("\u5546\u54c1\u7ed3\u7b97", panel2);

                //======== panel3 ========
                {
                    panel3.setLayout(new BorderLayout());

                    //======== panel10 ========
                    {

                        //---- comboBox4 ----
                        comboBox4.setModel(new DefaultComboBoxModel<>(new String[] {
                            "\u8ba2\u5355\u53f7",
                            "\u8ba2\u5355\u65f6\u95f4"
                        }));

                        //---- button17 ----
                        button17.setText("\u67e5\u8be2");
                        button17.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button17MouseClicked(e);
                            }
                        });

                        //---- checkBox4 ----
                        checkBox4.setText("\u6a21\u7cca\u67e5\u8be2");

                        //---- button12 ----
                        button12.setText("\u8be6\u60c5");
                        button12.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button12MouseClicked(e);
                            }
                        });

                        GroupLayout panel10Layout = new GroupLayout(panel10);
                        panel10.setLayout(panel10Layout);
                        panel10Layout.setHorizontalGroup(
                            panel10Layout.createParallelGroup()
                                .addGroup(panel10Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(textField5, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addGap(15, 15, 15)
                                    .addComponent(button17, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkBox4)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 271, Short.MAX_VALUE)
                                    .addComponent(button12, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())
                        );
                        panel10Layout.setVerticalGroup(
                            panel10Layout.createParallelGroup()
                                .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(textField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBox4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(button17)
                                    .addComponent(checkBox4, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button12))
                        );
                    }
                    panel3.add(panel10, BorderLayout.NORTH);

                    //======== scrollPane5 ========
                    {

                        //---- table5 ----
                        table5.setModel(new DefaultTableModel(
                            new Object[][] {
                            },
                            new String[] {
                                "\u8ba2\u5355\u53f7", "\u64cd\u4f5c\u7528\u6237", "\u8ba2\u5355\u65f6\u95f4", "\u8ba2\u5355\u603b\u4ef7"
                            }
                        ));
                        table5.setAutoCreateRowSorter(true);
                        table5.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        scrollPane5.setViewportView(table5);
                    }
                    panel3.add(scrollPane5, BorderLayout.CENTER);
                }
                tabbedPane1.addTab("\u8ba2\u5355\u7ba1\u7406", panel3);

                //======== panel4 ========
                {
                    panel4.setLayout(new BorderLayout());

                    //======== panel9 ========
                    {

                        //---- button6 ----
                        button6.setText("\u67e5\u8be2");
                        button6.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button6MouseClicked(e);
                            }
                        });

                        //---- checkBox2 ----
                        checkBox2.setText("\u6a21\u7cca\u67e5\u8be2");

                        //---- button7 ----
                        button7.setText("\u6dfb\u52a0");
                        button7.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button7MouseClicked(e);
                            }
                        });

                        //---- button8 ----
                        button8.setText("\u5220\u9664");
                        button8.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button8MouseClicked(e);
                            }
                        });

                        //---- comboBox2 ----
                        comboBox2.setModel(new DefaultComboBoxModel<>(new String[] {
                            "\u5546\u54c1\u7f16\u53f7",
                            "\u5546\u54c1\u540d",
                            "\u5546\u54c1\u7c7b\u578b"
                        }));

                        //---- button18 ----
                        button18.setText("\u4fee\u6539");
                        button18.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button18MouseClicked(e);
                            }
                        });

                        GroupLayout panel9Layout = new GroupLayout(panel9);
                        panel9.setLayout(panel9Layout);
                        panel9Layout.setHorizontalGroup(
                            panel9Layout.createParallelGroup()
                                .addGroup(panel9Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button6, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkBox2)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                                    .addComponent(button18)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button7, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button8, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())
                        );
                        panel9Layout.setVerticalGroup(
                            panel9Layout.createParallelGroup()
                                .addGroup(panel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button6)
                                    .addComponent(checkBox2, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button8)
                                    .addComponent(button7)
                                    .addComponent(button18))
                        );
                    }
                    panel4.add(panel9, BorderLayout.NORTH);

                    //======== scrollPane4 ========
                    {

                        //---- table4 ----
                        table4.setModel(new DefaultTableModel(
                            new Object[][] {
                            },
                            new String[] {
                                "\u5546\u54c1ID", "\u5546\u54c1\u540d", "\u5206\u7c7bID", "\u5206\u7c7b\u540d", "\u56fe\u50cfurl", "\u4ef7\u683c", "\u5e93\u5b58", "\u4e0a\u67b6/\u4e0b\u67b6"
                            }
                        ));
                        table4.setAutoCreateRowSorter(true);
                        table4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        table4.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                table4MouseClicked(e);
                            }
                        });
                        scrollPane4.setViewportView(table4);
                    }
                    panel4.add(scrollPane4, BorderLayout.CENTER);
                }
                tabbedPane1.addTab("\u5546\u54c1\u7ba1\u7406", panel4);

                //======== panel5 ========
                {
                    panel5.setLayout(new BorderLayout());

                    //======== scrollPane6 ========
                    {

                        //---- table6 ----
                        table6.setModel(new DefaultTableModel(
                            new Object[][] {
                            },
                            new String[] {
                                "\u5206\u7c7bID", "\u5206\u7c7b\u540d\u79f0", "\u5206\u7c7b\u56fe\u4f8burl", "\u5206\u7c7b\u72b6\u6001"
                            }
                        ));
                        table6.setAutoCreateRowSorter(true);
                        table6.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        scrollPane6.setViewportView(table6);
                    }
                    panel5.add(scrollPane6, BorderLayout.CENTER);

                    //======== panel14 ========
                    {

                        //---- button14 ----
                        button14.setText("\u67e5\u8be2");
                        button14.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button14MouseClicked(e);
                            }
                        });

                        //---- checkBox5 ----
                        checkBox5.setText("\u6a21\u7cca\u67e5\u8be2");

                        //---- button15 ----
                        button15.setText("\u65b0\u589e\u5206\u7c7b");
                        button15.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button15MouseClicked(e);
                            }
                        });

                        //---- button16 ----
                        button16.setText("\u4fee\u6539");
                        button16.addActionListener(e -> button16(e));

                        //---- comboBox5 ----
                        comboBox5.setModel(new DefaultComboBoxModel<>(new String[] {
                            "\u5206\u7c7b\u540d\u79f0",
                            "\u5206\u7c7bID"
                        }));

                        //---- button21 ----
                        button21.setText("\u5220\u9664");
                        button21.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button21MouseClicked(e);
                            }
                        });

                        GroupLayout panel14Layout = new GroupLayout(panel14);
                        panel14.setLayout(panel14Layout);
                        panel14Layout.setHorizontalGroup(
                            panel14Layout.createParallelGroup()
                                .addGroup(panel14Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(textField6, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button14, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkBox5)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                                    .addComponent(button16, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button15, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button21, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())
                        );
                        panel14Layout.setVerticalGroup(
                            panel14Layout.createParallelGroup()
                                .addGroup(panel14Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(textField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBox5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button14)
                                    .addComponent(checkBox5, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
                                .addGroup(panel14Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(button21)
                                    .addComponent(button15)
                                    .addComponent(button16))
                        );
                    }
                    panel5.add(panel14, BorderLayout.NORTH);
                }
                tabbedPane1.addTab("\u5546\u54c1\u5206\u7c7b", panel5);

                //======== panel6 ========
                {
                    panel6.setLayout(new BorderLayout());

                    //======== panel13 ========
                    {

                        //---- comboBox6 ----
                        comboBox6.setModel(new DefaultComboBoxModel<>(new String[] {
                            "\u8fdb\u8d27\u53f7",
                            "\u5546\u54c1\u540d",
                            "\u5546\u54c1ID"
                        }));

                        //---- button5 ----
                        button5.setText("\u67e5\u8be2");
                        button5.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button5MouseClicked(e);
                            }
                        });

                        //---- checkBox6 ----
                        checkBox6.setText("\u6a21\u7cca\u67e5\u8be2");

                        //---- button19 ----
                        button19.setText("\u65b0\u589e\u8fdb\u8d27");
                        button19.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button19MouseClicked(e);
                            }
                        });

                        //---- button20 ----
                        button20.setText("\u4fdd\u5b58");

                        GroupLayout panel13Layout = new GroupLayout(panel13);
                        panel13.setLayout(panel13Layout);
                        panel13Layout.setHorizontalGroup(
                            panel13Layout.createParallelGroup()
                                .addGroup(panel13Layout.createSequentialGroup()
                                    .addComponent(textField3, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(comboBox6, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, 0)
                                    .addComponent(button5, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkBox6)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 226, Short.MAX_VALUE)
                                    .addComponent(button19, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button20, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())
                        );
                        panel13Layout.setVerticalGroup(
                            panel13Layout.createParallelGroup()
                                .addComponent(textField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(comboBox6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(button5)
                                .addGroup(panel13Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(button20)
                                    .addComponent(button19))
                                .addComponent(checkBox6, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                        );
                    }
                    panel6.add(panel13, BorderLayout.NORTH);

                    //======== scrollPane7 ========
                    {

                        //---- table7 ----
                        table7.setAutoCreateRowSorter(true);
                        table7.setModel(new DefaultTableModel(
                            new Object[][] {
                            },
                            new String[] {
                                "\u8fdb\u8d27ID", "\u5546\u54c1ID", "\u5546\u54c1\u540d", "\u8fdb\u8d27\u65f6\u95f4", "\u8fdb\u8d27\u6570\u91cf"
                            }
                        ));
                        table7.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        scrollPane7.setViewportView(table7);
                    }
                    panel6.add(scrollPane7, BorderLayout.CENTER);
                }
                tabbedPane1.addTab("\u8fdb\u8d27\u7ba1\u7406", panel6);

                //======== panel7 ========
                {
                    panel7.setLayout(new BorderLayout());

                    //======== panel11 ========
                    {
                        panel11.setPreferredSize(new Dimension(661, 27));

                        //---- comboBox3 ----
                        comboBox3.setModel(new DefaultComboBoxModel<>(new String[] {
                            "\u59d3\u540d",
                            "\u7528\u6237ID",
                            "\u804c\u4f4d"
                        }));

                        //---- checkBox3 ----
                        checkBox3.setText("\u6a21\u7cca\u67e5\u8be2");

                        //---- button11 ----
                        button11.setText("\u6dfb\u52a0");
                        button11.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button11MouseClicked(e);
                            }
                        });

                        //---- button10 ----
                        button10.setText("\u5220\u9664");
                        button10.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button10MouseClicked(e);
                            }
                        });

                        //---- button13 ----
                        button13.setText("\u67e5\u8be2");
                        button13.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                button13MouseClicked(e);
                            }
                        });

                        //---- button9 ----
                        button9.setText("\u4fee\u6539");
                        button9.addActionListener(e -> button9(e));

                        GroupLayout panel11Layout = new GroupLayout(panel11);
                        panel11.setLayout(panel11Layout);
                        panel11Layout.setHorizontalGroup(
                            panel11Layout.createParallelGroup()
                                .addGroup(panel11Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(textField4, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button13, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkBox3)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                                    .addComponent(button9)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button11, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(button10, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())
                        );
                        panel11Layout.setVerticalGroup(
                            panel11Layout.createParallelGroup()
                                .addComponent(textField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGroup(panel11Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button13)
                                    .addComponent(checkBox3, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button10)
                                    .addComponent(button11)
                                    .addComponent(button9))
                        );
                    }
                    panel7.add(panel11, BorderLayout.NORTH);

                    //======== scrollPane3 ========
                    {

                        //---- table3 ----
                        table3.setModel(new DefaultTableModel(
                            new Object[][] {
                            },
                            new String[] {
                                "\u7528\u6237ID", "\u767b\u5f55\u540d", "\u5bc6\u7801", "\u59d3\u540d", "\u7535\u8bdd", "\u89d2\u8272"
                            }
                        ));
                        table3.setAutoCreateRowSorter(true);
                        table3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        scrollPane3.setViewportView(table3);
                    }
                    panel7.add(scrollPane3, BorderLayout.CENTER);
                }
                tabbedPane1.addTab("\u7528\u6237\u7ba1\u7406", panel7);

                //======== panel16 ========
                {
                    panel16.setLayout(new BorderLayout());
                }
                tabbedPane1.addTab("\u8bbe\u7f6e", panel16);
            }
            panel1.add(tabbedPane1, BorderLayout.CENTER);
        }
        contentPane.add(panel1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    protected JPanel panel1;
    protected JTabbedPane tabbedPane1;
    protected JPanel panel2;
    protected JPanel panel8;
    protected JTextField textField1;
    protected JButton button1;
    protected JLabel label1;
    protected JSpinner spinner1;
    protected JButton button2;
    protected JButton button3;
    protected JCheckBox checkBox1;
    protected JComboBox<String> comboBox1;
    protected JButton button4;
    protected JPanel panel12;
    protected JScrollPane scrollPane1;
    protected JTable table2;
    protected JPanel panel15;
    protected JScrollPane scrollPane2;
    protected JTable table1;
    protected JPanel panel3;
    protected JPanel panel10;
    protected JTextField textField5;
    protected JComboBox<String> comboBox4;
    protected JButton button17;
    protected JCheckBox checkBox4;
    protected JButton button12;
    protected JScrollPane scrollPane5;
    protected JTable table5;
    protected JPanel panel4;
    protected JPanel panel9;
    protected JTextField textField2;
    protected JButton button6;
    protected JCheckBox checkBox2;
    protected JButton button7;
    protected JButton button8;
    protected JComboBox<String> comboBox2;
    protected JButton button18;
    protected JScrollPane scrollPane4;
    protected JTable table4;
    protected JPanel panel5;
    protected JScrollPane scrollPane6;
    protected JTable table6;
    protected JPanel panel14;
    protected JTextField textField6;
    protected JButton button14;
    protected JCheckBox checkBox5;
    protected JButton button15;
    protected JButton button16;
    protected JComboBox<String> comboBox5;
    protected JButton button21;
    protected JPanel panel6;
    protected JPanel panel13;
    protected JTextField textField3;
    protected JComboBox<String> comboBox6;
    protected JButton button5;
    protected JCheckBox checkBox6;
    protected JButton button19;
    protected JButton button20;
    protected JScrollPane scrollPane7;
    protected JTable table7;
    protected JPanel panel7;
    protected JPanel panel11;
    protected JTextField textField4;
    protected JComboBox<String> comboBox3;
    protected JCheckBox checkBox3;
    protected JButton button11;
    protected JButton button10;
    protected JButton button13;
    protected JButton button9;
    protected JScrollPane scrollPane3;
    protected JTable table3;
    protected JPanel panel16;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
