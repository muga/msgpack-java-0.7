//
// MessagePack for Java
//
// Copyright (C) 2009-2013 FURUHASHI Sadayuki
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
package org.msgpack.packer;

import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class OutputStreamPackerChannel implements PackerChannel {
    private DataOutputStream stream;

    public OutputStreamPackerChannel(OutputStream stream) {
        this.stream = new DataOutputStream(stream);
    }

    public void writeByteArray(byte[] b, int off, int len) throws IOException {
        stream.write(b, off, len);
    }

    public void writeByteBuffer(ByteBuffer bb) throws IOException {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public void writeByte(byte v) throws IOException {
        stream.writeByte(v);
    }

    public void writeShort(short v) throws IOException {
        stream.writeShort(v);
    }

    public void writeInt(int v) throws IOException {
        stream.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        stream.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        stream.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        stream.writeDouble(v);
    }

    public void writeByteAndByte(byte b, byte v) throws IOException {
        stream.writeByte(b);
        stream.writeByte(v);
    }

    public void writeByteAndShort(byte b, short v) throws IOException {
        stream.writeByte(b);
        stream.writeShort(v);
    }

    public void writeByteAndInt(byte b, int v) throws IOException {
        stream.writeByte(b);
        stream.writeInt(v);
    }

    public void writeByteAndLong(byte b, long v) throws IOException {
        stream.writeByte(b);
        stream.writeLong(v);
    }

    public void writeByteAndFloat(byte b, float v) throws IOException {
        stream.writeByte(b);
        stream.writeFloat(v);
    }

    public void writeByteAndDouble(byte b, double v) throws IOException {
        stream.writeByte(b);
        stream.writeDouble(v);
    }

    public void flush() throws IOException {
        stream.flush();
    }

    public void close() throws IOException {
        stream.close();
    }
}

