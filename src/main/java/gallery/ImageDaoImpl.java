package gallery;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import abstractdao.ImageDao;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import user.AppSession;

public class ImageDaoImpl implements ImageDao {
	private List<ImageView> imageViews;
	
	
	public ImageDaoImpl() {
		this.imageViews = new ArrayList<>();
	}
	
	public ImageDaoImpl(List<ImageView> imageViews) {
		this.imageViews = imageViews;
	}
	
	
	public List<ImageView> getImageViews() {
		return this.imageViews;
	}
	
	
	@Override
	// I'll switch over to Amazon S3 to store the images in buckets, which should improve the image resolution
	public ImageView addImage(Connection con, File file) throws SQLException, IOException {
    	Image image = new Image(file.toURI().toString(), 1000, 1000, true, true);
		String encodedBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(file.toURI())));
		
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO image_gallery VALUES(? , ?)");
		pstmt.setNString(1, encodedBase64);
		pstmt.setInt(2, AppSession.getStudentId());
		
		pstmt.execute();
		ImageView iv = new ImageView(image);
		this.imageViews.add(iv);
		
		pstmt.close();
		return iv;
	}
	
	
	@Override
	public void loadImages(Connection con) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("SELECT encoded_base_64 FROM image_gallery WHERE student_id = ?");
		pstmt.setInt(1, AppSession.getStudentId());
		ResultSet resultSet = pstmt.executeQuery();
		
		this.imageViews.clear();
		while (resultSet.next()) {
			byte[] imgData = Base64.getDecoder().decode(resultSet.getNString(1));
			this.imageViews.add(new ImageView(new Image(new ByteArrayInputStream(imgData), 100, 100, true, true)));
		}
		
		resultSet.close();
		pstmt.close();
	}
}
