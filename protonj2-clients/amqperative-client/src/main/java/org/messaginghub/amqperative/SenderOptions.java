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
package org.messaginghub.amqperative;

/**
 * Options that control the behavior of a {@link Sender} created from them.
 */
public class SenderOptions {

    private String linkName;
    private boolean dynamic;
    private boolean autoSettle;

    public SenderOptions() {
    }

    public SenderOptions setLinkName(String linkName) {
        this.linkName = linkName;
        return this;
    }

    public String getLinkName() {
        return linkName;
    }

    public SenderOptions setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
        return this;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    /**
     * Sets whether sent deliveries should be automatically locally-settled once
     * they have become remotely-settled by the receiving peer.
     *
     * True by default.
     *
     * @param autoSettle
     *            whether deliveries should be auto settled locally after being
     *            settled by the receiver
     * @return the sender
     */
    public SenderOptions setAutoSettle(boolean autoSettle) {
        this.autoSettle = autoSettle;
        return this;
    }

    /**
     * Get whether the receiver is auto settling deliveries.
     *
     * @return whether deliveries should be auto settled locally after being settled
     *         by the receiver
     * @see #setAutoSettle(boolean)
     */
    public boolean isAutoSettle() {
        return autoSettle;
    }
}
