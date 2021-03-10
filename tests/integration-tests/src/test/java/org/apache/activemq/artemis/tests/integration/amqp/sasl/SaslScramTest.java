/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.tests.integration.amqp.sasl;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.spi.core.security.ActiveMQJAASSecurityManager;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.exceptions.JMSSecuritySaslException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test SASL-SCRAM Support
 */
public class SaslScramTest {

   private static EmbeddedActiveMQ BROKER;

   @BeforeClass
   public static void startBroker() throws Exception {
      String loginConfPath = new File(SaslScramTest.class.getResource("/login.config").toURI()).getAbsolutePath();
      System.out.println(loginConfPath);
      System.setProperty("java.security.auth.login.config", loginConfPath);
      BROKER = new EmbeddedActiveMQ();
      BROKER.setConfigResourcePath(SaslScramTest.class.getResource("/broker-saslscram.xml").toExternalForm());
      BROKER.setSecurityManager(new ActiveMQJAASSecurityManager("artemis-sasl-scram"));
      BROKER.start();
   }

   @AfterClass
   public static void shutdownBroker() throws Exception {
      BROKER.stop();
   }

   @Test
   public void testUnencryptedWorksWithAllMechanism() throws JMSException {
      sendRcv("SCRAM-SHA-1", "hello", "ogre1234");
      sendRcv("SCRAM-SHA-256", "hello", "ogre1234");
   }

   @Test(expected = JMSSecuritySaslException.class)
   public void testEncryptedWorksOnlyWithMechanism() throws JMSException {
      sendRcv("SCRAM-SHA-1", "test", "test");
   }

   @Test
   public void testEncryptedWorksWithMechanism() throws JMSException {
      sendRcv("SCRAM-SHA-256", "test", "test");
   }

   private void sendRcv(String method, String username, String password) throws JMSException {
      ConnectionFactory connectionFactory =
               new JmsConnectionFactory("amqp://localhost:5672?amqp.saslMechanisms=" + method);
      try (Connection connection = connectionFactory.createConnection(username, password)) {
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         Queue queue = session.createQueue("exampleQueue");
         MessageProducer sender = session.createProducer(queue);
         String text = "Hello " + method;
         sender.send(session.createTextMessage(text));
         connection.start();
         MessageConsumer consumer = session.createConsumer(queue);
         TextMessage m = (TextMessage) consumer.receive(5000);
         assertEquals(text, m.getText());
      }
   }
}