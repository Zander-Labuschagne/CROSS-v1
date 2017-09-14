package cryogen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * Java class handler for the CROSS application initial GUI
 * Copyright (C) 2017  Zander Labuschagne
 * This program is free software; you can redistribute it and/or modify it under the terms of the Apache Version 2.0 License
 */

public class Criterions implements Initializable
{
	//Instance Variables
	public static String laf;
	private Stage currentStage;
	private boolean exiting;
	private int projectCount;
	private int pC;
	//GUI Variables
	@FXML private AnchorPane anchorPane;
	@FXML private Button btnContinue;
	@FXML private TextField txtCriterion1;
	@FXML private TextField txtCriterion2;
	@FXML private TextField txtCriterion3;
	@FXML private TextField txtCriterion4;
	@FXML private TextField txtCriterion5;
	@FXML private TextField txtCriterion6;
	@FXML private TextField txtCriterion7;
	@FXML private TextField txtCriterion8;
	@FXML private TextField txtCriterion9;
	@FXML private Slider sldCriterion1;
	@FXML private Slider sldCriterion2;
	@FXML private Slider sldCriterion3;
	@FXML private Slider sldCriterion4;
	@FXML private Slider sldCriterion5;
	@FXML private Slider sldCriterion6;
	@FXML private Slider sldCriterion7;
	@FXML private Slider sldCriterion8;
	@FXML private Slider sldCriterion9;



	/**
	 * Default Constructor
	 */
	public Criterions()
	{
		this.exiting = false;
	}

	/**
	 * Overloaded Constructor
	 */
	Criterions(int projectCount, String p_laf)
	{
		this();
		laf = p_laf;
		setProjectCount(CROSSStart.projectCount);
		System.out.println(this.projectCount);

	}

	/**
	 * Initialize method for GUI
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{

	}

	/**
	 * Initialize method for GUI
	 * @param currentStage
	 */
	void initialize(Stage currentStage)
	{
		this.currentStage = currentStage;
		getCurrentStage().setOnCloseRequest(confirmCloseEventHandler);//Set default close event
		//txtProjectCount.requestFocus();
		//mnuLaF_BreathDark_Clicked(new ActionEvent());

	}

	private Stage getCurrentStage()
	{
		return this.currentStage;
	}

	/**
	 * Move on to criterions window
	 */
	@FXML
	protected void btnContinue_Clicked(ActionEvent event)
	{
		try
		{
			//TODO: Replace hardcode with dynamically scalable criterions
			String criterionNames[] = new String[9];
			int criterionValues[] = new int[9];//Values ranges from 0 to 100
			int cN, cV;
			cN = cV = 0;

			//Traverse through all components inside/on anchorPane
			for (Node node : anchorPane.getChildren())
			{
				//System.out.println("Id: " + node.getId());
				if (node instanceof TextField && !((TextField)node).getText().equals(""))
					criterionNames[cN++] = ((TextField)node).getText();
				else if (node instanceof Slider)
					criterionValues[cV++] = ((int) ((Slider) node).getValue());
			}
			int criterionCount = cN;

			FXMLLoader loader       = new FXMLLoader(getClass().getResource("CriterionIterator.fxml"));
			Stage      cross_window = new Stage(StageStyle.DECORATED);//
			cross_window.setResizable(false);
			cross_window.setTitle("CROSS 1.0");
			cross_window.setScene(new Scene((Pane) loader.load()));
			//CriterionIterator criterions = new CriterionIterator(laf, 0, projectCount, criterionCount, criterionNames, criterionValues);

			CriterionIterator criterions = loader.<CriterionIterator>getController();
			criterions.initialize(cross_window, laf, 0, CROSSStart.projectCount, criterionCount, criterionNames, criterionValues);

			cross_window.show();
			((Node) (event.getSource())).getScene().getWindow().hide();//Hide Previous Window
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			handleException(ex);
		}
	}

	public void setProjectCount(int projectCount)
	{
		this.projectCount = projectCount;
	}

	public int getProjectCount()
	{
		return this.projectCount;
	}


	/**
	 * Method to prompt before exit
	 * Exits application with 0 error code if user prompt is confirmed else application continues
	 */
	private EventHandler<WindowEvent> confirmCloseEventHandler = event ->
	{
		Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
		Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
		exitButton.setText("Exit");
		closeConfirmation.setHeaderText("Confirm Exit");
		closeConfirmation.initModality(Modality.APPLICATION_MODAL);
		closeConfirmation.initOwner(getCurrentStage());
		exiting = true;
		DialogPane dialogPane = closeConfirmation.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource(laf).toExternalForm());
		dialogPane.getStyleClass().add("dlgDefault");
		Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
		if (!ButtonType.OK.equals(closeResponse.get()))
		{
			exiting = false;
			event.consume();
		}
	};

	/**
	 * method to handle exceptions
	 * @param ex Exception thrown to handle
	 */
	protected void handleException(Exception ex)
	{
		handleException(ex, "Error");
	}

	/**
	 * method to handle exceptions with optional window title
	 * @param ex Exception thrown to handle
	 * @param title to be displayed in message box
	 */
	protected void handleException(Exception ex, String title)
	{
		handleException(ex, title, ex.getMessage());
	}

	/**
	 * method to handle exceptions with optional window title and header
	 * @param ex Exception thrown to handle
	 * @param title to be displayed in message box
	 * @param header caption to be displayed in message box
	 */
	protected void handleException(Exception ex, String title, String header)
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
	protected void handleException(Exception ex, String title, String header, String content)
	{
		if(ex != null)
			ex.printStackTrace();
		Alert error = new Alert(Alert.AlertType.ERROR, content);
		error.initModality(Modality.APPLICATION_MODAL);
		error.initOwner(getCurrentStage());
		error.setTitle(title);
		error.setHeaderText(header);
		DialogPane dialogPane = error.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource(laf).toExternalForm());
		dialogPane.getStyleClass().add("dlgDefault");
		error.showAndWait();
	}
}

