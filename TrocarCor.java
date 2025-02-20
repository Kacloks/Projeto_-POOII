package pacotegame;

import java.awt.Color;
import java.util.List;
import javax.swing.JButton;

public class TrocaCor implements Runnable {
    private final List<Ordem> ordens;
    private final JButton[] btns;
    private final Runnable callback;

    public TrocaCor(List<Ordem> ordens, JButton[] btns, Runnable callback) {
        this.ordens = ordens;
        this.btns = btns;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            for (Ordem ordem : ordens) {
                JButton btn = btns[ordem.posicao];
                Color originalColor = btn.getBackground();
                btn.setBackground(ordem.cor);
                Thread.sleep(800);
                btn.setBackground(originalColor);
                Thread.sleep(200);
            }
            callback.run();
        } catch (InterruptedException ignored) {}
    }
}

