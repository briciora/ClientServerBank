import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.lang.Math;

public class Client {
    static void doCommand(Scanner read, String command, Float amount, Socket socket)
    {
        try 
        {
            //can get server input
            Scanner fromServer = new Scanner(socket.getInputStream());
            //can give server output
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            toServer.println(command);
            if(command.equals("deposit") || command.equals("withdraw"))
            {
                toServer.println(amount);
                System.out.println(fromServer.nextLine());
            }
            System.out.println(fromServer.nextLine());
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        try (Socket socket = new Socket(args[0], 59899)) {
            while(true) {
                System.out.println("Please enter a command (deposit, withdraw, check, or exit)");
                String command;
                Float amount = (float) 0;
                Scanner read = new Scanner(System.in);
                command = read.nextLine();
                if(command.equals("deposit") || command.equals("withdraw"))
                {
                    System.out.println("Enter the amount ($$.$$)");
                    if(read.hasNextInt() || read.hasNextFloat())
                    {
                        String amountString = read.nextLine();
                        if(amountString.contains(" ") || amountString.isEmpty())
                        {
                            System.out.println("Enter the amount with no spaces");
                        }
                        else if(amountString.contains("."))
                        {
                            String[] amt = amountString.split("\\.", 2);
                            if(amt[1].length() < 3)
                            {
                                amount = new Float(amountString);
                                doCommand(read, command, amount, socket);
                            }
                            else 
                            {
                                System.out.println("Amount is invalid!");
                            }
                        }
                        else
                        {
                            amount = new Float(amountString);
                            if(amount < 0)
                            {
                                System.out.println("Enter only positive values!");
                            }
                            else
                            {
                                doCommand(read, command, amount, socket);
                            }
                        }    
                    }
                    else
                    {
                        System.out.println("Amount is invalid!");
                    }
                }
                else if(command.equals("check"))
                {
                    doCommand(read, command, amount, socket);
                }
                else if(command.equals("exit"))
                {
                    break;
                }
                else 
                {
                    System.out.println("Command is invalid!");
                }
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}