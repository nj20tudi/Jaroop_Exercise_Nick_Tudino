import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.text.DecimalFormat;
/**
 * Allows the user to deposit money, withdraw money, and check the balance of their account
 * 
 * @author Nicholas Tudino
 * @version 01/06/2017
 */
public class AccountFinal
{
    private FileWriter fWriter = null;
    //initializes a FileWriter to be able to read and write
    private BufferedWriter bWriter = null;
    //initializes a BufferedWriter to be able to read and write
    private ArrayList<Double> transactions = new ArrayList<Double>();
    //An ArrayList of the amounts in the transaction table in the log.html docment
    private static DecimalFormat dec = new DecimalFormat("#.##");
    //sets the decimal format to two decimal places

    /**
     * main method for class AccountFinal
     * 
     * @param String args[] - empty array to make the program execute
     */
    public static void main(String args[])
    {
        new AccountFinal().prompt();
    }

    /**
     * prompts the user to choose between deposit, withdraw,
     * balance, and exit
     */
    public void prompt(){
        Scanner input = new Scanner(System.in);  
        // Reading from System.in
        System.out.println("Please enter in a command (Deposit, Withdraw, Balance, Exit) : ");
        String a = input.nextLine().toLowerCase();
        //stores input to string a and converts it to 
        //lowercase so that the input is not case sensitive
        if (a.equals("deposit")){
            deposit();
        }
        else if (a.equals("withdraw")){
            withdraw();
        }
        else if (a.equals("balance")){
            balance();
        }
        else if (a.equals("exit")){
            exit();
        }
        else{
            System.out.println("This response is not valid. Please try again.");
        }
        prompt();
    }

    /**
     * Prompts the user for an amount to deposit and writes the amount to 
     * the transaction table
     */
    public void deposit(){
        Scanner dAmount = new Scanner(System.in);
        //scanner for amount user wants to deposit
        System.out.println("Please enter an amount to deposit:");
        //prompts user for deposit amount
        String depositAmount = dAmount.nextLine();
        //stores the deposit amount in a String
        if(checkFormatting(depositAmount) == true){ //checks whether the deposit amount is valid
            if(depositAmount.contains(".")){
                if((depositAmount.length() - depositAmount.indexOf("."))==2){
                    addToFile(depositAmount + "0"); //adds withdraw amount to the end of the transactions table
                }
                else{addToFile(depositAmount); }//adds withdraw amount to the end of the transactions table
            }
            else{
                addToFile(depositAmount + ".00"); //adds withdraw amount to the end of the transactions table
            }
        }
        else{
            System.out.println("Monetary values can have maximum two decimal places and must be greater than zero.");
            deposit();
        }
    }

    /**
     * Prompts the user for an amount to withdraw and writes the amount to 
     * the transaction table as a negative amount
     */
    public void withdraw(){
        Scanner wAmount = new Scanner(System.in);
        //scanner for amount user wants to withdraw
        System.out.println("Please enter an amount to withdraw:");
        //prompts user for withdraw amount
        String withdrawAmount = wAmount.nextLine();
        //stores the withdraw amount in a String

        if(checkFormatting(withdrawAmount) == true){
            if(withdrawAmount.contains(".")){
                if((withdrawAmount.length() - withdrawAmount.indexOf("."))==2){
                    addToFile("-" + withdrawAmount + "0"); //adds withdraw amount to the end of the transactions table
                }
                else{addToFile("-" + withdrawAmount); }//adds withdraw amount to the end of the transactions table
            }
            else{
                addToFile("-" + withdrawAmount + ".00"); //adds withdraw amount to the end of the transactions table
            }
        }
        else{
            System.out.println("Monetary values can have maximum two decimal places and must be greater than zero.");
            withdraw();
        }
    }

    /**
     * Prints out the sum of the amounts in the transaction table
     */
    public void balance(){
        readIn();
        double tot = 0; //sum of the amounts in the transactions table
        String total = "";
        for( int i = 0; i < transactions.size(); i++){
            tot = tot + transactions.get(i);
            total = dec.format(tot);
            tot = Double.parseDouble(total);
        }
        if(tot == 0.0) System.out.println("The current balance is: $0.00");
        else if(total.contains(".")){
            if((total.length() - total.indexOf("."))==2)
                System.out.println("The current balance is: $" + dec.format(tot) + "0");
            else System.out.println("The current balance is: $" + dec.format(tot));
        }
        else{
            System.out.println("The current balance is: $" + dec.format(tot) + ".00");
        }
    }

