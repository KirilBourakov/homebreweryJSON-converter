package helpers.ocr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class OcrController {
    private static String pathToPython;

    public static ArrayList<String> read(String filepath){
        ProcessBuilder processBuilder = new ProcessBuilder(pathToPython, "helpers/ocr/scripts/reader.py", filepath);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            String line;
            ArrayList<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            int exitCode = process.waitFor();
            return lines;
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void setup(){
        ProcessBuilder processBuilder = new ProcessBuilder("python", "helpers/ocr/scripts/setup.py");

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (isFile(line)) {
                    pathToPython = line;
                }
            }
            
            // Wait for the process to complete
            // TODO: handle errors, and clean up how python treats them
            int exitCode = process.waitFor();    
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean isFile(String pathString) {
        Path path = Paths.get(pathString);
        return Files.isRegularFile(path);
    }
    
}
