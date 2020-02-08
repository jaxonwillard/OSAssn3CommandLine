import java.lang.Process;
import java.util.concurrent.TimeUnit;
import java.lang.ProcessBuilder;
public class inclass {
    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        System.out.printf("Current dir: %s\n", currentDir);
        java.nio.file.Path proposed = java.nio.file.Paths.get(currentDir, "test");
        System.out.printf("Proposed Dir: %s\n", proposed);
        System.setProperty("user.dir", proposed.toString());

        if (proposed.toFile().isDirectory()) {

            String newDir = System.getProperty("user.dir");
            System.out.printf("New directory: " + newDir);
        }

        String[] command = {"vim", "process.java"};
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);


        try{
            System.out.println("about to start the process...");
            Process p = pb.start();
            System.out.println(p.getOutputStream());
            p.waitFor();
            System.out.println("process finished");
        }
        catch (Exception e){
            System.out.println(e + " happened");
        }


    }
}
