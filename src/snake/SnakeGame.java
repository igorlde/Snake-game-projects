
	package snake;

	import java.awt.Color;
	import java.awt.Dimension;
	import java.awt.Font;
	import java.awt.Graphics;
	import java.awt.Point;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.KeyEvent;
	import java.awt.event.KeyListener;
	import java.util.ArrayList;
	import java.util.Random;

	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.Timer;

	public class SnakeGame extends JPanel implements ActionListener, KeyListener {
		// Tamanho de cada celula
		private final int TILE_SIZE = 25;
		private final int GAME_WIDTH = 500;
		private final int GAME_HEIGHT = 500;
		private int DELAY = 100;// velocidade do jogo milisegundos
		private Timer timer;
		private ArrayList<Point> snake;
		private Point food;
		private static String direction = "D";// Direção inicial do jogo
		private boolean running = true;
		private int score = 0;

		public SnakeGame() {
			this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
			this.setBackground(Color.BLACK);
			this.setFocusable(true);
			this.addKeyListener(this);
			startGame();
		}

		private void startGame() {
			snake = new ArrayList<>();
			snake.add(new Point(5, 5)); // Cabeça inicial da cobra
			spawnFood(); // Gera a comida
			timer = new Timer(DELAY, this); // Timer controla a velocidade do jogo
			timer.start(); // Inicia o "loop" do jogo
		}

		private void spawnFood() {
			Random random = new Random();
			int x, y;
			do {
				x = random.nextInt(GAME_WIDTH / TILE_SIZE);
				y = random.nextInt(GAME_HEIGHT / TILE_SIZE);
				food = new Point(x, y);
			} while (isFoodOnSnake(x, y));
			food = new Point(x, y);
		}

		private boolean isFoodOnSnake(int x, int y) {
			for (Point segment : snake) {
				if (segment.x == x && segment.y == y) {
					return true;
				}
			}
			return false;
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (running) {
				// desenha comida
				g.setColor(Color.RED);
				g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
				// Desenha cobra
				g.setColor(Color.YELLOW);
				for (Point segment : snake) {
					g.fillRect(segment.x * TILE_SIZE, segment.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
				}
				// Contador de pontos.
				g.setColor(Color.BLUE);
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.drawString("PONTOS: " + score, 10, 30);

			} else {
				gameOver(g);
			}
		}

		private void gameOver(Graphics g) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.drawString("GAME OVER", GAME_WIDTH / 4, GAME_HEIGHT / 2);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString("Press R to Restart", GAME_WIDTH / 3, GAME_HEIGHT / 2 + 50); // Mensagem para reiniciar
		}

		private void restartGame() {
			running = true;
			direction = "D"; // Resetar direção
			startGame(); // Recriar o estado inicial do jogo
			score = 0;
		}

		private void move() {
			Point head = snake.get(0);
			Point newHead = switch (direction) {
			case "W" -> new Point(head.x, head.y - 1);
			case "S" -> new Point(head.x, head.y + 1);
			case "A" -> new Point(head.x - 1, head.y);
			case "D" -> new Point(head.x + 1, head.y);
			default -> head;
			};
			// Adicionar cabeça
			snake.add(0, newHead);
			// verificar se a comida foi comida.
			if (newHead.equals(food)) {
				spawnFood();
				score++;
			} else {
				// Remover o último segmento.
				snake.remove(snake.size() - 1);
			}
		}

		private void checkCoilisions() {
			Point head = snake.get(0);
			// Colisao com o proprio corpo.
			for (int i = 1; i < snake.size(); i++) {
				if (head.equals(snake.get(i))) {
					running = false;
				}
			}
			if (head.x < 0 || head.x >= GAME_WIDTH / TILE_SIZE || head.y < 0 || head.y >= GAME_HEIGHT / TILE_SIZE) {
				running = false;

			}
			if (!running) {
				timer.stop();
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (running) {
				move(); // Mover a cobra
				checkCoilisions(); // Verificar colisões
			}
			repaint(); // Repintar a tela a cada intervalo do timer
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (!running) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_R) {
					restartGame();
					return;
				}
			}

			int key = e.getKeyCode();

			// Altenar direção com base na tecla.
			if (key == KeyEvent.VK_W && !"S".equals(direction)) {
				direction = "W";
			} else if (key == KeyEvent.VK_S && !"W".equals(direction)) {
				direction = "S";
			} else if (key == KeyEvent.VK_A && !"D".equals(direction)) {
				direction = "A";
			} else if (key == KeyEvent.VK_D && !"A".equals(direction)) {
				direction = "D";
			}

		}

		@Override
		// Nessa operação nao sao utilizados
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		public static void main(String[] args) {
			JFrame frame = new JFrame("Snake game");
			SnakeGame gamePanel = new SnakeGame();
			frame.add(gamePanel);
			frame.pack();
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}


