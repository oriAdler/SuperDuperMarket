package common;

public class Input {

    public static int countWordsUsingSplit(String input){
        if(input.isEmpty()){
            return 0;
        }
        else{
            return input.split("\\s+").length;
        }
    }

    public static boolean isPositiveInteger(String input) {
        boolean isPositiveInteger;

        if (Input.countWordsUsingSplit(input) != 1) {
            isPositiveInteger = false;
        } else {
            try {
                int number = Integer.parseInt(input);
                if (number <= 0) {
                    isPositiveInteger = false;
                } else {
                    isPositiveInteger = true;
                }
            } catch (NumberFormatException exception) {
                isPositiveInteger = false;
            }
        }

        return isPositiveInteger;
    }

    public static boolean isNonNegativeInteger(String input) {
        boolean isNonNegativeInteger;

        if (Input.countWordsUsingSplit(input) != 1) {
            isNonNegativeInteger = false;
        } else {
            try {
                int number = Integer.parseInt(input);
                if (number < 0) {
                    isNonNegativeInteger = false;
                } else {
                    isNonNegativeInteger = true;
                }
            } catch (NumberFormatException exception) {
                isNonNegativeInteger = false;
            }
        }

        return isNonNegativeInteger;
    }

    public static boolean isPositiveDouble(String input) {
        boolean isPositiveDouble;

        if (Input.countWordsUsingSplit(input) != 1) {
            isPositiveDouble = false;
        } else {
            try {
                double number = Double.parseDouble(input);
                if (number <= 0) {
                    isPositiveDouble = false;
                } else {
                    isPositiveDouble = true;
                }
            } catch (NumberFormatException exception) {
                isPositiveDouble = false;
            }
        }

        return isPositiveDouble;
    }
}
