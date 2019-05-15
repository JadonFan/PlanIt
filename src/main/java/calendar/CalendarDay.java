package calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.DropDownButton;

public class CalendarDay {
	private final Calendar jCal; // the Calendar class in the java.util package
	private Color boxColor;
	private List<CalendarEvent> events;
	private boolean isImportantDay;
	private boolean isSelected;
	
	private int dayDisplayMode = Calendar.LONG;

	
	public CalendarDay(Calendar jCal) {
		this.jCal = jCal;
		this.boxColor = Color.WHITE;
		this.events = new ArrayList<>();

	}
	
	public CalendarDay(Calendar jCal, Color boxColor) {
		this.jCal = jCal;
		this.boxColor = boxColor;
		this.events = new ArrayList<>();
	}
	
	
	public Calendar getJCal() {
		return this.jCal;
	}
	
	public boolean getIsImportantDay() {
		return this.isImportantDay;
	}
	
	public void setImportantDay(boolean isImportantDay) {
		this.isImportantDay = isImportantDay;
	}

	public boolean getIsSelected() {
		return this.isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public List<CalendarEvent> getEvents() {
		return this.events;
	}

	
	public String reprDay() {
		Locale locale = Locale.getDefault();
		String dayOfWeek = "";
		if (this.dayDisplayMode != -99) {
			dayOfWeek = this.jCal.getDisplayName(Calendar.DAY_OF_WEEK, this.dayDisplayMode, locale) + " ";
		}
		
		return String.format("%s%s %s, %s", dayOfWeek, 
				this.jCal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale), this.jCal.get(Calendar.DAY_OF_MONTH),
				this.jCal.get(Calendar.YEAR));
	}


	public void showDayInfo(Stage window, CalendarMonth calMonth) {				
		VBox dayInfoLayoutBox = new VBox();
		dayInfoLayoutBox.setPadding(new Insets(15));
		dayInfoLayoutBox.setSpacing(15);
		
		VBox filtersBox = new VBox();
		// filtersBox.setPadding(new Insets(15));
		filtersBox.setSpacing(5);
		CheckBox lectureFilter = new CheckBox("Lecture");
		CheckBox labFilter = new CheckBox("Lab");
		CheckBox astmtFilter = new CheckBox("Assessment");
		CheckBox socialFilter = new CheckBox("Social Event");
		filtersBox.getChildren().addAll(lectureFilter, labFilter, astmtFilter, socialFilter);
		
		Text dayTitleText = new Text(this.reprDay());
		dayTitleText.setFont(Font.font(35));

		Pane spacer = new Pane();
		spacer.setMinSize(50, 1);
		final int prefBtnWidth = 130;

		// TODO add icons to the buttons
		HBox btnTopBarBox = new HBox();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		btnTopBarBox.setSpacing(5);

		Button addEventBtn = new Button("Add Event");
		addEventBtn.setPrefWidth(prefBtnWidth);
		btnTopBarBox.getChildren().add(addEventBtn);
		
		DropDownButton showFiltersBtn = new DropDownButton("Show Filters", filtersBox, btnTopBarBox, dayInfoLayoutBox);
		showFiltersBtn.setPrefWidth(prefBtnWidth);
		btnTopBarBox.getChildren().add(showFiltersBtn);
		
		Button useOtherFormBtn = new Button("Use Short Day");
		useOtherFormBtn.setPrefWidth(prefBtnWidth);
		useOtherFormBtn.setOnAction(event -> {
			switch (this.dayDisplayMode) {
				case Calendar.LONG:
					this.dayDisplayMode = Calendar.SHORT;
					useOtherFormBtn.setText("Show No Day");
					break;
				case Calendar.SHORT:
					this.dayDisplayMode = -99;
					useOtherFormBtn.setText("Show Long Day");
					break;
				case -99:
					this.dayDisplayMode = Calendar.LONG;
					useOtherFormBtn.setText("Show Short Day");
					break;
				default:
					// do nothing
			}
			dayTitleText.setText(this.reprDay());
		});
		btnTopBarBox.getChildren().add(useOtherFormBtn);
		
		Button goBackToCalBtn = new Button("Back to Calendar");
		goBackToCalBtn.setPrefWidth(prefBtnWidth);
		goBackToCalBtn.setOnAction(event -> new PlannerCalendar().display(window, calMonth));
		btnTopBarBox.getChildren().addAll(spacer, goBackToCalBtn);
		
		dayInfoLayoutBox.getChildren().addAll(btnTopBarBox, dayTitleText);
		
		for (CalendarEvent event : this.events) {
			VBox vb = new VBox();
			Text text = new Text(event.toString());
			vb.getChildren().add(text);
			
			StackPane coursePane = new StackPane();
			Rectangle rect = new Rectangle();
			rect.setWidth(window.getWidth() - 200);
			coursePane.getChildren().addAll(rect, vb);
			// TODO change to be more colourful
			coursePane.setStyle("-fx-border-color: black; \n" +
								"-fx-border-insets: 5; \n" +
								"-fx-border-width: 2; \n"); //$NON-NLS-1$	
			dayInfoLayoutBox.getChildren().add(coursePane);
		}
		
		Scene scene = new Scene(dayInfoLayoutBox);
		window.setScene(scene);
		window.show();
	}
	
	
	public StackPane buildCalendarDayPane(Stage window, CalendarMonth calMonth) {
		StackPane dayPane = new StackPane();
		
		Label dateLabel = new Label(Integer.toString(this.jCal.get(Calendar.DAY_OF_MONTH)));

		Rectangle dayRect = new Rectangle(100, 75);
		dayRect.setFill(this.boxColor);
		dayPane.getChildren().addAll(dayRect, dateLabel);
		dayPane.setOnMouseClicked(event -> this.showDayInfo(window, calMonth));
		// TODO get borders to the day panes
		// dayPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		return dayPane;
	}
}
