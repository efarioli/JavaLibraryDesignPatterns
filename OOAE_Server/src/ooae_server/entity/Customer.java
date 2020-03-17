package ooae_server.entity;

import java.util.HashMap;
import ooae_library.data_transfer_object.CustomerDTO;
import ooae_library.data_transfer_object.OrderDTO;
import ooae_server.database.CustomerGateway;

/**
 *
 * @author gdm1
 */
public class Customer
{
    private int customerId;
    private String name;
    private HashMap<Integer, Order> orders;
    private String password;
    private String userName;

    public Customer()
    {
    }

    public Customer(CustomerDTO cust)
    {
        this.customerId = cust.getCustomerId();
        this.name = cust.getName();
        this.password = cust.getPassword();
        this.userName = cust.getUserName();
        
        if (cust.getOrders() != null)
        {
            orders = new HashMap<>(cust.getOrders().size());
            for (OrderDTO orderDTO : cust.getOrders().values())
            {
                Order order = new Order(orderDTO);
                order.setCustomer(this);
                orders.put(order.getOrderId(), order);
            }
        }
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public String getName()
    {
        return name;
    }

    public HashMap<Integer, Order> getOrders()
    {
        return orders;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUserName()
    {
        return userName;
    }

    public Customer login() throws Exception
    {
        CustomerGateway customerTable = new CustomerGateway();
        
        Customer user = customerTable.findCustomer(this);
        
        if (user.getCustomerId() == -1 || !user.getPassword().equals(getPassword()))
        {
            user = new Customer();
            user.setCustomerId(-1);
        }
        
        user.setPassword("");
        return user;
    }

    public void setCustomerId(int customerId)
    {
        this.customerId = customerId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setOrders(HashMap<Integer, Order> orders)
    {
        this.orders = orders;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }
}
