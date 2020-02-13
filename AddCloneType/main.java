
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;


public class main {
    public static void main(String[] args) throws IOException {

        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> sim = new ArrayList<>();
        File file = new File("SelectedClones.csv");
        Scanner sc = new Scanner(file);

//        while (sc.hasNextLine()) {
//            String s = sc.nextLine();
////            System.out.println(s);
//            list.add(s);
//        }
        int counter = 0;
//        for (int i = 0; i < list.size(); i++) {
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            String[] del = s.split(",");
            // Create a new process and run the command
            String[] command = new String[] {"psql", "postgres"};
// Can also directly be put into the process builder as an argument without it being in an array
            ProcessBuilder builder = new ProcessBuilder(command);
            Process process = builder.start();

            OutputStream stdin = process.getOutputStream();
            InputStream stdout = process.getInputStream();
            InputStream stderr = process.getErrorStream();

// Store the input and output streams in a buffer
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
            BufferedReader error = new BufferedReader(new InputStreamReader(stderr));

// Send input
            writer.write("select clones.similarity_token from" +
                    "(select functions.id from functions where functions.name='"+ del[0] +
                    "' AND functions.startline='" + del[1] + "'and functions.endline='" + del[2] + "') AS A," +
                    "    (select functions.id from functions where functions.name='" + del[4] + "' AND functions.startline='" + del[5] + "' and functions.endline='" + del[6] + "') AS B," +
                    "        clones where clones.function_id_one = A.id AND clones.function_id_two = B.id;\n");
            // Don't forget the '\n' here, otherwise it'll continue to wait for input
            writer.flush();
            writer.close(); // Add if doesn't work without it

// Display the output
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null){
//            System.out.println(line);
                count++;
                if(count == 3 ){
                    if(Double.parseDouble(line)==1){
                        sim.add(s + "," + "Type-1+2");
                    }
                    else if(Double.parseDouble(line)>=0.7){
                        sim.add(s + "," + "StronglyType-3");
                    }
                    else if(Double.parseDouble(line)>=0.5){
                        sim.add(s + "," + "ModeratelyType-3");
                    }
                    else if(Double.parseDouble(line)>=0.0){
                        sim.add(s + "," + "WeaklyType-3+4");
                    }
                }
            }
// Display any errors
//            while ((line = error.readLine()) != null) System.out.println(line);
            counter++;
            if(counter%100==0){
                System.out.println("counter : "+ counter);
            }
        }

        FileWriter f = new FileWriter("SelectedClonesWithType.csv");
        for (int i = 0; i < sim.size(); i++) {
            f.write(sim.get(i) +"\n");
        }
        f.close();
    }

}
