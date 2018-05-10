package gui;


import gui.cipherModule.CryptoException;
import gui.cipherModule.FileEncryptor;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class that handles user interface.
 */
public class View {

    /**
     * Creates an view with controller and translationsMap.
     * Sets by default font size to 10.0.
     *
     * @param translationMap map with translations for all GUI elements.
     */
    View(Map<String, String> translationMap) {
        m_translationMap = translationMap;
        m_fontSize = 10.0;
    }

    /**
     * Sets traslation map.
     *
     * @param translationsMap map to set.
     */
    public void setTranslationsMap(Map<String, String> translationsMap) {
        m_translationMap = translationsMap;
    }

    /**
     * Sets translations for all GUI elements.
     */
    public void setTranslationsForGuiElements() {
        Controller m_controller = ControllerFactory.getController();
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

        if (m_controller.decryptFiles.isSelected()) {
            m_controller.addHint.setText(getDisplayString("showHintLabel"));
            m_controller.encryptOrDecryptFilesBtn.setText(getDisplayString("decryptFiles"));
        } else {
            m_controller.addHint.setText(getDisplayString("addHintLabel"));
            m_controller.encryptOrDecryptFilesBtn.setText(getDisplayString("encryptFiles"));
        }

        m_controller.help.setText(getDisplayString("help"));
        m_controller.polish.setText(getDisplayString("polish"));
        m_controller.english.setText(getDisplayString("english"));
        m_controller.showChoosenFolderPath.setText(getDisplayString("showChoosenFolderPath"));
        m_controller.undoSelection.setText(getDisplayString("undoSelection"));
        m_controller.clearSelection.setText(getDisplayString("clearSelection"));
    }

    /**
     * Provides proper string to be displayed(using translation map).
     *
     * @param displayStr string that we want to display.
     * @return String to display in proper translation if found - empty String otherwise.
     */
    public String getDisplayString(String displayStr) {
        return m_translationMap.getOrDefault(displayStr, "");
    }

    /**
     * Sets font size.
     *
     * @param fontSize Double font size to set.
     */
    public void setFonts(Double fontSize) {
        m_fontSize = fontSize;
        String fontSizeFormat = String.format("-fx-font-size: %dpt;", m_fontSize.intValue());
        ControllerFactory.getController().mainGrid.setStyle(fontSizeFormat);
        setButtonStyle(fontSize, true);
    }

    /**
     * Sets background color
     *
     * @param backgroundColor color to set.
     */
    public void setBackgroundColor(String backgroundColor) {
        Controller m_controller = ControllerFactory.getController();
        String currentStyle = m_controller.mainGrid.getStyle();
        m_controller.mainGrid.setStyle(currentStyle + " -fx-background-color: " + backgroundColor);
    }

    /**
     * Handles situation when encode radioButton is clicked.
     * Changes changes text in proper controls, background color, and fonts.
     * Sets selection of add hint RadioButton to false.
     * Enables hintTextField and sets its content to empty string.
     * Shows all only encryption fields
     */
    public void encodeRadioClick() {
        Controller m_controller = ControllerFactory.getController();
        m_controller.addHint.setSelected(false);
        setHintTextFieldVisibility(false);

        m_controller.encryptOrDecryptFilesBtn.setText(getDisplayString("encryptFiles"));
        m_controller.addHint.setText(getDisplayString("addHintLabel"));
        setFonts(m_fontSize);
        setBackgroundColor("#FFFFFF");

        m_controller.hintTextField.setDisable(false);
        m_controller.hintTextField.setText("");

        m_controller.passwordAgainLabel.setDisable(false);
        m_controller.passwordTextRepeat.setDisable(false);
        m_controller.encryptionSpeedLabel.setDisable(false);
        m_controller.slowEncSpeed.setDisable(false);
        m_controller.defaultEncSpeed.setDisable(false);
        m_controller.fastEncSpeed.setDisable(false);

    }

