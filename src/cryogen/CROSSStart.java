package cryogen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.*;
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
	private SharedMemoryRepository memory;
	private Stage currentStage;
	//GUI Variables
	@FXML private TextField txtProjectCount;

	/**
	 * Default Constructor
	 */
	public CROSSStart()
	{
		setMemory(new SharedMemoryRepository());
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
		setCurrentStage(currentStage);
		setMemory(new SharedMemoryRepository(getCurrentStage()));
		getCurrentStage().setOnCloseRequest(getMemory().confirmCloseEventHandler);//Set default close event
		//txtProjectCount.requestFocus();
		//mnuLaF_BreathDark_Clicked(new ActionEvent());

	}

	public void setCurrentStage(Stage stage)
	{
		this.currentStage = stage;
	}

	public Stage getCurrentStage()
	{
		return this.currentStage;
	}

	public void setMemory(SharedMemoryRepository memory)
	{
		this.memory = memory;
	}

	public SharedMemoryRepository getMemory()
	{
		return this.memory;
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
			cross_window.setTitle("CROSS 1.0 - Criterions");
			cross_window.setScene(new Scene((Pane) loader.load()));
			SharedMemoryRepository.setProject_count(Integer.parseInt(txtProjectCount.getText()));
			SharedMemoryRepository.init_project_names();
//			cross_window.getScene().getStylesheets().add(getClass().getResource(SharedMemoryRepository.getLaF()).toExternalForm());
			Criterions criterions = new Criterions();
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
			getMemory().handleException(nfex, "Number Format Exception", "The amount of projects is unacceptable", "Please enter a valid number of projects to evaluate.");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			getMemory().handleException(ex);
		}
	}

	@FXML
	protected void mnuFile_Preferences_Clicked(ActionEvent event)
	{
		try
		{
			Stage preferencesWindow = new Stage(StageStyle.DECORATED);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Preferences.fxml"));
			preferencesWindow.setResizable(false);
			preferencesWindow.setTitle("CROSS Preferences");
			preferencesWindow.setScene(new javafx.scene.Scene((javafx.scene.layout.Pane) loader.load(), javafx.scene.paint.Color.TRANSPARENT));
//			preferencesWindow.getScene().getStylesheets().add(getClass().getResource(SharedMemoryRepository.getLaF()).toExternalForm());
			Preferences preferences = loader.<Preferences>getController();
			preferences.initialize(getCurrentStage());
			preferencesWindow.showAndWait();
		}
		catch (IOException ex)
		{
			memory.handleException(ex);
		}
		catch (Exception ex)
		{
			memory.handleException(ex);
		}
	}

	/**
	 * Event handler to view user manual
	 * @param event
	 */
	@FXML
	protected void mnuHelp_UserManual_Clicked(ActionEvent event)
	{
		SharedMemoryRepository.showManual();
	}

	/**
	 * Event handler to view user manual
	 * @param event
	 */
	@FXML
	protected void mnuHelp_License_Clicked(ActionEvent event)
	{
		SharedMemoryRepository.showLicense();
	}


	@FXML
	protected void mnuFile_Exit_Clicked(ActionEvent event)
	{
		getMemory().exit();
	}
}

