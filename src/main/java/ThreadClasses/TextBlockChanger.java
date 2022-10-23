package ThreadClasses;

import FileWorkers.FileCommunicator;

import java.io.IOException;

public record TextBlockChanger(FileCommunicator fileCommunicator) implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println("Changing...");
            fileCommunicator.insertTextInOutputFile();
            fileCommunicator.updateBlocks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
