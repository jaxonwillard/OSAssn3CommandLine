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
        String permissions = (child.isDirectory() ? "t" : "f" ) +
                (child.canRead() ? "t" : "f" ) +
                (child.canWrite() ? "t" : "f" ) +
                (child.canExecute() ? "t" : "f" );
        String length = String.format("%10d", child.length());
        Date lastModified = new Date(child.lastModified());

        tr.append(permissions).append(" ").append(length).append(" ").append(lastModified).append(" ").append(child);

        return tr.toString();
    }

    static void changeDirectory(String[] command) throws IOException {
        System.out.printf("Current Directory: %s\n", System.getProperty("user.dir"));
        String currentDir = System.getProperty("user.dir");
        File fileDir = new File(currentDir);
        System.out.printf("The parent folder is: %s\n", fileDir.getParent());
        System.out.println("fileDir.getAbsoluteFile() = " + fileDir.getAbsoluteFile());
        System.out.println("fileDir.createNewFile(\"test\") = " + fileDir.createNewFile());
        System.out.println("fileDir.list() = " + Arrays.toString(fileDir.list()));


        System.out.println("Arrays.toString(fileDir.list()) = " + Arrays.toString(fileDir.list()));
        java.nio.file.Path proposed = java.nio.file.Paths.get(String.valueOf(fileDir), ".git");
        File proposedFile = new File(String.valueOf(proposed));
        System.out.println("proposedFile.isDirectory() = " + proposedFile.isDirectory());
        System.out.println("proposedFile.isFile() = " + proposedFile.isFile());


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


}
