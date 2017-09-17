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

import java.awt.font.ShapeGraphicAttribute;
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
	private Stage currentStage;
	private SharedMemoryRepository memory;
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
	void initialize(Stage currentStage)
	{
		setCurrentStage(currentStage);
		setMemory(new SharedMemoryRepository(getCurrentStage()));
		getCurrentStage().setOnCloseRequest(getMemory().confirmCloseEventHandler);//Set default close event
		//txtProjectCount.requestFocus();
		//mnuLaF_BreathDark_Clicked(new ActionEvent());
	}

	private void setCurrentStage(Stage stage)
	{
		this.currentStage = stage;
	}

	private Stage getCurrentStage()
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
			//TODO: Replace hardcode with dynamically scalable criterions
			int cN, cV;
			cN = cV = 0;

			//Traverse through all components inside/on anchorPane
			for (Node node : anchorPane.getChildren())
			{
				//System.out.println("Id: " + node.getId());
				if (node instanceof TextField && !((TextField)node).getText().equals(""))
					SharedMemoryRepository.setCriterion_names(cN++, ((TextField)node).getText());
				else if (node instanceof Slider)
				{
					SharedMemoryRepository.setCriterion_values(cV++, ((int) ((Slider) node).getValue()));
				}
			}
			SharedMemoryRepository.setCriterion_count(cN);
			SharedMemoryRepository.init_project_criterion_values();

			FXMLLoader loader       = new FXMLLoader(getClass().getResource("CriterionIterator.fxml"));
			Stage      cross_window = new Stage(StageStyle.DECORATED);//
			cross_window.setResizable(false);
			cross_window.setTitle("CROSS 1.0 - " + SharedMemoryRepository.getCriterion_names()[SharedMemoryRepository.getCriterion_iterator()]);
			cross_window.setScene(new Scene((Pane) loader.load()));
			//CriterionIterator criterions = new CriterionIterator(laf, 0, projectCount, criterionCount, criterionNames, criterionValues);

			CriterionIterator criterions = loader.<CriterionIterator>getController();
			criterions.initialize(cross_window);

			cross_window.show();
			((Node) (event.getSource())).getScene().getWindow().hide();//Hide Previous Window
		}
		catch (NumberFormatException nfex)
		{
			nfex.printStackTrace();
			getMemory().handleException(nfex);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			getMemory().handleException(ex);
		}
	}
}

