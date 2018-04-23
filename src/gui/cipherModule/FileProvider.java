package gui.cipherModule;

import java.util.*;
import java.io.*;


public class FileProvider implements FileProviderInterface {

    private ArrayList<File> file_list = new ArrayList<>();
    private int bytesSaved;
    private Iterator<File> iter;
    private File current_file;
    private File fileToSave;
    private File destDir;
    private String destName;
    private FileOutputStream fos;

    public void closeOutputFile() {
        try {
            fos.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public FileProvider(ArrayList<String> files_path) {
        for (String path : files_path) {
            file_list.add(new File(path));
        }
        iter = file_list.iterator();
        this.destName = "CopyFolder/";
    }

    public FileProvider(ArrayList<String> files_path, String destDriName) {
        for (String path : files_path) {
            file_list.add(new File(path));
        }
        iter = file_list.iterator();
        this.destName = destDriName;
    }

     public File getNext() {
        if (this.hasNext()) {
            current_file = iter.next();
            bytesSaved = 0;
            fileToSave = new File(this.destName + current_file.getPath());
            destDir = fileToSave.getParentFile();
            destDir.mkdirs();

            try {
                fos = new FileOutputStream(fileToSave);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage() + " File not found");
            }
            return current_file;
        }

        return null;
    }

    public boolean hasNext() {
        return iter.hasNext();
    }

    public void saveNextBytes(byte[] bytesToSave) {
        bytesSaved += bytesToSave.length;
        try {
            fos.write(bytesToSave);
        } catch (IOException | NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }
}