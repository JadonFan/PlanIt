package abstractdao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface ImageDao {
	void addImage(Connection con, File file) throws SQLException, IOException;
	
	void loadImages(Connection con) throws SQLException;
}
