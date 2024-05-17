package Server;

import Entity.Message;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The ActivityController class controls the writing and reading to the log file.
 */
public class ActivityController {

    /**
     * Writing to the log file.
     * @param message contains the activity which has occurred
     */
    public void writeToLogFile(Message message) {
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

    /**
     * Reads from the log file.
     * @return the list of messages to be printed.
     */
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
