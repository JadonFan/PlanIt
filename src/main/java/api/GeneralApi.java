package api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import course.Course;
import course.CourseDaoImpl;
import user.AppSession;

@RestController
public class GeneralApi {
	// TODO add some Spring dependency injections and implement better API security, 
	// such as authorization tokens, in future...
	// For now, I'm just implementing the API features to ensure functionality is OK...
	// With that being said, the APIs are definitely not production ready right now
	@RequestMapping("/courses/{studentId}")
	public List<String> getCourses(@PathVariable int studentId) throws SQLException {
		AppSession.setStudentId(studentId);
		List<Course> courses = new ArrayList<>();
		new CourseDaoImpl(courses).loadCourses(ApiDatabase.getConnection());
		
		return courses.stream().map(x -> x.getTitle()).collect(Collectors.toList());
	}
}
