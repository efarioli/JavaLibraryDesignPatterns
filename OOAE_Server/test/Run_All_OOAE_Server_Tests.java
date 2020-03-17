

import ooae_server.AdminClientResponderTest;
import ooae_server.CustomerClientResponderTest;
import ooae_server.database.GatewayTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author gdm1
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{
    AdminClientResponderTest.class,
    CustomerClientResponderTest.class,
    GatewayTest.class
})
public class Run_All_OOAE_Server_Tests
{
    
}
