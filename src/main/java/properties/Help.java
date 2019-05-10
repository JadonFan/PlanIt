package properties;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import ui.CommonButtonFactory;
import ui.TreeComponentsFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Help {
	private static volatile Help help;
	private TreeView<String> tree;
	
	
	private Help() { }
	
	public static Help getInstance() {
		if (help == null) {
			help = new Help();
		}
		
		return help;
	}
	
	
	private static String getHelpText(TreeItem<String> selectResult) {
		String helpTitle = selectResult.getValue();
		String helpText = "";
		int helpId = -1;
		
		for (HelpSection helpSection : HelpSection.values()) {
			if (helpTitle.equals(helpSection.getHelpTitle())) {
				helpId = helpSection.getHelpId();
				break;
			}
		}
		
		// TODO map to a specific resources directory that is packaged with the JAR file for this project, and allow the user
		// to re-route the path of that directory
		final String helpFilePath = Paths.get(System.getProperty("user.dir")).toString()
				.concat("/src/main/resources/helptext.txt"); //$NON-NLS-1$ //$NON-NLS-2$
		
		try (InputStream inStream = new FileInputStream(helpFilePath)) {
			BufferedReader buf = new BufferedReader(new InputStreamReader(inStream));
			StringBuilder sb = new StringBuilder();
			String line;

			while ((line = buf.readLine()) != null) {
				if (line.trim().equals("help " + helpId)) {
					line = buf.readLine();
					while (line != null && !Pattern.matches("help \\d+", line.trim())) {  //$NON-NLS-1$
						sb.append(line).append("\n");
						line = buf.readLine();
					} 
				}
			}
			
			helpText = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return helpText;
	}
	
	
	@SuppressWarnings("unused")
	public void display(Stage window) {
		TreeComponentsFactory<String> treeBuilder = new TreeComponentsFactory<String>();
		
		// Root
		TreeItem<String> treeRoot = new TreeItem<String>();
		treeRoot.setExpanded(true);
		
		// General
		TreeItem<String> generalBranch = treeBuilder.buildBranch("General", treeRoot);
		List<HelpSection> helpTitles = HelpSection.getAllHelpSectionsinCategory(HelpSection.HelpCategory.GENERAL);
		for (HelpSection helpTitle : helpTitles) {
			treeBuilder.buildBranch(helpTitle.getHelpTitle(), generalBranch);
		}
		
		// Connections
		TreeItem<String> connectionBranch = treeBuilder.buildBranch("Connection", treeRoot);
		
		// Help description
		Label descripLabel = new Label();
		descripLabel.setPadding(new Insets(20));
		
		// Set the tree view and its event listener
		this.tree = new TreeView<String>(treeRoot);
		this.tree.setShowRoot(false);
		this.tree.setMaxWidth(600);
		this.tree.getSelectionModel().selectedItemProperty()
			.addListener((results, oldResult, selectResult) -> {
				descripLabel.setText(Help.getHelpText(selectResult));
			});
		
		Button backButton = CommonButtonFactory.buildBackToHomeButton(window);
		
		BorderPane borderPane = new BorderPane();
		borderPane.setLeft(this.tree);
		borderPane.setCenter(descripLabel);
		borderPane.setBottom(backButton);
		
		BorderPane.setAlignment(backButton, Pos.CENTER);
		BorderPane.setAlignment(descripLabel, Pos.TOP_CENTER);
		
		Scene scene = new Scene(borderPane, 600, 600);
		window.setScene(scene);
		window.show();	
	}
}
