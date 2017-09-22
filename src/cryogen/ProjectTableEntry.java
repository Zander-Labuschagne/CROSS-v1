package cryogen;

import javafx.beans.property.SimpleStringProperty;

public class ProjectTableEntry
{
	private SimpleStringProperty projectTitle;
	private SimpleStringProperty projectRankingScore;

	public ProjectTableEntry()
	{
		setProjectTitle("");
		setProjectRankingScore("");
	}

	public ProjectTableEntry(String projectTitle, String projectRankingScore)
	{
		this();
		setProjectTitle(projectTitle);
		setProjectRankingScore(projectRankingScore);
	}

	public void setProjectTitle(String projectTitle)
	{
		this.projectTitle = new SimpleStringProperty(projectTitle);
	}

	public void setProjectRankingScore(String projectRankingScore)
	{
		this.projectRankingScore = new SimpleStringProperty(projectRankingScore);
	}

	public String getProjectTitle()
	{
		return this.projectTitle.get();
	}

	public String getProjectRankingScore()
	{
		return this.projectRankingScore.get();
	}
}
