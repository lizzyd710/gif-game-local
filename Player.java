import javax.swing.*;

public class Player
{
   private int score;
   public String[] hand = new String[5];
   private boolean judge, inHand;
   private String selectedGif;
   /**
   *The default constructor for a Player. Sets the player's score to zero and sets their judging status to false.
   */
   public Player()
   {
      score = 0;
      judge = false;
   }
   
   /**
   *Returns the score of the player.
   *@return The player's current score.
   */
   public int getScore()
   {
      return score;
   }
   
   /**
   *Sets the player's score to the int value passed.
   *@param x The new score of the player.
   */
   public void setScore(int x)
   {
      score = x;
   }
   
   /**
   *Returns true if the player is currently the judge, false otherwise.
   *@return 
   */
   public boolean isJudge()
   {
      return judge;
   }
   
   /**
   *Set's the player's judge status.
   *@param j The desired judge status of the player.
   */
   public void setJudge(boolean j)
   {
      judge = j;
   }
   
   /**
   *Checks if the gif with the path specified is in the player's hand.
   */
   public boolean inHand(String s)
   {
      for(int x = 0; x < hand.length; x++)
      {
         if(hand[x] == s || inHand == true)
            inHand = true;
         else
            inHand = false;
      }
      return inHand;
   }
   
   /**
   *Returns the path of the gif in the specified position of the player's hand.
   *@param x The hand position to be searched.
   *@return The path of the gif in the hand position specified.
   */
   public String getGifInHand(int x)
   {
      return hand[x];
   }
   
   /**
   *Sets the gif in the hand position specified to the gif at the path specified.
   *@param x The desired hand position of the new gif.
   *@param s The path of the gif to be added to the player's hand.
   */
   public void setGifInHand(int x, String s)
   {
      hand[x] = s;
   }
   
   /**
   *Returns the position in the player's hand of the gif with the path name specified.
   *@param s The path of the gif to be located in the player's hand.
   *@return The hand position of the gif at the path specified.
   */
   public int getGifHandPos(String s)
   {
      int pos = -1;
      for(int x = 0; x < hand.length; x++)
         if(hand[x] == s)
         {
            pos = x;
         }
      return pos;
   }
   
   /**
   *Chooses the gif at the path specified as the player's selection for the current round.
   *@param s The path of the gif to be selected.
   */
   public void selectGif(String s)
   {
      selectedGif = s;
   }
   
   /**
   *Returns the path of the gif the player selected for the current round.
   *@return The path of the player's selected gif.
   */
   public String getSelectedGif()
   {
      return selectedGif;
   }
}
