import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class shell {
    public static void main(String[] args) throws IOException {
        long totalExtProgramRunTime = 0;
        Scanner input = new Scanner(System.in);
        System.out.print(System.getProperty("user.dir") + "\n-$ ");
        ArrayList<String[]> history = new ArrayList<>();

        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] command = splitCommand(line);
            history.add(command);
            totalExtProgramRunTime += processCommand(command, history, totalExtProgramRunTime);

            System.out.print("\n" + System.getProperty("user.dir") + "\n-$ ");
        }
        input.close();
    }

    static long processCommand(String[] command, ArrayList<String[]> history, long totalExtProgramRunTime) throws IOException {
        long localRunTime = 0;
        switch (command[0]) {
            case "":
                break;
            case "exit":
                System.exit(-1);
            case "mdir":
                mdir(command);
                break;
            case "rdir":
                rdir(command);
                break;
            case "history":
                history(history);
                break;
            case "^":
                queryHistory(Integer.parseInt(command[1]), history, totalExtProgramRunTime);
                break;
            case "cd":
                changeDirectory(command);
                break;
            case "list":
                list();
                break;
            case "ptime":
                System.out.println(totalExtProgramRunTime);
                break;
            default:
                long start = System.currentTimeMillis();
                System.out.println(runExternalCommand(command));
                localRunTime += System.currentTimeMillis() - start;
        }
        return localRunTime;
    }

    static void history(ArrayList<String[]> history){
        for (int i=1; i<history.size()+1; i++){
            System.out.printf("%d: %s\n", i, formatArray(history.get(i-1)));
        }
    }

    static String formatArray(String[] array){
        StringBuilder sb = new StringBuilder();
        for (String s : array){
            sb.append(s).append(" ");
        }
        return sb.toString();
    }

    static void queryHistory(int queryIndex, ArrayList<String[]> history, long totalExtProgramRunTime) throws IOException {
        if (queryIndex > history.size()+1) return;
        processCommand(history.get(queryIndex-1), history, totalExtProgramRunTime);
    }

    static void list() {
        File fileDir = new File(System.getProperty("user.dir"));
        for (int i = 0; i< Objects.requireNonNull(fileDir.list()).length; i++){
            System.out.println(processFile(new File(Objects.requireNonNull(fileDir.list())[i])));
        }
    }

    static String processFile(File child){
        StringBuilder tr = new StringBuilder();
        String permissions = (child.isDirectory() ? "d" : "-" ) +
                (child.canRead() ? "r" : "-" ) +
                (child.canWrite() ? "w" : "-" ) +
                (child.canExecute() ? "x" : "-" );
        String length = String.format("%10d", child.length());
        Date lastModified = new Date(child.lastModified());

        tr.append(permissions).append(" ").append(length).append(" ").append(lastModified).append(" ").append(child);

        return tr.toString();
    }

    static void changeDirectory(String[] command) throws IOException {
        String currentDir = System.getProperty("user.dir");
        File fileDir = new File(currentDir);
        if (command.length == 2 && command[1].equals("..")){
            System.setProperty("user.dir", fileDir.getParent());
        }
        else if (command.length == 1){
            File homeFile = new File(System.getProperty("user.home"));
            // System.setProperty(currentDir)
            java.nio.file.Path proposed = java.nio.file.Paths.get(String.valueOf(homeFile));
            System.setProperty("user.dir", String.valueOf(homeFile));

        }
        else if (command.length == 2) {
            if (dirInList(command[1])){
                java.nio.file.Path proposed = java.nio.file.Paths.get(String.valueOf(fileDir), command[1]);
                File proposedFile = new File(String.valueOf(proposed));
                System.setProperty("user.dir", String.valueOf(proposedFile));

            }


        }
    }

    static boolean dirInList(String dir){
        File fileDir = new File(System.getProperty("user.dir"));
        for (String contents : Objects.requireNonNull(fileDir.list())){
            if (dir.equals(contents)) return true;
        }
        return false;
    }

    static String runExternalCommand(String[] command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(System.getProperty("user.dir")));
        pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        try {
            Process p = pb.start();
            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String readerLine;
            StringBuilder toReturn = new StringBuilder();
            while ((readerLine = reader.readLine()) != null) {
                toReturn.append(readerLine);
                toReturn.append("\n");
            }
            return toReturn.toString();

        } catch (Exception e) {

        }
        return String.format("Error: %s command not found", command[0]);
    }

    /**
     * Split the user command by spaces, but preserving them when inside double-quotes.
     * Code Adapted from: https://stackoverflow.com/questions/366202/regex-for-splitting-a-string-using-space-when-not-surrounded-by-single-or-double
     */
    public static String[] splitCommand(String command) {
        java.util.List<String> matchList = new java.util.ArrayList<>();

        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(command);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                matchList.add(regexMatcher.group(2));
            } else {
                // Add unquoted word
                matchList.add(regexMatcher.group());
            }
        }

        return matchList.toArray(new String[matchList.size()]);
    }

    public static void pipe(String[] command){
        
    }

    public static void mdir(String[] command) throws IOException {
        if (command.length <= 1) return;
        String currentDir = System.getProperty("user.dir");
        java.nio.file.Path proposed = java.nio.file.Paths.get(currentDir, command[1]);
        System.setProperty("user.dir", proposed.toString());
        File newFile = new File(System.getProperty("user.dir"));
        if (!newFile.mkdir()) System.out.printf("Error: %s is already directory\n", command[1]);
        else{
        changeDirectory(new String[]{"cd", ".."});}
    }

    public static void rdir(String[] command) throws IOException {
        if (command.length <= 1) return;
        if (!dirInList(command[1])) {
            System.out.printf("Error: %s is not a directory", command[1]);
        }
        String currentDir = System.getProperty("user.dir");
        java.nio.file.Path proposed = java.nio.file.Paths.get(currentDir, command[1]);
        System.setProperty("user.dir", proposed.toString());
        File newFile = new File(System.getProperty("user.dir"));
        newFile.delete();
        changeDirectory(new String[]{"cd", ".."});

    }

}
