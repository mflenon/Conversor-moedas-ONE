import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Tela extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int telaX = 0;
	static int telaY = 0;
	static Image[] iconBandeiras;

	Tela() {
		//this.setSize(385, 177);
		this.setSize(385, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setTitle("Conversor de moedas - ONE ");
		this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 0 ,0));
		
		
		JPanelPersonalizado panelPersonalizado = new JPanelPersonalizado(this);

		// Array para pegar as imagens das bandeiras.
		carregarBandeiras();

		this.add(panelPersonalizado);
		this.setVisible(true);

		atualizarTela(this);

	}

	// Metodo para carregar as imagens das bandeiras.
	private void carregarBandeiras() {

		String[] nomesBandeiras = { "brasil.png", "usa.png", "ue.png", "reino-unido.png", "argentina.png",
				"chile.png" };

		iconBandeiras = new Image[6];

		for (int i = 0; i < 6; i++) {
			iconBandeiras[i] = new ImageIcon((Tela.class.getResource("/" + nomesBandeiras[i]))).getImage()
					.getScaledInstance(45, 30, java.awt.Image.SCALE_SMOOTH);
		}

	}

	// Metodo para não mudar as coordenadas.
	private void atualizarTela(JFrame frame) {
		new Thread() {
			public void run() {
				while (true) {

					telaX = frame.getLocation().x + 11;
					telaY = frame.getLocation().y + 11;

					try {
						Thread.sleep(16);
						frame.repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

}
