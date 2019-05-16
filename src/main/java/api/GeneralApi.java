package api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import course.Course;
import course.CourseDaoImpl;
import user.AppSession;

@RestController
@Controller
public class GeneralApi {
	// TODO add some Spring dependency injections and implement better API security, 
	// such as authorization tokens, in future...
	// For now, I'm just implementing the API features to ensure functionality is OK...
	// With that being said, the APIs are definitely not production ready right now
	@Autowired
	Course course;
	
	
	@RequestMapping(method=RequestMethod.GET, value="/courses/{studentId}")
	public List<String> getCourses(@NonNull @PathVariable int studentId) throws SQLException {
		AppSession.setStudentId(studentId);
		List<Course> courses = new ArrayList<>();
		new CourseDaoImpl(courses).loadCourses(ApiDatabase.getConnection());
		
		return courses.stream().map(x -> x.getTitle()).collect(Collectors.toList());
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/courses/{studentId}")
	public Course addCourse(@NonNull @PathVariable int studentId, @RequestBody Course course) throws SQLException {
		AppSession.setStudentId(studentId);
		new CourseDaoImpl().addCourse(ApiDatabase.getConnection(), course);
		
		return course;
	}
}
