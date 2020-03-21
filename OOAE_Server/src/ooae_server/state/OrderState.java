package ooae_server.state;

import ooae_server.entity.Order;

/**
 *
 * @author gdm1
 */
public abstract class OrderState
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

    public void setState(String state)
    {
        this.state = state;
    }
    
    

    public abstract Order ship(Order order) throws Exception;

    public abstract Order cancel(Order order) throws Exception;

}
