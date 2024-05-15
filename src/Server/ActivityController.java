// ActivityController.java
package Server;

import Entity.Message;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ActivityController {

    public static void LogFile(Message message) {
        try (FileWriter fileWriter = new FileWriter("logger.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            bufferedWriter.write(timestamp + " - " + message);
            bufferedWriter.newLine();

        } catch (IOException e) {
            System.out.println("Error occurred while writing to file: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static List<String> readLogFile() {
        List<String> logMessages = new ArrayList<>();

        try (Scanner myReader = new Scanner(new File("logger.txt"))) {
            while (myReader.hasNextLine()) {
                logMessages.add(myReader.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the log file: " + e.getMessage());
            e.printStackTrace();
        }

        Collections.sort(logMessages); //Sort the log messages by timestamp
        return logMessages;
    }

}
