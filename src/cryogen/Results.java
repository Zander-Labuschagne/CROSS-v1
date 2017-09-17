package cryogen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * Java class handler for the CROSS application initial GUI
 * Copyright (C) 2017  Zander Labuschagne
 * This program is free software; you can redistribute it and/or modify it under the terms of the Apache Version 2.0 License
 */

public class Results implements Initializable
{
	//Instance Variables
	private Stage currentStage;
	private SharedMemoryRepository memory;
	//Decision Support Variables
	private double[][] criteria_pairwise_comparison_matrix;
	private double[] criteria_pairwise_comparison_matrix_sum;
	private double[][] normalized_criteria_pairwise_comparison_matrix;
	private double[] criteria_preference_vector;
	private double criteriaCI;
	private double criteriaCR;
	private final double CRITERIA_RI = (((2.7699 * SharedMemoryRepository.getCriterion_count()) - 4.3513) - SharedMemoryRepository.getCriterion_count()) / (SharedMemoryRepository.getCriterion_count() - 1);
	private final double PROJECT_RI = (((2.7699 * SharedMemoryRepository.getProject_count()) - 4.3513) - SharedMemoryRepository.getProject_count()) / (SharedMemoryRepository.getProject_count() - 1);
	//GUI Variables
	@FXML private Button btnContinue;
	@FXML private Button btnSave;
	@FXML private TableView<ProjectTableEntry> tblResults;
	private TableColumn<ProjectTableEntry, String> tcolProjectTitle;
	private TableColumn<ProjectTableEntry, String> tcolRankingScore;
	private final ObservableList<ProjectTableEntry> projectTableEntry = FXCollections.observableArrayList();
	@FXML private Label lblCI;


	/**
	 * Default Constructor
	 */
	public Results()
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
		btnSave.setLayoutX(80);
		btnContinue.setLayoutX(300);
		btnContinue.setPrefWidth(150);
		btnSave.setPrefWidth(150);
		tblResults.setEditable(false);
		tcolProjectTitle = new TableColumn("Project Title");
		tcolProjectTitle.setMinWidth(350);
		tcolProjectTitle.setCellValueFactory(new PropertyValueFactory<ProjectTableEntry, String>("projectTitle"));
		tcolRankingScore = new TableColumn("Project Ranking Score");
		tcolRankingScore.setMinWidth(200);
		tcolRankingScore.setCellValueFactory(new PropertyValueFactory<ProjectTableEntry, String>("projectRankingScore"));
		tblResults.getColumns().setAll(tcolProjectTitle, tcolRankingScore);
		tblResults.setItems(projectTableEntry);

		displayResults();
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

	private void initCriteriaPairwiseComparisonMatrix()
	{
		this.criteria_pairwise_comparison_matrix = new double[SharedMemoryRepository.getCriterion_count()][SharedMemoryRepository.getCriterion_count()];

		for(int i = 0; i < SharedMemoryRepository.getCriterion_count(); i++)
			for(int ii = 0; ii < SharedMemoryRepository.getCriterion_count(); ii++)
				this.criteria_pairwise_comparison_matrix[i][ii] = (double)SharedMemoryRepository.getCriterion_values()[ii] / (double)SharedMemoryRepository.getCriterion_values()[i];

//		criteria_pairwise_comparison_matrix[0][0] = 1;
//		criteria_pairwise_comparison_matrix[0][1] = (double)1/(double)5;
//		criteria_pairwise_comparison_matrix[0][2] = 3;
//		criteria_pairwise_comparison_matrix[0][3] = 4;
//		criteria_pairwise_comparison_matrix[1][0] = 5;
//		criteria_pairwise_comparison_matrix[1][1] = 1;
//		criteria_pairwise_comparison_matrix[1][2] = 9;
//		criteria_pairwise_comparison_matrix[1][3] = 7;
//		criteria_pairwise_comparison_matrix[2][0] = (double)1/(double)3;
//		criteria_pairwise_comparison_matrix[2][1] = (double)1/(double)9;
//		criteria_pairwise_comparison_matrix[2][2] = 1;
//		criteria_pairwise_comparison_matrix[2][3] = 2;
//		criteria_pairwise_comparison_matrix[3][0] = (double)1/(double)4;
//		criteria_pairwise_comparison_matrix[3][1] = (double)1/(double)7;
//		criteria_pairwise_comparison_matrix[3][2] = (double)1/(double)2;
//		criteria_pairwise_comparison_matrix[3][3] = 1;
	}

	private double[][] getCriteria_pairwise_comparison_matrix()
	{
		return this.criteria_pairwise_comparison_matrix;
	}

