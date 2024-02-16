import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color; // Import the Color class
import java.util.Random;

public class ChatServer {
    static ArrayList<String> userNames=new ArrayList<String>();
    static ArrayList<PrintWriter> printWriters= new ArrayList<PrintWriter>();
    // now whenever a client sends a message to the server  then server need to send the same messages to all the client so for that we need Printwritter  message of all the clients
    static List<Color> userColors = new ArrayList<Color>(); // to add color list 

    public static void main(String[] args) throws Exception {
        System.out.println("waiting for the Clients ...");
        //its a multiuser chat application
        ServerSocket ss = new ServerSocket(9806);
        // now we want our server to continuously wait for incoming client connections so for that we want a while loop
        while (true) {
            Socket soc = ss.accept();
            // after establishing the connections this method accept will return us the socket object
            System.out.println("connection Established ");
           // we are creating Multi threaded Chat server basically which means it will handel multiple client simultaneously
            ConversationHandler handler=new ConversationHandler(soc);
            handler.start();
            //starting this thread
        }
    }
}
class ConversationHandler extends Thread{
    Socket socket;
    // now this scoket is the socket for which we will be creating the ConversationHandler Thread
    BufferedReader in;
    PrintWriter out;
    String name;
    PrintWriter pw;
    static FileWriter fw;
    static BufferedWriter bw;
    private Color userColor;  // Declare the userColor variable
    
    public ConversationHandler(Socket socket)throws Exception{
        // this is a constructor of the class above with one parameter Socket as input
        this.socket=socket;
        // once we get the socket at the soc there we will jsut pass the socket object into the object of the ConversationaHandler
        fw=new FileWriter("C:\\ASA-LAB\\Chat API 1\\src\\chatlog.txt",true);
        bw=new BufferedWriter(fw);
        pw=new PrintWriter(bw,true);
    }
    public void run(){
        //all our server side logic will come into the run method

     try {
         in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
         // used for reading data from client side
         out=new PrintWriter(socket.getOutputStream(),true);
         // used to write to the client request
         int count=0;
         while (true){
             if (count>0){
                 out.println("NAMEALREADYEXITS");
             }
             else {
                 out.println("NAMEREQUIRED");
             }
             name=in.readLine();
             // the name once enterd will be captured in the variable called name

             if (name==null){
                 return;
             }
             if (!ChatServer.userNames.contains(name)){
                 ChatServer.userNames.add(name);
                 break;
             }
             count++;
             // here if the name already exists it will increment the counter and and it will got at the top
             // and a message is displayed that name already exists
             // and if the name is not present the count will increment and the loop will break

         }
         // Generate a random color for the client
         Random random = new Random();
         int red = random.nextInt(256);
         int green = random.nextInt(256);
         int blue = random.nextInt(256);
         userColor = new Color(red, green, blue);
         ChatServer.userColors.add(userColor); // Store user color

         out.println("NAMEaccepted " + red + " " + green + " " + blue + " " + name);
         ChatServer.printWriters.add(out);

         
         
       
        while (true){
            // this loop will read the messages from client and send it to other client
            String message=in.readLine();
            if (message==null){
                return;
            }
            pw.println(name+": "+message);
            for (PrintWriter Writer:ChatServer.printWriters){
                Writer.println(name+": "+message);
            }
        }

     }
     catch (Exception e){
         System.out.println(e);
     }





    }
}