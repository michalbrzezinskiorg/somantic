package Persistence;

import MainProgram.Interface;
import WNprocess.SomanticRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Persistence {

    private static boolean saving = false;

    public static void save(SomanticRepository repository) {
        File f = new File(Interface.getLibraryFile());
        if(!f.exists())
            try {
                f.setWritable(true);
                f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!saving) {
            try {
                saving = true;
                FileOutputStream fileOut = new FileOutputStream(Interface.getLibraryFile());
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(repository);
                out.close();
                System.out.println("saved " + fileOut.getClass() + " to file " + Interface.getLibraryFile());
                fileOut.close();
            } catch (IOException i) {
                System.err.println("saving error " + i.getMessage());
                i.printStackTrace();
            }
            saving = false;
        }
    }

    public static SomanticRepository load() {
        SomanticRepository riTaRepo = null;
        File f = new File(Interface.getLibraryFile());
        if (f.exists()) {
            try {
                FileInputStream fileIn = new FileInputStream(Interface.getLibraryFile());
                ObjectInputStream in = new ObjectInputStream(fileIn);
                riTaRepo = (SomanticRepository) in.readObject();
                in.close();
                fileIn.close();
                System.out.println("loaded repo successfully from file" + Interface.getLibraryFile());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(" error " + e.getLocalizedMessage());
            }
        }
        return riTaRepo;
    }

    public static void saveNewLineInFile(String line) {
        Path pathText = null;
        Charset encoding = Charset.forName("UTF-8");
        try {
            pathText = Paths.get(Interface.getGeneratedSentencesFilePath());
        } catch (Exception e) {
            System.err.println(e);
        }
        if (pathText != null) {
            List<String> newline = new ArrayList<>();
            File file = pathText.toFile();
            if (!file.exists()) {
                try {
                    file.setWritable(true);
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    newline.addAll(Files.readAllLines(pathText, encoding));
                } catch (IOException ex) {
                    Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            newline.add(line);
            try {
                Files.write(pathText, newline, encoding);
            } catch (IOException ex) {
                Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
