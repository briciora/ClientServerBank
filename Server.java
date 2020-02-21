import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.net.InetAddress;

public class Server {
    static Float balance = (float) 10000.00;
    static Socket socket = new Socket();

    static boolean withdraw(Float amount)
    {
        Float tempBalance = balance - amount;
        if(tempBalance < 0)
        {
            return false;
        }
        balance = balance - amount;
        return true;
    }

    static Float deposit(Float amount)
    {
        balance = balance + amount;
        return balance;
    } 

    static Float check()
    {
        return balance;
    }

    public static void main(String[] args) throws Exception 
    {
        try (ServerSocket listener = new ServerSocket(59899)) 
        {
            System.out.println("Your current IP address is :" + InetAddress.getLocalHost());
            System.out.println("The server is running...");
            while(true)
            {
                try 
                {
                    socket = listener.accept();
                    System.out.println("Connected: " + socket);
                    Scanner fromClient = new Scanner(socket.getInputStream());
                    PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);
                    while (fromClient.hasNextLine()) 
                    {
                        String command = fromClient.nextLine();
                        switch(command)
                        {
                            case "withdraw":
                                Float amount1 = new Float(fromClient.nextLine());
                                if(amount1 < 0)
                                {
                                    toClient.println("Cannot enter negative values");
                                    break;
                                }
                                boolean success = withdraw(amount1);
                                if(success)
                                {
                                    toClient.println("Withdraw was successful!");
                                    String balance = String.format("%.2f", check());
                                    toClient.println("Your current balance is now $" + balance);
                                }
                                else
                                {
                                    toClient.println("Insufficient funds!");
                                }
                                break;
                            case "deposit":
                                Float amount2 = new Float(fromClient.nextLine());
                                if(amount2 < 0)
                                {
                                    toClient.println("Cannot enter negative values");
                                }
                                else
                                {
                                    deposit(amount2);
                                    toClient.println("Deposit was successful!");
                                    String balance = String.format("%.2f", check());
                                    toClient.println("Your current balance is now $" + balance);
                                }
                                break;
                            case "check":
                                String balance = String.format("%.2f", check());
                                toClient.println("Your current balance is $" + balance);
                                break;
                            default:
                                System.out.println("Error");
                                break;
                        }
                    }
                } 
                catch (Exception e) 
                {
                    System.out.println("Error:" + socket);
                } 
                finally 
                {
                    try 
                    { 
                        socket.close(); 
                    } 
                    catch (IOException e) 
                    {
                    }
                    System.out.println("Closed: " + socket);
                }  
            } 
        }
    }
}