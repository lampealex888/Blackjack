/**
* The BlackjackCards class manages the hand value
* of a number of cards using the RandIndexQueue 
* class.
*
* @author  Alex Lampe
* @since   1-4-2022 
*/
public class BlackjackCards extends RandIndexQueue<Card>
{
    public BlackjackCards(int initSize) 
    {
        super(initSize);
    }

    public int getValue()
    {
        int packValue = 0;
        for (int i = 0; i < this.size(); i++)
        {
            packValue += this.get(i).value();
            if (packValue > 21) 
            {
                packValue -= this.get(i).value();
                packValue += this.get(i).value2();
            }
        }
        if (packValue > 21) 
        {
            packValue = 0;
            for (int i = 0; i < this.size(); i++)
            {
                packValue += this.get(i).value2();
            }
        }
        return packValue;
    }
}