package brick_Bracker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;
import java.awt.Rectangle;

/**
 *
 * @author Mateus
 */
public class Gameplay extends JPanel implements KeyListener, ActionListener{ //KeyListener para detectar as setas do teclado
    private boolean play = false; 
    private int score = 0;
    
    //Total de tijolos
    private int totalBricks = 21;
    
    //Tempo de movimentação
    private int delay = 8;
    private Timer timer;
    
    
    private int playerX = 310;
    
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    
    private MapGenerator map;
    
    //Construtor
    public Gameplay(){
        //Definindo a ordem de linha e colunas de tijolos
        map = new MapGenerator(3, 7);
        
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay , this);
        timer.start(); 
    
        
    }
    
       
    public void paint(Graphics g){
        //Fundo da tela
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);
        
        //Desenhando os tijolos no Mapa
        map.draw((Graphics2D)g);
        
        //Bordas
        g.setColor(Color.yellow);
        g.fillRect(0,0, 3, 592);
        //Borda da esquerda
        g.fillRect(0, 0, 690, 3);
        //Borda da direita
        g.fillRect(681, 0, 3, 592);
        
        //Contador da pontuação
        g.setColor(Color.white);
        g.setFont(new Font("Serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);
        
        
        //Raquete
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);
        
        //Bola
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);
        
        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            //Se passou, é fim de jogo, escrevendo mensagem com aviso e pontunação
            g.setColor(Color.GREEN);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Vitória. Pontuação: " + score, 190, 300);
            
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Aperte ENTER para jogar novamente", 197, 350);         
                       
        }
        
        //Verificando se a bola passou pela raquete no canto inferior
        if(ballposY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            //Se passou, é fim de jogo, escrevendo mensagem com aviso e pontunação
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Fim de jogo. Pontuação: " + score, 190, 300);
                                               
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Aperte ENTER para jogar novamente", 197, 350);
        
        }
        g.dispose();
    }
    
    //Métodos Abstratos
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYdir = -ballYdir;
            }
            A: for(int i=0; i< map.map.length; i++){
                for(int j = 0; j < map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                    int brickX = j * map.brickWidth + 80; 
                    int brickY = i * map.brickHeight + 50;
                    int brickWidth = map.brickWidth;
                    int brickHeight = map.brickWidth;
                    
                    Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                    Rectangle ballRect = new Rectangle (ballposX, ballposY, 20, 20);
                    Rectangle brickRect = rect;
                    
                    //Condição para diminuir a quantidade de tijolos
                        if (ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;
                        
                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
        
        //Verificando onde a bola está tocando as bordas
        
            ballposX += ballXdir;
            ballposY += ballYdir;
            //Borda da esquerda
            if(ballposX < 0 ){
                ballXdir = -ballXdir;
            }
            //Borda de cima
            if(ballposY < 0 ){
                ballYdir = -ballYdir;
            }
            //Borda da direita
            if(ballposX > 670 ){
                ballXdir = -ballXdir;
            }    
        }
        repaint();
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
    //Capturando o movimento da tecla (seta) pra direita
    @Override
    public void keyPressed(KeyEvent e) {
        //Limitando a raquete no eixo x da tecla para não sair do JPanel
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 581){
                playerX = 581;
            } else {
                moveRight();
            }
            
        }
        //Capturando o movimento da tecla (seta) pra esquerda
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            //Limitando eixo x da tecla para não sair do JPanel
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            } else {
                moveLeft();
            }
        }
    }
        
        //Caso pressionado a tecla ENTER, restaura a definições para as iniciais
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3,7);
                
                repaint();
            }
        }
        
        

    
    }
    
    //Definindo a quantida de pixel que a raquete vai se mover
    public void moveRight(){
        play = true;
        playerX += 20;
    }
    public void moveLeft(){
        play = true;
        playerX += -20;
    }

}
