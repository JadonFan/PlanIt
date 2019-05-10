/**
 * 
 */
/**
 * @author jadon
 *
 */
module planner {
	requires java.desktop;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires transitive java.sql;
	requires javafx.base;
	requires jdk.compiler;
	requires javafx.fxml;
	requires java.base;
	requires jbcrypt;
	requires com.jfoenix;
	exports home;
	opens home to javafx.graphics;
	exports course;
	opens course to javafx.fxml;
}