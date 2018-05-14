package gui;

import gui.cipherModule.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FilePathTreeItem extends TreeItem<String> {
    public static Image folderCollapseImage = new Image("file:img/folder.png");
    public static Image folderExpandImage = new Image("file:img/folder-open.png");
    public static Image fileImage = new Image("file:img/text-x-generic.png");
    private final String extension = "chr";
    private final String slash = System.getProperty("os.name").startsWith("Windows") ? "\\" : "/";

    private boolean isLeaf;
    private boolean isFirstTimeChildren = true;
    private boolean isFirstTimeLeaf = true;

    private final File file;

    public File getFile() {
        return this.file;
    }

    private final String absolutePath;

    public String getAbsolutePath() {
        return this.absolutePath;
    }

    private final boolean isDirectory;

    public boolean isDirectory() {
        return this.isDirectory;
    }

    /**
     * Constructor
     * Sets up file path tree for chosen file
     * @param file chosen file to build tree for
     * */
    public FilePathTreeItem(File file) {
        super(file.toString());
        this.file = file;
        this.absolutePath = file.getAbsolutePath();
        this.isDirectory = file.isDirectory();

        if (this.isDirectory) {
            this.setGraphic(new ImageView(folderCollapseImage));
            registerEventHandlersForDirectory();
        } else {
            this.setGraphic(new ImageView(fileImage));
        }

        setValueDisplayedInTree();
    }
    /**
     * Absolute path on the head node
     * @return Absolute path on the head node
     * */
    public String toString()        //function needed to compare path
    {
        return this.absolutePath;
    }

    private void registerEventHandlersForDirectory() {
        this.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeModificationEvent<String> event) -> {
            FilePathTreeItem source = (FilePathTreeItem) event.getSource();
            if (!source.isExpanded()) {
                ((ImageView) source.getGraphic()).setImage(folderCollapseImage);
            }
        });

        this.addEventHandler(TreeItem.branchExpandedEvent(), (TreeModificationEvent<String> event) -> {
            FilePathTreeItem source = (FilePathTreeItem) event.getSource();
            if (source.isExpanded()) {
                ((ImageView) source.getGraphic()).setImage(folderExpandImage);
            }
        });
    }

    private void setValueDisplayedInTree() {
        if (file.getAbsolutePath().endsWith(File.separator))
            return;

        String value = file.toString();
        int index = value.lastIndexOf(File.separator);
        if (index > 0) {
            this.setValue(value.substring(index + 1));
        } else {
            this.setValue(value);
        }
    }

    @Override
    public ObservableList<TreeItem<String>> getChildren() {
        if (isFirstTimeChildren) {
            isFirstTimeChildren = false;

            // First getChildren() call, so we actually go off and
            // determine the children of the File contained in this TreeItem.
            super.getChildren().setAll(buildChildren(this));
        }
        return super.getChildren();
    }

    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            isLeaf = this.file.isFile();
        }
        return isLeaf;
    }

    private ObservableList<FilePathTreeItem> buildChildren(FilePathTreeItem treeItem) {
        File f = treeItem.getFile();
        if (f != null && f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                ObservableList<FilePathTreeItem> children = FXCollections.observableArrayList();

                for (File childFile : files) {
                    children.add(new FilePathTreeItem(childFile));
                }
                return children;
            }
        }
        return FXCollections.emptyObservableList();
    }
    /**
     * Checks if file/folder exists in files tree
     * @param pathToFind path to find
     * @return flase if file/folder doesn't exist
     * */
    public boolean findPath(String pathToFind) {
        System.out.println("szukam " + pathToFind + " w " + this.toString());
        if (pathToFind.equals(this.toString()))      //check if this is searched path
            return true;

        //keep digging...
        File f = this.file;
        if (f != null && f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    if (new FilePathTreeItem(childFile).findPath(pathToFind))
                        return true;
                }
            }
        }

        return false;
    }
    /**
     * Iterates through files tree, creates folders in destination folder and calls endrypt/decrypt function.
     * @param cryptoTask CryptoTask object.
     * @param mainPath Destination folder path.
     * @param localPath Local file path.
     * */
    public void encryptOrDecryptFileTree(CryptoTask cryptoTask, String mainPath, String localPath)
    {
        File f = this.file;
        String newLocalPath = "";

        if (f != null && f.isDirectory())  // this is file ->keep digging deeper...
        {
            String plik = f.getAbsolutePath();
            if (localPath.isEmpty()) {
                plik = plik.substring(plik.lastIndexOf(slash), plik.length());
            } else {
                plik = plik.substring(plik.lastIndexOf(localPath), plik.length());
            }

            //--------------
            //System.out.println("creating: "+mainPath+plik);
            Path newDirPath = Paths.get(mainPath + plik);
            try {
                if (!Files.exists(newDirPath))
                    Files.createDirectory(newDirPath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            newLocalPath = plik;
            File[] files = f.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    new FilePathTreeItem(childFile).encryptOrDecryptFileTree(cryptoTask, mainPath, newLocalPath);
                }
            }
        } else if (f != null && f.isFile())       //this is file --> encode
        {
            newLocalPath = mainPath + localPath;
            if (cryptoTask instanceof DecryptorTask && f.getName().lastIndexOf(".chr") != -1)
                // don't decrypt regular files
                encryptOrDecrypt(cryptoTask, f, newLocalPath);
            else if (cryptoTask instanceof EncryptorTask)
                encryptOrDecrypt(cryptoTask, f, newLocalPath);
        }
    }
    /**
     * Adds cryptoTask to proceed
     * @param cryptoTask CryptoTask object.
     * @param toEncrypt Path to file to encrypt.
     * @param newPathFile Destination file path.
     * */
    public void encryptOrDecrypt(CryptoTask cryptoTask, File toEncrypt, String newPathFile) {
        String name = toEncrypt.getName();
        String newFilePath = newPathFile + slash + name;
        System.out.println("--processing:" + toEncrypt + " --> " + newFilePath);
        cryptoTask.add(toEncrypt.toString(), newFilePath);
    }

    /**
     * Generates list of selected files by recursive traversal of the tree
     */
    private void generateSelectedFilesList() {
        File f = this.file;
        if (f != null && f.isDirectory())  // this is file ->keep digging deeper...
        {
            File[] files = f.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    new FilePathTreeItem(childFile).generateSelectedFilesList();
                }
            }
        } else if (f.isFile())       //this is file ->encode
        {
            selectedFilesList.add(f.toString());
        }
    }

    /**
     * Provides list of all selected files in the tree
     * @return complete list of selected files
     */
    public List<String> getSelectedFilesList() {
        selectedFilesList = new ArrayList<>();
        generateSelectedFilesList();
        return selectedFilesList;
    }

    private static List<String> selectedFilesList;

}