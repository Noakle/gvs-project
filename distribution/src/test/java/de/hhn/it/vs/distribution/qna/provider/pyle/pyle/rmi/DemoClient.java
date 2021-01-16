package de.hhn.it.vs.distribution.qna.provider.pyle.pyle.rmi;

/*public class DemoClient {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(DemoClient.class);
    private RmipyleService rmipyleService;
    private BDUserManagementService bdUserManagementService;


    public DemoClient(final TestMode mode)
    {
        instantiateBDClient(mode);
    }

    private void instantiateBDClient(TestMode mode) {
        switch(mode)
        {
            case MOCK:
                bdUserManagementService = new WnckUserManagementService();
               // rmipyleService = new WnckQnAService(bdUserManagementService);
                break;
            case RMI:
                bdUserManagementService = new BDUserManagementServiceViaRmi("localhost", 1099);
                rmipyleService = new BDpyleServiceViaRmi("localhost", 1099);
                break;
            case REST:
            default:
                throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);

        }
    }

    public static void main(String[] args) throws IllegalParameterException,
            ServiceNotAvailableException, UserNameAlreadyAssignedException, InvalidTokenException, RemoteException {
        DemoClient demoClient = new DemoClient(TestMode.RMI);
        demoClient.runDemo();
    }
    private void runDemo() throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException, RemoteException {
        QnAServiceDemoClient commonDemo = new QnAServiceDemoClient();
        commonDemo.runDemo(bdUserManagementService, (BDQnAService) rmipyleService);
    }


}*/
