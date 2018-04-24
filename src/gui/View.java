package gui;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;

import java.util.Map;
import java.util.Optional;

/**
 * Class that handles user interface.
 *
 */
public class View {

    /**
     * Creates an view with controller and translationsMap.
     * Sets by default font size to 10.0.
     * @param controller main controller.
     * @param translationMap map with translations for all GUI elements.
     */
    View(Controller controller, Map<String, String> translationMap){
        m_controller = controller;
        m_translationMap = translationMap;
        m_fontSize = 10.0;
    }

    /**
     * Sets traslation map.
     * @param translationsMap map to set.
     */
    public void setTranslationsMap(Map<String, String> translationsMap){
        m_translationMap = translationsMap;
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

        if(m_controller.decryptFiles.isSelected()){
            m_controller.addHint.setText(getDisplayString("showHintLabel"));
        }else{
            m_controller.addHint.setText(getDisplayString("addHintLabel"));
        }
        
        m_controller.encryptOrDecryptFilesBtn.setText(getDisplayString("encryptFiles"));
        m_controller.help.setText(getDisplayString("help"));
        m_controller.polish.setText(getDisplayString("polish"));
        m_controller.english.setText(getDisplayString("english"));
        m_controller.showChoosenFolderPath.setText(getDisplayString("showChoosenFolderPath"));
        m_controller.undoSelection.setText(getDisplayString("undoSelection"));
        m_controller.clearSelection.setText(getDisplayString("clearSelection"));
    }

    /**
     * Provides proper string to be displayed(using translation map).
     * @param displayStr string that we want to display.
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
        setButtonStyle(fontSize,true);
    }

    /**
     * Sets background color
     * @param backgroundColor color to set.
     */
    public void setBackgroundColor(String backgroundColor){
        String currentStyle = m_controller.mainGrid.getStyle();
        m_controller.mainGrid.setStyle(currentStyle + " -fx-background-color: " + backgroundColor);
    }

    /**
     * Handles situation when encode radioButton is clicked.
     * Changes changes text in proper controls, background color, and fonts.
     * Sets selection of add hint RadioButton to false.
     * Enables hintTextField and sets its content to empty string.
     */
    public void encodeRadioClick(){
        m_controller.addHint.setSelected(false);
        setHintTextFieldVisibility(false);

        m_controller.encryptOrDecryptFilesBtn.setText(getDisplayString("encryptFiles"));
        m_controller.addHint.setText(getDisplayString("addHintLabel"));
        setFonts(m_fontSize);
        setBackgroundColor("#FFFFFF");

        m_controller.hintTextField.setDisable(false);
        m_controller.hintTextField.setText("");
    }

    /**
     * Handles situation when decode radioButton is clicked.
     * Changes changes text in proper controls, background color, and fonts.
     * Sets selection of add hint RadioButton to false.
     */
    public void decodeRadioClick(){
        m_controller.addHint.setSelected(false);
        setHintTextFieldVisibility(false);

        m_controller.encryptOrDecryptFilesBtn.setText(getDisplayString("decryptFiles"));
        m_controller.addHint.setText(getDisplayString("showHintLabel"));
        setFonts(m_fontSize);
        setBackgroundColor("#90EE90");
    }

    /**
     * Displays alert when there are no files to encrypt or decrypt.
     */
    public void noFilesToEncryptOrDecryptAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("failureMsg"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("noFilesFailureMsg"));
        alert.showAndWait();
    }

    /**
     * Displays alert when there is empty path for chosen folder.
     */
    public void folderChosenPathEmptyAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("failureMsg"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("noDestinationFolderMsg"));
        alert.showAndWait();
    }

    /**
     * Displays alert with encrypt files status.
     */
    public void encryptFilesStatusAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("informationDialogMsg"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("encryptFilesStatusMsg"));
        alert.showAndWait();
    }

    /**
     * Displays alert with decrypt files status.
     */
    public void decryptFilesStatusAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("informationDialogMsg"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("decryptFilesStatusMsg"));

        alert.showAndWait();
    }

    /**
     * Changes destination folder color and enables button to show chosen folder path.
     */
    public void destinationFolderClick(){
        m_controller.chooseDestinationFolder.setStyle("-fx-background-color: #006400");
        m_controller.showChoosenFolderPath.setDisable(false);
    }

    /**
     * Displays alert when no object is clicked.
     */
    public void noObjectClicked(){
        Alert alert = new Alert(Alert.AlertType.WARNING, getDisplayString("selectFileOrFolderWarning"));
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("warningMsg"));
        alert.setHeaderText("");
        alert.showAndWait();
    }

    /**
     * Displays alert that waits for confirmation to add file or folder.
     * @param currentObjectClickedFullPath full path to object(file or folder) to be added.
     *
     * @return An {@link Optional} that contains the result.
     * Refer to the {@link Dialog} class documentation for more detail.
     */
    public Optional<ButtonType> confirmAddFileOrFolder(String currentObjectClickedFullPath){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, getDisplayString("encryptionDecryptionConfirmationMsg") + currentObjectClickedFullPath + " ?");
        setActualFontsSizeForAlert(alert);

        alert.setTitle(getDisplayString("confirmationMsg"));
        alert.setHeaderText("");
        return alert.showAndWait();
    }

    /**
     * Displays success message.
     */
    public void successMsg(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, getDisplayString("successMsg"));
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("successMsg"));
        alert.setHeaderText("");
        alert.showAndWait();
    }


    /**
     * Displays alert that informs us about the fact that we can not select a file or folder.
     */
    public void cannotSelectFileOrFolderMsg(){
        Alert alert = new Alert(Alert.AlertType.ERROR, getDisplayString("cannotSelectFile"));
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("cannotSelectFile"));
        alert.setHeaderText("");
        alert.showAndWait();
    }

    /**
     * Displays help menu.
     */
    public void displayHelpMenu(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("helpMenuTitle"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("helpMenuCotext"));

        alert.showAndWait();
    }

    /**
     * Displays path to chosen folder.
     * @param folderChosenPath path to chosen folder.
     */
    public void showChosenFolderPathClick(String folderChosenPath){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("informationDialogMsg"));
        alert.setHeaderText(null);
        alert.setContentText(folderChosenPath);

        alert.showAndWait();
    }

    /**
     * Sets hint text visibility.
     * @param setVisible value that indicates if we want to set it visible or not.
     */
    public void setHintTextFieldVisibility(Boolean setVisible){
        m_controller.hintTextField.setVisible(setVisible);
    }

    /**
     * Sets buttons size.
     * @param fontSize Double font size to set.
     * @param isEncrypt Is Encryption value
     */
    private void setButtonStyle(Double fontSize,Boolean isEncrypt){
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

    /**
     * Sets actual fonts size for alert.
     * @param alert An alert which we want to set fonts size.
     */
    private void setActualFontsSizeForAlert(Alert alert){
        alertRootNode = alert.getDialogPane();
        alertRootNode.setStyle(String.format("-fx-font-size: %dpt;", m_fontSize.intValue()));
    }

    /**
     * Displays hint. It is done when decryptFiles RadioButton is selected.
     */
    public void displayHint(){
        m_controller.hintTextField.setDisable(true);
        m_controller.hintTextField.setText("HINT"); //TODO: use getHelpMessage from FileEncryptor
    }

    private Controller m_controller;
    private Map<String, String> m_translationMap;
    private Double m_fontSize;
    private DialogPane alertRootNode;
}
