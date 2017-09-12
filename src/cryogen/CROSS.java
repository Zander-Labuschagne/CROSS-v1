package cryogen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

public class CROSS implements Initializable
{
	//Instance Variables
	public static String laf;
	private Stage currentStage;
	private boolean exiting;
	private int projectCount;
	//GUI Variables
	@FXML private Button btnContinue;
	@FXML private TextField txtProjectCount;


	/**
	 * Default Constructor
	 */
	public CROSS()
	{
		this.laf = "Midna.css";
		this.exiting = false;
	}

	/**
	 * Overloaded Constructor
	 */
	public CROSS(int projectCount)
	{
		this.laf = "Midna.css";
		this.exiting = false;
		this.projectCount = projectCount;
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
	 * Move on to main window
	 */
	@FXML
	protected void btnContinue_Clicked(ActionEvent event)
	{

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
}

