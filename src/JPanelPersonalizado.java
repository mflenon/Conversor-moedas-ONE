import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JPanelPersonalizado extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Filtro filtro = new Filtro();
	Conversor conversor = new Conversor();

	Image ImagensComponentes[] = new Image[5];

	int baseX = 0;
	int baseY = 30;
	Point comboBox = new Point(204, 63);
	Point comboBandeira = new Point(219, 73);
	Point comboMoeda = new Point(289, 96);
	Point inputString = new Point(25, 43);
	Point bandeiraInput = new Point(184, 20);
	Point moedaInput = new Point(254, 43);
	Point posicaoArrasta = new Point(0, 0);
	
	String titulo = "Conversor de moedas - ONE";
	boolean arrastar = false;
	int imagemDe = 0;
	int foco = 0;
	int cursor = 0;
	String outputValor = "";
	String inputQuantidade = "";
	int inputQuantidadeBandeira = 0;
	int outputValorBandeira = 1;
	String[] nomeMoedas = { "BRL", "USD", "EUR", "GBP", "ARS", "CLP" };

	public JPanelPersonalizado(JFrame tela) {
		this.setLayout(null);
		this.setBackground(new Color(32, 33, 36));
		this.setBounds(0, 0, 800, 600);
		this.setOpaque(false);
		carregaImagensComponentes();

		this.setFocusable(true);
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (foco == 1 && e.getKeyCode() != 8) {
//					System.out.println(e.getKeyCode());
					inputQuantidade += e.getKeyChar();
				} else if (foco == 1 && e.getKeyCode() == 8 && inputQuantidade.length() > 0) {
					inputQuantidade = inputQuantidade.substring(0, inputQuantidade.length() - 1);
				}

				inputQuantidade = filtro.filtro(inputQuantidade);

			}
		});

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				arrastar = false;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				PointerInfo mousePosition = MouseInfo.getPointerInfo();
				int mouseX = mousePosition.getLocation().x - Tela.telaX;
				System.out.println(mouseX);
				int mouseY = mousePosition.getLocation().y - Tela.telaY;
				System.out.println(mouseY);

				if (mouseX > (-9) && mouseX < 330 && mouseY > (-10) && mouseY < 20) {
					arrastar = true;
					posicaoArrasta = new Point(mouseX, mouseY);
				}
				
				if (mouseX > 334 && mouseX < 370 && mouseY > (-7) && mouseY < 15) {
					System.exit(0);					
				}
				
				if (foco == 3 && mouseX > baseX + 194 && mouseX < baseX + 352 && mouseY > baseY + 128
						&& mouseY < baseY + 416) {
					for (int i = 0; i < 6; i++) {
						if (mouseX > baseX + 194 && mouseX < baseX + 352 && mouseY > baseY + 128 + (i * 48)
								&& mouseY < baseY + 176 + (i * 48)) {
							outputValorBandeira = i;
							foco = 0;
						}
					}
				}

				else if (foco == 2 && mouseX > baseX + 194 && mouseX < baseX + 352 && mouseY > baseY + 58
						&& mouseY < baseY + 346) {
					for (int i = 0; i < 6; i++) {
						if (mouseX > baseX + 194 && mouseX < baseX + 352 && mouseY > baseY + 58 + (i * 48)
								&& mouseY < baseY + 106 + (i * 48)) {
							inputQuantidadeBandeira = i;
							foco = 0;
						}
					}
				}

				else if (mouseX > baseX && mouseX < baseX + 133 && mouseY > baseY && mouseY < baseY + 53) {
//					System.out.println("CLICKOU");
					foco = 1;
				} else if (mouseX > baseX + 135 && mouseX < baseX + 351 && mouseY > baseY && mouseY < baseY + 53) {
//					System.out.println("CLICKOU COMBO BOX");
					foco = 2;
				}

				else if (mouseX > baseX + 135 && mouseX < baseX + 351 && mouseY > baseY + 70 && mouseY < baseY + 123) {
//					System.out.println("SEGUNDO COMBO BOX");
					foco = 3;

				} else {
//					System.out.println("CLICKOU FORA");
					foco = 0;
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		atualizarTela(this, tela);
	}

	// imagem 370x70

	public void atualizarTela(JPanelPersonalizado panel, JFrame tela) {
		new Thread() {
			public void run() {
				while (true) {

					PointerInfo mousePosition = MouseInfo.getPointerInfo();
					int mouseX = mousePosition.getLocation().x - Tela.telaX;
					int mouseY = mousePosition.getLocation().y - Tela.telaY;
//					// Imagem com highlight
					if (mouseX > baseX && mouseX < baseX + 351 && mouseY > baseY && mouseY < baseY + 53) {
						imagemDe = 1;
					} else {
						imagemDe = 0;
					}

					outputValor = conversor.converte(filtro, inputQuantidade, inputQuantidadeBandeira,
							outputValorBandeira);
					
					if (arrastar == true) {
						mouseX = mousePosition.getLocation().x - (posicaoArrasta.x + 11);
						mouseY = mousePosition.getLocation().y - (posicaoArrasta.y + 11);
						tela.setLocation(mouseX, mouseY);
					}
					
					try {
						Thread.sleep(16);
						panel.repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	// Desenha o cursor.
	private void desenhaCursor(Graphics g) {
		if (foco == 1 && cursor < 50 && inputQuantidade.contains(".")) {
			g.drawString("|", baseX + 19 + (inputQuantidade.length() * 11), baseY + 41);
		} else if (foco == 1 && cursor < 50) {
			g.drawString("|", baseX + 25 + (inputQuantidade.length() * 11), baseY + 41);
		} else if (cursor > 100) {
			cursor = 0;
		}
		cursor += 1;
	}

	// Desenha os 2 campos, input e output.
	private void desenhaCampos(Graphics g) {
		g.drawImage(ImagensComponentes[imagemDe], baseX, baseY, this); // PRIMEIRO CAMPO
		g.drawImage(ImagensComponentes[4], baseX, baseY + 70, this); // SEGUNDO CAMPO
	}
	
	// Desenha a barra.
	private void desenhaBarra(Graphics g) {
		g.drawImage(ImagensComponentes[3], 0, 0, this); // BARRA
		g.setColor(new Color(189, 193, 198));
		g.setFont(new Font("Proxima Nova Bold", Font.PLAIN, 18));
		g.drawString(titulo, 15, 22);
	}

	// Limpa a tela.
	private void limpaTela(Graphics g) {
		g.setColor(new Color(0, 0, 0, 0));
//		g.clearRect(0, 0, 800, 600);
		g.fillRect(0, 0, 800, 600);
	}

	// Desenha bandeira e tipos das moedas.
	private void desenhaMoedas(Graphics g) {
		g.drawImage(Tela.iconBandeiras[inputQuantidadeBandeira], baseX + bandeiraInput.x, baseY + bandeiraInput.y,
				this);
		g.drawString(nomeMoedas[inputQuantidadeBandeira], baseX + moedaInput.x, baseY + moedaInput.y);

		g.drawImage(Tela.iconBandeiras[outputValorBandeira], baseX + bandeiraInput.x, baseY + bandeiraInput.y + 70,
				this);
		g.drawString(nomeMoedas[outputValorBandeira], baseX + moedaInput.x, baseY + moedaInput.y + 70);
	}

	// Desenha valores da String output e input.
	private void desenhaValores(Graphics g) {
		g.drawString(inputQuantidade, baseX + inputString.x, baseY + inputString.y);
		g.drawString(outputValor, baseX + inputString.x, baseY + inputString.y + 70);
	}

	// Mudar fonte do Graphics.
	private void fonteGraphics(Graphics g) {
		g.setColor(new Color(189, 193, 198));
		g.setFont(new Font("Proxima Nova Bold", Font.PLAIN, 20));
	}

	// Desenha os ComboBoxes.
	private void novoComboBox(Graphics g) {
		if (foco == 2) {
			g.drawImage(ImagensComponentes[2], baseX + comboBox.x, baseY + comboBox.y, this);
			for (int i = 0; i < 6; i++) {
				g.drawImage(Tela.iconBandeiras[i], baseX + comboBandeira.x, baseY + comboBandeira.y + (i * 48), this);
				g.drawString(nomeMoedas[i], baseX + comboMoeda.x, baseY + comboMoeda.y + (i * 48));
			}
		}

		if (foco == 3) {
			g.drawImage(ImagensComponentes[2], baseX + comboBox.x, baseY + comboBox.y + 70, this);
			for (int i = 0; i < 6; i++) {
				g.drawImage(Tela.iconBandeiras[i], baseX + comboBandeira.x, baseY + comboBandeira.y + 70 + (i * 48),
						this);
				g.drawString(nomeMoedas[i], baseX + comboMoeda.x, baseY + comboMoeda.y + 70 + (i * 48));
			}
		}
	}

	public void paint(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g.setColor(new Color(32, 33, 36));
		
		limpaTela(g);

		desenhaBarra(g);
		
		desenhaCampos(g);

		fonteGraphics(g);

		desenhaCursor(g);

		desenhaValores(g);

		desenhaMoedas(g);

		novoComboBox(g);
	}

	public void carregaImagensComponentes() {

		ImagensComponentes[0] = new ImageIcon((Tela.class.getResource("/novoField.png"))).getImage();
		ImagensComponentes[1] = new ImageIcon((Tela.class.getResource("/fieldHighlight.png"))).getImage();
		ImagensComponentes[2] = new ImageIcon((Tela.class.getResource("/ComboBox.png"))).getImage();
		ImagensComponentes[3] = new ImageIcon((Tela.class.getResource("/barra.png"))).getImage();
		ImagensComponentes[4] = new ImageIcon((Tela.class.getResource("/fieldArredondado.png"))).getImage();

	}

}
