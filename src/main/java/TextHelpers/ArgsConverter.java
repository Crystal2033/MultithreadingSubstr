package TextHelpers;

import Exceptions.BadArgsException;

import static Settings.CONSTANTS.VALUE_OF_LINES_IN_BLOCK;

/**
 * @project SubstrFinder
 * ©Crystal2033
 * @date 24/10/2022
 */

public class ArgsConverter {
    private final String[] args;

    public ArgsConverter(String[] args) throws BadArgsException {
        if(args.length != 3 && args.length != 4){
            throw new BadArgsException("""
                    There should be 3 or 4 arguments:
                     1. input file
                     2. output file
                     3. key word
                     4. value of lines before and after (default=3).""");
        }
        this.args = args;
    }



    public String getInputFileName(){
        return args[0];
    }
    public String getOutputFileName(){
        return args[1];
    }
    public String getKeyWord(){
        return args[2];
    }


    public int getPrevAndNextTextLines() throws BadArgsException {
        if(args.length == 4){
            int epsilonValue = Integer.parseInt(args[3]);

            if(epsilonValue < 0 || epsilonValue > VALUE_OF_LINES_IN_BLOCK){
                throw new BadArgsException("Your epsilon value for next and previous value of lines should be more than 0 and " +
                        "less than " + VALUE_OF_LINES_IN_BLOCK + ".");
            }
            return epsilonValue;
        }

        return 3;
    }
}
