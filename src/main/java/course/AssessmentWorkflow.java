package course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.CoordinatesManager;
import common.Graph;
import application.PlannerDb;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import user.Session;

public class AssessmentWorkflow extends Graph<Course> {
	private MyCourses myCourses;
	private Map<Vertex<Course>, StackPane> graphPaneMap;
	
	
	public AssessmentWorkflow(MyCourses myCourses) {
		this.myCourses = myCourses;
		this.graphPaneMap = new HashMap<>();
	}
	
	
	public MyCourses getMyCourses() {
		return this.myCourses;
	}
	
	public void setMyCourses(MyCourses myCourses) {
		this.myCourses = myCourses;
	}
	
	public Map<Vertex<Course>, StackPane> getGraphPaneMap() {
		return this.graphPaneMap;
	}

	public void setGraphPaneMap(Map<Vertex<Course>, StackPane> graphPaneMap) {
		this.graphPaneMap = graphPaneMap;
	}


	public void display(Stage window) {		
		window.setTitle("Assessment Workflow");
		GridPane graphSheet = new GridPane();
		// CoordinatesManager.forceColsAndRows(graphSheet, 50, 50);
		CoordinatesManager coordsManager = new CoordinatesManager(graphSheet);
		
		try {
			this.loadWorkflowGraph(PlannerDb.getConnection());
			
			// Set the vertices of the assessment workflow graph
			for (Vertex<Course> vertex : super.getAdjacencyList()) {
				StackPane stackPane = new StackPane();
				
				Text text = new Text(vertex.getElement().toString());
				Circle node = new Circle();
				
				stackPane.getChildren().addAll(node, text);
				
				Pair<Number, Number> rCoords = coordsManager.getRandomUniqueCoordinates();
				Pair<Integer, Integer> graphCoords = new Pair<>(rCoords.getKey().intValue(), rCoords.getValue().intValue());
				// GridPane.setConstraints(stackPane, graphCoords.getKey(), graphCoords.getValue());
				graphSheet.add(stackPane, graphCoords.getKey(), graphCoords.getValue());
				
				this.graphPaneMap.put(vertex, stackPane);
			}
			
			
			// Set the edges of the assessment workflow graph
			for (Vertex<Course> mainVertex : super.getAdjacencyList()) {				
				Bounds mainVertexBounds = this.graphPaneMap.get(mainVertex).getBoundsInParent();
	
				for (Vertex<Course> adjVertex : mainVertex.getAdjacentVertices().keySet()) {
					Line edgeLine = new Line();
					Bounds adjVertexBounds = this.graphPaneMap.get(adjVertex).getBoundsInParent();
					
					edgeLine.setStartX(mainVertexBounds.getMinX() + mainVertexBounds.getWidth() / 2);
					edgeLine.setStartY(mainVertexBounds.getMinY() + mainVertexBounds.getHeight() / 2);
					edgeLine.setEndX(adjVertexBounds.getMinX() + adjVertexBounds.getWidth() / 2);
					edgeLine.setEndY(adjVertexBounds.getMinX() + adjVertexBounds.getHeight() / 2);
					
					graphSheet.getChildren().add(edgeLine);
				}				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Scene scene = new Scene(graphSheet);
		window.setScene(scene);
		window.show();
	}
	
	
 	public void addVertex(Connection con, Vertex<Course> vertex, Map<Vertex<Course>,Double> adjacentVertices) throws SQLException {
		super.addVertex(vertex, false);
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO astmt_workflow(course_number, adjacent_vertices,"
				+ "distances, student_id) VALUES(? , ? , ? , ?)");
		
		// Course number
		pstmt.setInt(1, vertex.getElement().getCrsNo());
		
		// Adjacent course vertices
		String adjacencies = "";
		for (Vertex<Course> currentVertex : adjacentVertices.keySet()) {
			adjacencies += Integer.toString(currentVertex.getId()).concat(",");
		}
		pstmt.setNString(2, adjacencies.substring(0, adjacencies.length() - 1));
		
		// Adjacent distances
		String distances = "";
		for (Double currentDistance : adjacentVertices.values()) {
			distances += Double.toString(currentDistance).concat(",");
		}
		pstmt.setNString(3, distances.substring(0, distances.length() - 1));
		
		// Associated student ID
		pstmt.setInt(4, Session.getStudentId());

		pstmt.executeUpdate();
		pstmt.close();
	}
	
	
	public void loadWorkflowGraph(Connection con) throws SQLException {
		PreparedStatement pstmt = con.prepareStatement("SELECT * FROM astmt_workflow WHERE student_id = ? AND is_active = 'Y'");
		pstmt.setInt(1, Session.getStudentId());
		
		ResultSet resultSet = pstmt.executeQuery();
		// A sad O(n^3) algorithm...
		// FIXME use a SELECT FROM INNER JOIN ON query to improve time efficiency 
		while (resultSet.next()) {
			int vertexId = resultSet.getInt(1);
			Course course = this.myCourses.getCourseById(resultSet.getInt(2));
			
			List<Vertex<Course>> connectingCourses = new ArrayList<>();
			for (String adjVertexStrId : resultSet.getNString(3).split(",")) {
				connectingCourses.add(super.getVertexById(Integer.parseInt(adjVertexStrId)));
			}
			
			List<Double> connectingDistances = new ArrayList<>();
			for (String adjDistance : resultSet.getNString(4).split(",")) {
				connectingDistances.add(Double.parseDouble(adjDistance));
			}
			
			Map<Vertex<Course>, Double> adjacentCourses = new HashMap<>();
			for (int i = 0; i < connectingCourses.size(); i++) {
				adjacentCourses.put(connectingCourses.get(i), connectingDistances.get(i));
			}
			
			Vertex<Course> vertex = new Vertex<Course>(vertexId, course, false, adjacentCourses);
			this.addVertex(vertex, false);
		}
		
		resultSet.close();
		pstmt.close();
	}
}
