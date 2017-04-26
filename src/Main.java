import java.sql.*;
import java.util.Scanner;

public class Main {

    /* PART 3 */

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

    // Exercise 2
    public static void moveDepartment(Connection connection, int empno, int newDeptno) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("UPDATE EMP SET DEPTNO = " + newDeptno + " WHERE EMPNO = " + empno);
    }

    // Exercise 3
    public static void displayTable(Connection connection, String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        displayResultSet(resultSet);
        resultSet.close();
    }

    // Exercise 4
    // The way of execute raw sql statement cannot prevent leak caused by SQL injection.

    /* PART 4 */

    public static void displayResultSet(ResultSet resultSet) throws SQLException {
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
    }

    public static void searchEmployer(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM EMP WHERE EFIRST = ? AND ENAME = ?"
        );
        System.out.print("Employer Name: ");
        Scanner sc = new Scanner(System.in);
        String firstName = sc.next();
        preparedStatement.setString(1, firstName);
        String lastName = sc.next();
        preparedStatement.setString(2, lastName);
        ResultSet resultSet = preparedStatement.executeQuery();
        displayResultSet(resultSet);
    }

    // Exercise 5
    public static void moveDepartmentSafe(Connection connection, int empno, int newDeptno) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE EMP SET DEPTNO = ? WHERE EMPNO = ?"
        );
        preparedStatement.setInt(1, newDeptno);
        preparedStatement.setInt(2, empno);
        preparedStatement.execute();
    }

    // Exercise 6
    // You can't.
    // You need to construct the sql with string concatenation/placeholder with String.format.
    // Prepared statement is for the column values not for table name.
    // It is rather easy to check the table name against the list of available tables in the database.

    // Exercise 7
    // You may better check it in WEB Technologies TPs.

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
            searchEmployer(connection);
            moveDepartmentSafe(connection, 7584,20);

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
