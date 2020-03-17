package ooae_server.state;

/**
 *
 * @author gdm1
 */
public class OrderState
{
    private String state;

    public OrderState(String state)
    {
        this.state = state;
    }
    
    @Override
    public String toString()
    {
        return state;
    }
    
}
