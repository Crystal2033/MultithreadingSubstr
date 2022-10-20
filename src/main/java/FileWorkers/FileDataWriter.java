package FileWorkers;

import java.io.*;
import java.util.List;

public class FileDataWriter {
    private final BufferedWriter bufferedWriter;

    public FileDataWriter(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }
        FileWriter fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);

    }

    public void writeLine(String line) throws IOException {
        bufferedWriter.write(line);
        bufferedWriter.newLine();
    }

    public void writeLines(List<String> lines) throws IOException {
        for (String line : lines) {
            writeLine(line);
        }
    }

    public void closeWriter() throws IOException {
        bufferedWriter.close();
    }
}
