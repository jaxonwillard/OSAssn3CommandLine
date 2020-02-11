import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



public class shell {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.print(System.getProperty("user.dir"));
//        String command = input.nextLine();
        while(input.hasNextLine()){
            runCommand(input.nextLine());


        }
        input.close();
        ArrayList<String> hi = new ArrayList<>();
    }
    static void runCommand(String command){
        System.out.println("you ran a command");
    }
}
