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
            if (this.fos != null)
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

    public boolean addNextPrimitive(String path, String dst) {
        if (this.fos != null)
            closeOutputFile();
        file_list.clear();
        fileToSave = new File(dst);
        if (fileToSave.exists()) return false;
        file_list.add(new File(path));
        return true;

    }

    public File getNextPrimitive() {
        if (this.hasNext()) {
            current_file = file_list.get(0);
            bytesSaved = 0;

            try {
                fos = new FileOutputStream(fileToSave);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage() + " File not found primitive");
            }
            return current_file;
        }

        return null;
    }

    public File getNextPrimitiveNoStream() {
        if (this.hasNext()) {
            current_file = file_list.get(0);
            bytesSaved = 0;
            return current_file;
        }

        return null;
    }

    public void cleanBrokenDestination() {
        if (this.fos != null)
            this.closeOutputFile();
        if (this.fileToSave.exists()) {
            this.fileToSave.delete();
        }
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

    public void saveNextBytes(byte[] bytesToSave) throws IOException {
        bytesSaved += bytesToSave.length;
        try {
            fos.write(bytesToSave);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }
}
