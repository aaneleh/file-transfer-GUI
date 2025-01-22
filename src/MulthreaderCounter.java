import javax.swing.*;

public class MulthreaderCounter {

    private static String status = "Desconectado";

    private static Integer cont = 0;

    JFrame frame = new JFrame();
    JLabel label = new JLabel();
    public void ServerGUI(){
        label.setText(">"+cont);
        frame.add(label);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static Runnable guiThread = new Runnable() {
        public void run(){
            System.out.println("Thread gui iniciada");

            while(cont < 10) {

                    synchronized (cont){  //LOCKA O CONT
                        try {
                            System.out.println("gui com o lock");
                            cont++;
                            System.out.println("gui " + cont);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }

            }

            System.out.println("Terminando thread gui");

        }

    };

    public static Runnable serverThread = new Runnable(){
        public void run(){
            System.out.println("Thread server iniciada");

            while(cont < 10) {

                    synchronized (cont){
                        try {
                            System.out.println("server com o lock");
                            cont++;
                            System.out.println("server " + cont);

                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                    }
            }

            System.out.println("Terminando thread server");
        }
    };


    public static void main(String[] args){

        Thread gui = new Thread(guiThread, "gui");

        Thread server = new Thread(serverThread, "server");

        gui.start();

        server.start();



    }
}