package de.hhn.it.vs.distribution.pyle.provider.pyle.rmi;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.distribution.pyle.rmi.BDpyleServiceViaRmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DemoClient {
public static void main(String[] args)
{
    Token token = new Token();
    Area area = new Area();
    String host = (args.length < 1) ? null : args[0];
    try
    {
        long response = 0;
        //Registry registry = LocateRegistry.getRegistry(host);
        BDpyleServiceViaRmi bDpyleServiceViaRmi = new BDpyleServiceViaRmi("127.0.0.1", 1099);
       if (bDpyleServiceViaRmi.serviceKommuication()) {
            response = bDpyleServiceViaRmi.createArea(token, area);
           System.out.println("Response: "+response);
       }
       else
       {
           System.out.println("Client is not connect to the service");
       }

    }catch (RemoteException re)
    {
        System.out.println(re.toString());
    } catch (ServiceNotAvailableException e) {
        e.toString();
    } catch (InvalidTokenException e) {
        e.toString();
    } catch (IllegalParameterException e) {
        e.toString();
    }
}
}
