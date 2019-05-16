package abstractdao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import javafx.scene.image.ImageView;

@Repository
public interface ImageDao {
	ImageView addImage(Connection con, File file) throws SQLException, IOException;
	
	void loadImages(Connection con) throws SQLException;
}
