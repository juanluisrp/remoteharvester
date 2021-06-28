package geocat.dblogging.logback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class CustomDBAppender extends ch.qos.logback.classic.db.DBAppender {


    String SQL = "update logging_event  set jms_correlation_id=? where event_id = ?";

    @Override
    protected void insertProperties(Map<String, String> mergedMap, Connection connection, long eventId)
            throws SQLException {
        if (!mergedMap.containsKey("JMSCorrelationID"))
            return; // nothing to do

        String value = mergedMap.get("JMSCorrelationID");
        if ((value == null) || (value.isEmpty()))
            return; //nothing to do

        PreparedStatement pstmt = connection.prepareStatement(SQL);
        pstmt.setString(1, value);
        pstmt.setLong(2, eventId);

        int affectedRows = pstmt.executeUpdate();
    }
}
