package cryogen;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class SharedMemoryRepository
{
	private static String laf;
	private static boolean exiting_status;
	private static Stage stage;

	private static int project_count;
	private static String[] criterion_names = new String[9];
	private static int[] criterion_values = new int[9];
	private static int criterion_count;
	private static int criterion_iterator = 0;
	private static int project_criterion_values[][];
	private static String[] project_names;

	public static void setLaF(String LaF)
	{
		laf = LaF;
	}

	public static String getLaF()
	{
		return laf;
	}

	public static void setExiting_status(boolean exitingStatus)
	{
		exiting_status = exitingStatus;
	}

	public static boolean getExiting_status()
	{
		return exiting_status;
	}

	public static void setProject_count(int projectCount)
	{
		project_count = projectCount;
	}

	public static int getProject_count()
	{
		return project_count;
	}

	public static void setCriterion_names(int index, String name)
	{
		criterion_names[index] = name;
	}

	public static String[] getCriterion_names()
	{
		return criterion_names;
	}

	public static void setCriterion_values(int index, int value)
	{
		criterion_values[index] = value;
	}

	public static int[] getCriterion_values()
	{
		return criterion_values;
	}

	public static void setCriterion_count(int criterionCount)
	{
		criterion_count = criterionCount;
	}

	public static int getCriterion_count()
	{
		return criterion_count;
	}

	public static void increase_criterion_iterator()
	{
		criterion_iterator++;
	}

	public static int getCriterion_iterator()
	{
		return criterion_iterator;
	}

	public static void init_project_names()
	{
		project_names = new String[getProject_count()];
	}

	public static void setProject_names(int index, String name)
	{
		project_names[index] = name;
	}
	public static String[] getProject_names()
	{
		return project_names;
	}

	public static void init_project_criterion_values()
	{
		project_criterion_values = new int[getCriterion_count()][getProject_count()];
	}

	public static void setProject_criterion_values(int criterion_index, int project_index, int value)
	{
		project_criterion_values[criterion_index][project_index] = value;
	}

	public static int[][] getProject_criterion_values()
	{
		return project_criterion_values;
	}

	public static void setStage(Stage currentStage)
	{
		stage = currentStage;
	}

	public static Stage getStage()
	{
		return stage;
	}



	/**
	 * TODO:
	 */

	public SharedMemoryRepository()
	{
		setCurrentStage(null);
	}

	private Stage currentStage;

	public SharedMemoryRepository(Stage currentStage)
	{
		this();
		setCurrentStage(currentStage);
	}

	public void setCurrentStage(Stage currentStage)
	{
		this.currentStage = currentStage;
	}

	public Stage getCurrentStage()
	{
		return this.currentStage;
	}

	/**
	 * method to handle exceptions
	 * @param ex Exception thrown to handle
	 */
	public void handleException(Exception ex)
	{
		handleException(ex, "Error");
	}

	/**
	 * method to handle exceptions with optional window title
	 * @param ex Exception thrown to handle
	 * @param title to be displayed in message box
	 */
	public void handleException(Exception ex, String title)
	{
		handleException(ex, title, ex.getMessage());
	}

	/**
	 * method to handle exceptions with optional window title and header
	 * @param ex Exception thrown to handle
	 * @param title to be displayed in message box
	 * @param header caption to be displayed in message box
	 */
	public void handleException(Exception ex, String title, String header)
	{
		handleException(ex, title, header, ex.toString());
	}

	/**
	 * method to handle exceptions with optional window title, header and message text
	 * @param ex Exception thrown to handle
	 * @param title to be displayed in message box
	 * @param header caption to be displayed in message box
	 * @param content message for message box to contain
	 */
	public void handleException(Exception ex, String title, String header, String content)
	{
		Alert error = new Alert(Alert.AlertType.ERROR);
		error.initModality(Modality.APPLICATION_MODAL);
		error.initOwner(getCurrentStage());
		error.setContentText(content);
		error.setTitle(title);
		error.setHeaderText(header);

// Create expandable Exception.
		StringWriter sw            = new StringWriter();
		PrintWriter  pw            = new PrintWriter(sw);;
		String       exceptionText;
		ex.printStackTrace(pw);
		exceptionText = sw.toString();
		Label        label         = new Label("The exception stacktrace was:");
		TextArea     textArea      = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
// Set expandable Exception into the dialog pane.
		DialogPane dialogPane = error.getDialogPane();
//		dialogPane.getStylesheets().add(getClass().getResource(getLaF()).toExternalForm());
		dialogPane.getStyleClass().add("dlgDefault");
		dialogPane.setExpandableContent(expContent);
		dialogPane.setPrefWidth(640);
		dialogPane.setPrefHeight(240);
		error.showAndWait();
	}

	/**
	 * Method to prompt before exit
	 * Exits application with 0 error code if user prompt is confirmed else application continues
	 */
	public EventHandler<WindowEvent> confirmCloseEventHandler = event ->
	{
		Alert  closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
		Button exitButton        = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
		exitButton.setText("Exit");
		closeConfirmation.setHeaderText("Confirm Exit");
		closeConfirmation.initModality(Modality.APPLICATION_MODAL);
		closeConfirmation.initOwner(getCurrentStage());
		setExiting_status(true);
		DialogPane dialogPane = closeConfirmation.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource(getLaF()).toExternalForm());
		dialogPane.getStyleClass().add("dlgDefault");
		Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
		if (!ButtonType.OK.equals(closeResponse.get()))
		{
			setExiting_status(false);
			event.consume();
		}
	};
}
