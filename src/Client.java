import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Client {

    BufferedReader doUsuario = new BufferedReader(new InputStreamReader(System.in));
    int[] fileIndex;
    File[] fileList;
    int bytes;
    long size;
    byte[] buffer;

    String IP = "192.168.1.20";
    int PORT = 6789;

    JFrame frame;
    JPanel clientPanel;
    JLabel status = new JLabel("Desconectado");
    JLabel ip; //TODO: Tornar um input, por default é o localhost
    JPanel listagemArquivos;
    JButton botaoInicarConexao;
    JButton botaoResetar;

    public File[] connect(int[] lista){
        try {
            Socket socketCliente = new Socket(IP, PORT);
            System.out.println("Conexão efetuada com sucesso");

            ObjectOutputStream paraServidor = new ObjectOutputStream(socketCliente.getOutputStream());
            ObjectInputStream doServidor = new ObjectInputStream(socketCliente.getInputStream());
            DataInputStream doServidorData = new DataInputStream(socketCliente.getInputStream());

            fileList = (File[])doServidor.readObject();

            //TODO Cria um array lista pra uso "interno" daí aqui converter pra um int[]
            //daí garante que se estiver nulo vai ser o int[0] e também que todas as posições vão estar preenchidas

            if(lista == null) lista = new int[0];

            paraServidor.writeObject(lista);

            FileOutputStream fileOutputStream;
            for(int i = 0; i < lista.length; i++){
                fileOutputStream = new FileOutputStream("ClientDirectory/"+fileList[i].getName());
                bytes = 0;
                size = doServidorData.readLong();
                buffer = new byte[4 * 1024];
                while (size > 0 && (bytes = doServidorData.read(buffer, 0, (int)Math.min(buffer.length, size)))!= -1) {
                    fileOutputStream.write(buffer, 0, bytes);
                    size -= bytes; // Subtrai do tamanho conhecido do arquivo a quantidade que já foi salva
                }
                System.out.println("Arquivo salvo");
                fileOutputStream.close();
            }

        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return fileList;
    }

    public void iniciaGUI() {
        frame = new JFrame();
        clientPanel = new JPanel();
        clientPanel.setLayout(null);

        status.setBounds(220, 50, 100, 40);

        JPanel selecaoConexao = new JPanel();
        ip = new JLabel("192.168.56.1"); //todo separar numa variavel .env
        ip.setSize(100, 40);
        botaoInicarConexao = new JButton("Conectar");
        botaoInicarConexao.setSize(100, 40);
        botaoInicarConexao.setFocusable(false);
        botaoInicarConexao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client cliente = new Client();
                //TODO lista será dinâmica baseada nas checkboxes e não precisa estar em ordem
                fileIndex = new int[1];
                fileIndex[0] = 0;
                //fileIndex[1] = 1;
                File[] fileList = cliente.connect(fileIndex);
                System.out.println("primeiro nome do retorno da funcao:" + fileList[0].getName());

                for(int i = 0; i < fileList.length; i ++){
                    listagemArquivos.add(new JLabel(fileList[i].getName()));
                }
                listagemArquivos.revalidate();
            }
        });
        selecaoConexao.setBounds(170, 100, 200,40);

        selecaoConexao.add(ip);
        selecaoConexao.add(botaoInicarConexao);

        listagemArquivos = new JPanel();
        listagemArquivos.setLayout(new BoxLayout(listagemArquivos, BoxLayout.Y_AXIS));
        listagemArquivos.setBounds(80,  155, 400, 200);
        listagemArquivos.setBorder(new LineBorder(new Color(192, 192, 192)));

        //TODO: trocar esse botão pra ele "limpar" a exibição e os arquivos selecionados
        botaoResetar = new JButton("Resetar");
        botaoResetar.setFocusable(false);
        botaoResetar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listagemArquivos.removeAll();
                listagemArquivos.revalidate();
                listagemArquivos.repaint();
                fileIndex = null;
            }
        });
        JPanel selecaoConfirmacao = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        selecaoConfirmacao.setBounds(80, 380, 400, 40);
        selecaoConfirmacao.add(botaoResetar);

        clientPanel.add(status);
        clientPanel.add(selecaoConexao);
        clientPanel.add(listagemArquivos);
        clientPanel.add(selecaoConfirmacao);
        frame.add(clientPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        frame.setVisible(true);
    }


    public static void main(String[]args){
        new Client().iniciaGUI();
    }

}