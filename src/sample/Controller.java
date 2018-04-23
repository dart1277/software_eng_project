package sample;

import TranslationsImporter.*;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller  implements Initializable {
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
    public RadioButton addHintLabel;
    public Button encryptOrDecryptFilesBtn;
    public MenuItem help;
    public MenuItem polish;
    public MenuItem english;
    public Button showChoosenFolderPath;
    public Button undoSelection;
    public Button clearSelection;
    public TextField hintTextField;

    public final ToggleGroup groupEncryptOrDecrypt = new ToggleGroup();
    public final ToggleGroup encyptSpeed = new ToggleGroup();

    private String currentObjectClickedFullPath; // file or folder
    private TreeItem<String> chosenFilesTree=new TreeItem<>("", new ImageView(new Image("file:img/computer.jpg")));
    private boolean isEncrypt;
    private Double fontSizeSelect;
    private String folderChoosenPath;
    private View view;

    private void setUpFileBrowser(){
        String hostName="computer";
        try{
            hostName = InetAddress.getLocalHost().getHostName();
        }catch(UnknownHostException ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("hostName: " + hostName);
        TreeItem<String> rootNode= new TreeItem<>(hostName, new ImageView(new Image("file:img/computer.jpg")));

        Iterable<Path> rootDirectories= FileSystems.getDefault().getRootDirectories();
        for(Path name : rootDirectories){
            rootNode.getChildren().add(new FilePathTreeItem(name.toFile()));
        }
        rootNode.setExpanded(true);

        TreeView<String> treeView= new TreeView<>(rootNode);

        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> handleMouseClicked(e, treeView));

        fileBrowserPane.getChildren().add(treeView);
    }
    private boolean addPathChoice(String path)
    {
        System.out.println("trying to add "+path);

        if(checkIfNodeAlreadyAdded(path))//check if adding is possible
            return false;         //cannot add, file found

        chosenFilesTree.getChildren().add(new FilePathTreeItem(new File(path)));
        chosenFilesTree.setExpanded(true);

        TreeView<String> treeView= new TreeView<>(chosenFilesTree);

        selectedFileBrowserPane.getChildren().add(treeView);
        return true;
    }

    private void handleMouseClicked(MouseEvent event, TreeView treeView) {
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String fullPath="";
            try {
                fullPath = ((FilePathTreeItem) treeView.getSelectionModel().getSelectedItem()).getAbsolutePath();
            }catch(ClassCastException ex){System.out.println("werid exception");}
            currentObjectClickedFullPath = fullPath;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isEncrypt=true;
        folderChoosenPath="";
        setUpFileBrowser();
        chosenFilesTree.setExpanded(true);
        TreeView<String> treeView= new TreeView<>(chosenFilesTree);
        selectedFileBrowserPane.getChildren().add(treeView);

        fontSizeSelect=10.0;
        TranslationsImporter translImp = TranslationsImporterFactory.getTranslationImporter(TranslationsImporterType.TranslationsImporterENG);
        if(!translImp.translate()){
            System.out.println("Translation failed!");
            return;
        }

        view = new View(this, translImp.getTranslations());
        view.setTranslationsForGuiElements();
        view.setFonts(fontSizeSelect);
        view.setHintTextFieldVisibility(false);
    }

    public void encodeRadioClick(){
        isEncrypt=true;
        view.encodeRadioClick();
    }

    public void decodeRadioClick(){
        isEncrypt=false;
        view.decodeRadioClick();
    }

    public void encryptOrDecryptFilesClick(){
        if(chosenFilesTree.getChildren().isEmpty()) {
            view.noFilesToEncryptOrDecryptAlert();
            return;
        }
        if(folderChoosenPath.isEmpty()){
            view.folderChoosenPathEmptyAlert();
            return;
        }

        if (isEncrypt){
            encryptFiles();
        }
        else{
            decryptFiles();
        }
    }

    private void encryptFiles(){
        System.out.println("encryptFiles");
        view.encryptFilesAlert();

        startEncryptingProcedure();
    }

    private void decryptFiles(){
        System.out.println("decryptFiles");
        view.decryptFilesAlert();

        startDecryptingProcedure();
    }

    public Path chooseDestinationFolderClick(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(view.getDisplayString("directoryChooserTitle"));
        File selectedFolder = directoryChooser.showDialog(new Stage());

        if (selectedFolder != null) {
            System.out.println("selected folder");
            view.destinationFolderClick();

            folderChoosenPath = selectedFolder.toPath().toString();
            setHeadNodeName(folderChoosenPath);             //sets chosen head node name as path
            return selectedFolder.toPath();
        } else{
            return null;
        }
    }
    private void setHeadNodeName(String name)
    {
        chosenFilesTree.setValue(name);
    }

    public void addFileOrFolderClick(){
        if(currentObjectClickedFullPath == null){
            view.noObjectClicked();
            return;
        }

        Optional<ButtonType> confirmResult = view.confirmAction(currentObjectClickedFullPath);
        if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
            boolean result = true; // temporary

            if(!addPathChoice(currentObjectClickedFullPath)) //trying to add file
                result=false;

            if(result){
                view.successMsg();
            }else{
                view.cannotSelectFileMsg();
            }
        }
    }
    private void startEncryptingProcedure()
    {
        if(!chosenFilesTree.getChildren().isEmpty()) {
            Object []chosenFilesArr=chosenFilesTree.getChildren().toArray();
            for(Object chosenFile:chosenFilesArr)
            {
                String fileName=chosenFile.toString();

                FilePathTreeItem fileTree=new FilePathTreeItem(new File(fileName));
                fileTree.encryptFileTree();
            }
        }
        else{
            System.out.println("THERE ARE NO FILES IN HERE");
        }
    }

    private void startDecryptingProcedure(){
        System.out.println("Decrypting procedure. ");
    }

    private boolean checkIfNodeAlreadyAdded(String newPath)
    {
        //checking under chosen files
        Object []chosenFilesArr=chosenFilesTree.getChildren().toArray();
        for(Object chosenFile:chosenFilesArr)
        {
            FilePathTreeItem fileTree=new FilePathTreeItem(new File(chosenFile.toString()));
            if(fileTree.findPath(newPath))
                return true;

        }
        //checking under newPath file
        FilePathTreeItem fileTree=new FilePathTreeItem(new File(newPath));
        for(Object chosenFile:chosenFilesArr)
        {
            if(fileTree.findPath(chosenFile.toString()))
            {
                chosenFilesTree.getChildren().remove(chosenFile);
            }
        }
        return false;
    }

    public void chooseEnglishLanguage(){
        TranslationsImporter translImp = TranslationsImporterFactory.getTranslationImporter(TranslationsImporterType.TranslationsImporterENG);
        if(!translImp.translate()){
            System.out.println("Translation failed!");
            return;
        }
        view.setTranslationsMap(translImp.getTranslations());
        view.setTranslationsForGuiElements();
    }

    public void choosePolishLanguage(){
        TranslationsImporter translImp = TranslationsImporterFactory.getTranslationImporter(TranslationsImporterType.TranslationsImporterPL);
        if(!translImp.translate()){
            System.out.println("Translation failed!");
            return;
        }
        view.setTranslationsMap(translImp.getTranslations());
        view.setTranslationsForGuiElements();
    }

    private void setBackgroundStyle(){
        if(isEncrypt) {
            view.setBackgroundStyle("#FFFFFF");
        }
        else{
            view.setBackgroundStyle("#90EE90");
        }
    }

    public void fontSize10pxSelected(){
        fontSizeSelect=10.0;
        view.setFonts(fontSizeSelect);
        setBackgroundStyle();
    };

    public void fontSize12pxSelected(){
        fontSizeSelect=12.0;
        view.setFonts(fontSizeSelect);
        setBackgroundStyle();
    }

    public void fontSize14pxSelected(){
        fontSizeSelect=14.0;
        view.setFonts(fontSizeSelect);
        setBackgroundStyle();
    }

    public void helpMenuSelected(){
        view.helpMenuSelected();
    }

    public void showChoosenFolderPathClick(){
        view.showChoosenFolderPathClick(folderChoosenPath);
    }
    public void undoSelectionClick(){
        int last_item_index=chosenFilesTree.getChildren().size()-1;
        if(last_item_index>=0)
            chosenFilesTree.getChildren().remove(last_item_index);
    }
    public void  clearSelectionClick(){
        chosenFilesTree.getChildren().clear();
    }
    public void addHintClick(){
        view.setHintTextFieldVisibility(addHintLabel.isSelected());
    }

}
