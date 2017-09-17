package cryogen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * Java class handler for the CROSS application initial GUI
 * Copyright (C) 2017  Zander Labuschagne
 * This program is free software; you can redistribute it and/or modify it under the terms of the Apache Version 2.0 License
 */

public class CriterionIterator implements Initializable
{
	//Instance Variables
	private Stage currentStage;
	private SharedMemoryRepository memory;

	//GUI Variables
	@FXML private AnchorPane anchorPane;
	@FXML private ScrollPane scrollPane;
	@FXML private VBox vContent;
	@FXML private Label lblCaption;



	/**
	 * Default Constructor
	 */
	public CriterionIterator()
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
		lblCaption.setText("Please specify the " + SharedMemoryRepository.getCriterion_names()[SharedMemoryRepository.getCriterion_iterator()] + " rating for each project");
		//content.setAlignment(Pos.TOP_CENTER);
		vContent.setSpacing(5);
		vContent.setLayoutX(10);
		vContent.setLayoutY(10);
		scrollPane.setContent(vContent);

		for(int i = 1; i <= SharedMemoryRepository.getProject_count(); i++)
		{
			HBox hContent = new HBox();
			hContent.setSpacing(10);
			hContent.setAlignment(Pos.CENTER);

			TextField txtProjectTitle = new TextField();
//			this.anchorPane.getChildren().add(txtProjectTitle);
			hContent.getChildren().add(txtProjectTitle);

			txtProjectTitle.setLayoutX(30);
			txtProjectTitle.setPrefWidth(360);
			txtProjectTitle.setId("txtProjectTitle" + String.format("%d", i));
			txtProjectTitle.setEditable(true);
			txtProjectTitle.setVisible(true);
			txtProjectTitle.setDisable(false);

			Slider sldCriterionValue = new Slider();
			hContent.getChildren().add(sldCriterionValue);
			sldCriterionValue.setLayoutX(100);
			sldCriterionValue.setLayoutY(50);
			sldCriterionValue.setPrefWidth(360);
			sldCriterionValue.setId("sldCriterionValue" + String.format("%d", i));
			sldCriterionValue.setDisable(false);
			sldCriterionValue.setVisible(true);
			sldCriterionValue.setMax(10);
			sldCriterionValue.setBlockIncrement(1);
			sldCriterionValue.setMajorTickUnit(2);
			sldCriterionValue.setMinorTickCount(1);
			vContent.getChildren().add(hContent);
		}

		if(SharedMemoryRepository.getCriterion_iterator() > 0)
		{
			int pN = 0;
			for (Node node : vContent.getChildren())
				if (node instanceof HBox)
					for(Node innernode : ((HBox) node).getChildren())
						if(innernode instanceof TextField)
						{
							((TextField) innernode).setText(SharedMemoryRepository.getProject_names()[pN++]);
							((TextField) innernode).setEditable(false);
						}
		}
		getCurrentStage().setOnShown(projectTitlesNotifier);
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
			//Traverse through all components inside/on anchorPane
			int pN = 0;
			int pV = 0;
			for (Node node : vContent.getChildren())
				if(node instanceof HBox)
					for(Node innernode : ((HBox) node).getChildren())
					{
						if(innernode instanceof TextField)
						{
							((TextField) innernode).getStyleClass().remove("txtDefaultError");
							((TextField) innernode).getStyleClass().add("txtDefault");
						}
						if (SharedMemoryRepository.getCriterion_iterator() == 0 && innernode instanceof TextField && !((TextField)innernode).getText().equals(""))
							SharedMemoryRepository.setProject_names(pN++, ((TextField)innernode).getText());
						else if(SharedMemoryRepository.getCriterion_iterator() == 0 && innernode instanceof TextField && ((TextField)innernode).getText().equals(""))
						{
							((TextField)innernode).getStyleClass().remove("txtDefault");
							((TextField)innernode).getStyleClass().add("txtDefaultError");
							throw new InvalidProjectTitleException("Please enter a valid project title.");//TODO: Finish this
						}
						else if (innernode instanceof Slider)
						{
							if ((int)((Slider)innernode).getValue() == 0)
							{
								innernode.requestFocus();
								throw new NumberFormatException("Please use the slider to select a value greater than zero");
							}
							SharedMemoryRepository.setProject_criterion_values(SharedMemoryRepository.getCriterion_iterator(), pV++, ((int) ((Slider) innernode).getValue()));
						}
			}

			SharedMemoryRepository.increase_criterion_iterator();
			if(SharedMemoryRepository.getCriterion_iterator() < SharedMemoryRepository.getCriterion_count())
			{
				FXMLLoader loader       = new FXMLLoader(getClass().getResource("CriterionIterator.fxml"));
				Stage      cross_window = new Stage(StageStyle.DECORATED);//
				cross_window.setResizable(false);
				cross_window.setTitle("CROSS 1.0 - " + SharedMemoryRepository.getCriterion_names()[SharedMemoryRepository.getCriterion_iterator()]);
				cross_window.setScene(new Scene((Pane) loader.load()));
				CriterionIterator criterions = loader.<CriterionIterator>getController();
				criterions.initialize(cross_window);
				cross_window.show();
				((Node) (event.getSource())).getScene().getWindow().hide();//Hide Previous Window

//				CriterionIterator criterions = new CriterionIterator(laf, criterionIterator, projectCount, criterionCount, criterionNames, criterionValues, projectNames, projectCriterionValues);
//				criterions.initialize(cross_window);
//				cross_window.show();
//				((Node) (event.getSource())).getScene().getWindow().hide();//Hide Previous Window
			}
			else
			{
				FXMLLoader loader       = new FXMLLoader(getClass().getResource("Results.fxml"));
				Stage      cross_results_window = new Stage(StageStyle.DECORATED);//
				cross_results_window.setResizable(false);
				cross_results_window.setTitle("CROSS 1.0 - Results");
				cross_results_window.setScene(new Scene((Pane) loader.load()));
				Results result = loader.<Results>getController();
				result.initialize(cross_results_window);
				cross_results_window.show();
				((Node) (event.getSource())).getScene().getWindow().hide();//Hide Previous Window
			}
		}
		catch (InvalidProjectTitleException iptex)
		{
			iptex.printStackTrace();
			getMemory().handleException(iptex);
		}
		catch(NumberFormatException nfex)
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

	/**
	 * Method to prompt before exit
	 * Exits application with 0 error code if user prompt is confirmed else application continues
	 */
	private EventHandler<WindowEvent> projectTitlesNotifier = event ->
	{
		if(SharedMemoryRepository.getCriterion_iterator() < 1)
		{
			Alert projectTitleNotification = new Alert(Alert.AlertType.INFORMATION, "Please specify project titles and their \nmatching criterion value for the specific criterion \nmentioned in the title above.");
			projectTitleNotification.initModality(Modality.APPLICATION_MODAL);
			projectTitleNotification.initOwner(getCurrentStage());
			projectTitleNotification.setTitle("Initialize Project Titles");
			projectTitleNotification.setHeaderText("Projects need titles");
			DialogPane dialogPane = projectTitleNotification.getDialogPane();
			dialogPane.getStylesheets().add(getClass().getResource(SharedMemoryRepository.getLaF()).toExternalForm());
			dialogPane.getStyleClass().add("dlgDefault");
			projectTitleNotification.showAndWait();
		}
	};
}

