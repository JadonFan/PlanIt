package course;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import abstractdao.AstmtWorkflowDao;
import common.Graph.Vertex;
import user.Session;

public class AstmtWorkflowDaoImpl implements AstmtWorkflowDao {
 	public void addVertex(Connection con, AssessmentWorkflow astmtWf, Vertex<Assessment> vertex,  
 			Map<Vertex<Assessment>,Double> adjacentVertices) throws SQLException {
 		astmtWf.addVertex(vertex, false);
		PreparedStatement pstmt = con.prepareStatement("INSERT INTO astmt_workflow(astmt_id, adjacent_vertices,"
				+ "distances, is_active, student_id) VALUES(? , ? , ? , 'Y' , ?)");
		
		// Course number
		pstmt.setInt(1, vertex.getElement().getId());
		
		// Adjacent course vertices
		String adjacencies = "";
		for (Vertex<Assessment> currentVertex : adjacentVertices.keySet()) {
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
 	
 	
 	// I couldn't find any method in Hibernate to call a stored procedure and get multiple out parameters
 	// so I'm resorting to JPA here. Even then, the JDBC approach is much more simple than the JPA, so I'd
 	// prefer using the loadWorkflowGraph(EntityManagerFactory, AssessmentWorkflow) method below instead. 
 	/*
 	public void loadWorkflowGraph(Connection con, AssessmentWorkflow astmtWf) throws SQLException {
 		StoredProcedureQuery query = entityManager
 				.createStoredProcedureQuery("getWfDetails")
 				.registerStoredProcedureParameter("student_id", Integer.class, ParameterMode.IN)
 				.registerStoredProcedureParameter("vertex_id", Integer.class, ParameterMode.OUT)
 				.registerStoredProcedureParameter("astmt_id", Integer.class, ParameterMode.OUT)
 				.registerStoredProcedureParameter("course_number", Integer.class, ParameterMode.OUT)
 				.registerStoredProcedureParameter("adjacent_vertices", String.class, ParameterMode.OUT)
 				.registerStoredProcedureParameter("distances", String.class, ParameterMode.OUT)
 				.setParameter("student_id", Session.getStudentId());
 				
 		try {
 			query.execute();
 			List<Object[]> astmtWfDetails = query.getResultList();
 			
 			// TODO get the out parameter values
 			for (Object[] astmtWfDetail : query.getResultList()) {
 				
 			}
 		} finally {
 			query.unwrap(ProceduresOutput.class).release();
 		}
 	}
 	*/
	
 	
 	public void loadWorkflowGraph(EntityManagerFactory emf, AssessmentWorkflow astmtWf, AstmtWorkflowDaoImpl awdi) throws SQLException {
 		EntityManager em = emf.createEntityManager();
 		em.getTransaction().begin();
 		org.hibernate.Session session = em.unwrap(org.hibernate.Session.class);
 		session.doWork(con -> awdi.loadWorkflowGraph(con, astmtWf));
 		session.close();
 	}
 	

	public void loadWorkflowGraph(Connection con, AssessmentWorkflow astmtWf) throws SQLException {
		/*
		 The procedure being called is defined as follows:
		 	DELIMITER \\
			DROP PROCEDURE IF EXISTS planner.getWfDetails \\
			CREATE PROCEDURE planner.getWfDetails (IN student_id INT, OUT vertex_id INT, OUT astmt_id INT, OUT course_number INT,
				OUT adjacent_vertices VARCHAR(100), OUT distances VARCHAR(100))
			BEGIN 
			SELECT 
				vertex_id,
			    astmt_id,
			    course_number,  
				GROUP_CONCAT(adjacent_vertices) AS adjacent_vertices, 
				GROUP_CONCAT(distances) AS distances
			INTO vertex_id, astmt_id, course_number, adjacent_vertices, distances
			FROM (
				SELECT 
					wf.vertex_id,
			        wf.astmt_id,
			        astmt.course_number,
					wf.adjacent_vertices,
			        wf.distances
			    FROM 
					astmt_workflow AS wf
						INNER JOIN assessments AS astmt 
							ON wf.astmt_id = astmt.astmt_id 
				WHERE wf.student_id = student_id AND wf.is_active = 'Y'
			) wf_group
			GROUP BY astmt_id 
			ORDER BY astmt_id DESC;
			END \\
			DELIMITER ;
		 */
		CallableStatement cstmt = con.prepareCall("{call getWfDetails(? , ? , ? , ? , ? , ?)}"); 
		cstmt.setInt(1, Session.getStudentId());
		cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
		cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
		cstmt.registerOutParameter(6, java.sql.Types.VARCHAR);
		
		cstmt.execute();
		ResultSet resultSet = cstmt.getResultSet();
		if (resultSet != null) {
			while (resultSet.next()) {
				Map<Integer, Double> adjacentCourses = new HashMap<>();
				int vertexId = resultSet.getInt(2);
				Assessment astmt = astmtWf.getMyCourses().getCourseById(resultSet.getInt(4)).getAstmtById(resultSet.getInt(3));
				
				String[] adjVerticesIds = resultSet.getNString(5).split(",");
				String[] distances = resultSet.getNString(6).split(",");
				for (int i = 0; i < adjVerticesIds.length; i++) {
					adjacentCourses.put(Integer.parseInt(adjVerticesIds[i].trim()), Double.parseDouble(distances[i].trim()));
				}
				
				Vertex<Assessment> vertex = new Vertex<Assessment>(vertexId, astmt, false, adjacentCourses);
				astmtWf.addVertex(vertex, false);
			}
			
			resultSet.close();
		}
		
		cstmt.close();
	}
}
