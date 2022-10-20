package FileWorkers;

import TextHelpers.TextBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Settings.CONSTANTS.VALUE_OF_LINES_IN_BLOCK;

public class FileCommunicator {
    private final FileDataReader fileDataReader;
    private final FileDataWriter fileDataWriter;
    private TextBlock prevBlock, currentBlock, nextBlock;

    public FileCommunicator(String fileName) throws IOException {
        fileDataReader = new FileDataReader(fileName);
        fileDataWriter = new FileDataWriter(fileName);
        prevBlock = new TextBlock();
        currentBlock = new TextBlock();
        nextBlock = new TextBlock();
        initTextBlocks();
    }

    public TextBlock refillBlocksAndGetCurrent() throws IOException {
        prevBlock = currentBlock;
        currentBlock = nextBlock;
        insertTextFromFileInBlock(nextBlock);
        return currentBlock;
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
        for(int i = 0; i < VALUE_OF_LINES_IN_BLOCK; i++){
            if(!fileDataReader.hasNextLine()){
                break;
            }
            textForBlock.add(fileDataReader.getNextLine());
        }
        block.setTextLines(textForBlock);
    }


}
