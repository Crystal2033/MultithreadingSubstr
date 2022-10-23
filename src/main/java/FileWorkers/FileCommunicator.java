package FileWorkers;

import TextHelpers.TextBlock;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

import static Settings.CONSTANTS.VALUE_OF_LINES_IN_BLOCK;

public class FileCommunicator {
    private final FileDataReader fileDataReader;
    private final FileDataWriter fileDataWriter;
    private final TextBlock prevBlock, currentBlock, nextBlock;
    private final Queue<TextBlock> queueForOutputText;

    public FileCommunicator(String inputFilename, String outputFileName) throws IOException {
        fileDataReader = new FileDataReader(inputFilename);
        fileDataWriter = new FileDataWriter(outputFileName);
        prevBlock = new TextBlock();
        currentBlock = new TextBlock();
        nextBlock = new TextBlock();
        queueForOutputText = new PriorityBlockingQueue<>(1, Comparator.comparingInt(TextBlock::getPosInAllText));
        initTextBlocks();
    }

    public void updateBlocks() throws IOException {
        prevBlock.setTextLines(currentBlock.getTextLines());
        currentBlock.setTextLines(nextBlock.getTextLines());
        insertTextFromFileInBlock(nextBlock);
    }

    public void insertTextInOutputFile() throws IOException {
        List<String> allFoundTextList = new LinkedList<>();
        while(!queueForOutputText.isEmpty()) {
            TextBlock textBlock = queueForOutputText.poll();
            //fileDataWriter.writeLines(textBlock.getTextLines());
            allFoundTextList.addAll(textBlock.getTextLines());
        }
        fileDataWriter.writeLines(allFoundTextList);

    }

    public void closeBuffers() throws IOException {
        fileDataWriter.closeWriter();
        fileDataReader.closeReader();
    }

    public void insertTextInQueueForOutput(List<String> textForInsertion, int posInAllText) throws IOException {
        queueForOutputText.add(new TextBlock(posInAllText, textForInsertion));
        //fileDataWriter.writeLines(textForInsertion);
    }

    public TextBlock getPrevBlock() {
        return prevBlock;
    }

    public TextBlock getCurrentBlock() {
        return currentBlock;
    }

    public TextBlock getNextBlock() {
        return nextBlock;
    }

    private void initTextBlocks() throws IOException {
        insertTextFromFileInBlock(currentBlock);
        insertTextFromFileInBlock(nextBlock);
    }

    private void insertTextFromFileInBlock(TextBlock block) throws IOException {
        List<String> textForBlock = new ArrayList<>();
        for (int i = 0; i < VALUE_OF_LINES_IN_BLOCK; i++) {
            if (!fileDataReader.hasNextLine()) {
                break;
            }
            textForBlock.add(fileDataReader.getNextLine());
        }
        block.setTextLines(textForBlock);
    }
}
