package cryogen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class CROSSStart implements Initializable
{
	//Instance Variables
	public static String laf;
	private Stage currentStage;
	private boolean exiting;
	//GUI Variables
	@FXML private Button btnContinue;
	@FXML private TextField txtProjectCount;


	/**
	 * Default Constructor
	 */
	public CROSSStart()
	{
		laf = "Midna.css";
		exiting = false;
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
	public void initialize(Stage currentStage)
	{
		this.currentStage = currentStage;
		getCurrentStage().setOnCloseRequest(confirmCloseEventHandler);//Set default close event
		//txtProjectCount.requestFocus();
		//mnuLaF_BreathDark_Clicked(new ActionEvent());

	}

	public Stage getCurrentStage()
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
			txtProjectCount.getStyleClass().remove("txtDefaultError");
			txtProjectCount.getStyleClass().add("txtDefault");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("Criterions.fxml"));
			Stage cross_window  = new Stage(StageStyle.DECORATED);//
			cross_window.setResizable(false);
			cross_window.setTitle("CROSS 1.0");
			cross_window.setScene(new Scene((Pane) loader.load()));
			Criterions criterions = new Criterions(Integer.parseInt(txtProjectCount.getText()));
			criterions.initialize(cross_window);
			cross_window.show();
			((Node) (event.getSource())).getScene().getWindow().hide();//Hide Previous Window
		}
		catch(NumberFormatException nfex)
		{
			txtProjectCount.getStyleClass().remove("txtDefault");
			txtProjectCount.getStyleClass().add("txtDefaultError");
			nfex.printStackTrace();
			txtProjectCount.requestFocus();
			handleException(nfex, "Number Format Exception", "The amount of projects is unacceptable", "Please enter a valid number of projects to evaluate.");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			handleException(ex);
		}
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