	private void setCriteriaPairwiseComparisonMatrixSum()
	{
		initCriteriaPairwiseComparisonMatrix();

		this.criteria_pairwise_comparison_matrix_sum = new double[SharedMemoryRepository.getCriterion_count()];

		for(int v = 0; v < SharedMemoryRepository.getCriterion_count(); v++)
			this.criteria_pairwise_comparison_matrix_sum[v] = 0;

		for(int iii = 0; iii < SharedMemoryRepository.getCriterion_count(); iii++)
			for (int iv = 0; iv < SharedMemoryRepository.getCriterion_count(); iv++)
				this.criteria_pairwise_comparison_matrix_sum[iv] += getCriteria_pairwise_comparison_matrix()[iii][iv];
	}

	private double[] getCriteria_pairwise_comparison_matrix_sum()
	{
		return this.criteria_pairwise_comparison_matrix_sum;
	}

	private void normalizeCriteriaPairwiseComparisonMatrix()
	{
		setCriteriaPairwiseComparisonMatrixSum();
		this.normalized_criteria_pairwise_comparison_matrix = new double[SharedMemoryRepository.getCriterion_count()][SharedMemoryRepository.getCriterion_count()];

		for(int vi = 0; vi < SharedMemoryRepository.getCriterion_count(); vi++)
			for(int vii = 0; vii < SharedMemoryRepository.getCriterion_count(); vii++)
				this.normalized_criteria_pairwise_comparison_matrix[vi][vii] = getCriteria_pairwise_comparison_matrix()[vi][vii] / getCriteria_pairwise_comparison_matrix_sum()[vii];
	}

	private double[][] getNormailizedCriteriaPairwiseComparisonMatrix()
	{
		return this.normalized_criteria_pairwise_comparison_matrix;
	}

	private void setCriteriaPreferenceVector()
	{
		normalizeCriteriaPairwiseComparisonMatrix();

		this.criteria_preference_vector = new double[SharedMemoryRepository.getCriterion_count()];

		for(int x = 0; x < SharedMemoryRepository.getCriterion_count(); x++)
			this.criteria_preference_vector[x] = 0;

		for(int viii = 0; viii < SharedMemoryRepository.getCriterion_count(); viii++)
			for(int ix = 0; ix < SharedMemoryRepository.getCriterion_count(); ix++)
				this.criteria_preference_vector[viii] += getNormailizedCriteriaPairwiseComparisonMatrix()[viii][ix];

		for(int xi = 0; xi < SharedMemoryRepository.getCriterion_count(); xi++)
			this.criteria_preference_vector[xi] /= SharedMemoryRepository.getCriterion_count();
	}

	private double[] getCriteriaPreferenceVector()
	{
		return this.criteria_preference_vector;
	}

	private void setCriteriaCI()
	{
		setCriteriaPreferenceVector();

		double[] criteria_preference_vector_multiplcation_result = new double[SharedMemoryRepository.getCriterion_count()];

		for(int xiv = 0; xiv < SharedMemoryRepository.getCriterion_count(); xiv++)
			criteria_preference_vector_multiplcation_result[xiv] = 0;

		for(int xii = 0; xii < SharedMemoryRepository.getCriterion_count(); xii++)
			for(int xiii = 0; xiii < SharedMemoryRepository.getCriterion_count(); xiii++)
				criteria_preference_vector_multiplcation_result[xii] += getCriteriaPreferenceVector()[xiii] * getCriteria_pairwise_comparison_matrix()[xii][xiii];

		double[] criteria_preference_vector_multiplcation_division_result = new double[SharedMemoryRepository.getCriterion_count()];

		for(int xv = 0; xv < SharedMemoryRepository.getCriterion_count(); xv++)
			criteria_preference_vector_multiplcation_division_result[xv] = criteria_preference_vector_multiplcation_result[xv] / getCriteriaPreferenceVector()[xv];

		double temp_sum = 0;
		for(int xvi = 0; xvi < SharedMemoryRepository.getCriterion_count(); xvi++)
			temp_sum += criteria_preference_vector_multiplcation_division_result[xvi];

		double criteria_preference_vector_multiplaction_division_results_average = temp_sum / SharedMemoryRepository.getCriterion_count();

		this.criteriaCI = (double)(criteria_preference_vector_multiplaction_division_results_average - SharedMemoryRepository.getCriterion_count()) / (double)(SharedMemoryRepository.getCriterion_count() - 1);
	}

	private double getCriteriaCI()
	{
		return this.criteriaCI;
	}

