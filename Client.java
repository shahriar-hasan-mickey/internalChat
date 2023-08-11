public class Client extends javax.swing.JFrame{

    java.net.Socket socket;
    java.io.BufferedReader incoming;
    java.io.PrintWriter outgoing_standard_output;


    //GUI component declaration:
    private javax.swing.JLabel heading = new javax.swing.JLabel("Client Area");
    private javax.swing.JTextArea messageArea = new javax.swing.JTextArea();
    private javax.swing.JTextField messageInput = new javax.swing.JTextField();
    private java.awt.Font font = new java.awt.Font("Roboto", java.awt.Font.PLAIN, 20);


    //Constructor
    public Client(){
        try{
            System.out.println("Sending request to server");
            socket = new java.net.Socket("127.0.0.1", 7777);
            System.out.println("Connention Done.");

            incoming = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            outgoing_standard_output = new java.io.PrintWriter(socket.getOutputStream());



            createGUI();
            handleEvents();
            startReading();
            startWriting();
        }catch(Exception e){
            System.out.println("Closing Connection");
        }

    }

    public void handleEvents(){
        messageInput.addKeyListener(new java.awt.event.KeyListener(){
            @Override
            public void keyTyped(java.awt.event.KeyEvent e){

            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e){
                
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e){
                if(e.getKeyCode() == 10){
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : "+contentToSend+"\n");
                    outgoing_standard_output.println(contentToSend);
                    outgoing_standard_output.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                    if(contentToSend.equals("exit")){
                        dispose();
                        // return ;
                    }
                }
            }
        });
    }




    public void createGUI(){
        this.setTitle("Client Messager[END]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        


        //Coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        java.awt.Image originalImage = new javax.swing.ImageIcon("java.png").getImage();
        int newHeight = 40;
        int newWidth = newHeight;
        java.awt.Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
        javax.swing.ImageIcon resizedIcon = new javax.swing.ImageIcon(resizedImage);
        heading.setIcon(resizedIcon);

        heading.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        heading.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        heading.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        //Layout of frame
        this.setLayout(new java.awt.BorderLayout());
        this.add(heading, java.awt.BorderLayout.NORTH);
        javax.swing.JScrollPane jScrollPane = new javax.swing.JScrollPane(messageArea);
        this.add(jScrollPane, java.awt.BorderLayout.CENTER);
        this.add(messageInput, java.awt.BorderLayout.SOUTH);

        this.setVisible(true);
    }

    //Reader ...
    public void startReading(){
        Runnable r1 = () -> {
            System.out.println("Client side reader started..");

            try{   
                while(!socket.isClosed()){
                    
                        String message = incoming.readLine();
                        if(message.equals("exit")){
                            System.out.println("Server Terminated the Chat");

                            // FOR GUI
                            javax.swing.JOptionPane.showMessageDialog(this, "Server Terminated the Chat");
                            messageInput.setEnabled(false);

                            socket.close();
                            break;
                        }
                        System.out.println("Server : "+message);

                        // FOR GUI
                        messageArea.append("Server : "+message+"\n");
                    
                }
            }catch(Exception e){
                System.out.println("Connection Closed");
            }
        };

        new Thread(r1).start();
    }


    //Writer ...
    public void startWriting(){
        Runnable r2 = () -> {
            System.out.println("Client Side Writer Started..");
            
            try{
                while(!socket.isClosed()){
                    
                        java.io.BufferedReader outgoing = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
                        String content = outgoing.readLine();
                        outgoing_standard_output.println(content);
                        outgoing_standard_output.flush();
                    
                        if(content.equals("exit")){
                            socket.close();
                            break;
                        }

                }
            }catch(Exception e){
                System.out.println("Connection Closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args){
        System.out.println("Running");
        new Client();
    }
}