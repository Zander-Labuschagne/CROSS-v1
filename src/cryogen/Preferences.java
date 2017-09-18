package cryogen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * Class to manage events for Preferences Window
 * @author Zander Labuschagne 23585137
 * E-Mail: zander.labuschagne@protonmail.ch
 *
 *
 */

public class Preferences implements Initializable
{
    //Instance Variables
    private Stage currentStage;
    //GUI Instance Variables
    @FXML private ComboBox cmbLaF;
    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources)
    {

    }

    /**
     *
     * @param stage
     */
    public void initialize(Stage stage)
    {
        this.currentStage = stage;
        final ObservableList<String> lafs = FXCollections.observableArrayList("Midna", "Midna Dark", "Breath Dark");
        cmbLaF.getItems().setAll(lafs);
        if(SharedMemoryRepository.getLaF().equals("Midna.css"))
            cmbLaF.setValue(lafs.get(0));
        else if(SharedMemoryRepository.getLaF().equals("MidnaDark.css"))
            cmbLaF.setValue(lafs.get(1));
        else if(SharedMemoryRepository.getLaF().equals("BreathDark.css"))
            cmbLaF.setValue(lafs.get(2));
    }

    /**
     *
     * @param event
     */
    @FXML
    protected void btnAccept_Clicked(ActionEvent event)
    {
        if(cmbLaF.getValue().toString().equals("Midna"))
        {
            SharedMemoryRepository.setLaF("Midna.css");
            currentStage.getScene().getStylesheets().clear();
            currentStage.getScene().setUserAgentStylesheet(null);
            currentStage.getScene().getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        }
        else if(cmbLaF.getValue().toString().equals("Midna Dark"))
        {
            SharedMemoryRepository.setLaF("MidnaDark.css");
            currentStage.getScene().getStylesheets().clear();
            currentStage.getScene().setUserAgentStylesheet(null);
            currentStage.getScene().getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        }
        else if (cmbLaF.getValue().toString().equals("Breath Dark"))
        {
            SharedMemoryRepository.setLaF("BreathDark.css");
            currentStage.getScene().getStylesheets().clear();
            currentStage.getScene().setUserAgentStylesheet(null);
            currentStage.getScene().getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        }

        ((Node) (event.getSource())).getScene().getWindow().hide();//Hide This Window
    }

    /**
     *
     * @param event
     */
    @FXML
    protected void btnDiscard_Clicked(ActionEvent event)
    {
        ((Node) (event.getSource())).getScene().getWindow().hide();//Hide This Window
    }
}
