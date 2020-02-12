import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class shell {
    public static void main(String[] args) throws IOException {
        long totalExtProgramRunTime = 0;
        Scanner input = new Scanner(System.in);
        System.out.print(System.getProperty("user.dir") + "\n-$ ");

        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (line.equals("ptime"))
                System.out.println(totalExtProgramRunTime);
            else {
                totalExtProgramRunTime += runCommand(line);
            }
            System.out.print("\n" + System.getProperty("user.dir") + "\n-$ ");

        }
        input.close();
    }

    static long runCommand(String line) {
        long runTime = 0;
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
            changeDirectory();
            break;
        default:
            long start = System.currentTimeMillis();
            System.out.println(runExternalCommand(command));
            long total = System.currentTimeMillis() - start;
            runTime = total;

        }
        return runTime;
    }

    static void changeDirectory() {
        System.out.printf("Current Directory: %s\n", System.getProperty("user.dir"));
        String currentDir = System.getProperty("user.dir");
        File fileDir = new File(currentDir);
        System.out.printf("The parent folder is: %s\n", fileDir.getParent());
    }

    static String[] truncate(String[] list) {
        String[] toReturn = new String[list.length - 1];
        for (int i = 0; i < list.length - 1; i++) {
            toReturn[i] = list[i];
        }

        return toReturn;
    }

    static boolean hasPtime(String[] command) {
        for (String param : command) {
            if (param.equals("ptime"))
                return true;
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
}
