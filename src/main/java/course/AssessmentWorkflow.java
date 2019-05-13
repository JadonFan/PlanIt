package course;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import application.PlannerDb;
import common.Graph;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import ui.CommonTooltipFactory;
import ui.CoordinatesManager;

public class AssessmentWorkflow extends Graph<Assessment> {
	private MyCourses myCourses;
	private Map<Vertex<Assessment>, StackPane> graphPaneMap;
	
	
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
	
	public Map<Vertex<Assessment>, StackPane> getGraphPaneMap() {
		return this.graphPaneMap;
	}

	public void setGraphPaneMap(Map<Vertex<Assessment>, StackPane> graphPaneMap) {
		this.graphPaneMap = graphPaneMap;
	}


	public void display(Stage window) {		
		window.setTitle("Assessment Workflow");
		 
		VBox vb = new VBox();
		vb.getChildren().add(this.buildTopBtnBox(window));
		ScrollPane layout = new ScrollPane(vb);
		layout.setFitToWidth(true);
		layout.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		layout.setHbarPolicy(ScrollBarPolicy.NEVER);
		layout.setStyle("-fx-background-color:transparent;");
		
		GridPane graphSheet = new GridPane();
		vb.getChildren().add(graphSheet);
		// CoordinatesManager.forceColsAndRows(graphSheet, 50, 50);
		CoordinatesManager coordsManager = new CoordinatesManager(graphSheet);
		
		try {
			new AstmtWorkflowDaoImpl().loadWorkflowGraph(PlannerDb.getConnection(), this);
			
			// Set the vertices of the assessment workflow graph
			for (Vertex<Assessment> vertex : super.getAdjacencyList()) {
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
			for (Vertex<Assessment> mainVertex : super.getAdjacencyList()) {				
				Bounds mainVertexBounds = this.graphPaneMap.get(mainVertex).getBoundsInParent();
	
				for (Vertex<Assessment> adjVertex : mainVertex.getAdjacentVertices().keySet()) {
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
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.show();
	}
	
	
	private HBox buildTopBtnBox(Stage window) {
		HBox btnTopBarBox = new HBox();
		btnTopBarBox.setSpacing(10);
		btnTopBarBox.setPadding(new Insets(10));
		btnTopBarBox.setMinWidth(window.getWidth());
		// btnTopBarBox.setMaxWidth(this.window.getWidth());
		btnTopBarBox.setStyle("-fx-background-color:#90EE90;");
		
		// "Add workflow assessment" button
		ImageView plusImgView = new ImageView(new Image(getClass().getResourceAsStream("/add.png"))); //$NON-NLS-1$
		plusImgView.setFitHeight(20);
		plusImgView.setFitWidth(20);
		
		Button addAstmtBtn = new Button("Add"); 
		addAstmtBtn.setGraphic(plusImgView);
		addAstmtBtn.setMinWidth(100);
		addAstmtBtn.setMaxWidth(100);
		addAstmtBtn.setTooltip(CommonTooltipFactory.buildRectToolTip("Add an assessment to the workflow"));
		// addAstmtBtn.setOnAction(event -> courseForm.display(this));
		btnTopBarBox.getChildren().add(addAstmtBtn);
		
		// "Remove workflow assessment" button
		ImageView minusImgView = new ImageView(new Image(getClass().getResourceAsStream("/minus.png"))); //$NON-NLS-1$
		minusImgView.setFitHeight(20);
		minusImgView.setFitWidth(20);
		
		Button removeAstmtBtn = new Button("Remove"); 
		removeAstmtBtn.setGraphic(minusImgView);
		removeAstmtBtn.setMinWidth(100);
		removeAstmtBtn.setMaxWidth(100);
		removeAstmtBtn.setTooltip(CommonTooltipFactory.buildRectToolTip("Remove an assessment from the workflow"));
		// removeAstmtBtn.setOnAction(event -> courseForm.display(this));
		btnTopBarBox.getChildren().add(removeAstmtBtn);
	
		Pane spacer = new Pane();
		spacer.setPrefSize(50, 1);
		btnTopBarBox.getChildren().add(spacer);
		
		// "Show my courses" button
		ImageView coursesImgView = new ImageView(new Image(getClass().getResourceAsStream("/books.png"))); //$NON-NLS-1$
		coursesImgView.setFitHeight(20);
		coursesImgView.setFitWidth(20);
		
		Button coursesBtn = new Button("Courses"); 
		coursesBtn.setGraphic(coursesImgView);
		coursesBtn.setMinWidth(100);
		coursesBtn.setMaxWidth(100);
		coursesBtn.setTooltip(CommonTooltipFactory.buildRectToolTip("Show my current courses"));
		coursesBtn.setOnAction(event -> MyCourses.getInstance().display(true));		
		btnTopBarBox.getChildren().add(coursesBtn);
		
		return btnTopBarBox;
	}
}
