import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ServerPanel extends JPanel implements ActionListener {

    //TODO copiar todo codigo do servidor

    JLabel statusConexao;
    JLabel enderecoDiretorio;
    JButton botaoFecharConexao;
    JButton botaoSelecionarDiretorio;
    JPanel bodyListagem;

    ServerPanel() {

        this.setPreferredSize(new Dimension(600,500));
        this.setLayout(null);

        statusConexao = new JLabel();
        //TODO mudar número da porta pra variavel q será criada
        statusConexao.setText("Aguardando conexão na porta 6789");
        statusConexao.setBounds(195, 50, 300,50);

        botaoFecharConexao = new JButton();
        botaoFecharConexao.setText("Fechar conexao");
        //TODO set enable quando a conexao for feita
        botaoFecharConexao.setEnabled(false);
        botaoFecharConexao.setBounds(195, 100, 200,30);
        botaoFecharConexao.setFocusable(false);
        botaoFecharConexao.addActionListener(this);

        JPanel headerListagem = new JPanel();
        headerListagem.setBounds(100, 170, 400, 40);
        headerListagem.setBackground(new Color(224,224,224));
        headerListagem.setBorder(new LineBorder(new Color(192,192,192)));
        enderecoDiretorio = new JLabel();
        enderecoDiretorio.setText("Nenhum diretorio selecionado");

        botaoSelecionarDiretorio = new JButton();
        botaoSelecionarDiretorio.setText("Selecionar diretório");
        botaoSelecionarDiretorio.setFocusable(false);
        botaoSelecionarDiretorio.addActionListener(this);

        headerListagem.setLayout(new FlowLayout());
        headerListagem.add(enderecoDiretorio);
        headerListagem.add(botaoSelecionarDiretorio);

        bodyListagem = new JPanel();
        bodyListagem.setLayout(new BoxLayout(bodyListagem, BoxLayout.Y_AXIS));
        bodyListagem.setBounds(100,210,400,200);
        bodyListagem.setBorder(new LineBorder(new Color(192,192,192)));

        this.add(statusConexao);
        this.add(botaoFecharConexao);
        this.add(headerListagem);
        this.add(bodyListagem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == botaoFecharConexao){
            //TODO fecha conexao e desabilita o botao de novo
            //TODO muda texto de volta para "Aguardando conexao na porta" + PORT
            System.out.println("conexao fechou \uD83D\uDE01");

        } else if(e.getSource() == botaoSelecionarDiretorio){
            JFileChooser exploradorArquivos = new JFileChooser();
            int res = exploradorArquivos.showOpenDialog(null);

            if(res == JFileChooser.APPROVE_OPTION) {

                while(bodyListagem.getComponentCount() > 0)
                    bodyListagem.remove(0);

                File arquivoSelecionado = exploradorArquivos.getSelectedFile().getAbsoluteFile();
                File pastaSelecionado = arquivoSelecionado.getParentFile();
                enderecoDiretorio.setText(pastaSelecionado.getAbsolutePath());

                File[] listaArquivos = pastaSelecionado.listFiles();
                for(int i = 0; i < listaArquivos.length; i++)
                    if(!listaArquivos[i].isDirectory() && !listaArquivos[i].isHidden())
                        bodyListagem.add(new JLabel(listaArquivos[i].getName()));

                bodyListagem.revalidate();
                bodyListagem.repaint();
            }
        }

    }

}