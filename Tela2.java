package pacotegame;

import java.awt.Color;
import javax.swing.*;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tela2 extends JFrame {
    private JPanel contentPane;
    private JButton btn1, btn2, btn3, btn4, btnIniciar, btnReset;
    private JButton[] btns;
    private List<Ordem> ordens = new ArrayList<>();
    private List<Integer> respostaUsuario = new ArrayList<>();
    private JLabel lbCount;
    private int rodada = 1;
    private boolean esperandoResposta = false;
    
    private final Color ROSA = new Color(255, 102, 255);
    private final Color AZUL = new Color(0, 153, 204);
    private final Color VERDE = new Color(102, 255, 10);
    private final Color AMARELO = new Color(255, 255, 51);
    
    private ExecutorService executor;
    private Random random = new Random();

    public static void main(String[] args) {
        Tela2 frame = new Tela2();
        frame.setVisible(true);
    }

    public Tela2() {
        setTitle("Jogo de Memória");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 400);
        setResizable(false);
        
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        btn1 = new JButton("");
        btn1.setBackground(Color.WHITE);
        btn1.setBounds(50, 50, 100, 100);
        contentPane.add(btn1);

        btn2 = new JButton("");
        btn2.setBackground(Color.WHITE);
        btn2.setBounds(160, 50, 100, 100);
        contentPane.add(btn2);

        btn3 = new JButton("");
        btn3.setBackground(Color.WHITE);
        btn3.setBounds(50, 160, 100, 100);
        contentPane.add(btn3);

        btn4 = new JButton("");
        btn4.setBackground(Color.WHITE);
        btn4.setBounds(160, 160, 100, 100);
        contentPane.add(btn4);

        btnIniciar = new JButton("INICIAR");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIniciar.setBounds(50, 280, 210, 40);
        contentPane.add(btnIniciar);

        btnReset = new JButton("RESET");
        btnReset.setFont(new Font("Arial", Font.BOLD, 14));
        btnReset.setBounds(270, 280, 80, 40);
        contentPane.add(btnReset);

        lbCount = new JLabel("Rodada: " + rodada);
        lbCount.setFont(new Font("Arial", Font.BOLD, 16));
        lbCount.setBounds(270, 30, 120, 30);
        contentPane.add(lbCount);

        btns = new JButton[]{btn1, btn2, btn3, btn4};
        
        btnIniciar.addActionListener(e -> iniciarJogo());
        btnReset.addActionListener(e -> resetJogo());

        for (int i = 0; i < btns.length; i++) {
            int index = i;
            btns[i].addActionListener(e -> processarClique(index));
        }

        executor = Executors.newCachedThreadPool();
    }

    private void iniciarJogo() {
        ordens.clear();
        respostaUsuario.clear();
        esperandoResposta = false;
        rodada = 1;
        lbCount.setText("Rodada: " + rodada);
        
        adicionarNovaOrdem();
        executarSequencia();
    }

    private void adicionarNovaOrdem() {
        int posicao = random.nextInt(4);
       Color cor;
        switch (posicao) {
            case 0:
                cor = ROSA;
                break;
            case 1:
                cor = AZUL;
                break;
            case 2:
                cor = VERDE;
                break;
            default:
                cor = AMARELO;
                break;
        };

         
        ordens.add(new Ordem(posicao, cor));
    }

    private void executarSequencia() {
        btnIniciar.setEnabled(false);
        btnReset.setEnabled(false);
        esperandoResposta = false;

        executor.execute(new TrocaCor(ordens, btns, () -> {
            esperandoResposta = true;
            respostaUsuario.clear();
            btnReset.setEnabled(true);
        }));
    }

    private void processarClique(int index) {
        if (!esperandoResposta) return;
        
        respostaUsuario.add(index);
        if (respostaUsuario.size() == ordens.size()) {
            validarResposta();
        }
    }

    private void validarResposta() {
        for (int i = 0; i < ordens.size(); i++) {
            if (ordens.get(i).posicao != respostaUsuario.get(i)) {
                JOptionPane.showMessageDialog(this, "Errou! Tente novamente.");
                iniciarJogo();
                return;
            }
        }
        
        JOptionPane.showMessageDialog(this, "Acertou! Próxima rodada.");
        rodada++;
        lbCount.setText("Rodada: " + rodada);
        
        adicionarNovaOrdem();
        executarSequencia();
    }

    private void resetJogo() {
        executor.shutdownNow();
        executor = Executors.newCachedThreadPool();
        iniciarJogo();
    }
}