	private void setCriteriaCR()
	{
		setCriteriaCI();

		this.criteriaCR = getCriteriaCI() / getCRITERIA_RI();
	}

	private double getCRITERIA_RI()
	{
		return this.CRITERIA_RI;
	}

	private double getCriteriaCR()
	{
		return this.criteriaCR;
	}

	private double getPROJECT_RI()
	{
		return this.PROJECT_RI;
	}



	private void displayResults()
	{
		setCriteriaCR();
		boolean ci_accept = true;
		if(getCriteriaCR() > 0.10)
			ci_accept = false;

		System.out.println("RI: " + getCRITERIA_RI());
		System.out.println("CR: " + getCriteriaCR());

		//A vector of matrices
		double[][][] projectPairwiseComparisonMatrices = new double[SharedMemoryRepository.getCriterion_count()][SharedMemoryRepository.getProject_count()][SharedMemoryRepository.getProject_count()];
		//Compuet Pairwise Comparison Matrices for all projects for each criterion
		for(int xviii = 0; xviii < SharedMemoryRepository.getCriterion_count(); xviii++)
			for(int xix = 0; xix < SharedMemoryRepository.getProject_count(); xix++)
				for(int xx = 0; xx < SharedMemoryRepository.getProject_count(); xx++)
					projectPairwiseComparisonMatrices[xviii][xix][xx] = SharedMemoryRepository.getProject_criterion_values()[xviii][xix] / SharedMemoryRepository.getProject_criterion_values()[xviii][xx];

//		projectPairwiseComparisonMatrices[0][0][0] = 1;
//		projectPairwiseComparisonMatrices[0][0][1] = 3;
//		projectPairwiseComparisonMatrices[0][0][2] = 2;
//		projectPairwiseComparisonMatrices[0][1][0] = (double)1/(double)3;
//		projectPairwiseComparisonMatrices[0][1][1] = 1;
//		projectPairwiseComparisonMatrices[0][1][2] = (double)1/(double)5;
//		projectPairwiseComparisonMatrices[0][2][0] = (double)1/(double)2;
//		projectPairwiseComparisonMatrices[0][2][1] = 5;
//		projectPairwiseComparisonMatrices[0][2][2] = 1;
//
//		projectPairwiseComparisonMatrices[1][0][0] = 1;
//		projectPairwiseComparisonMatrices[1][0][1] = 6;
//		projectPairwiseComparisonMatrices[1][0][2] = (double)1/(double)3;
//		projectPairwiseComparisonMatrices[1][1][0] = (double)1/(double)6;
//		projectPairwiseComparisonMatrices[1][1][1] = 1;
//		projectPairwiseComparisonMatrices[1][1][2] = (double)1/(double)9;
//		projectPairwiseComparisonMatrices[1][2][0] = 3;
//		projectPairwiseComparisonMatrices[1][2][1] = 9;
//		projectPairwiseComparisonMatrices[1][2][2] = 1;
//
//		projectPairwiseComparisonMatrices[2][0][0] = 1;
//		projectPairwiseComparisonMatrices[2][0][1] = (double)1/(double)3;
//		projectPairwiseComparisonMatrices[2][0][2] = 1;
//		projectPairwiseComparisonMatrices[2][1][0] = 3;
//		projectPairwiseComparisonMatrices[2][1][1] = 1;
//		projectPairwiseComparisonMatrices[2][1][2] = 7;
//		projectPairwiseComparisonMatrices[2][2][0] = 1;
//		projectPairwiseComparisonMatrices[2][2][1] = (double)1/(double)7;
//		projectPairwiseComparisonMatrices[2][2][2] = 1;
//
//		projectPairwiseComparisonMatrices[2][0][0] = 1;
//		projectPairwiseComparisonMatrices[2][0][1] = (double)1/(double)3;
//		projectPairwiseComparisonMatrices[2][0][2] = (double)1/(double)2;
//		projectPairwiseComparisonMatrices[2][1][0] = 3;
//		projectPairwiseComparisonMatrices[2][1][1] = 1;
//		projectPairwiseComparisonMatrices[2][1][2] = 4;
//		projectPairwiseComparisonMatrices[2][2][0] = 2;
//		projectPairwiseComparisonMatrices[2][2][1] = (double)1/(double)4;
//		projectPairwiseComparisonMatrices[2][2][2] = 1;


		//A vector of vectors
		double[][] projectPairwiseComparisonsMatricesSums = new double[SharedMemoryRepository.getCriterion_count()][SharedMemoryRepository.getProject_count()];
		for(int xxiv = 0; xxiv < SharedMemoryRepository.getCriterion_count(); xxiv++)
			for(int xxv = 0; xxv < SharedMemoryRepository.getProject_count(); xxv++)
				projectPairwiseComparisonsMatricesSums[xxiv][xxv] = 0;
		//Compute sum totals of all columns for each project for all criteions
		for(int xxi = 0; xxi < SharedMemoryRepository.getCriterion_count(); xxi++)
			for(int xxii = 0; xxii < SharedMemoryRepository.getProject_count(); xxii++)
				for(int xxiii = 0; xxiii < SharedMemoryRepository.getProject_count(); xxiii++)
					projectPairwiseComparisonsMatricesSums[xxi][xxiii] += projectPairwiseComparisonMatrices[xxi][xxii][xxiii];

		//A vector of matrices
		double[][][] normailizedProjectPairwiseComparisonMatrices = new double[SharedMemoryRepository.getCriterion_count()][SharedMemoryRepository.getProject_count()][SharedMemoryRepository.getProject_count()];
		//Compute Normalized Pairwise Comparison Matrices for all projects for each criterion
		for(int xxvi = 0; xxvi < SharedMemoryRepository.getCriterion_count(); xxvi++)
			for(int xxvii = 0; xxvii < SharedMemoryRepository.getProject_count(); xxvii++)
				for(int xxviii = 0; xxviii < SharedMemoryRepository.getProject_count(); xxviii++)
					normailizedProjectPairwiseComparisonMatrices[xxvi][xxvii][xxviii] = projectPairwiseComparisonMatrices[xxvi][xxvii][xxviii] / projectPairwiseComparisonsMatricesSums[xxvi][xxviii];

		//A vector of vectors
		double[][] preferenceVectors = new double[SharedMemoryRepository.getCriterion_count()][SharedMemoryRepository.getProject_count()];
		for(int xxix = 0; xxix < SharedMemoryRepository.getCriterion_count(); xxix++)
			for(int xxx = 0; xxx < SharedMemoryRepository.getProject_count(); xxx++)
				preferenceVectors[xxix][xxx] = 0;
		//Compute average of all rows for each project for all criterions - preference vector
		for(int xxxi = 0; xxxi < SharedMemoryRepository.getCriterion_count(); xxxi++)
			for(int xxxii = 0; xxxii < SharedMemoryRepository.getProject_count(); xxxii++)
				for(int xxxiii = 0; xxxiii < SharedMemoryRepository.getProject_count(); xxxiii++)
					preferenceVectors[xxxi][xxxii] += normailizedProjectPairwiseComparisonMatrices[xxxi][xxxii][xxxiii];
		for(int xxxiv = 0; xxxiv < SharedMemoryRepository.getCriterion_count(); xxxiv++)
			for(int xxxv = 0; xxxv < SharedMemoryRepository.getProject_count(); xxxv++)
				preferenceVectors[xxxiv][xxxv] /= SharedMemoryRepository.getProject_count();

		//A vector of project scores
		double[] rankingScores = new double[SharedMemoryRepository.getProject_count()];
		for (int xxxviii = 0; xxxviii < SharedMemoryRepository.getProject_count(); xxxviii++)
			rankingScores[xxxviii] = 0;
		for(int xxxvi = 0; xxxvi < SharedMemoryRepository.getProject_count(); xxxvi++)
			for(int xxxvii = 0; xxxvii < SharedMemoryRepository.getCriterion_count(); xxxvii++)
				rankingScores[xxxvi] += preferenceVectors[xxxvii][xxxvi] * getCriteriaPreferenceVector()[xxxvii];

		for(int xvii = 0; xvii < SharedMemoryRepository.getProject_count(); xvii++)
			projectTableEntry.add(new ProjectTableEntry(SharedMemoryRepository.getProject_names()[xvii], String.format("%5f", rankingScores[xvii])));

		double[][] projectMultiplaction = new double[SharedMemoryRepository.getCriterion_count()][SharedMemoryRepository.getProject_count()];
		for(int xxxxii = 0; xxxxii < SharedMemoryRepository.getCriterion_count(); xxxxii++)
			for(int xxxxiii = 0; xxxxiii < SharedMemoryRepository.getProject_count(); xxxxiii++)
				projectMultiplaction[xxxxii][xxxxiii] = 0;
		for(int xxxix = 0; xxxix < SharedMemoryRepository.getCriterion_count(); xxxix++)
			for(int xxxx = 0; xxxx < SharedMemoryRepository.getProject_count(); xxxx++)
				for(int xxxxi = 0; xxxxi < SharedMemoryRepository.getProject_count(); xxxxi++)
					projectMultiplaction[xxxix][xxxx] += preferenceVectors[xxxix][xxxxi] * projectPairwiseComparisonMatrices[xxxix][xxxx][xxxxi];

		double[][] projectDivision = new double[SharedMemoryRepository.getCriterion_count()][SharedMemoryRepository.getProject_count()];
		for(int xxxxiv = 0; xxxxiv < SharedMemoryRepository.getCriterion_count(); xxxxiv++)
			for(int xxxxv = 0; xxxxv < SharedMemoryRepository.getProject_count(); xxxxv++)
				projectDivision[xxxxiv][xxxxv] = projectMultiplaction[xxxxiv][xxxxv] / preferenceVectors[xxxxiv][xxxxv];

		double[] temp_sum = new double[SharedMemoryRepository.getCriterion_count()];
		for(int xxxxvii = 0; xxxxvii < SharedMemoryRepository.getCriterion_count(); xxxxvii++)
			temp_sum[xxxxvii] = 0;
		for(int xxxxvi = 0; xxxxvi < SharedMemoryRepository.getCriterion_count(); xxxxvi++)
			for(int xxxxviii = 0; xxxxviii < SharedMemoryRepository.getProject_count(); xxxxviii++)
				temp_sum[xxxxvi] += projectDivision[xxxxvi][xxxxviii];

		for(int li = 0; li < SharedMemoryRepository.getCriterion_count(); li++)
			temp_sum[li] /= SharedMemoryRepository.getProject_count();

		double[] ci = new double[SharedMemoryRepository.getCriterion_count()];
		for (int xxxxix = 0; xxxxix < SharedMemoryRepository.getCriterion_count(); xxxxix++)
			ci[xxxxix] = (double)(temp_sum[xxxxix] - SharedMemoryRepository.getProject_count()) / (double)(SharedMemoryRepository.getProject_count() - 1);

		double[] cr = new double[SharedMemoryRepository.getCriterion_count()];
		for(int l = 0; l < SharedMemoryRepository.getCriterion_count(); l++)
		{
			System.out.println("CI: " + ci[l]);

			cr[l] = ci[l] / getPROJECT_RI();
			if(cr[l] > 0.10)
				ci_accept = false;
			System.out.println("CR: " + cr[l]);
		}
		System.out.println("RI: " + getPROJECT_RI());


		if(ci_accept)
		{
			lblCI.setTextFill(Paint.valueOf("#0b8043"));
			lblCI.setText("Consistency ratio is acceptable");
		}
		else
		{
			lblCI.setTextFill(Paint.valueOf("#ea4335"));
			lblCI.setText("Consistency ratio is unacceptable!");
		}

		tblResults.setRowFactory(row -> new TableRow<ProjectTableEntry>()
		{
			@Override
			public void updateItem(ProjectTableEntry item, boolean empty)
			{
				super.updateItem(item, empty);
				if (item == null || empty)
				{
					setStyle("");
				}
				else
				{
					//Now 'item' has all the info of the Person in this row
					if (Double.parseDouble(item.getProjectRankingScore()) > 0.5)
					{
						//We apply now the changes in all the cells of the row
						for(int i=0; i<getChildren().size();i++)
						{
							//((Labeled) getChildren().get(i)).setTextFill(Color.LIMEGREEN);
							((Labeled) getChildren().get(i)).setStyle("-fx-background-color: lightgreen");
						}
					}
					else if (Double.parseDouble(item.getProjectRankingScore()) < 0.3)
					{
						//We apply now the changes in all the cells of the row
						for(int i=0; i<getChildren().size();i++)
						{
							//((Labeled) getChildren().get(i)).setTextFill(Color.LIMEGREEN);
							((Labeled) getChildren().get(i)).setStyle("-fx-background-color: indianred");
						}
					}
					else
					{
						if(getTableView().getSelectionModel().getSelectedItems().contains(item))
						{
							for(int i=0; i<getChildren().size();i++)
							{
								((Labeled) getChildren().get(i)).setTextFill(Color.WHITE);;
							}
						}
						else
						{
							for(int i=0; i<getChildren().size();i++)
							{
								((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);;
							}
						}
					}
				}
			}
		});
	}

	/**
	 * Move on to criterions window
	 */
	@FXML
	protected void btnContinue_Clicked(ActionEvent event)
	{
			System.exit(0);
	}

	@FXML
	protected void btnSave_Clicked(ActionEvent event)
	{
		//TODO:Save to file
	}
}

