package luxoft.console.application.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by mac on 16.06.17.
 */
public class DBConnection {
    private Connection conn = null;
    private final String propertiesFileName = "dbconnection.properties";

    public DBConnection() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("src/main/resources/" + propertiesFileName)) {
            props.load(in);
        } catch (FileNotFoundException e) {
            System.err.println("Can't open a properties file!");
            e.printStackTrace();
        } catch (IOException ioe) {
            System.err.println("Can't read a properties file");
            ioe.printStackTrace();
        }

        /* Retrieving db connection data from a properties object */
        String driver = props.getProperty("jdbc.driver");
        if (driver != null) {
            try {
                Class.forName(driver);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("No such driver!");
            }
        }

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        String settings = "?useUnicode=true&" +
                "characterEncoding=utf-8&" +
                "useJDBCCompliantTimezoneShift=true&" +
                "useLegacyDatetimeCode=false&" +
                "serverTimezone=UTC&" +
                "useSSL=false";

        /* Connection to a db */
        try {
            conn = DriverManager.getConnection(url + settings, username, password);
        } catch (SQLException e) {
            System.err.println("Database connection error!");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Database closing error!");
                e.printStackTrace();
            }
        }
    }
}

