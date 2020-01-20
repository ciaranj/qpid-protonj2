/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.messaginghub.amqperative.example;

import org.messaginghub.amqperative.Client;
import org.messaginghub.amqperative.ClientOptions;
import org.messaginghub.amqperative.Connection;
import org.messaginghub.amqperative.Delivery;
import org.messaginghub.amqperative.Message;
import org.messaginghub.amqperative.Receiver;
import org.messaginghub.amqperative.ReceiverOptions;
import org.messaginghub.amqperative.Sender;

/**
 * Listens for Requests on a request Queue and sends a response.
 */
public class Respond {

    public static void main(String[] args) throws Exception {

        String brokerHost = "localhost";
        int brokerPort = 5672;
        String address = "examples";

        ClientOptions options = new ClientOptions();
        Client client = Client.create(options);

        try {
            Connection connection = client.connect(brokerHost, brokerPort);

            ReceiverOptions receiverOptions = new ReceiverOptions();
            receiverOptions.sourceOptions().capabilities("queue");

            Receiver receiver = connection.openReceiver(address, receiverOptions);

            Delivery request = receiver.receive(30_000);
            if (request != null) {
                Message<String> received = request.message();
                System.out.println(received.body());

                String replyAddress = received.replyTo();
                if (replyAddress != null) {
                    Sender sender = connection.openSender(address);
                    sender.send(Message.create("Response").durable(true));
                    sender.close();
                }
            }
        } catch (Exception exp) {
            System.out.println("Caught exception, exiting.");
            exp.printStackTrace(System.out);
            System.exit(1);
        } finally {
            client.close().get();
        }
    }
}
