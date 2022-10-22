package ThreadClasses;

import FileWorkers.FileCommunicator;

import java.io.IOException;

public record TextBlockChanger(FileCommunicator fileCommunicator) implements Runnable {

    @Override
    public void run() {
        try {
            fileCommunicator.updateBlocks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