    /**
     * Exits the program
     */
    public void exit(){
        System.exit(0);
    }

    /**
     * Checks if an input amount meets all requirements
     * 
     * 
     * @param String check - a monetary amount being checked for formatting
     * @returns - true if formatting is correct and false otherwise
     */
    public boolean checkFormatting(String check){
        boolean formatting = false; //boolean for whether or not input has a max of two decimal places
        boolean valid = true; //boolean for whether or not input only contains numbers or a decimal

        //checks that input is comprised of only digits and a max of one decimal, and has a max of two decimal places
        if(check.length() - check.replace(".", "").length() <= 1){
            for(int i =0; i < check.length(); i++){
                int c = (int)check.charAt(i);
                if(!(((c > 47) && (c < 58)) || (c == 46))){
                    valid = false;
                }
            }
            if( valid == true){
                if(check.contains(".")){
                    if(((check.indexOf(".") >= 0) && ((check.length() - check.indexOf("."))<=3)) && (Double.parseDouble(check) > 0)){
                        formatting = true;
                    }
                }
                else{
                    if(Double.parseDouble(check) > 0)  formatting = true;
                }
            }
        }
        return formatting;
    }

    /**
     * adds an input amount to the end of the transactions table in the log.html file
     * 
     * @param String amount - monetary amount being added to the end of the transactions table
     */
    public void addToFile(String amount){
        ArrayList<String> fileInfo = new ArrayList<String>();
        //ArrayList of every line that is going to be in log.html after transaction
        boolean added = false;
        //boolean of whether new transaction has been added to the fileInfo ArrayList
        boolean reachedSpot = false;
        //boolean of whether the end of the transactions in the table has been reached
        boolean correctTable = false;
        //boolean of whether the transactions table has been reached
        boolean correctLocation = false;
        //boolean of whether the body of the table has been reached

        //Reads each line of the log.html file into the fileInfo ArrayList with the new transaction in the correct position
        try (BufferedReader br = new BufferedReader(new FileReader("log.html"))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                if(added == false){
                    if(line.contains("transactions")){
                        correctTable = true;
                    }
                    if((line.contains("<tbody>")) && (correctTable == true)){
                        correctLocation = true;
                    }
                    if((line.contains("<tr><td>")) && (correctTable == true) && (correctLocation == true)){
                        reachedSpot = true;
                    }
                    if(reachedSpot == true && !line.contains("<tr><td>")){
                        fileInfo.add("                <tr><td>" + amount + "</tr></td>");
                        added = true;
                    }
                }
                fileInfo.add(line);
            }
        }
        catch (Exception e) {}

        //Clears the current log.html file
        try {
            fWriter = new FileWriter("log.html");
            bWriter = new BufferedWriter(fWriter);
            bWriter.write("");
            bWriter.close(); 
        }
        catch (Exception e) {}

        //writes the lines in the fileInfo ArrayList to the log.html file
        for(int i = 0; i < fileInfo.size(); i++){
            try {
                fWriter = new FileWriter("log.html", true);
                bWriter = new BufferedWriter(fWriter);
                bWriter.write(fileInfo.get(i));
                bWriter.newLine(); 
                bWriter.close(); 
            }
            catch (Exception e) {}
        }
    }

    /**
     * Reads all monetary values from the transactions table and stores in trans ArrayList
     */
    public void readIn(){
        ArrayList<Double> trans = new ArrayList<Double>();
        //ArrayList of all monetary values in the transactions table
        boolean correctTable = false;
        //boolean of whether the transactions table has been reached

        try (BufferedReader br = new BufferedReader(new FileReader("log.html"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String isNum = "";
                if(line.contains("transactions")) correctTable = true;
                if (correctTable == true){
                    for(int i = 0; i < line.length(); i++){
                        int c = (int)line.charAt(i);
                        if(!line.contains("table-bordered")){
                            if( ((c > 47) && (c < 58)) || (c == 45) || (c == 46)){
                                isNum = isNum + line.charAt(i);
                            }
                        }
                    }
                    if(!isNum.equals("")){
                        double num = Double.parseDouble(isNum);
                        trans.add(num);
                    }
                }
            }
            transactions = trans;
            correctTable = false;
        }
        catch (Exception e) {}
    }
}

