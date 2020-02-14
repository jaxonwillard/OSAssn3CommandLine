import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class shell {
    public static void main(String[] args) throws IOException {
        long totalExtProgramRunTime = 0;
        Scanner input = new Scanner(System.in);
        System.out.print(System.getProperty("user.dir") + "\n-$ ");

        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] command = line.split(" ");
            switch (command[0]) {
                case "":
                    break;
                case "exit":
                    System.exit(-1);
                case "history":
                    System.out.println("you typed history!");
                    break;
                case "cd":
                    changeDirectory(command);
                    break;
                case "ptime":
                    System.out.println(totalExtProgramRunTime);
                    break;
                default:
                    long start = System.currentTimeMillis();
                    System.out.println(runExternalCommand(command));
                    totalExtProgramRunTime += System.currentTimeMillis() - start;
                }
            System.out.print("\n" + System.getProperty("user.dir") + "\n-$ ");
        }
        input.close();
    }

    static void changeDirectory(String[] command) throws IOException {
        System.out.printf("Current Directory: %s\n", System.getProperty("user.dir"));
        String currentDir = System.getProperty("user.dir");
        File fileDir = new File(currentDir);
        System.out.printf("The parent folder is: %s\n", fileDir.getParent());
        System.out.println("fileDir.getAbsoluteFile() = " + fileDir.getAbsoluteFile());
        System.out.println("fileDir.createNewFile(\"test\") = " + fileDir.createNewFile());
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
