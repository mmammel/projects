import java.sql.*;

public interface Preparer {
  public PreparedStatement prepareStatement( PreparedStatement ps ) throws SQLException;
}

