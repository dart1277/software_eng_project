package gui;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Map;
import java.util.Optional;

public class View {
    private Controller m_controller;
    private Map<String, String> m_translationMap;
    private Double m_fontSize;
    private DialogPane alertRootNode;

    View(Controller controller, Map<String, String> translationMap){
        m_controller = controller;
        m_translationMap = translationMap;
        m_fontSize = 10.0;
        handleRadioButtons();
    }

    public void setTranslationsMap(Map<String, String> translationsMap){
        m_translationMap = translationsMap;
    }
    private void styleAlert(Alert alert){
        alertRootNode = alert.getDialogPane();
        alertRootNode.setStyle(String.format("-fx-font-size: %dpt;", getFontSize().intValue()));
    }

    /**
     * Sets toggle groups and default selected operations.
     *
     */
    private void handleRadioButtons(){
        m_controller.slowEncSpeed.setToggleGroup(m_controller.encyptSpeed);
        m_controller.defaultEncSpeed.setToggleGroup(m_controller.encyptSpeed);
        m_controller.fastEncSpeed.setToggleGroup(m_controller.encyptSpeed);

        m_controller.defaultEncSpeed.setSelected(true);

        m_controller.encryptFiles.setToggleGroup(m_controller.groupEncryptOrDecrypt);
        m_controller.decryptFiles.setToggleGroup(m_controller.groupEncryptOrDecrypt);

        m_controller.encryptFiles.setSelected(true);
    }

    /**
     * Sets translations for all GUI elements.
     */
    public void setTranslationsForGuiElements(){
        m_controller.addBtn.setText(getDisplayString("addBtn"));
        m_controller.fileSelectedLabel.setText(getDisplayString("fileSelectedLabel"));
        m_controller.fileBrowserLabel.setText(getDisplayString("fileBrowserLabel"));
        m_controller.encryptFiles.setText(getDisplayString("encryptFiles"));
        m_controller.decryptFiles.setText(getDisplayString("decryptFiles"));
        m_controller.program.setText(getDisplayString("program"));
        m_controller.chooseLanguage.setText(getDisplayString("chooseLanguage"));
        m_controller.fontSize.setText(getDisplayString("fontSize"));
        m_controller.chooseDestinationFolder.setText(getDisplayString("chooseDestinationFolder"));
        m_controller.encryptionSpeedLabel.setText(getDisplayString("encryptionSpeedLabel"));
        m_controller.slowEncSpeed.setText(getDisplayString("slowEncSpeed"));
        m_controller.defaultEncSpeed.setText(getDisplayString("defaultEncSpeed"));
        m_controller.fastEncSpeed.setText(getDisplayString("fastEncSpeed"));
        m_controller.passwordLabel.setText(getDisplayString("passwordLabel"));
        m_controller.passwordAgainLabel.setText(getDisplayString("passwordAgainLabel"));
        m_controller.addHintLabel.setText(getDisplayString("addHintLabel"));
        m_controller.encryptOrDecryptFilesBtn.setText(getDisplayString("encryptFiles"));
        m_controller.help.setText(getDisplayString("help"));
        m_controller.polish.setText(getDisplayString("polish"));
        m_controller.english.setText(getDisplayString("english"));
        m_controller.showChoosenFolderPath.setText(getDisplayString("showChoosenFolderPath"));
        m_controller.undoSelection.setText(getDisplayString("undoSelection"));
        m_controller.clearSelection.setText(getDisplayString("clearSelection"));
    }

    /**
     * Function that provides String to be displayed.
     * @param displayStr String that we want to display.
     * @return String to display in proper translation if found - empty String otherwise.
     */
    public String getDisplayString(String displayStr){
        return m_translationMap.getOrDefault(displayStr, "");
    }

    /**
     * Sets font size.
     * @param fontSize Double font size to set.
     */
    public void setFonts(Double fontSize){
        m_fontSize = fontSize;
        String fontSizeFormat = String.format("-fx-font-size: %dpt;", m_fontSize.intValue());
        m_controller.mainGrid.setStyle(fontSizeFormat);
    }
    /**
     * Gets font size.
     * @return Double font size.
     */
    public Double getFontSize(){
        return m_fontSize;
    }


