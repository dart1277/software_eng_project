package gui;

import gui.cipherModule.CryptoTask;
import gui.cipherModule.DecryptorTask;
import gui.cipherModule.EncryptorTask;
import gui.cipherModule.FileEncryptor;
import gui.translationsImporter.TranslationsImporter;
import gui.translationsImporter.TranslationsImporterFactory;
import gui.translationsImporter.TranslationsImporterType;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    @FXML
    private StackPane fileBrowserPane;
    @FXML
    private StackPane selectedFileBrowserPane;
    @FXML
    private GridPane mainGrid;
    @FXML
    private Button addBtn;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Label fileSelectedLabel;
    @FXML
    private Label fileBrowserLabel;
    @FXML
    private RadioButton encryptFiles;
    @FXML
    private RadioButton decryptFiles;
    @FXML
    private Menu program;
    @FXML
    private Menu chooseLanguage;
    @FXML
    private Menu fontSize;
    @FXML
    private Button chooseDestinationFolder;
    @FXML
    private Label encryptionSpeedLabel;
    @FXML
    private RadioButton slowEncSpeed;
    @FXML
    private RadioButton defaultEncSpeed;
    @FXML
    private RadioButton fastEncSpeed;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label passwordAgainLabel;
    @FXML
    private RadioButton addHint;
    @FXML
    private Button encryptOrDecryptFilesBtn;
    @FXML
    private MenuItem help;
    @FXML
    private MenuItem polish;
    @FXML
    private MenuItem english;
    @FXML
    private Button showChoosenFolderPath;
    @FXML
    private Button undoSelection;
    @FXML
    private Button clearSelection;
    @FXML
    private TextField hintTextField;
    @FXML
    private PasswordField passwordText;
    @FXML
    private PasswordField passwordTextRepeat;

    /**
     * Initializes controller fields.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerFactory.init(this);
        setToggleGroups();
        setDefaultSelectedOperations();
        addTextLimiter(getHintTextField(), 60);
        setEncrypt(true);
        refreshTreeView();
        setFontSizeSelect(10.0);
        TranslationsImporter translImp = TranslationsImporterFactory.getTranslationImporter(TranslationsImporterType.TranslationsImporterENG);
        if (!translImp.translate()) {
            System.out.println("Translation failed!");
            return;
        }

        setView(new View(translImp.getTranslations()));
        getView().setTranslationsForGuiElements();
        getView().setFonts(getFontSizeSelect());
        getView().setHintTextFieldVisibility(false);
    }

    /**
     * Sets all parameters needed for encoding.
     */
    public void encodeRadioClick() {
        if (!isEncrypt()) {      //if changed
            clearSelectionClick();
        }
        setEncrypt(true);
        refreshTreeView();
        getView().encodeRadioClick();
    }

    /**
     * Sets all parameters needed for decoding.
     */
    public void decodeRadioClick() {
        if (isEncrypt()) {     //if changed
            clearSelectionClick();
        }
        setEncrypt(false);
        refreshTreeView();
        getView().decodeRadioClick();
    }

    /**
     * Checks all failure cases and runs encode/decode procedure.
     *
     * @return is encode/decode procedure run successfully.
     */
    public boolean encryptOrDecryptFilesClick() {
        //failure cases
        if (getChosenFilesTree().getChildren().isEmpty()) {
            getView().noFilesToEncryptOrDecryptAlert();
            return false;
        }
        if (getFolderChoosenPath().isEmpty()) {
            getView().folderChosenPathEmptyAlert();
            return false;
        }
        if (getPasswordText().getText().isEmpty()) {
            getView().noPasswordProvided();
            return false;
        }
        if (isEncrypt() && !getPasswordText().getText().equals(getPasswordTextRepeat().getText())) {
            getView().passwordsNotEqual();
            return false;
        }
        if (getPasswordText().getText().length() < 5) {
            getView().passwordTooShort();
            return false;
        }
        //start procedure
        if (isEncrypt()) {
            encryptFiles();
        } else {
            decryptFiles();
        }
        return true;
    }

    /**
     * Allows user to choose destination folder for encoding/decoding.
     *
     * @return path to choosen folder or null if nothing selected.
     */
    public Path chooseDestinationFolderClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(getView().getDisplayString("directoryChooserTitle"));
        File selectedFolder = directoryChooser.showDialog(new Stage());

        if (selectedFolder != null) {
            System.out.println("selected folder");
            getView().destinationFolderClick();

            setFolderChoosenPath(selectedFolder.toPath().toString());
            setHeadNodeName(getFolderChoosenPath());             //sets chosen head node name as path
            return selectedFolder.toPath();
        } else {
            return null;
        }
    }

    /**
     * Adds folder or file. Displays result.
     */
    public void addFileOrFolderClick() {
        if (getCurrentObjectClickedFullPath() == null) {
            getView().noObjectClicked();
            return;
        }

        Optional<ButtonType> confirmResult = getView().confirmAddFileOrFolder(getCurrentObjectClickedFullPath());
        if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
            boolean result = true; // temporary

            if (!addPathChoice(getCurrentObjectClickedFullPath())) //trying to add file
                result = false;

            if (result) {
                getView().successMsg();
            } else {
                getView().cannotSelectFileOrFolderMsg();
            }
        }
    }


    /**
     * Chooses english language for entire GUI.
     */
    public void chooseEnglishLanguage() {
        TranslationsImporter translImp = TranslationsImporterFactory.getTranslationImporter(TranslationsImporterType.TranslationsImporterENG);
        if (!translImp.translate()) {
            System.out.println("Translation failed!");
            return;
        }
        getView().setTranslationsMap(translImp.getTranslations());
        getView().setTranslationsForGuiElements();
    }

    /**
     * Chooses polish language for entire GUI.
     */
    public void choosePolishLanguage() {
        TranslationsImporter translImp = TranslationsImporterFactory.getTranslationImporter(TranslationsImporterType.TranslationsImporterPL);
        if (!translImp.translate()) {
            System.out.println("Translation failed!");
            return;
        }
        getView().setTranslationsMap(translImp.getTranslations());
        getView().setTranslationsForGuiElements();
    }

    /**
     * Sets font size to 10px, and background color depending on encrypt/decrypt mode.
     */
    public void fontSize10pxSelected() {
        setFontSizeSelect(10.0);
        getView().setFonts(getFontSizeSelect());
        setBackgroundColor();
    }

    /**
     * Sets font size to 12px, and background color depending on encrypt/decrypt mode.
     */
    public void fontSize12pxSelected() {
        setFontSizeSelect(12.0);
        getView().setFonts(getFontSizeSelect());
        setBackgroundColor();
    }

    /**
     * Sets font size to 14px, and background color depending on encrypt/decrypt mode.
     */
    public void fontSize14pxSelected() {
        setFontSizeSelect(14.0);
        getView().setFonts(getFontSizeSelect());
        setBackgroundColor();
    }

    /**
     * Displays help menu.
     */
    public void helpMenuSelected() {
        getView().displayHelpMenu();
    }

    /**
     * Shows choosen folder path.
     */
    public void showChoosenFolderPathClick() {
        getView().showChosenFolderPathClick(getFolderChoosenPath());
    }

    /**
     * Reverts last selected file from selected files list.
     */
    public void undoSelectionClick() {
        int last_item_index = getChosenFilesTree().getChildren().size() - 1;
        if (last_item_index >= 0)
            getChosenFilesTree().getChildren().remove(last_item_index);
    }

    /**
     * Deletes all files from selected files list.
     */
    public void clearSelectionClick() {
        getChosenFilesTree().getChildren().clear();
    }

    /**
     * Handles add hint RadioButton click.
     */
    public void addHintClick() {
        getView().setHintTextFieldVisibility(getAddHint().isSelected());

        if (getDecryptFiles().isSelected()) {
            List<String> selectedFiles = generateSelectedFilesList();
            String randomFilePath = "";
            for (String filePath : selectedFiles) {
                if (new File(filePath).isFile()) {
                    randomFilePath = filePath;
                    break;
                }
            }
            getView().displayHintFromFile(randomFilePath);
        }
    }

    /**
     * Sets background application color depending on encryp/decrypt mode
     */
    private void setBackgroundColor() {
        if (isEncrypt()) {
            getView().setBackgroundColor("#FFFFFF");
        } else {
            getView().setBackgroundColor("#90EE90");
        }
    }

    /**
     * Generates list of all selected files.
     *
     * @return List of selected files.
     */
    private List<String> generateSelectedFilesList() {
        List<String> result = new ArrayList<>();
        if (!getChosenFilesTree().getChildren().isEmpty()) {
            Object[] chosenFilesArr = getChosenFilesTree().getChildren().toArray();
            for (Object chosenFile : chosenFilesArr) {
                String fileName = chosenFile.toString();

                FilePathTreeItem fileTree = new FilePathTreeItem(new File(fileName));
                result.addAll(fileTree.getSelectedFilesList());
            }
        } else {
            System.out.println("THERE ARE NO FILES IN HERE");
        }
        return result;
    }

    /**
     * Checks if proposed file/folder is already on choosen files list.
     *
     * @param newPath Selested path to check.
     * @return True if node already exists, flase if node do not exist.
     */
    private boolean checkIfNodeAlreadyAdded(String newPath) {
        //checking under chosen files
        Object[] chosenFilesArr = getChosenFilesTree().getChildren().toArray();
        for (Object chosenFile : chosenFilesArr) {
            FilePathTreeItem fileTree = new FilePathTreeItem(new File(chosenFile.toString()));
            if (fileTree.findPath(newPath))
                return true;

        }
        //checking under newPath file
        FilePathTreeItem fileTree = new FilePathTreeItem(new File(newPath));
        for (Object chosenFile : chosenFilesArr) {
            if (fileTree.findPath(chosenFile.toString())) {
                getChosenFilesTree().getChildren().remove(chosenFile);
            }
        }
        return false;
    }

    private void setDefaultSelectedOperations() {
        getDefaultEncSpeed().setSelected(true);
        getEncryptFiles().setSelected(true);
    }

    private void setToggleGroups() {
        getSlowEncSpeed().setToggleGroup(getEncyptSpeedGroup());
        getDefaultEncSpeed().setToggleGroup(getEncyptSpeedGroup());
        getFastEncSpeed().setToggleGroup(getEncyptSpeedGroup());

        getEncryptFiles().setToggleGroup(getOperationToPerformGroup());
        getDecryptFiles().setToggleGroup(getOperationToPerformGroup());
    }

    private void refreshTreeView() {
        setFolderChoosenPath("");
        setUpFileBrowser();
        getChosenFilesTree().setExpanded(true);
        TreeView<String> treeView = new TreeView<>(getChosenFilesTree());
        getSelectedFileBrowserPane().getChildren().add(treeView);
        getShowChoosenFolderPath().setDisable(true);
        setHeadNodeName("");
    }

    private void setUpFileBrowser() {
        String hostName = "computer";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        TreeItem<String> rootNode = new TreeItem<>(hostName, new ImageView(new Image("file:img/computer.jpg")));

        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for (Path name : rootDirectories) {
            rootNode.getChildren().add(new FilePathTreeItem(name.toFile()));
        }
        rootNode.setExpanded(true);

        TreeView<String> treeView = new TreeView<>(rootNode);

        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> handleMouseClicked(e, treeView));

        getFileBrowserPane().getChildren().add(treeView);
    }

    /**
     * Tries to add/adds new file to chosen files list .
     *
     * @param path Selected file to add.
     * @return True if file added successfully, false if file can't be added.
     */
    public boolean addPathChoice(String path) {
        System.out.println("trying to add " + path);

        if (checkIfNodeAlreadyAdded(path))//check if adding is possible
            return false;         //cannot add, file found

        getChosenFilesTree().getChildren().add(new FilePathTreeItem(new File(path)));
        getChosenFilesTree().setExpanded(true);

        TreeView<String> treeView = new TreeView<>(getChosenFilesTree());

        getSelectedFileBrowserPane().getChildren().add(treeView);
        return true;
    }

    private void handleMouseClicked(MouseEvent event, TreeView treeView) {
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String fullPath = "";
            try {
                fullPath = ((FilePathTreeItem) treeView.getSelectionModel().getSelectedItem()).getAbsolutePath();
            } catch (ClassCastException | NullPointerException ex) {
                System.out.println("handleMouseClicked treeView exception");
            }
            setCurrentObjectClickedFullPath(fullPath);
        }

    }

    private static void addTextLimiter(final TextField textField, final int maxLength) {
        textField.textProperty().addListener((final ObservableValue<? extends String> ov, final String oldValue, final String newValue) -> {
            if (textField.getText().length() > maxLength) {
                String s = textField.getText().substring(0, maxLength);
                textField.setText(s);
            }
        });
    }

    /**
     * Enables/Disables GUIElements.
     *
     * @param sel Boolean value to Enable or Disable GUI elements.
     */
    public void setDisableGUIElements(boolean sel) {
        getChooseDestinationFolder().setDisable(sel);
        getEncryptOrDecryptFilesBtn().setDisable(sel);
        getAddBtn().setDisable(sel);
        getEncryptFiles().setDisable(sel);
        getDecryptFiles().setDisable(sel);
        getChooseLanguage().setDisable(sel);
        getSlowEncSpeed().setDisable(sel);
        getDefaultEncSpeed().setDisable(sel);
        getFastEncSpeed().setDisable(sel);
        getAddHint().setDisable(sel);
        getUndoSelection().setDisable(sel);
        getClearSelection().setDisable(sel);
        getHintTextField().setDisable(sel);
        getPasswordText().setDisable(sel);
    }

    private void freezeGUI() {
        Platform.runLater(() -> setDisableGUIElements(true));
    }

    private void unfreezeGUI(List<String> successList, List<String> failedList) {
        Platform.runLater(() -> {
            setDisableGUIElements(false);
            getView().cipheringResultAlert(successList, failedList);
        });


    }

    private void encryptFiles() {
        System.out.println("encryptFiles");
        List<String> list = generateSelectedFilesList();
        List<String> chrFilesList = list.stream()
                .filter(s -> s.endsWith(".chr"))
                .collect(Collectors.toList());
        if (chrFilesList.isEmpty()) {
            getView().encryptFilesStatusAlert();
            startEncryptingProcedure();
        } else {
            StringBuilder alertBuilder = new StringBuilder();
            for (String file : chrFilesList) {
                alertBuilder.append(file + "\n");
            }

            Optional<ButtonType> confirmResult = getView().showMultipleEncryptionConfirmation(alertBuilder.toString());
            if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
                getView().encryptFilesStatusAlert();
                startEncryptingProcedure();
            }

        }
    }

    private void setHeadNodeName(String name) {
        getChosenFilesTree().setValue(name);
    }

    private void decryptFiles() {
        System.out.println("decryptFiles");
        getView().decryptFilesStatusAlert();

        startDecryptingProcedure();
    }

    private int getEncryptionSpeedValue() {
        //get speed value from encryption speed controls on gui
        int x = getEncyptSpeedGroup().getSelectedToggle().selectedProperty().toString().lastIndexOf("id=");
        int y = getEncyptSpeedGroup().getSelectedToggle().selectedProperty().toString().indexOf(",", x);
        String speedText = getEncyptSpeedGroup().getSelectedToggle().selectedProperty().toString().substring(x + 3, y);
        switch (speedText) {
            case "fastEncSpeed":
                return 1;
            case "defaultEncSpeed":
                return 2;
            case "slowEncSpeed":
                return 3;
        }
        return 2;
    }

    private void startEncryptingProcedure() {
        String pass = getPasswordText().getText();
        String hint = "";

        List<String> successList = Collections.synchronizedList(new ArrayList<>());
        List<String> failedList = Collections.synchronizedList(new ArrayList<>());

        int speed = getEncryptionSpeedValue();
        if (getHintTextField().getText().isEmpty())
            hint = getView().getDisplayString("noHintMsg");
        else
            hint = getHintTextField().getText();

        FileEncryptor fileEncryptor = new FileEncryptor();
        fileEncryptor.configure(pass, speed, true, hint);
        CryptoTask cryptoTask = new EncryptorTask(fileEncryptor, this::freezeGUI, this::unfreezeGUI);
        cryptoTask.setLists(successList, failedList);
        if (!getChosenFilesTree().getChildren().isEmpty()) {
            Object[] chosenFilesArr = getChosenFilesTree().getChildren().toArray();
            for (Object chosenFile : chosenFilesArr) {
                String fileName = chosenFile.toString();

                FilePathTreeItem fileTree = new FilePathTreeItem(new File(fileName));
                fileTree.encryptOrDecryptFileTree(cryptoTask, getFolderChoosenPath(), "");

            }
            cryptoTask.process();
        } else {
            System.out.println("THERE ARE NO FILES IN HERE");
        }
    }

    private void startDecryptingProcedure() {
        String pass = getPasswordText().getText();


        List<String> successList = Collections.synchronizedList(new ArrayList<>());
        List<String> failedList = Collections.synchronizedList(new ArrayList<>());
        FileEncryptor fileEncryptor = new FileEncryptor();
        fileEncryptor.setKey(pass);
        CryptoTask cryptoTask = new DecryptorTask(fileEncryptor, this::freezeGUI, this::unfreezeGUI);
        cryptoTask.setLists(successList, failedList);

        if (!getChosenFilesTree().getChildren().isEmpty()) {
            Object[] chosenFilesArr = getChosenFilesTree().getChildren().toArray();
            for (Object chosenFile : chosenFilesArr) {
                String fileName = chosenFile.toString();

                FilePathTreeItem fileTree = new FilePathTreeItem(new File(fileName));
                fileTree.encryptOrDecryptFileTree(cryptoTask, getFolderChoosenPath(), "");

            }
            cryptoTask.process();
        } else {
            System.out.println("THERE ARE NO FILES IN HERE");
        }
    }

    /**
     * Sets path for selected destination folder.
     *
     * @param path Path to set as destination folder.
     */
    public void setChosenFolderPath(String path) {
        setFolderChoosenPath(path);
    }

    /**
     * Gets chosen files nodes
     *
     * @return All chosen files nodes
     */
    public TreeItem<String> getChosenFilesTree() {
        return chosenFilesTree;
    }

    private final ToggleGroup operationToPerformGroup = new ToggleGroup();
    private final ToggleGroup encyptSpeedGroup = new ToggleGroup();

    private String currentObjectClickedFullPath; // file or folder
    private TreeItem<String> chosenFilesTree = new TreeItem<>("", new ImageView(new Image("file:img/computer.jpg")));
    private boolean isEncrypt;
    private Double fontSizeSelect;
    private String folderChoosenPath;
    private View view;

    StackPane getFileBrowserPane() {
        return fileBrowserPane;
    }


    StackPane getSelectedFileBrowserPane() {
        return selectedFileBrowserPane;
    }


    GridPane getMainGrid() {
        return mainGrid;
    }


    Button getAddBtn() {
        return addBtn;
    }


    Label getFileSelectedLabel() {
        return fileSelectedLabel;
    }

    Label getFileBrowserLabel() {
        return fileBrowserLabel;
    }


    RadioButton getEncryptFiles() {
        return encryptFiles;
    }


    RadioButton getDecryptFiles() {
        return decryptFiles;
    }


    Menu getProgram() {
        return program;
    }


    Menu getChooseLanguage() {
        return chooseLanguage;
    }


    Menu getFontSize() {
        return fontSize;
    }


    Button getChooseDestinationFolder() {
        return chooseDestinationFolder;
    }


    Label getEncryptionSpeedLabel() {
        return encryptionSpeedLabel;
    }


    RadioButton getSlowEncSpeed() {
        return slowEncSpeed;
    }


    RadioButton getDefaultEncSpeed() {
        return defaultEncSpeed;
    }


    RadioButton getFastEncSpeed() {
        return fastEncSpeed;
    }


    Label getPasswordLabel() {
        return passwordLabel;
    }


    Label getPasswordAgainLabel() {
        return passwordAgainLabel;
    }


    RadioButton getAddHint() {
        return addHint;
    }


    Button getEncryptOrDecryptFilesBtn() {
        return encryptOrDecryptFilesBtn;
    }


    MenuItem getHelp() {
        return help;
    }


    MenuItem getPolish() {
        return polish;
    }


    MenuItem getEnglish() {
        return english;
    }


    Button getShowChoosenFolderPath() {
        return showChoosenFolderPath;
    }


    Button getUndoSelection() {
        return undoSelection;
    }


    Button getClearSelection() {
        return clearSelection;
    }


    TextField getHintTextField() {
        return hintTextField;
    }


    PasswordField getPasswordText() {
        return passwordText;
    }


    PasswordField getPasswordTextRepeat() {
        return passwordTextRepeat;
    }


    ToggleGroup getOperationToPerformGroup() {
        return operationToPerformGroup;
    }

    ToggleGroup getEncyptSpeedGroup() {
        return encyptSpeedGroup;
    }

    String getCurrentObjectClickedFullPath() {
        return currentObjectClickedFullPath;
    }

    void setCurrentObjectClickedFullPath(String currentObjectClickedFullPath) {
        this.currentObjectClickedFullPath = currentObjectClickedFullPath;
    }


    boolean isEncrypt() {
        return isEncrypt;
    }

    void setEncrypt(boolean encrypt) {
        isEncrypt = encrypt;
    }

    Double getFontSizeSelect() {
        return fontSizeSelect;
    }

    void setFontSizeSelect(Double fontSizeSelect) {
        this.fontSizeSelect = fontSizeSelect;
    }

    String getFolderChoosenPath() {
        return folderChoosenPath;
    }

    void setFolderChoosenPath(String folderChoosenPath) {
        this.folderChoosenPath = folderChoosenPath;
    }

    View getView() {
        return view;
    }

    void setView(View view) {
        this.view = view;
    }
}
