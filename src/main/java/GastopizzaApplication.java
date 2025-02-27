import db.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class GastopizzaApplication {
    static Logger logger = Logger.getLogger(GastopizzaApplication.class.getName());

    public static void main(String[] args)  throws SQLException {
        DataSource dataSource = new DataSource();
        Connection connection = dataSource.getConnection();
        logger.info(connection.toString());
        connection.close();


    }
}
