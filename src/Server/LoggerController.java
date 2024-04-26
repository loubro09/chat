package Server;

import Entity.Message;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class LoggerController {

    public static void LogFile(Message message) {
        try (FileWriter fileWriter = new FileWriter("logger.txt", true);

             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            bufferedWriter.write(String.valueOf(message));
            bufferedWriter.newLine();

        } catch (IOException e) {
            System.out.println("Error occurred while writing to file: " + e.getMessage());
            e.printStackTrace();
        }

    }
    public String[] readFile() {
        String[] stringArray = new String[100];
        int i = 0;

        try {
            File logger = new File("logger.txt");
            Scanner myReader = new Scanner(logger);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                stringArray[i] = data;
                i++;
            }

            myReader.close();
            stringArray = Arrays.copyOf(stringArray, i);

            // Sortera arrayen baserat på poäng
            Arrays.sort(stringArray);

            // Ta bort null-värden från arrayen
            String[] nonNullArray = Arrays.stream(stringArray)
                    .filter(line -> line != null && !line.trim().isEmpty())
                    .toArray(String[]::new);


        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. ");
            e.printStackTrace();
        }

        return new String[0]; // Returnera en tom array om något går fel
    }


}
