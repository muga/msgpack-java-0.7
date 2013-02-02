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

import java.io.IOException;
import java.nio.ByteBuffer;
import org.msgpack.buffer.Buffer;

public class BufferPackerChannel implements PackerChannel {
    private Buffer buffer;
    private ByteBuffer castBlock;

    public BufferPackerChannel(Buffer buffer) {
        this.buffer = buffer;
        this.castBlock = ByteBuffer.allocate(9);
    }

    public void writeByteArray(byte[] b, int off, int len) throws IOException {
        buffer.write(b, off, len);
    }

    public void writeByteBuffer(ByteBuffer bb) throws IOException {
        buffer.write(bb);
    }

    public void writeByte(byte v) throws IOException {
        buffer.write(v);
    }

    public void writeShort(short v) throws IOException {
        castBlock.limit(2);
        castBlock.putShort(0, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }

    public void writeInt(int v) throws IOException {
        castBlock.limit(4);
        castBlock.putInt(0, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }

    public void writeLong(long v) throws IOException {
        castBlock.limit(8);
        castBlock.putLong(0, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }

    public void writeFloat(float v) throws IOException {
        castBlock.limit(4);
        castBlock.putFloat(0, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }

    public void writeDouble(double v) throws IOException {
        castBlock.limit(8);
        castBlock.putDouble(0, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }

    public void writeByteAndByte(byte b, byte v) throws IOException {
        castBlock.limit(2);
        castBlock.put(0, b);
        castBlock.put(1, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }

    public void writeByteAndShort(byte b, short v) throws IOException {
        castBlock.limit(3);
        castBlock.put(0, b);
        castBlock.putShort(1, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }

    public void writeByteAndInt(byte b, int v) throws IOException {
        castBlock.limit(5);
        castBlock.put(0, b);
        castBlock.putInt(1, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }

    public void writeByteAndLong(byte b, long v) throws IOException {
        castBlock.limit(9);
        castBlock.put(0, b);
        castBlock.putLong(1, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }

    public void writeByteAndFloat(byte b, float v) throws IOException {
        castBlock.limit(5);
        castBlock.put(0, b);
        castBlock.putFloat(1, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }

    public void writeByteAndDouble(byte b, double v) throws IOException {
        castBlock.limit(9);
        castBlock.put(0, b);
        castBlock.putDouble(1, v);
        castBlock.position(0);
        buffer.write(castBlock);
    }
}

