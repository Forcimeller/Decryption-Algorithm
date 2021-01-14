package task1;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Task1 {
    public static void main(String[] args) {
        
        //initialise Array for vowels with values
        String []vowels = {"a", "A", "e", "E", "i", "I", "o", "O", "u", "U", "y", "Y"};
        
        //initialise Arrays for consonants with values
        String [] consonants = {"b", "B", "c", "C", "d", "D", "f", "F", "g", "G", "h", "H", "j",
                                    "J", "k", "K", "l", "L", "m", "M", "n", "N", "p", "P", "q",
                                    "Q", "r", "R", "s", "S", "t", "T", "v", "V", "w", "W", "x", "X",
                                    "z", "Z"};
        
        String sourceFile = readFile(); //Reads the file and adds it to the 'sourceFile' String

        String[] wordArray = splitIntoWords(sourceFile); //source content into an array of words (by using whitespaces as delimeters)       
        char[][] charArray = splitIntoChars(wordArray); //splits the characters in the file into arrays and ads it t0 charArray
          
        String decodedFile = decode(charArray, vowels, consonants); //decodes file and adds it to the Decoded file string

        writeToFile(decodedFile); // Writes the decoded text to the output file
        
    }
    
    public static String readFile(){
           
        String fileName = "datafile.txt";
        String dataFile = ""; //initialises the data file string which will contain the entire 
        
        try {//Exception block for readng the file
            
            //initialising the file
            String fileLocation = System.getProperty("user.dir");
            String sourceFile = fileLocation + File.separator + fileName;
            File arraySource = new File(sourceFile); 
            
            Scanner readHead = new Scanner(arraySource); //initialising the scanner for the file
        
            for (int i = 0; readHead.hasNextLine(); i++) {// FOR loop for reading each file line and adding it to string
                dataFile += readHead.nextLine() + ", ";
            }
            
            readHead.close(); //closes the file scanner

        } catch (FileNotFoundException fnfe) { //Catches an exception if no file is found
            System.out.println("The required file isn't available right now.");
            System.exit(1);
        }
        
        return dataFile;// returns the string
       
    }

    public static String[] splitIntoWords(String originFile){
        
        String delimiter = " "; //Sets the delimter value which will be used as a RegEx in the .split function
        
        String[] wordArray = originFile.split(delimiter); //splits (and removes) all of the spaces in the string and turns it into array
               
        return wordArray; //returns the new array
    }

    public static char[][] splitIntoChars(String[] originArray){
        
        ArrayList<char[]> characters = new ArrayList<>(); //creates a dynamic array of fixed length character arrays
           
        //for loop to turn the strings from the warry of words into char arrays 
        //  and adds to character Array List
        for (String word : originArray){
            characters.add(word.toCharArray());
        }
        
        //Creates Multi-Dimentional array for characters then runs for loop
        //  to move each element to the new array 
        char[][] finalArray = new char[characters.size()][];
        for (int i = 0; i < characters.size(); i++){  
            finalArray[i] = characters.get(i);
        }
        
        
        return finalArray; //returns the newly created array
    }
    
    public static String decode(char[][] charArray, String[] vowelArray, String[] consonantArray){
        
        String decodedWords = ""; //Variable for appending each decoded word
        String singleWord = ""; //Variable for appending letters to a single word
        char currentLetter = 0; //Variable for the character value for current letter
        char currentLetterTypeCode = 0; //Variable for storing the TYPE of character (C for consonant, V for Vowel))
        String currentNumberString = ""; //Variable for keeping the string of numbers following the Letter Type
        
        
        for(char[] word : charArray){//Iterates over each character array converted from the string of words
            for (int i = 0; i < (word.length); i++){//Iterates over the characters in the words
                currentLetter = word[i]; //Places the corresponding element into the current letter variable
               if(Character.isAlphabetic(currentLetter)){ //checks if currentLetter is a letter
                   //Places the letter type cipehertext (C or V) into the currentLetterType variable
                   if (currentLetter != 'V' && currentLetter != 'C'){ 
                        //Validation: Makes sure letters in the file conform to the encryption
                        //      standard
                        System.out.println("CRITICAL ERROR: The Enccoded File appears to be invalid. \n"
                                + "Please Check the File and try again.");
                        System.exit(1);//Ends the program with an error
                   }
                   currentLetterTypeCode = currentLetter; //If checks pass, set the letter type to the character found
               }else if(Character.isDigit(currentLetter)){ //checks if currentLetter is a number
                   //Turns character into string and adds it to the rest of the current string of 
                   //       numbers representing a word
                   currentNumberString += Character.toString(currentLetter); //Adds to a string of characters representing the letter
                   
                   if (currentLetterTypeCode == 'V'){
                       if(Integer.parseInt(currentNumberString) > vowelArray.length-1){
                            //Validation: Makes sure letters in the file conform to the encryption
                            //      standard                           
                           System.out.println("CRITICAL ERROR: The Enccoded File appears to be invalid. \n"
                                + "Please Check the File and try again.V");
                        System.exit(1); //Ends the program with an error
                       }
                   }else if (currentLetterTypeCode == 'C'){
                       if(Integer.parseInt(currentNumberString) > consonantArray.length-1){
                           //Validation: Makes sure letters in the file conform to the encryption
                            //      standard     
                           System.out.println("CRITICAL ERROR: The Enccoded File appears to be invalid. \n"
                                + "Please Check the File and try again.C");
                        System.exit(1); //Ends the program with an error
                       }
                   }
                   
               }
               
               if ((i == (word.length-1)) || (Character.isAlphabetic(word[i+1]))){ 
               // Checks if the next character is a string. If it is, then the current encoded letter is complete
               //   and is ready to be decoded. The Decipher function is then called
                   singleWord += decipher(currentLetterTypeCode, currentNumberString, vowelArray, consonantArray);
                   currentNumberString = ""; //Number string reset ready for the next character
               }
            }
            
            if(currentLetter == 44) 
                // Checks if the end of line delimter (,) is a part of this word
                //      Starts a new line if so.
                decodedWords += singleWord += "\n";
            else
                decodedWords += singleWord + " ";
            
            singleWord = ""; //clears the single word string ready for the next word
            currentLetterTypeCode = 0; //clears the single word string ready for the next word
        }
        
        //prints and returns the decoded words
        System.out.println("THE CIPHERTEXT HAS EVALUATED TO THE FOLLOWING: \n\n" + decodedWords);
        return decodedWords;
    }
    
    public static String decipher(char currentLetterTypeCode, String currentNumberString, String[] vowelArray, String[] consonantArray){
        
        String decipheredLetter = ""; //String for the return value
        
            //deciphers letter by referencing the relevant array using a parsed version
            //  of the number -1 and making that value the deciphered letter.
            switch(currentLetterTypeCode){
                case 'V':
                    decipheredLetter = vowelArray[Integer.parseInt(currentNumberString)-1];
                    break;

                case 'C':
                    decipheredLetter = consonantArray[Integer.parseInt(currentNumberString)-1];
                    break;
            }

            return decipheredLetter; //returns the deciphered letter
        }
    
    public static void writeToFile(String exportFile){

        //initialising the file to be written to
        String outputFileName = "results.txt";
        String fileLocation = System.getProperty("user.dir");
        String outFile = fileLocation + File.separator + outputFileName;
        
        try { //Try block to allow writing to the specified file
            FileWriter writeHead = new FileWriter(outFile); //initialising the writer for the file
            
            writeHead.write(exportFile); //Writes the to the file using the arguement string
            writeHead.close(); //Closes the file writer
            
            System.out.println("THE DECHIPHERED FILE HAS BEEN WRITTEN TO: "
                    + "\n" + outFile);
        
        } catch (IOException ioe) { //catches any thrown exceptions
            System.out.println("Unfortunately, the file could not be written at this time.");
            System.out.println(ioe);
        } 
        
    }

}

