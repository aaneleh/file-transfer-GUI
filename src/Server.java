import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import javax.swing.border.LineBorder;


public class Server {

    public static int[] connect(File[] novaListaArquivos) {
        System.out.printf("Iniciando server na porta: %s.%n", System.getenv("PORT"));
        int PORT = Integer.parseInt(System.getenv("PORT"));
        ServerSocket socketRecepcao = null;
        File pastaArquivos = new File("DefaultFolder");
        File[] listaArquivos = pastaArquivos.listFiles();

        try {
            System.out.println("Server Aguardando");
            socketRecepcao = new ServerSocket(PORT);
            Socket socketConexao = socketRecepcao.accept();
            System.out.println("Server conectou");

            ObjectInputStream doCliente = new ObjectInputStream(socketConexao.getInputStream());
            ObjectOutputStream paraCliente = new ObjectOutputStream(socketConexao.getOutputStream());

            paraCliente.writeObject(novaListaArquivos); //manda o file[]
            int[] indiceArquivos = (int[])doCliente.readObject(); //le um int indiceArquivos[]

            FileInputStream fileInputStream;
            DataOutputStream paraClienteData;
            int bytes;
            byte[] buffer;
            for(int i = 0; i < indiceArquivos.length; i++){
                bytes = 0;
                fileInputStream = new FileInputStream(listaArquivos[indiceArquivos[0]]);
                paraClienteData = new DataOutputStream(socketConexao.getOutputStream());
                paraClienteData.writeLong(listaArquivos[0].length());
                buffer = new byte[4 * 1024];
                while ((bytes = fileInputStream.read(buffer))!= -1) {
                    paraClienteData.write(buffer, 0, bytes);
                    paraClienteData.flush();
                }
                fileInputStream.close();
            }

            socketConexao.close();
            socketRecepcao.close();

            return indiceArquivos;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    File pastaArquivos = new File("DefaultFolder");
    File[] listaArquivos = pastaArquivos.listFiles();

    JFrame frame;
    JPanel serverPanel;
    JLabel statusConexao;
    JLabel enderecoDiretorio;
    JButton botaoConexao;
    JButton botaoSelecionarDiretorio;
    JPanel bodyListagem;
    JLabel arquivoLabel;
    ImageIcon iconePasta = new ImageIcon("src/folderIcon.png");
    ImageIcon iconeEnviado = new ImageIcon("src/sentIcon.png");

    //INICIALIZA GUI
    public void iniciaGUI(){
        System.out.println("Inicia GUI");
        frame = new JFrame();

        serverPanel = new JPanel();
        serverPanel.setPreferredSize(new Dimension(600,500));
        serverPanel.setLayout(null);
        statusConexao = new JLabel();
        statusConexao.setText("Servidor desligado");
        statusConexao.setBounds(220, 50, 300,50);

        JPanel selecaoConexao = new JPanel();
        botaoConexao = new JButton();
        botaoConexao.setText("Iniciar conexao");
        botaoConexao.setBounds(195, 100, 200,30);
        botaoConexao.setFocusable(false);
        botaoConexao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int[] indicesEnviados = connect(listaArquivos);
                bodyListagem.removeAll();

                for (int i = 0 ; i < listaArquivos.length; i++) {
                    arquivoLabel = new JLabel(listaArquivos[i].getName());
                    for (int indicesEnviado : indicesEnviados)
                        if (i == indicesEnviado) {
                            arquivoLabel.setIcon(iconeEnviado);
                            arquivoLabel.setHorizontalTextPosition(JLabel.LEFT);
                            break;
                        }
                    bodyListagem.add(arquivoLabel);
                    bodyListagem.revalidate();
                    bodyListagem.repaint();
                }

            }
        });


        JPanel headerListagem = new JPanel(new FlowLayout());
        headerListagem.setBounds(50, 170, 500, 60);
        headerListagem.setBackground(new Color(224,224,224));
        headerListagem.setBorder(new LineBorder(new Color(192,192,192)));
        enderecoDiretorio = new JLabel();
        enderecoDiretorio.setText(pastaArquivos.getAbsolutePath());

        botaoSelecionarDiretorio = new JButton();
        botaoSelecionarDiretorio.setText("Selecionar diretório");
        botaoSelecionarDiretorio.setIcon(iconePasta);
        botaoSelecionarDiretorio.setFocusable(false);
        botaoSelecionarDiretorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser exploradorArquivos = new JFileChooser();
                int res = exploradorArquivos.showOpenDialog(null);

                if(res == JFileChooser.APPROVE_OPTION) {

                    bodyListagem.removeAll();

                    File arquivoSelecionado = exploradorArquivos.getSelectedFile().getAbsoluteFile();
                    File pastaSelecionado = arquivoSelecionado.getParentFile();
                    enderecoDiretorio.setText(pastaSelecionado.getAbsolutePath());
                    File[] novaListaArquivos = pastaSelecionado.listFiles();

                    int i = 0;
                    pastaArquivos = pastaSelecionado;

                    for (File novoArquivo : novaListaArquivos)
                        if (!novoArquivo.isDirectory() && !novoArquivo.isHidden()) {
                            i++;
                            bodyListagem.add(new JLabel(novoArquivo.getName()));
                        }

                    listaArquivos = new File[i];
                    i=0;
                    for (File novoArquivo : novaListaArquivos)
                        if (!novoArquivo.isDirectory() && !novoArquivo.isHidden()) {
                            listaArquivos[i] = novoArquivo;
                            i++;
                        }

                    bodyListagem.revalidate();
                    bodyListagem.repaint();
                }
            }

        });

        headerListagem.setLayout(new FlowLayout());
        headerListagem.add(enderecoDiretorio);
        headerListagem.add(botaoSelecionarDiretorio);

        bodyListagem = new JPanel();
        bodyListagem.setLayout(new BoxLayout(bodyListagem, BoxLayout.Y_AXIS));
        bodyListagem.setBounds(50,229,500,200);
        bodyListagem.setBorder(new LineBorder(new Color(192,192,192)));
        for (File listaArquivo : listaArquivos)
            bodyListagem.add(new JLabel(listaArquivo.getName()));

        serverPanel.add(statusConexao);
        serverPanel.add(botaoConexao);
        serverPanel.add(headerListagem);
        serverPanel.add(bodyListagem);
        frame.add(serverPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);
        frame.setVisible(true);
    }


    public static void main(String[] args){ new Server().iniciaGUI(); }

}