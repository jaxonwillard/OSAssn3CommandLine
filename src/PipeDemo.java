//
//import java.io.IOException;
//import java.util.*;
//
//public class ProcessBuilderExample
//{
//        public static void main(String[] args)
//                throws IOException, InterruptedException
//        {
//                // you need a shell to execute a command pipeline
//                List<String> commands = new ArrayList<String>();
//                commands.add("/bin/sh");
//                commands.add("-c");
//                commands.add("ls -l /var/tmp | grep foo");
//
//                SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
//                int result = commandExecutor.executeCommand();
//
//                StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
//                StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();
//
//                System.out.println("STDOUT");
//                System.out.println(stdout);
//
//                System.out.println("STDERR");
//                System.out.println(stderr);
//        }
//}