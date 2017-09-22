package cryogen;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * About class to handle execution code for About Window
 * Copyright (C) 2017  Zander Labuschagne and Elnette Moller
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation
 */
public class About implements Initializable
{
    @FXML private StackPane stackPaneIcon;
    @FXML private AnchorPane anchorPane;
    @FXML private Label label;

    /**
     * Default Constructor
     */
    public About()
    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if(SharedMemoryRepository.getLaF().equals("Midna.css"))
        {
            anchorPane.getStylesheets().clear();
            anchorPane.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        }
        stackPaneIcon.getStyleClass().add("stackPaneIcon");
    }
}
