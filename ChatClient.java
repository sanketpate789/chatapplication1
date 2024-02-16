import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
public class ChatClient {
    static JFrame chatWindow = new JFrame("Chat Application");
    // JFrame is the outer container on which we are going to add our components
    static JTextArea ChatArea= new JTextArea(22,40);
    // displaying all our messages
    static JTextField textField= new JTextField(40);
    // used to send messages
    static JLabel blankLabel=new JLabel("    ");
    // used to display the blank space between the chat area and the text filed
    static JButton sendButton=new JButton("Send");

    static BufferedReader in;
    static PrintWriter out;
    static JLabel nameLabel = new JLabel("		");
    static Color nameColor;
 // Added this variable to store the name color

    ChatClient(){
        //constructor of Chatclient
        chatWindow.setLayout(new FlowLayout());
        // this defines how are components will be defined on our JFrame

        chatWindow.add(nameLabel);
        chatWindow.add(new JScrollPane(ChatArea));
        // it is a scroll bar and here the chat area also gets added
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendButton);
        sendButton.setBackground(new Color(7,94,84));
        sendButton.setForeground(Color.white);

        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // we have a close button on the top of the box and once we press it it will just hide the application but the GUI wont stop working
        // now if we want to close the application we have to add this
        chatWindow.setSize(475,500);
        // set size for our chat window
        chatWindow.setVisible(true);
        // it will display all the messages on the screen as the property is set to true
        textField.setEditable(false);
        // it should be false only because once the server gives permissino to the client then ony it can add the messages so it is set to false
        ChatArea.setEditable(false);
        //as it is only for displaying messages not to editing messages
        sendButton.addActionListener(new Listener());
        // now the user clicks on send the message will be sent to the server
        textField.addActionListener(new Listener());


    }
    void startChat() throws Exception{
        //logic for the chat client
        String ipAddress=JOptionPane.showInputDialog(
                // now this method showInputDialog is used to show the Dialog box
               
                chatWindow,
                // chatWindow is the component that we need to display the dialogbox
                "Enter IP Address:",
                // message we need to display
                "IP Address Required!!",
                // this is the title of the Dialogbox
                JOptionPane.PLAIN_MESSAGE);
        // now this is used to display the plane simple message
        
        Random random = new Random();
        Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        Socket soc =new Socket(ipAddress,9806);
        in=new BufferedReader(new InputStreamReader(soc.getInputStream()));
        out=new PrintWriter(soc.getOutputStream(),true);
        // waiting for the incoming messages from the server for that create a while loop
        while (true){
               String str=in.readLine();
               //what ever the message the server sends us
             if (str.equals("NAMEREQUIRED")){
                 String name=JOptionPane.showInputDialog(
                         chatWindow,
                         "Enter the unique Name:  ",
                         "Name Required!!",
                         JOptionPane.PLAIN_MESSAGE);
                 out.println(name);
             } else if (str.equals("NAMEALREADYEXITS")) {
                 String name=JOptionPane.showInputDialog(
                         chatWindow,
                         "Enter another name : ",
                         "Name Already Exits!!",
                         JOptionPane.WARNING_MESSAGE);
                          out.println(name);
             } 
             else if (str.startsWith("NAMEaccepted")) {
                 String[] components = str.split(" ");
                 if (components.length == 5) {
                     int red = Integer.parseInt(components[1]);
                     int green = Integer.parseInt(components[2]);
                     int blue = Integer.parseInt(components[3]);
                     nameColor = new Color(red, green, blue);
                 }
                 nameLabel.setForeground(nameColor);
                 nameLabel.setText("You are Logged in as: " + components[4]);
                 textField.setEditable(true);
             } 
             else {
            	 
                 ChatArea.append(str+"\n");
                 ChatArea.setForeground(nameColor);
                 // Set the color of the name text to the nameColor
                
                 
             }


        }
    }

    public static void main(String[] args)throws Exception {
    //create a gui and for this we will be using Swing component library
        ChatClient client=new ChatClient();
        client.startChat();
    }
}
class Listener implements ActionListener{
    // this is a listener class used for implementation of send button
    public void actionPerformed(ActionEvent e){
        // it has only one interface that is action performed and it has one parameter as input
    ChatClient.out.println(ChatClient.textField.getText());
    // whatever the user writes in the textfield it will go into the server and then server will send that message to all clients
    ChatClient.textField.setText("");
    // after that just clear the text field
    }
}