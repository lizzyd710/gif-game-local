import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;

/***********************GLITCHY GIFS********************
what what does this mean
#dead
/******************************************************/

/*******************PROBLEMS TO FIX********************
When you select a gif for the second time, it freezes [FIXED]
After the first judge thing, one of the positions of the hand of the judge is null[FIXED]
/******************************************************/
public class GamePanel extends JPanel
{
   private int numitems, availablePrompts, judge, currentPlayer;
   private JLabel currentPromptLabel, firstGif, secondGif, thirdGif, fourthGif, fifthGif, label, currentPlayerLabel;
   private Prompt[] promptsArray;
   private String[] gifsArray, chosenGifsArray;
   private BufferedImage myImage;
   private Graphics myBuffer;
   private int[] randomGif, usedRandoms, chosenGifsHandPos;
   private JButton button, resetPrompts, select1, select2, select3, select4, select5;
   private Player[] players;
   private JLabel[] scoreLabels, chosenGif;
   private JButton[] selectWinningGifButton, selectGifButton, playerReadyButton;
   public GamePanel() throws Exception
   {
      setLayout(new BorderLayout());
      setOpaque(false);
      myImage =  new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
      myBuffer = myImage.getGraphics();
      chosenGifsHandPos = new int[5];
      /********Making the array of prompts****************/
      //Scanner infile = new Scanner(new File("prompts.txt"));
      Scanner testLines = new Scanner(new File("prompts.txt"));
      while(testLines.hasNextLine())
      {
         testLines.nextLine();
         numitems++;
      }
      testLines.close();
      promptsArray = new Prompt[numitems];
      Scanner infile = new Scanner(new File("prompts.txt"));
   
      for(int x = 0; x < promptsArray.length; x++)
         promptsArray[x] = new Prompt(infile.nextLine());
      availablePrompts = (promptsArray.length - 2);
     /****************end*********************************/
      
      /**************making the array of gifs**************/
      File gifFolder = new File("C:/Users/Lizzy/Google Drive/GIFs");
      FilenameFilter ImageFilter = 
         new FilenameFilter() {
            public boolean accept(File dir, String name) {
               return name.endsWith("gif");
            }
         };
      int numgifs = 0;
      for (final File f : gifFolder.listFiles(ImageFilter))
         numgifs++;
      
      int count = 0;
      gifsArray = new String[numgifs];
      for (final File f : gifFolder.listFiles(ImageFilter))
      {
         gifsArray[count] = f.getPath();
         count++;
      }
      /********************end****************************/
      int playerCount = Integer.parseInt(JOptionPane.showInputDialog("How many players? (From 3 to 6)"));
      /**********creating player array*********/
      players = new Player[playerCount];
      for(int x = 0; x < players.length; x++)
         players[x] = new Player();
      chosenGif = new JLabel[playerCount - 1];
      selectWinningGifButton = new JButton[chosenGif.length];
   
      for(int x = 0; x < selectWinningGifButton.length; x++)
      {
         chosenGif[x] = new JLabel("", SwingConstants.CENTER);
         selectWinningGifButton[x] = new JButton("Select");
         selectWinningGifButton[x].addActionListener(new SelectWinningGifListener());
      }
      
      int randomJudge = (int) (Math.random() * (players.length)); //chooses a random player to be the first judge
      players[randomJudge].setJudge(true);
      judge = randomJudge;
      currentPlayer = (judge + 1) % players.length;
      
      /*************making the scoreboard*************/
      scoreLabels = new JLabel[playerCount];
      playerReadyButton = new JButton[playerCount];
      Scoreboard scoreboard = new Scoreboard();
      add(scoreboard, BorderLayout.WEST);
      
      JPanel[] scoreboardRows = new JPanel[players.length];
      Font scoreboardFont = new Font("Serif", Font.BOLD, 20);
      for(int x = 0; x < scoreboardRows.length; x++)
      {
         scoreboardRows[x] = new JPanel();
         scoreboard.add(scoreboardRows[x]);
      }
      for(int x = 0; x < players.length; x++)
      {
         players[x] = new Player();
         if(x == randomJudge)
         {
            players[x].setJudge(true);
            scoreLabels[x] = new JLabel("(J) Player " + (x + 1) + ": " + players[x].getScore());
         }
         else if(x == currentPlayer)
            scoreLabels[x] = new JLabel(" > Player " + (x + 1) + ": " + players[x].getScore());
         else
            scoreLabels[x] = new JLabel("Player " + (x+1) + ": " + players[x].getScore());
         scoreboardRows[x].add(scoreLabels[x]);
         scoreLabels[x].setFont(scoreboardFont);
      }
      for(int x = 0; x < playerReadyButton.length; x++)
      {
         playerReadyButton[x] = new JButton("Ready");
         playerReadyButton[x].setFont(scoreboardFont);
         playerReadyButton[x].addActionListener(new PlayerReadyListener());
         playerReadyButton[x].setEnabled(false);
         scoreboardRows[x].add(playerReadyButton[x]);
      }
      /*****************************************/
      chosenGifsArray = new String[players.length - 1];
      usedRandoms = new int[chosenGifsArray.length];
   
      JPanel promptPanel = new JPanel();
      
      Font promptFont = new Font("Serif", Font.BOLD, 20);
      JLabel label = new JLabel("Prompt:");
      label.setFont(promptFont);
      promptPanel.add(label);
      currentPromptLabel = new JLabel("");
      currentPromptLabel.setFont(promptFont);
      promptPanel.add(currentPromptLabel);
   
      newPrompt(); 
      
      //generating random hand
      for(int x = 0; x < players.length; x++)
         generatePlayerHand(players[x]);
    
      add(promptPanel, BorderLayout.NORTH);
      
      //where the gifs in the players' hand are displayed
      HandPanel hand = new HandPanel();
      
      selectGifButton = new JButton[5];
      for(int x = 0; x < selectGifButton.length; x++)
      {
         selectGifButton[x] = new JButton("Select");
         selectGifButton[x].addActionListener(new SelectGifListener());
         selectGifButton[x].setEnabled(false);
      }
   
      JPanel gif1 = new JPanel();
      gif1.setLayout(new GridLayout(2, 1));
      firstGif = new JLabel("", SwingConstants.CENTER);
      gif1.add(firstGif);
      JPanel button1Panel = new JPanel();
      button1Panel.add(selectGifButton[0]);
      gif1.add(button1Panel);
      hand.add(gif1);
     
      JPanel gif2 = new JPanel();
      gif2.setLayout(new GridLayout(2, 1));
      secondGif = new JLabel("", SwingConstants.CENTER);
      gif2.add(secondGif);
      JPanel button2Panel = new JPanel();
      button2Panel.add(selectGifButton[1]);
      gif2.add(button2Panel);
      hand.add(gif2);
      
      JPanel gif3 = new JPanel();
      gif3.setLayout(new GridLayout(2, 1));
      thirdGif = new JLabel("", SwingConstants.CENTER);
      gif3.add(thirdGif);
      JPanel button3Panel = new JPanel();
      button3Panel.add(selectGifButton[2]);
      gif3.add(button3Panel);
      hand.add(gif3);
               
      JPanel gif4 = new JPanel();
      gif4.setLayout(new GridLayout(2, 1));
      fourthGif = new JLabel("", SwingConstants.CENTER);
      gif4.add(fourthGif);
      JPanel button4Panel = new JPanel();
      button4Panel.add(selectGifButton[3]);
      gif4.add(button4Panel);
      hand.add(gif4);
      
      JPanel gif5 = new JPanel();
      gif5.setLayout(new GridLayout(2, 1));
      fifthGif = new JLabel("", SwingConstants.CENTER);
      gif5.add(fifthGif);
      JPanel button5Panel = new JPanel();
      button5Panel.add(selectGifButton[4]);
      gif5.add(button5Panel);
      hand.add(gif5);
      
      add(hand, BorderLayout.SOUTH);
      /*************************end****************************/
      
      /*****the panels where the chosen gifs are displayed*****/
      JPanel chosenGifs = new JPanel();
      chosenGifs.setLayout(new GridLayout(1, 5));
      
      JPanel[] chosenGifPanel = new JPanel[players.length - 1];
      JPanel[] buttonHolder = new JPanel[chosenGifPanel.length];
      
      for(int x = 0; x < chosenGifPanel.length; x++)
      {
         chosenGifPanel[x] = new JPanel();
         chosenGifPanel[x].setLayout(new GridLayout(2, 1));
         chosenGifPanel[x].add(chosenGif[x]);
         buttonHolder[x] = new JPanel();
         selectWinningGifButton[x].setEnabled(false);
         buttonHolder[x].add(selectWinningGifButton[x]);
         chosenGifPanel[x].add(buttonHolder[x]);
         chosenGifs.add(chosenGifPanel[x]);
      }
      
      add(chosenGifs, BorderLayout.CENTER);
      /**********Enables the player Ready button for the current player**********/
      playerReadyButton[currentPlayer].setEnabled(true);
   }
   private class NewPromptListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         newPrompt();
         rotateJudge();
      }
   }
   private class resetPromptListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
      //canvas array and setUsed to false for all prompts
      //and maybe change the availablePrompts initiall value. two of the prompts didn't show up first cycle through
         for(int x = 0; x < promptsArray.length; x++)
            promptsArray[x].setUsed(false);
         availablePrompts = (promptsArray.length - 2);
         button.setEnabled(true);
      }
   }
   private class SelectGifListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(e.getSource() == selectGifButton[0])
            selectGif(players[currentPlayer].getGifInHand(0));
         else if(e.getSource() == selectGifButton[1])
            selectGif(players[currentPlayer].getGifInHand(1));
         else if(e.getSource() == selectGifButton[2])
            selectGif(players[currentPlayer].getGifInHand(2));
         else if(e.getSource() == selectGifButton[3])
            selectGif(players[currentPlayer].getGifInHand(3));
         else if(e.getSource() == selectGifButton[4])
            selectGif(players[currentPlayer].getGifInHand(4));
            
         firstGif.setIcon(null);
         secondGif.setIcon(null);
         thirdGif.setIcon(null);
         fourthGif.setIcon(null);
         fifthGif.setIcon(null);
         
         currentPlayer++;
         currentPlayer = (currentPlayer % players.length);
         if(players[currentPlayer].isJudge() == true)
            showJudgeScreen();
         else
         {
            for(int x = 0; x < players.length; x++)
               scoreLabels[x].setText("Player " + (x + 1) + ": " + players[x].getScore());
            scoreLabels[currentPlayer].setText("> Player " + (currentPlayer + 1) + ": " + players[currentPlayer].getScore());
            scoreLabels[judge].setText("(J) Player " + (judge + 1) + ": " + players[judge].getScore());
            playerReadyButton[currentPlayer].setEnabled(true);
            for(int x = 0; x < selectGifButton.length; x++)
               selectGifButton[x].setEnabled(false);
         }
      }
   }
   private class SelectWinningGifListener implements ActionListener 
   {
      public void actionPerformed(ActionEvent e)
      {
         for(int x = 0; x < players.length - 1; x++)
         {
            newGif(players[usedRandoms[x]]);
         }
         players[judge].setJudge(false);
         if(e.getSource() == selectWinningGifButton[0])
         {
            players[usedRandoms[0]].setScore(players[usedRandoms[0]].getScore() + 1);
            players[usedRandoms[0]].setJudge(true);
            judge = usedRandoms[0];
         }            
         else if(e.getSource() == selectWinningGifButton[1])
         {
            players[usedRandoms[1]].setScore(players[usedRandoms[1]].getScore() + 1);
            players[usedRandoms[1]].setJudge(true);
            judge = usedRandoms[1];
         }
         else if(selectWinningGifButton.length > 2 && e.getSource() == selectWinningGifButton[2])
         {
            players[usedRandoms[2]].setScore(players[usedRandoms[2]].getScore() + 1);
            players[usedRandoms[2]].setJudge(true);
            judge = usedRandoms[2];
         }
         else if(selectWinningGifButton.length > 3 && e.getSource() == selectWinningGifButton[3])
         {
            players[usedRandoms[3]].setScore(players[usedRandoms[3]].getScore() + 1);
            players[usedRandoms[3]].setJudge(true);
            judge = usedRandoms[3];
         }
         else if(selectWinningGifButton.length > 4 && e.getSource() == selectWinningGifButton[4])
         {
            players[usedRandoms[4]].setScore(players[usedRandoms[4]].getScore() + 1);
            players[usedRandoms[4]].setJudge(true);
            judge = usedRandoms[4];
         }
         else if(selectWinningGifButton.length > 5 && e.getSource() == selectWinningGifButton[5])
         {
            players[usedRandoms[5]].setScore(players[usedRandoms[5]].getScore() + 1);
            players[usedRandoms[5]].setJudge(true);
            judge = usedRandoms[5];
         }
         players[currentPlayer].setJudge(false);
         currentPlayer = (judge + 1) % players.length;
            
         updateScore();
         newPrompt();
         if(availablePrompts != 0)
         {
            for(int x = 0; x < selectWinningGifButton.length; x++)
               selectWinningGifButton[x].setEnabled(false);
            playerReadyButton[currentPlayer].setEnabled(true);
            for(int x = 0; x < selectGifButton.length; x++)
               selectGifButton[x].setEnabled(false);
            for(int x = 0; x < usedRandoms.length; x++)
            {
               chosenGif[x].setIcon(null);
               usedRandoms[x] = -1;
               chosenGifsArray[x] = null;
            }
         }
      }
   }
   private class PlayerReadyListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if(e.getSource() == playerReadyButton[0])
         {
            playerReady(players[0]);
         }            
         else if(e.getSource() == playerReadyButton[1])
         {
            playerReady(players[1]);
         }
         else if(e.getSource() == playerReadyButton[2])
         {
            playerReady(players[2]);
         }
         else if(playerReadyButton.length > 3 && e.getSource() == playerReadyButton[3])
         {
            playerReady(players[3]);
         }
         else if(selectWinningGifButton.length > 4 && e.getSource() == playerReadyButton[4])
         {
            playerReady(players[4]);
         }
         else if(selectWinningGifButton.length >= 5 && e.getSource() == playerReadyButton[5])
            playerReady(players[5]);
      }
   }
   
   /**
   *Sets a new and random prompt.
   *Checks if there are still availablePrompts, and if there is, generates a random integer from zero to the length of the prompt array.
   *Then, the method checks if the prompt in the index of the random number in promptArray has already been used.
   *If it has been used already, it generates a new random number and repeats the process. If it hasn't been used, it sets the prompt at the index of the random number in the prompt array as used.
   *Then the text of the JLabel that displays the prompt is set to the random prompt, and availablePrompts is decreased by one.
   *If there are no more available prompts, the text of the JLabel that displays the prompt is set to "Game over." and disables all  JButtons and sets the icons of all of the JLabels holding the gifs to null.
   */
   public void newPrompt()
   {
      if(availablePrompts > 0)
      {
         int q = (int) (Math.random() * (promptsArray.length));
         while(promptsArray[q].wasUsed() == true)
            q = (int) (Math.random() * (promptsArray.length));
         promptsArray[q].setUsed(true);
         currentPromptLabel.setText("" + promptsArray[q].getPrompt());
         availablePrompts--;
      }
      else
      {
      //maybe do like No More Prompts or reset button
         currentPromptLabel.setText("Game over.");
         for(int x = 0; x < playerReadyButton.length; x++)
            playerReadyButton[x].setEnabled(false);
         for(int x = 0; x < selectGifButton.length; x++)
            selectGifButton[x].setEnabled(false);
         firstGif.setIcon(null);
         secondGif.setIcon(null);
         thirdGif.setIcon(null);
         fourthGif.setIcon(null);
         fifthGif.setIcon(null);
         
      }
   }
   
   /**
   *Assigns a new gif to the empty hand slot of the player argument.
   *Goes through the player argument's hand and checks for any empty slots. Then, the method generates a random number from zero to the length of the gifsArray.
   *The empty slot of the player's hand is assigned the random gif.
   *@param p The player to be assigned a new gif.
   */
   public void newGif(Player p)
   {
      int emptyHandSlot = -1;
      for(int x = 0; x < p.hand.length; x++)
      {
         if(p.hand[x] == null)
         {
            emptyHandSlot = x;
         }
      }
      //selecting random index numbers
      int rand = (int) (Math.random() * (gifsArray.length));
      //determines which gif is to be replaced
      p.hand[emptyHandSlot] = gifsArray[rand]; //error here check it out
   }
   public void updateScore()
   {
      for(int x = 0; x < players.length; x++)
      {
         if(players[x].isJudge())
            scoreLabels[x].setText("(J) Player " + (x + 1) + ": " + players[x].getScore());
         else if(x == currentPlayer)
            scoreLabels[x].setText(" > Player " + (x + 1) + ": " + players[x].getScore());
         else
            scoreLabels[x].setText("Player " + (x + 1) + ": " + players[x].getScore());
      }
   }
   public void rotateJudge()
   {
      judge = (judge + 1) % players.length;
      for(int x = 0; x < players.length; x++)
         players[x].setJudge(false);
      players[judge % players.length].setJudge(true);
      for(int x = 0; x < players.length; x++)
      {
         if(players[x].isJudge() == true)
            scoreLabels[x].setText("Judge> Player " + (x + 1) + ": " + players[x].getScore());
         else
            scoreLabels[x].setText("Player " + (x+1) + ": " + players[x].getScore());
      }
   }
   public void selectGif(String s)
   {
      players[currentPlayer].selectGif(s);
      players[currentPlayer].setGifInHand(players[currentPlayer].getGifHandPos(s), null);
   }
   public void generatePlayerHand(Player p) //not sure if I will need this. or maybe i already usd it as a helper method
   {
      int[] randomGif = new int[5];
      for(int x = 0; x < 5; x++)
      {
         int rand = (int) (Math.random() * (gifsArray.length));
         randomGif[x] = rand;
      }
      for(int x = 0; x < p.hand.length; x++)
      {
         p.hand[x] = (gifsArray[randomGif[x]]);
      }
   }
   public void showPlayerHand(Player p)
   {
      firstGif.setIcon(new ImageIcon(p.hand[0]));
      secondGif.setIcon(new ImageIcon(p.hand[1]));
      thirdGif.setIcon(new ImageIcon(p.hand[2]));
      fourthGif.setIcon(new ImageIcon(p.hand[3]));
      fifthGif.setIcon(new ImageIcon(p.hand[4]));
      
      for(int x = 0; x < selectGifButton.length; x++)
         selectGifButton[x].setEnabled(true);
   }
   public void showJudgeScreen()
   {
      firstGif.setIcon(null);
      secondGif.setIcon(null);
      thirdGif.setIcon(null);
      fourthGif.setIcon(null);
      fifthGif.setIcon(null);
      
      for(int x = 0; x < selectGifButton.length; x++)
         selectGifButton[x].setEnabled(false);
      for(int x = 0; x < selectWinningGifButton.length; x++)
         selectWinningGifButton[x].setEnabled(true);
      
      for(int x = 0; x < players.length; x++)
         scoreLabels[x].setText("Player " + (x + 1) + ": " + players[x].getScore());
      scoreLabels[currentPlayer].setText("> (J) Player " + (currentPlayer + 1) + ": " + players[currentPlayer].getScore());
      
      //scrambles the selected gifs 
      //the numbers stored in usedRandoms are the player index numbers
      for(int x = 0; x < usedRandoms.length; x++)
      {
         int random = (int)(Math.random() * players.length);
         while(judge == random)
            random = (int)(Math.random() * players.length);
         for(int y = x - 1; y >= 0; y--)
         {
            while(usedRandoms[y] == random)
            {
               random = (int)(Math.random() * players.length);
               while(judge == random)
                  random = (int)(Math.random() * players.length);
            }
         }
         usedRandoms[x] = random;
         chosenGif[x].setIcon(new ImageIcon(players[usedRandoms[x]].getSelectedGif()));
      }
      //end of scrambling 
   }
   public void playerReady(Player p)
   {
      for(int x = 0; x < playerReadyButton.length; x++)
         playerReadyButton[x].setEnabled(false);
      showPlayerHand(p);
   }
}
