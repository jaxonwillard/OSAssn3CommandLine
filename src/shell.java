import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



public class shell {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.print(System.getProperty("user.dir") + "\n$ ");
        while(input.hasNextLine()){
            runCommand(input.nextLine());
            System.out.print(System.getProperty("user.dir") + "\n$ ");


        }
        input.close();

    }
    static void runCommand(String line){
        String[] command = line.split(" ");
        for (int i=0; i<command.length; i++){
            System.out.printf("Param %d: %s\n", i, command[i]);
        }
    }
    
}
