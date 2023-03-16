import java.awt.Desktop;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;

public class App extends JFrame{
    private static JButton saveClose;
    private static JPanel panel = new JPanel();
    static File filePaths = new File("data\\FilePaths.txt");
    static String gamePath, savePath;
    static Path localPath = Paths.get(savePath);
    static Path gitPath = Paths.get("data\\GitSave\\GitSave.sav");
    static long commitTime;

    public static void main(String[] args) throws Exception  {
        // Creates paths to the git repo and the save file in the git repo and the backups folder in the git repo
        Path gitRepo = Paths.get("data\\GitRepoSaves\\SatisfactorySaves");
        Path gitRepoSave = Paths.get("data\\GitRepoSaves\\SatisfactorySaves\\SaveFile\\saveFile.sav");

        // Pulls the latest version of the git repo
        Git.gitPull(gitRepo);

        try (// Creates a scanner to read the file paths from the file
            Scanner sc = new Scanner(filePaths)) {
            while(sc.hasNextLine()) {
                String[] line = sc.nextLine().split(":|\\s");
                if(line[0].equals("GamePath")) {
                    gamePath = line[1];
                } else if(line[0].equals("SavePath")) {
                    savePath = line[1];
                }
            }
        }

        // Creates a path to the save file in the game folder and the save file in the git repo
        BasicFileAttributes localAttr = Files.readAttributes(localPath, BasicFileAttributes.class);
        String localSaveTime = localAttr.lastModifiedTime().toString();

        String[] splitpSaveTime = localSaveTime.split("-|;|T|\\.|:"); //[]= {year, month, day, hour, minute, second}
        Save saveLocal = new Save(Integer.parseInt(splitpSaveTime[0]), Integer.parseInt(splitpSaveTime[1]), Integer.parseInt(splitpSaveTime[2]), Integer.parseInt(splitpSaveTime[3]),Integer.parseInt(splitpSaveTime[4]),Integer.parseInt(splitpSaveTime[5]), localPath);

        BasicFileAttributes gitAttr = Files.readAttributes(gitPath, BasicFileAttributes.class);
        String gitSaveTime = gitAttr.lastModifiedTime().toString();

        String[] splitGitSaveTime = gitSaveTime.split("-|;|T|\\.|:"); //[]= {year, month, day, hour, minute, second}
        Save saveGit = new Save(Integer.parseInt(splitGitSaveTime[0]), Integer.parseInt(splitGitSaveTime[1]), Integer.parseInt(splitGitSaveTime[2]), Integer.parseInt(splitGitSaveTime[3]),Integer.parseInt(splitGitSaveTime[4]),Integer.parseInt(splitGitSaveTime[5]), gitPath);
        
        Save[] saves = new Save[2];
        saves[0] = saveLocal;
        saves[1] = saveGit;
        arraySort(saves);

        if(saves[1] == saveGit) {
            Files.copy(gitRepoSave, localPath, StandardCopyOption.REPLACE_EXISTING);   
        }
       
        
        // Opens Game
        //Desktop.getDesktop().open(new File(gamePath));

        // Creates a JFrame to close the program
        JFrame frame = new JFrame("Satisfactory Save Manager");
        frame.setPreferredSize(new Dimension(300, 300));
        frame.setVisible(true);

        // Creates a window listener to save the save file to the git repo and close the program
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    saveToGithub();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } finally {
                    System.exit(0);
                }
                
            }
        });

        // Creates a button to save the save file to the git repo and close the program
        panel = new JPanel();
        saveClose = new JButton("Save and Close");
        saveClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    saveToGithub();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } finally {
                    System.exit(0);
                }
                
            }
        });
        panel.add(saveClose);
        frame.add(panel);
        frame.pack();
       
    }
    @Override
    public void dispose() {
        try {
            saveToGithub();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.exit(0);
    }
    public static void saveToGithub() throws Exception {
        // if git commit made in last 10 minutes, dont make another commit
        if() {
            System.exit(0);
        }

        System.out.println("Saving to Github");
        // Creates paths to the git repo and the save file in the git repo and the backups folder in the git repo
            Path gitRepo = Paths.get("data\\GitRepoSaves\\SatisfactorySaves"); 
            Path gitRepoSave = Paths.get("data\\GitRepoSaves\\SatisfactorySaves\\SaveFile\\saveFile.sav");
            Path gitRepoBackups = Paths.get("data\\GitRepoSaves\\SatisfactorySaves\\Backups\\saveFile" + "" + ".sav");

            // Creates paths to the save files
            

            // Creates a Save object for each save file
            // Localattr and gitattr are used to get the last modified time of the save files
            // splitpSaveTime and splitGitSaveTime are used to split the last modified time into an array of strings for each part of the time
            // Save is a class that stores the last modified time of the save file and the path to the save file
            BasicFileAttributes localAttr = Files.readAttributes(localPath, BasicFileAttributes.class);
            String localSaveTime = localAttr.lastModifiedTime().toString();

            String[] splitpSaveTime = localSaveTime.split("-|;|T|\\.|:"); //[]= {year, month, day, hour, minute, second}
            Save saveLocal = new Save(Integer.parseInt(splitpSaveTime[0]), Integer.parseInt(splitpSaveTime[1]), Integer.parseInt(splitpSaveTime[2]), Integer.parseInt(splitpSaveTime[3]),Integer.parseInt(splitpSaveTime[4]),Integer.parseInt(splitpSaveTime[5]), localPath);

            BasicFileAttributes gitAttr = Files.readAttributes(gitPath, BasicFileAttributes.class);
            String gitSaveTime = gitAttr.lastModifiedTime().toString();

            String[] splitGitSaveTime = gitSaveTime.split("-|;|T|\\.|:"); //[]= {year, month, day, hour, minute, second}
            Save saveGit = new Save(Integer.parseInt(splitGitSaveTime[0]), Integer.parseInt(splitGitSaveTime[1]), Integer.parseInt(splitGitSaveTime[2]), Integer.parseInt(splitGitSaveTime[3]),Integer.parseInt(splitGitSaveTime[4]),Integer.parseInt(splitGitSaveTime[5]), gitPath);
            
            Save[] saves = new Save[2];
            saves[0] = saveLocal;
            saves[1] = saveGit;
            arraySort(saves);
            

            // If the local save is newer than the git save, copy the local save to the git repo and commit it
            // If the git save is newer than the local save, copy the git save to the git repo and commit it
            if(saves[1] == saveLocal) {
            try{
                Files.copy(saveLocal.getPath(), gitRepoSave, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(saveLocal.getPath(), gitRepoBackups, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
                Git.gitStage(gitRepo);
                System.out.println("GitStaged");
                Git.gitCommit(gitRepo, localAttr.lastModifiedTime().toString());
                System.out.println("GitCommitted");
                Git.gitPush(gitRepo);
                System.out.println("GitPushed");
                commitTime = System.currentTimeMillis();
            }
            List<String> lines = Files.readAllLines(file.toPath());
            lines.set(4, "LastCommitTime:" + commitTime);
            Files.write(filePaths.toPath(), lines);

    }

    public static Save[] arraySort(Save[] saves) {
        // Sorts the saves by last modified time
        Comparator<Save> saveComparator = Comparator.comparing(Save :: getYear)
        .thenComparing(Save :: getMonth)
        .thenComparing(Save :: getDay)
        .thenComparing(Save :: getHour)
        .thenComparing(Save :: getMinute)
        .thenComparing(Save :: getSecond);
        Arrays.sort(saves, saveComparator);  
        return saves;
    }
}
