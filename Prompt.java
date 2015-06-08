public class Prompt
{
   private boolean used;
   private String promptText;

   public Prompt(String prompt)
   {
      promptText = prompt;
      used = false;
   }
   /**
   *Checks if the prompt was used already.
   *@return Value of the prompt's used field.
   */
   public boolean wasUsed()
   {
      return used;
   }
   /**
   *Sets the prompt's used field to the argument passed.
   *@param u The desired value of the prompt's used field.
   */
   public void setUsed(boolean u)
   {
      used = u;
   }
   /**
   *Returns the actual prompt string.
   *@return The prompt's text.
   */
   public String getPrompt()
   {
      return promptText;
   }
}
