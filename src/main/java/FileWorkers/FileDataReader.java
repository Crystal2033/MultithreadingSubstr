package FileWorkers;

import java.io.*;

public class FileDataReader {
    private final BufferedReader bufferedReader;

    public FileDataReader(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getName() + " was not found.");
        }
        FileReader fileReader = new FileReader(file);
        bufferedReader = new BufferedReader(fileReader);
    }

    public String getNextLine() throws IOException {
        String fileLine = "";
        if (!hasNextLine()) {
            bufferedReader.close();
            return fileLine;
        }
        return bufferedReader.readLine();
    }

    public void closeReader() throws IOException {
        bufferedReader.close();
    }

    public boolean hasNextLine() throws IOException {
        return bufferedReader.ready();
    }
}