    /**
     * Handles situation when decode radioButton is clicked.
     * Changes changes text in proper controls, background color, and fonts.
     * Sets selection of add hint RadioButton to false.
     * hide all only encryption fields
     */
    public void decodeRadioClick() {
        Controller m_controller = ControllerFactory.getController();
        m_controller.addHint.setSelected(false);
        setHintTextFieldVisibility(false);

        m_controller.encryptOrDecryptFilesBtn.setText(getDisplayString("decryptFiles"));
        m_controller.addHint.setText(getDisplayString("showHintLabel"));
        setFonts(m_fontSize);
        setBackgroundColor("#90EE90");

        m_controller.passwordAgainLabel.setDisable(true);
        m_controller.passwordTextRepeat.setDisable(true);
        m_controller.passwordTextRepeat.clear();
        m_controller.encryptionSpeedLabel.setDisable(true);
        m_controller.slowEncSpeed.setDisable(true);
        m_controller.defaultEncSpeed.setDisable(true);
        m_controller.fastEncSpeed.setDisable(true);


    }

    /**
     * Displays alert when there are no files to encrypt or decrypt.
     */
    public void noFilesToEncryptOrDecryptAlert() {
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
    public void folderChosenPathEmptyAlert() {
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
    public void encryptFilesStatusAlert() {
        this.processingAlert = new Alert(Alert.AlertType.INFORMATION);
        setActualFontsSizeForAlert(this.processingAlert);
        this.processingAlert.setTitle(getDisplayString("informationDialogMsg"));
        this.processingAlert.setHeaderText(null);
        this.processingAlert.setContentText(getDisplayString("encryptFilesStatusMsg"));
        this.processingAlert.show();
    }

    /**
     * Displays alert with decrypt files status.
     */
    public void decryptFilesStatusAlert() {
        this.processingAlert = new Alert(Alert.AlertType.INFORMATION);
        setActualFontsSizeForAlert(this.processingAlert);
        this.processingAlert.setTitle(getDisplayString("informationDialogMsg"));
        this.processingAlert.setHeaderText(null);
        this.processingAlert.setContentText(getDisplayString("decryptFilesStatusMsg"));
        this.processingAlert.show();
    }

    /**
     * Displays alert when there is no password provided.
     */
    public void noPasswordProvided() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("failureMsg"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("noPasswordProvided"));
        alert.showAndWait();
    }

    /**
     * Displays alert when the passwords are not equal.
     */
    public void passwordsNotEqual() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("failureMsg"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("passwordsNotEqual"));
        alert.showAndWait();
    }

    /**
     * Changes destination folder color and enables button to show chosen folder path.
     */
    public void destinationFolderClick() {
        Controller m_controller = ControllerFactory.getController();
        m_controller.chooseDestinationFolder.setStyle("-fx-background-color: #006400");
        m_controller.showChoosenFolderPath.setDisable(false);
    }

    /**
     * Displays alert when no object is clicked.
     */
    public void noObjectClicked() {
        Alert alert = new Alert(Alert.AlertType.WARNING, getDisplayString("selectFileOrFolderWarning"));
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("warningMsg"));
        alert.setHeaderText("");
        alert.showAndWait();
    }

    /**
     * Displays alert that waits for confirmation to add file or folder.
     *
     * @param currentObjectClickedFullPath full path to object(file or folder) to be added.
     * @return An {@link Optional} that contains the result.
     * Refer to the {@link Dialog} class documentation for more detail.
     */
    public Optional<ButtonType> confirmAddFileOrFolder(String currentObjectClickedFullPath) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, getDisplayString("encryptionDecryptionConfirmationMsg") + currentObjectClickedFullPath + " ?");
        setActualFontsSizeForAlert(alert);

        alert.setTitle(getDisplayString("confirmationMsg"));
        alert.setHeaderText("");
        return alert.showAndWait();
    }


    //redundant