    /**
     * Sets buttons size.
     * @param fontSize Double font size to set.
     * @param isEncrypt Is Encryption value
     */
    public void setButtonStyle(Double fontSize,Boolean isEncrypt){
        m_fontSize = fontSize;
        String fontSizeFormat = String.format("-fx-font-size: %dpt;", m_fontSize.intValue());

        String buttonBackgroundColor="#673ab7;"; //default for encryption
        if(!isEncrypt)
            buttonBackgroundColor="#ccccff;";     //for decryption

        String buttonsStyle = "-fx-background-color: "+buttonBackgroundColor +
                " -fx-border-style: solid;" +
                " -fx-border-color: "+buttonBackgroundColor +
                " -fx-border-width: 1;" +
                fontSizeFormat +
                "-fx-font-family: \"Arial\";";

        m_controller.encryptOrDecryptFilesBtn.setStyle(buttonsStyle);
        m_controller.chooseDestinationFolder.setStyle(buttonsStyle);
        m_controller.addBtn.setStyle(buttonsStyle);
    }

    public void setBackgroundStyle(String backgroundColor){
        String currentStyle = m_controller.mainGrid.getStyle();
        m_controller.mainGrid.setStyle(currentStyle + " -fx-background-color: " + backgroundColor);
    }

    public void encodeRadioClick(){
        setFonts(m_fontSize);
        m_controller.encryptOrDecryptFilesBtn.setText(getDisplayString("encryptFiles"));
        setBackgroundStyle("#FFFFFF");
    }
    public void decodeRadioClick(){
        setFonts(m_fontSize);
        m_controller.encryptOrDecryptFilesBtn.setText(getDisplayString("decryptFiles"));
        setBackgroundStyle("#90EE90");
    }

    public void noFilesToEncryptOrDecryptAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        styleAlert(alert);
        alert.setTitle(getDisplayString("failureMsg"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("noFilesFailureMsg"));
        alert.showAndWait();
    }

    public void folderChoosenPathEmptyAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        styleAlert(alert);
        alert.setTitle(getDisplayString("failureMsg"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("noDestinationFolderMsg"));
        alert.showAndWait();
    }

    public void encryptFilesAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        styleAlert(alert);
        alert.setTitle(getDisplayString("informationDialogMsg"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("encryptFilesStatusMsg"));
        alert.showAndWait();
    }

    public void decryptFilesAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        styleAlert(alert);
        alert.setTitle(getDisplayString("informationDialogMsg"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("decryptFilesStatusMsg"));

        alert.showAndWait();
    }

    public void destinationFolderClick(){
        m_controller.chooseDestinationFolder.setStyle("-fx-background-color: #006400");
        m_controller.showChoosenFolderPath.setDisable(false);
    }

    public void noObjectClicked(){
        Alert alert = new Alert(Alert.AlertType.WARNING, getDisplayString("selectFileOrFolderWarning"));
        styleAlert(alert);
        alert.setTitle(getDisplayString("warningMsg"));
        alert.setHeaderText("");
        alert.showAndWait();
    }

    public Optional<ButtonType> confirmAction(String currentObjectClickedFullPath){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, getDisplayString("encryptionDecryptionConfirmationMsg") + currentObjectClickedFullPath + " ?");
        styleAlert(alert);

        alert.setTitle(getDisplayString("confirmationMsg"));
        alert.setHeaderText("");
        return alert.showAndWait();
    }

    public void successMsg(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, getDisplayString("successMsg"));
        styleAlert(alert);
        alert.setTitle(getDisplayString("successMsg"));
        alert.setHeaderText("");
        alert.showAndWait();
    }

    public void cannotSelectFileMsg(){
        Alert alert = new Alert(Alert.AlertType.ERROR, getDisplayString("cannotSelectFile"));
        styleAlert(alert);
        alert.setTitle(getDisplayString("cannotSelectFile"));
        alert.setHeaderText("");
        alert.showAndWait();
    }

    public void helpMenuSelected(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        styleAlert(alert);
        alert.setTitle(getDisplayString("helpMenuTitle"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("helpMenuCotext"));

        alert.showAndWait();
    }

    public void showChoosenFolderPathClick(String folderChoosenPath){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        styleAlert(alert);
        alert.setTitle(getDisplayString("informationDialogMsg"));
        alert.setHeaderText(null);
        alert.setContentText(folderChoosenPath);

        alert.showAndWait();
    }
    public void setHintTextFieldVisibility(Boolean isSelected){
        m_controller.hintTextField.setVisible(isSelected);
    }



}
