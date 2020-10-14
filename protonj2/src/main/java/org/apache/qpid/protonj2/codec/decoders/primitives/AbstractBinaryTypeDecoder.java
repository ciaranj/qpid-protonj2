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
package org.apache.qpid.protonj2.codec.decoders.primitives;

import java.io.IOException;
import java.io.InputStream;

import org.apache.qpid.protonj2.buffer.ProtonBuffer;
import org.apache.qpid.protonj2.buffer.ProtonByteBufferAllocator;
import org.apache.qpid.protonj2.codec.DecodeException;
import org.apache.qpid.protonj2.codec.DecoderState;
import org.apache.qpid.protonj2.codec.StreamDecoderState;
import org.apache.qpid.protonj2.codec.decoders.AbstractPrimitiveTypeDecoder;
import org.apache.qpid.protonj2.types.Binary;

/**
 * Base class for the various Binary type decoders used to read AMQP Binary values.
 */
public abstract class AbstractBinaryTypeDecoder extends AbstractPrimitiveTypeDecoder<Binary> implements BinaryTypeDecoder {

    @Override
    public Binary readValue(ProtonBuffer buffer, DecoderState state) throws DecodeException {
        return new Binary(readValueAsBuffer(buffer, state));
    }

    public ProtonBuffer readValueAsBuffer(ProtonBuffer buffer, DecoderState state) throws DecodeException {
        int length = readSize(buffer);

        if (length > buffer.getReadableBytes()) {
            throw new DecodeException(
                String.format("Binary data size %d is specified to be greater than the amount " +
                              "of data available (%d)", length, buffer.getReadableBytes()));
        }

        ProtonBuffer payload = ProtonByteBufferAllocator.DEFAULT.allocate(length, length);

        buffer.readBytes(payload);

        return payload;
    }

    public byte[] readValueAsArray(ProtonBuffer buffer, DecoderState state) throws DecodeException {
        int length = readSize(buffer);

        if (length > buffer.getReadableBytes()) {
            throw new DecodeException(
                String.format("Binary data size %d is specified to be greater than the amount " +
                              "of data available (%d)", length, buffer.getReadableBytes()));
        }

        byte[] payload = new byte[length];

        buffer.readBytes(payload);

        return payload;
    }

    @Override
    public Binary readValue(InputStream stream, StreamDecoderState state) throws DecodeException {
        return new Binary(readValueAsBuffer(stream, state));
    }

    public ProtonBuffer readValueAsBuffer(InputStream stream, StreamDecoderState state) throws DecodeException {
        return ProtonByteBufferAllocator.DEFAULT.wrap(readValueAsArray(stream, state));
    }

    public byte[] readValueAsArray(InputStream stream, StreamDecoderState state) throws DecodeException {
        int length = readSize(stream);
        byte[] payload = new byte[length];

        try {
            stream.read(payload);
        } catch (IOException ex) {
            throw new DecodeException("Error while reading Binary payload bytes", ex);
        }

        return payload;
    }

    @Override
    public void skipValue(ProtonBuffer buffer, DecoderState state) throws DecodeException {
        int length = readSize(buffer);

        if (length > buffer.getReadableBytes()) {
            throw new DecodeException(
                String.format("Binary data size %d is specified to be greater than the amount " +
                              "of data available (%d)", length, buffer.getReadableBytes()));
        }

        buffer.skipBytes(length);
    }

    @Override
    public void skipValue(InputStream stream, StreamDecoderState state) throws DecodeException {
        try {
            stream.skip(readSize(stream));
        } catch (IOException ex) {
            throw new DecodeException("Error while reading Binary payload bytes", ex);
        }
    }

    @Override
    public abstract int readSize(ProtonBuffer buffer);

    @Override
    public abstract int readSize(InputStream stream);

}
