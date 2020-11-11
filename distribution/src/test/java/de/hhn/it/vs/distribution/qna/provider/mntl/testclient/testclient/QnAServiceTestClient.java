package de.hhn.it.vs.distribution.qna.provider.mntl.testclient.testclient;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.UserManagementDemoClient;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.qna.provider.mntl.sockets.BDQnAServiceViaSockets;
import de.hhn.it.vs.distribution.testsupport.TestMode;

public class QnAServiceTestClient {

        private static final org.slf4j.Logger logger =
                org.slf4j.LoggerFactory.getLogger(QnAServiceTestClient.class);

        BDUserManagementService userManagementService;
        BDQnAService qnAService;


        public QnAServiceTestClient(TestMode mode) {
            instantiateBDClient(mode);
        }

        private void instantiateBDClient(TestMode mode) {
            switch (mode) {
                case MOCK:
                    userManagementService = new WnckUserManagementService();
                    qnAService = new WnckQnAService(userManagementService);
                    break;
                case SOCKET:
                    userManagementService = new BDUserManagementServiceViaSockets("localhost", 1099);
                    qnAService = new BDQnAServiceViaSockets("localhost", 1100);
                    break;
                case RMI:
                case REST:
                default:
                    throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
            }
        }

        private void runDemo() {
            UserManagementDemoClient userManagementDemoClient = new UserManagementDemoClient();
            userManagementDemoClient.runDemo(userManagementService);
        }

        public static void main(String[] args) {
            QnAServiceTestClient client = new QnAServiceTestClient(TestMode.SOCKET);
            client.runDemo();
        }

    }

