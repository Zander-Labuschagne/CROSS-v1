package cryogen;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * Main class to be executed first.
 * Copyright (C) 2017  Zander Labuschagne
 * This program is free software; you can redistribute it and/or modify it under the terms of the Apache Version 2.0 License
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application
{

    @Override
    public void start(Stage startStage) throws Exception
    {
	    startStage.initStyle(StageStyle.DECORATED);
	    //startStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/cryogen/icon.png")));
	    startStage.setTitle("CROSS 1.0");
	    FXMLLoader loader;
	    /*if(System.getProperty("os.name").startsWith("Windows"))
		    loader = new FXMLLoader(getClass().getResource("CryptogenWindows.fxml"));
	    else*/
		    loader = new FXMLLoader(getClass().getResource("CROSSStart.fxml"));
	    startStage.setResizable(false);
	    startStage.setScene(createScene(loader.load()));
	    startStage.getScene().getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
	    CROSSStart start = loader.getController();
	    start.initialize(startStage);
	    startStage.show();
    }

	private Scene createScene(Pane layout)
	{
		return new Scene(layout, Color.TRANSPARENT);
	}


    public static void main(String[] args) {
        launch(args);
    }
}
