import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;



public class shell {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.print(System.getProperty("user.dir") + "\n-$ ");

        while(input.hasNextLine()){
            runCommand(input.nextLine());
            System.out.print(System.getProperty("user.dir") + "\n-$ ");
        }

        input.close();
    }

    static void runCommand(String line){
        String[] command = line.split(" ");
        switch (command[0]){
            case "":
                break;
            case "exit" :
                System.exit(-1);
            case "history":
                System.out.println("you typed history!");
                break;
            default :
                if (hasPtime(command)){
                long start = System.currentTimeMillis();
                System.out.println(runExternalCommand(truncate(command)));
                System.out.println(System.currentTimeMillis() - start);
                }
                else{
                    System.out.println(runExternalCommand(command));
                }
        }
    }
    static String[] truncate(String[] list){
        String[] toReturn = new String[list.length-1];
        for (int i=0; i<list.length-1; i++){
            toReturn[i] = list[i];
        }

        return toReturn;
    }

    static boolean hasPtime(String[] command){
        for (String param : command){
            if (param.equals("ptime")) return true;
        }
        return false;
    }
    static String runExternalCommand(String[] command){
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(System.getProperty("user.dir")));
        pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        try{
            Process p = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String readerLine;
            StringBuilder toReturn = new StringBuilder();
            while((readerLine = reader.readLine()) != null) {
                toReturn.append(readerLine);
                toReturn.append("\n");
            }
            return toReturn.toString();

        }catch(Exception e){
            System.out.println(e);
        }
        return "error in runExternalCommand()";
    }
}

