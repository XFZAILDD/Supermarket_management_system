import javax.swing.*;
import java.sql.*;
import java.util.List;


public class MySQLDatabase {
    private Connection conn;
    private ResultSet resultSet;
    private Statement statement;
    private PreparedStatement preparedStatement;//预处理
    private String driver = "com.mysql.cj.jdbc.Driver";//驱动
    private String url = "jdbc:mysql://localhost:3306/supermarketdata?useSSL=false&serverTimezone=GMT%2B8";//数据库
    private String username = "root";//用户名
    private String password = "admin";//密码


    public MySQLDatabase(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        init();
    }

    public MySQLDatabase() {
        init();
    }

    /**
     * 初始化 加载驱动 连接数据库
     */
    private void init() {
        try {
            //加载驱动
            Class.forName(driver);
            // 建立连接
            this.conn = DriverManager.getConnection(url, username, password);
            //执行sql的对象 statement
            this.statement = conn.createStatement();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "数据库连接错误,请检查配置", "警告",
                    JOptionPane.WARNING_MESSAGE);
            throw new RuntimeException(e);
        }
    }
    /**
     * 执行SQL语句
     *
     * @param sql
     * @return
     */
    public ResultSet executeSQLStatements(String sql) {
        System.out.println(sql);
        try {
            this.resultSet = this.statement.executeQuery(sql);
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


//    /**
//     * 预编译执行SQL
//     * @param sql    SQL语句
//     * @param values 替换参数 {""}
//     * @return ResultSet查询结果
//     * @deprecated
//     */
//    public ResultSet precompileQuerySQL(String sql, String[] values) {
//        try {
//            this.preparedStatement = conn.prepareStatement(sql);
//            int number = 1;
//            for (String value : values) {
//                String type = value.split("-")[0];
//                String replace_value = value.split("-")[1];
//                switch (type) {
//                    case "string":
//                        this.preparedStatement.setString(number++, replace_value);
//                        break;
//                    case "int":
//                        this.preparedStatement.setInt(number++, Integer.parseInt(replace_value));
//                        break;
//                    case "double":
//                        this.preparedStatement.setDouble(number++, Double.parseDouble(replace_value));
//                        break;
//                    case "date":
//                        this.preparedStatement.setDate(number++, Date.valueOf(replace_value));
//                        break;
//                    default:
//                        break;
//                }
//            }
//            // 执行查询
//            this.resultSet = preparedStatement.executeQuery();
//            return resultSet;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    /**
//     * 预编译执行SQL
//     *
//     * @param sql
//     * @param values
//     * @return
//     * @deprecated
//     */
//    public ResultSet precompileQuerySQL(String sql, List<String> values) {
//        String[] String_values = values.toArray(new String[values.size()]);
//        return precompileQuerySQL(sql, String_values);
//    }

    /**
     * 预编译查询SQL
     *
     * @param sql
     * @param values
     * @return
     */
    public ResultSet precompileQuerySQL(String sql, Object[] values) {
        System.out.println(sql);
        try {
            this.preparedStatement = conn.prepareStatement(sql);
            // 设置占位符的值
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    preparedStatement.setObject(i + 1, values[i]);
                }
            }
            // 执行查询操作
            this.resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultSet;
    }

    public ResultSet precompileQuerySQL(String sql, List<Object> values) {
        return precompileQuerySQL(sql, values.toArray());
    }

    /**
     *
     * @param sql
     * @param values
     * @return 执行成功返回true 错误返回false
     */
    public boolean precompileImplementSQL(String sql, Object[] values) {
        System.out.println(sql);
        try {
            this.preparedStatement = conn.prepareStatement(sql);
            // 设置占位符的值
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    preparedStatement.setObject(i + 1, values[i]);
                }
            }
            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 关闭数据库连接
     */
    public void close() {
        try {
            this.conn.close();
            this.resultSet.close();
            this.statement.close();
            this.preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
