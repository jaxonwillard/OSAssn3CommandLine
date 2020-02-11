import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.File;

public class ProcessExample1 {
    public static void main(String[] args) {

        execProcess();
    }

    public static void execProcess() {
        System.out.printf("Current Directory: %s\n", System.getProperty("user.dir"));
        String currentDir = System.getProperty("user.dir");
        File fileDir = new File(currentDir);
        System.out.printf("The parent folder is: %s\n", fileDir.getParent());

        // java.nio.file.Path proposed = java.nio.file.Paths.get(currentDir, "test");
        // System.setProperty("user.dir", proposed.toString());
        // System.out.printf("Updated Directory: %s\n", System.getProperty("user.dir"));

        // String[] command = {"nano", "ProcessExample.java"};
        String[] command = {"ls"};

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(System.getProperty("user.dir")));
        pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);



        try {
            Process p = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }





            long start = System.currentTimeMillis();
            System.out.println("Starting to wait");
            long end = System.currentTimeMillis();
            System.out.printf("Waited for %d milliseconds\n", end - start);
        }
        catch (IOException ex) {
            System.out.println("Illegal command \n" + ex);
        }
        catch (Exception ex) {
            System.out.println("Something else bad happened");
        }
    }
}

//
// Other Info
// String currentDir = System.getProperty("user.dir");
// java.nio.file.Path proposed = java.nio.file.Pathas.get(currentDir, "somefolder");
// if (proposed.toFile().isDirectory()) { it is a valid directory }