//    public Optional<ButtonType> showConfirmationAlert(String alertContent) {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, alertContent);
//        setActualFontsSizeForAlert(alert);
//
//        alert.setTitle(getDisplayString("confirmationMsg"));
//        alert.setHeaderText("");
//        return alert.showAndWait();
//    }

    /**
     * Displays success message.
     */
    public void successMsg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, getDisplayString("successMsg"));
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("successMsg"));
        alert.setHeaderText("");
        alert.showAndWait();
    }

    public Optional<ButtonType> showMultipleEncryptionConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle(getDisplayString("confirmMultipleEncryptionTitle"));
        alert.setHeaderText("");
        alert.setContentText("");
        Label label = new Label(getDisplayString("confirmMultipleEncryptionNote"));

        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setPrefSize(800, 300);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        return alert.showAndWait();
    }


    public void cipheringResultAlert(List<String> successList, List<String> failedList) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        this.processingAlert.close();

        alert.setTitle(getDisplayString("processingResult"));
        alert.setHeaderText("");
        alert.setContentText("");
        Label label = new Label(getDisplayString("processingResultNote"));

        StringBuilder sb = new StringBuilder();

        if (!successList.isEmpty())
            sb.append(getDisplayString("successWord") + ":\n");
        for (String s : successList) {
            sb.append(s);
            sb.append("\n");
        }

        if (!failedList.isEmpty())
            sb.append(getDisplayString("failureWord") + ":\n");
        for (String s : failedList) {
            sb.append(s);
            sb.append("\n");
        }

        String resultText = sb.toString();

        TextArea textArea = new TextArea(resultText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setPrefSize(800, 300);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alert.showAndWait();
    }


    /**
     * Displays alert that informs us about the fact that we can not select a file or folder.
     */
    public void cannotSelectFileOrFolderMsg() {
        Alert alert = new Alert(Alert.AlertType.ERROR, getDisplayString("cannotSelectFile"));
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("cannotSelectFile"));
        alert.setHeaderText("");
        alert.showAndWait();
    }

    /**
     * Displays help menu.
     */
    public void displayHelpMenu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        setActualFontsSizeForAlert(alert);
        alert.setTitle(getDisplayString("helpMenuTitle"));
        alert.setHeaderText(null);
        alert.setContentText(getDisplayString("helpMenuCotext"));

        alert.showAndWait();
    }

    /**
     * Displays path to chosen folder.
     *
     * @param folderChosenPath path to chosen folder.
     */
    public void showChosenFolderPathClick(String folderChosenPath) {
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
     *
     * @param setVisible value that indicates if we want to set it visible or not.
     */
    public void setHintTextFieldVisibility(Boolean setVisible) {
        ControllerFactory.getController().hintTextField.setVisible(setVisible);
    }

    /**
     * Sets buttons size.
     *
     * @param fontSize  Double font size to set.
     * @param isEncrypt Is Encryption value
     */
    private void setButtonStyle(Double fontSize, Boolean isEncrypt) {
        Controller m_controller = ControllerFactory.getController();
        m_fontSize = fontSize;
        String fontSizeFormat = String.format("-fx-font-size: %dpt;", m_fontSize.intValue());

        String buttonBackgroundColor = "#673ab7;"; //default for encryption
        if (!isEncrypt)
            buttonBackgroundColor = "#ccccff;";     //for decryption

        String buttonsStyle = "-fx-background-color: " + buttonBackgroundColor +
                " -fx-border-style: solid;" +
                " -fx-border-color: " + buttonBackgroundColor +
                " -fx-border-width: 1;" +
                fontSizeFormat +
                "-fx-font-family: \"Arial\";";

        m_controller.encryptOrDecryptFilesBtn.setStyle(buttonsStyle);
        m_controller.chooseDestinationFolder.setStyle(buttonsStyle);
        m_controller.addBtn.setStyle(buttonsStyle);
    }

    /**
     * Sets actual fonts size for alert.
     *
     * @param alert An alert which we want to set fonts size.
     */
    private void setActualFontsSizeForAlert(Alert alert) {
        alertRootNode = alert.getDialogPane();
        alertRootNode.setStyle(String.format("-fx-font-size: %dpt;", m_fontSize.intValue()));
    }

    /**
     * Displays hint. It is done when decryptFiles RadioButton is selected.
     *
     * @param path path to get message from.
     */
    public void displayHintFromFile(String path) {
        Controller m_controller = ControllerFactory.getController();
        m_controller.hintTextField.setDisable(true);
        FileEncryptor encryptor = new FileEncryptor();
        String hint = getDisplayString("noHintMsg");
        try {
            hint = encryptor.getHelpMessage(path);
        } catch (CryptoException e) {
            //e.printStackTrace();
        }
        m_controller.hintTextField.setText(hint);
    }

    private Alert processingAlert;
    private Map<String, String> m_translationMap;
    private Double m_fontSize;
    private DialogPane alertRootNode;
}
