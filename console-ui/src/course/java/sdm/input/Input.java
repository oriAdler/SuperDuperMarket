package course.java.sdm.input;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Input {

    public static String DATE_FORMAT = "dd/MM-HH:mm";

    public static int[] getTwoIntegers() {
        String inputString;
        int[] inputArray = new int[2];
        Scanner scanner = new Scanner(System.in);
        boolean isValid;

        do{
            inputString = scanner.nextLine().trim();
            if(countWordsUsingSplit(inputString) != 2){
                System.out.println("Invalid input: input must be two numbers");
                isValid = false;
            }
            else{
                try{
                    String[] strings = inputString.split("\\s+");
                    inputArray[0] = Integer.parseInt(strings[0]);
                    inputArray[1] = Integer.parseInt(strings[1]);
                    isValid = true;
                }
                catch (NumberFormatException exception){
                    System.out.println(
                            "Invalid input: input must be a two whole numbers (excluding 0)");
                    isValid = false;
                }
            }
        } while (!isValid);

        return inputArray;
    }

    public static int getOnePositiveInteger() {
        String inputString;
        int inputInt = 0;
        Scanner scanner = new Scanner(System.in);
        boolean isValid;

        do{
            inputString = scanner.nextLine().trim();
            if(countWordsUsingSplit(inputString) != 1){
                System.out.println("Invalid input: input must be one number");
                isValid = false;
            }
            else{
                try{
                    inputInt = Integer.parseInt(inputString);
                    if(inputInt <= 0){
                        System.out.println("Invalid input: number must be positive");
                        isValid = false;
                    }
                    else{
                        isValid = true;
                    }
                }
                catch (NumberFormatException exception){
                    System.out.println("Invalid input: input must be a whole number (excluding 0)");
                    isValid = false;
                }
            }
        } while (!isValid);

        return inputInt;
    }

    public static int getOnePositiveIntegerInRange(int min, int max, String errorMessage){
        boolean isValidChoice;
        int userChoice = 0;

        do{
            userChoice = Input.getOnePositiveInteger();
            if(!inRange(userChoice, min, max)){
                System.out.println(errorMessage);
                isValidChoice = false;
            }
            else{
                isValidChoice = true;
            }
        } while(!isValidChoice);

        return userChoice;
    }

    public static double getOnePositiveDouble() {
        String inputString;
        double inputDouble = 0;
        Scanner scanner = new Scanner(System.in);
        boolean isValid;

        do{
            inputString = scanner.nextLine().trim();
            if(countWordsUsingSplit(inputString) != 1){
                System.out.println("Invalid input: input must be one number");
                isValid = false;
            }
            else{
                try{
                    inputDouble = Double.parseDouble(inputString);
                    if(inputDouble <= 0){
                        System.out.println("Invalid input: number must be positive");
                        isValid = false;
                    }
                    else{
                        isValid = true;
                    }
                }
                catch (NumberFormatException exception){
                    System.out.println("Invalid input: input must be a number");
                    isValid = false;
                }
            }
        } while (!isValid);

        return inputDouble;
    }

    public static int countWordsUsingSplit(String input){
        if(input.isEmpty()){
            return 0;
        }
        else{
            return input.split("\\s+").length;
        }
    }

    public static Date getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern(DATE_FORMAT);
        formatter.setLenient(false);
        Scanner scanner = new Scanner(System.in);

        Date date = new Date();
        boolean validInput;

        do {
            try {
                String userInput = scanner.nextLine();
                date = formatter.parse(userInput);
                validInput = true;
            }
            catch (ParseException e) {
                e.getMessage();
                System.out.println("Invalid input: please enter a valid date by format");
                validInput = false;
            }
        } while (!validInput);

        return date;
    }

    public static boolean isPositiveInteger(String input) {
        boolean isPositiveInteger;

        if (Input.countWordsUsingSplit(input) != 1) {
            System.out.println("Invalid input: input must be one number");
            isPositiveInteger = false;
        }
        else {
            try {
                int number = Integer.parseInt(input);
                if(number <= 0){
                    System.out.println("Invalid input: number must be positive");
                    isPositiveInteger = false;
                }
                else {
                    isPositiveInteger = true;
                }
            }
            catch (NumberFormatException exception) {
                System.out.println("Invalid input: input must be a whole number (excluding 0)");
                isPositiveInteger = false;
            }
        }

        return isPositiveInteger;
    }

    private static boolean inRange(int number, int min, int max){
        return number>=min && number<=max;
    }
}
