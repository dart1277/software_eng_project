package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

public class FilePathTreeItem extends TreeItem<String>
{
        public static Image folderCollapseImage = new Image("file:img/folder.png");
        public static Image folderExpandImage = new Image("file:img/folder-open.png");
        public static Image fileImage = new Image("file:img/text-x-generic.png");

        private boolean isLeaf;
        private boolean isFirstTimeChildren=true;
        private boolean isFirstTimeLeaf=true;

        private final File file;
        public File getFile(){return this.file;}

        private final String absolutePath;
        public String getAbsolutePath(){return this.absolutePath ;}

        private final boolean isDirectory;
        public boolean isDirectory(){return this.isDirectory;}

        public FilePathTreeItem(File file){
            super(file.toString());
            this.file=file;
            this.absolutePath=file.getAbsolutePath();
            this.isDirectory=file.isDirectory();

            if(this.isDirectory){
                this.setGraphic(new ImageView(folderCollapseImage));
                registerEventHandlersForDirectory();
            }else{
                this.setGraphic(new ImageView(fileImage));
            }

            setValueDisplayedInTree();
        }

        public String toString()        //function needed to compare path
        {
            return this.absolutePath;
        }

        private void registerEventHandlersForDirectory()
        {
            this.addEventHandler(TreeItem.branchCollapsedEvent(),(TreeModificationEvent<String> event) -> {
                    FilePathTreeItem source=(FilePathTreeItem)event.getSource();
                    if(!source.isExpanded()){
                        ((ImageView)source.getGraphic()).setImage(folderCollapseImage);
                    }
            } );

            this.addEventHandler(TreeItem.branchExpandedEvent(), (TreeModificationEvent<String> event) -> {
                FilePathTreeItem source = (FilePathTreeItem)event.getSource();
                if(source.isExpanded()){
                    ((ImageView)source.getGraphic()).setImage(folderExpandImage);
                }
            });
        }

        private void setValueDisplayedInTree()
        {
            if(file.getAbsolutePath().endsWith(File.separator))
                return;

            String value = file.toString();
            int index = value.lastIndexOf(File.separator);
            if(index > 0){
                this.setValue(value.substring(index + 1));
            }else{
                this.setValue(value);
            }
        }

        @Override
        public ObservableList<TreeItem<String>> getChildren(){
            if(isFirstTimeChildren){
                isFirstTimeChildren=false;

                // First getChildren() call, so we actually go off and
                // determine the children of the File contained in this TreeItem.
                super.getChildren().setAll(buildChildren(this));
            }
            return super.getChildren();
        }

        @Override
        public boolean isLeaf(){
            if(isFirstTimeLeaf){
                isFirstTimeLeaf=false;
                isLeaf=this.file.isFile();
            }
            return isLeaf;
        }

        private ObservableList<FilePathTreeItem> buildChildren(FilePathTreeItem treeItem){
            File f=treeItem.getFile();
            if(f!=null && f.isDirectory()){
                File[] files=f.listFiles();
                if (files != null){
                    ObservableList<FilePathTreeItem> children = FXCollections.observableArrayList();

                    for(File childFile : files){
                        children.add(new FilePathTreeItem(childFile));
                    }
                    return children;
                }
            }
            return FXCollections.emptyObservableList();
        }

        public boolean findPath(String pathToFind)
        {
            //System.out.println("szukam "+pathToFind+" w "+this.toString());
            if(pathToFind.equals(this.toString()))      //check if this is searched path
                return true;

            //keep digging...
            File f=this.file;
            if(f!=null && f.isDirectory())
            {
                File[] files=f.listFiles();
                if (files != null)
                {
                    for(File childFile : files)
                    {
                        if(new FilePathTreeItem(childFile).findPath(pathToFind))
                            return true;
                    }
                }
            }

            return false;
        }
        public void encryptFileTree()
        {

            File f=this.file;
            if(f!=null && f.isDirectory())  // this is file ->keep digging deeper...
            {
                File[] files=f.listFiles();
                if (files != null)
                {
                    for(File childFile : files)
                    {
                        new FilePathTreeItem(childFile).encryptFileTree();
                    }
                }
            }
            else if(f.isFile())       //this is file ->encode
            {
                encrypt(f.toString());
            }
        }
        public void encrypt(String path)        //encrypting function
        {
            //encrypt here
        }

    }