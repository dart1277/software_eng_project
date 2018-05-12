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
    public StackPane fileBrowserPane;
    public StackPane selectedFileBrowserPane;
    public GridPane mainGrid;
    public Button addBtn;
    public MenuBar menuBar;
    public Label fileSelectedLabel;
    public Label fileBrowserLabel;
    public RadioButton encryptFiles;
    public RadioButton decryptFiles;
    public Menu program;
    public Menu chooseLanguage;
    public Menu fontSize;
    public Button chooseDestinationFolder;
    public Label encryptionSpeedLabel;
    public RadioButton slowEncSpeed;
    public RadioButton defaultEncSpeed;
    public RadioButton fastEncSpeed;
    public Label passwordLabel;
    public Label passwordAgainLabel;
    public RadioButton addHint;
    public Button encryptOrDecryptFilesBtn;
    public MenuItem help;
    public MenuItem polish;
    public MenuItem english;
    public Button showChoosenFolderPath;
    public Button undoSelection;
    public Button clearSelection;
    public TextField hintTextField;
    public PasswordField passwordText;
    public PasswordField passwordTextRepeat;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ControllerFactory.init(this);
        setToggleGroups();
        setDefaultSelectedOperations();
        addTextLimiter(hintTextField, 60);
        isEncrypt = true;
        refreshTreeView();
        fontSizeSelect = 10.0;
        TranslationsImporter translImp = TranslationsImporterFactory.getTranslationImporter(TranslationsImporterType.TranslationsImporterENG);
        if (!translImp.translate()) {
            System.out.println("Translation failed!");
            return;
        }

        view = new View(translImp.getTranslations());
        view.setTranslationsForGuiElements();
        view.setFonts(fontSizeSelect);
        view.setHintTextFieldVisibility(false);
    }

    public void encodeRadioClick() {
        if (!isEncrypt) {      //if changed
            clearSelectionClick();
        }
        isEncrypt = true;
        refreshTreeView();
        view.encodeRadioClick();
    }

    public void decodeRadioClick() {
        if (isEncrypt) {     //if changed
            clearSelectionClick();
        }
        isEncrypt = false;
        refreshTreeView();
        view.decodeRadioClick();
    }

    public boolean encryptOrDecryptFilesClick() {
        //failure cases
        if (chosenFilesTree.getChildren().isEmpty()) {
            view.noFilesToEncryptOrDecryptAlert();
            return false;
        }
        if (folderChoosenPath.isEmpty()) {
            view.folderChosenPathEmptyAlert();
            return false;
        }
        if (passwordText.getText().isEmpty()) {
            view.noPasswordProvided();
            return false;
        }
        if (isEncrypt && !passwordText.getText().equals(passwordTextRepeat.getText())) {
            view.passwordsNotEqual();
            return false;
        }
        if (passwordText.getText().length() < 5) {
            view.passwordTooShort();
            return false;
        }
        //start procedure
        if (isEncrypt) {
            encryptFiles();
        } else {
            decryptFiles();
        }
        return true;
    }

    public Path chooseDestinationFolderClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(view.getDisplayString("directoryChooserTitle"));
        File selectedFolder = directoryChooser.showDialog(new Stage());

        if (selectedFolder != null) {
            System.out.println("selected folder");
            view.destinationFolderClick();

            folderChoosenPath = selectedFolder.toPath().toString();
            setHeadNodeName(folderChoosenPath);             //sets chosen head node name as path
            return selectedFolder.toPath();
        } else {
            return null;
        }
    }

    /**
     * Adds folder or file. Displays result.
     */
    public void addFileOrFolderClick() {
        if (currentObjectClickedFullPath == null) {
            view.noObjectClicked();
            return;
        }

        Optional<ButtonType> confirmResult = view.confirmAddFileOrFolder(currentObjectClickedFullPath);
        if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
            boolean result = true; // temporary

            if (!addPathChoice(currentObjectClickedFullPath)) //trying to add file
                result = false;

            if (result) {
                view.successMsg();
            } else {
                view.cannotSelectFileOrFolderMsg();
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
        view.setTranslationsMap(translImp.getTranslations());
        view.setTranslationsForGuiElements();
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
        view.setTranslationsMap(translImp.getTranslations());
        view.setTranslationsForGuiElements();
    }

    /**
     * Sets font size to 10px, and background color depending on encrypt/decrypt mode.
     */
    public void fontSize10pxSelected() {
        fontSizeSelect = 10.0;
        view.setFonts(fontSizeSelect);
        setBackgroundColor();
    }

    /**
     * Sets font size to 12px, and background color depending on encrypt/decrypt mode.
     */
    public void fontSize12pxSelected() {
        fontSizeSelect = 12.0;
        view.setFonts(fontSizeSelect);
        setBackgroundColor();
    }

    /**
     * Sets font size to 14px, and background color depending on encrypt/decrypt mode.
     */
    public void fontSize14pxSelected() {
        fontSizeSelect = 14.0;
        view.setFonts(fontSizeSelect);
        setBackgroundColor();
    }

    /**
     * Displays help menu.
     */
    public void helpMenuSelected() {
        view.displayHelpMenu();
    }

    /**
     * Shows choosen folder path.
     */
    public void showChoosenFolderPathClick() {
        view.showChosenFolderPathClick(folderChoosenPath);
    }

    public void undoSelectionClick() {
        int last_item_index = chosenFilesTree.getChildren().size() - 1;
        if (last_item_index >= 0)
            chosenFilesTree.getChildren().remove(last_item_index);
    }

    public void clearSelectionClick() {
        chosenFilesTree.getChildren().clear();
    }

    /**
     * Handles add hint RadioButton click.
     */
    public void addHintClick() {
        view.setHintTextFieldVisibility(addHint.isSelected());

        if (decryptFiles.isSelected()) {
            List<String> selectedFiles = generateSelectedFilesList();
            String randomFilePath = "";
            for (String filePath : selectedFiles) {
                if (new File(filePath).isFile()) {
                    randomFilePath = filePath;
                    break;
                }
            }
            view.displayHintFromFile(randomFilePath);
        }
    }

    private void setBackgroundColor() {
        if (isEncrypt) {
            view.setBackgroundColor("#FFFFFF");
        } else {
            view.setBackgroundColor("#90EE90");
        }
    }

    private List<String> generateSelectedFilesList() {
        List<String> result = new ArrayList<>();
        if (!chosenFilesTree.getChildren().isEmpty()) {
            Object[] chosenFilesArr = chosenFilesTree.getChildren().toArray();
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

    private boolean checkIfNodeAlreadyAdded(String newPath) {
        //checking under chosen files
        Object[] chosenFilesArr = chosenFilesTree.getChildren().toArray();
        for (Object chosenFile : chosenFilesArr) {
            FilePathTreeItem fileTree = new FilePathTreeItem(new File(chosenFile.toString()));
            if (fileTree.findPath(newPath))
                return true;

        }
        //checking under newPath file
        FilePathTreeItem fileTree = new FilePathTreeItem(new File(newPath));
        for (Object chosenFile : chosenFilesArr) {
            if (fileTree.findPath(chosenFile.toString())) {
                chosenFilesTree.getChildren().remove(chosenFile);
            }
        }
        return false;
    }

    private void setDefaultSelectedOperations() {
        defaultEncSpeed.setSelected(true);
        encryptFiles.setSelected(true);
    }

    private void setToggleGroups() {
        slowEncSpeed.setToggleGroup(encyptSpeedGroup);
        defaultEncSpeed.setToggleGroup(encyptSpeedGroup);
        fastEncSpeed.setToggleGroup(encyptSpeedGroup);

        encryptFiles.setToggleGroup(operationToPerformGroup);
        decryptFiles.setToggleGroup(operationToPerformGroup);
    }

    private void refreshTreeView() {
        folderChoosenPath = "";
        setUpFileBrowser();
        chosenFilesTree.setExpanded(true);
        TreeView<String> treeView = new TreeView<>(chosenFilesTree);
        selectedFileBrowserPane.getChildren().add(treeView);
        showChoosenFolderPath.setDisable(true);
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

        //System.out.println("hostName: " + hostName);
        TreeItem<String> rootNode = new TreeItem<>(hostName, new ImageView(new Image("file:img/computer.jpg")));

        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for (Path name : rootDirectories) {
            rootNode.getChildren().add(new FilePathTreeItem(name.toFile()));
        }
        rootNode.setExpanded(true);

        TreeView<String> treeView = new TreeView<>(rootNode);

        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> handleMouseClicked(e, treeView));

        fileBrowserPane.getChildren().add(treeView);
    }

    public boolean addPathChoice(String path) {
        System.out.println("trying to add " + path);

        if (checkIfNodeAlreadyAdded(path))//check if adding is possible
            return false;         //cannot add, file found

        chosenFilesTree.getChildren().add(new FilePathTreeItem(new File(path)));
        chosenFilesTree.setExpanded(true);

        TreeView<String> treeView = new TreeView<>(chosenFilesTree);

        selectedFileBrowserPane.getChildren().add(treeView);
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
            currentObjectClickedFullPath = fullPath;
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

    public void setDisableGUIElements(boolean sel) {
        chooseDestinationFolder.setDisable(sel);
        encryptOrDecryptFilesBtn.setDisable(sel);
        addBtn.setDisable(sel);
        encryptFiles.setDisable(sel);
        decryptFiles.setDisable(sel);
        chooseLanguage.setDisable(sel);
        slowEncSpeed.setDisable(sel);
        defaultEncSpeed.setDisable(sel);
        fastEncSpeed.setDisable(sel);
        addHint.setDisable(sel);
        undoSelection.setDisable(sel);
        clearSelection.setDisable(sel);
        hintTextField.setDisable(sel);
        passwordText.setDisable(sel);
    }

    private void freezeGUI() {
        Platform.runLater(() -> setDisableGUIElements(true));
    }

    private void unfreezeGUI(List<String> successList, List<String> failedList) {
        Platform.runLater(() -> {
            setDisableGUIElements(false);
            view.cipheringResultAlert(successList, failedList);
        });


    }

    private void encryptFiles() {
        System.out.println("encryptFiles");
        List<String> list = generateSelectedFilesList();
        List<String> chrFilesList = list.stream()
                .filter(s -> s.endsWith(".chr"))
                .collect(Collectors.toList());
        if (chrFilesList.isEmpty()) {
            view.encryptFilesStatusAlert();
            startEncryptingProcedure();
        } else {
            StringBuilder alertBuilder = new StringBuilder();
            for (String file : chrFilesList) {
                alertBuilder.append(file + "\n");
            }

            Optional<ButtonType> confirmResult = view.showMultipleEncryptionConfirmation(alertBuilder.toString());
            if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
                view.encryptFilesStatusAlert();
                startEncryptingProcedure();
            }

        }

        //view.encryptFilesStatusAlert();

        //startEncryptingProcedure();
    }

    private void setHeadNodeName(String name) {
        chosenFilesTree.setValue(name);
    }

    private void decryptFiles() {
        System.out.println("decryptFiles");
        view.decryptFilesStatusAlert();

        startDecryptingProcedure();
    }

    private int getEncryptionSpeedValue() {
        //get speed value from encryption speed controls on gui
        int x = encyptSpeedGroup.getSelectedToggle().selectedProperty().toString().lastIndexOf("id=");
        int y = encyptSpeedGroup.getSelectedToggle().selectedProperty().toString().indexOf(",", x);
        String speedText = encyptSpeedGroup.getSelectedToggle().selectedProperty().toString().substring(x + 3, y);
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
        String pass = passwordText.getText();
        String hint = "";

        List<String> successList = Collections.synchronizedList(new ArrayList<>());
        List<String> failedList = Collections.synchronizedList(new ArrayList<>());

        int speed = getEncryptionSpeedValue();
        if (hintTextField.getText().isEmpty())
            hint = view.getDisplayString("noHintMsg");
        else
            hint = hintTextField.getText();

        FileEncryptor fileEncryptor = new FileEncryptor();
        fileEncryptor.configure(pass, speed, true, hint);
        CryptoTask cryptoTask = new EncryptorTask(fileEncryptor, this::freezeGUI, this::unfreezeGUI);
        cryptoTask.setLists(successList, failedList);
        if (!chosenFilesTree.getChildren().isEmpty()) {
            Object[] chosenFilesArr = chosenFilesTree.getChildren().toArray();
            for (Object chosenFile : chosenFilesArr) {
                String fileName = chosenFile.toString();

                FilePathTreeItem fileTree = new FilePathTreeItem(new File(fileName));
                fileTree.encryptOrDecryptFileTree(cryptoTask, folderChoosenPath, "");

            }
            cryptoTask.process();
        } else {
            System.out.println("THERE ARE NO FILES IN HERE");
        }
    }

    private void startDecryptingProcedure() {
        String pass = passwordText.getText();


        List<String> successList = Collections.synchronizedList(new ArrayList<>());
        List<String> failedList = Collections.synchronizedList(new ArrayList<>());
        FileEncryptor fileEncryptor = new FileEncryptor();
        fileEncryptor.setKey(pass);
        CryptoTask cryptoTask = new DecryptorTask(fileEncryptor, this::freezeGUI, this::unfreezeGUI);
        cryptoTask.setLists(successList, failedList);
        //fileEncryptor.configure(pass, CryptoModule.REGULAR_MODE, true, "Masełko");


        if (!chosenFilesTree.getChildren().isEmpty()) {
            Object[] chosenFilesArr = chosenFilesTree.getChildren().toArray();
            for (Object chosenFile : chosenFilesArr) {
                String fileName = chosenFile.toString();

                FilePathTreeItem fileTree = new FilePathTreeItem(new File(fileName));
                fileTree.encryptOrDecryptFileTree(cryptoTask, folderChoosenPath, "");

            }
            cryptoTask.process();
        } else {
            System.out.println("THERE ARE NO FILES IN HERE");
        }
    }

    public void setChosenFolderPath(String path) {
        folderChoosenPath = path;
    }

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
}
