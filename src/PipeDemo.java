import java.lang.*;

public class PipeDemo {

        public static void main(String[] args) {
                String[] p1Cmd = { "ls", "-l", "-a"};
                String[] p2Cmd = { "grep", "4096" };

                ProcessBuilder pb1 = new ProcessBuilder(p1Cmd);
                ProcessBuilder pb2 = new ProcessBuilder(p2Cmd);

                pb1.redirectInput(ProcessBuilder.Redirect.INHERIT);
                // pb1.redirectOutput(ProcessBuilder.Redirect.PIPE);

                pb2.redirectOutput(ProcessBuilder.Redirect.INHERIT);

                try {
                        Process p1 = pb1.start();
                        Process p2 = pb2.start();

                        java.io.InputStream in = p1.getInputStream();
                        java.io.OutputStream out = p2.getOutputStream();

                        int c;
                        while ((c = in.read()) != -1) {
                            // System.out.println();
                            out.write(c);
                                
                        }
                        System.out.println(out);

                        out.flush();
                        out.close();

                        p1.waitFor();
                        p2.waitFor();
                }
                catch (Exception ex) {
                }
        }

}
