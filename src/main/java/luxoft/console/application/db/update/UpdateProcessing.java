package luxoft.console.application.db.update;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by mac on 21.06.17.
 */
@FunctionalInterface
public interface UpdateProcessing {
    void insert(PreparedStatement preparedStatement) throws SQLException;
}
