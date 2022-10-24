package ThreadClasses;

import FileWorkers.FileCommunicator;

import java.io.IOException;

public record TextBlockChanger(FileCommunicator fileCommunicator) implements Runnable {

    @Override
    public void run() {
        try {
            fileCommunicator.insertTextInOutputFile();
            fileCommunicator.updateBlocks();
            getProgress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getProgress(){
        System.out.printf("Already checked: %.2f%% of file. Found: %d matches.\n",
                (float)fileCommunicator.getBytesAlreadyChecked() / (float)fileCommunicator.getFileSize() * 100,
                fileCommunicator.getFoundKeyWords());

//        final int valueOfTicksInProgress = (int)((float)fileCommunicator.getBytesAlreadyChecked()
//                / (float)fileCommunicator.getFileSize() * 100) / 2;
//        StringBuilder progressBar = new StringBuilder();
//        for(int i = 0; i < 50; i++){
//            if(i < valueOfTicksInProgress){
//                progressBar.append("*");
//                continue;
//            }
//            progressBar.append("-");
//        }
//        System.out.println("Already checked: " + progressBar
//                + "   Found: " + fileCommunicator.getFoundKeyWords() + " matches.");
    }
}
