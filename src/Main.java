import java.sql.*;

public class Main {

    // Exercise 1
    public static void displayDepartment(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT DEPTNO, DNAME, LOC FROM DEPT");
        while (resultSet.next()) {
            int deptno = resultSet.getInt("DEPTNO");
            String dname = resultSet.getString("DNAME");
            String loc = resultSet.getString("LOC");
            System.out.println("Department " + deptno + " is for " + dname + " and located in " + loc);
        }
        resultSet.close();
    }

    public static void moveDepartment(Connection connection, int empno, int newDeptno) throws SQLException {
        // Exercise 2
        Statement statement = connection.createStatement();
        statement.execute("UPDATE EMP SET DEPTNO = " + newDeptno + " WHERE EMPNO = " + empno);
    }

    // Exercise 3
    public static void displayTable(Connection connection, String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        for (int i = 1; i <= columnsNumber; i++) {
            System.out.print(rsmd.getColumnName(i) + " | ");
        }
        System.out.println();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                System.out.print(resultSet.getObject(i) + " | ");
            }
            System.out.println();
        }
        resultSet.close();
    }

    // Exercise 4


    public static void main(String[] args) {
        /* Load JDBC Driver. */
        try {
            Class.forName("oracle.jdbc.OracleDriver");  // Class.forName("oracle.jdbc.OracleDriver") ;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:oracle:thin:@localhost:49161:xe";
        String user = "darwin";
        String pass = "r0pavoga";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, pass);
            // Requests to bdd will be here
            System.out.println("Bdd Connected");
            displayDepartment(connection);
            moveDepartment(connection, 7584, 10);
            displayTable(connection, "EMP");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignore) {

                }
            }
        }
    }

}
