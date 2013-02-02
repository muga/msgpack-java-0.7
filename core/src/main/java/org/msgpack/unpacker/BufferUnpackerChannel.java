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
package org.msgpack.unpacker;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.msgpack.buffer.Buffer;

public class BufferUnpackerChannel implements UnpackerChannel {
    private Buffer buffer;

    private byte[] castBlockArray;
    private ByteBuffer castBlock;

    public BufferUnpackerChannel(Buffer buffer) {
        this.buffer = buffer;
        this.castBlockArray = new byte[9];
        this.castBlock = ByteBuffer.wrap(castBlockArray);
    }

    public int read(ByteBuffer dst) throws IOException {
        return buffer.read(dst);
    }

    public int skip(int n) throws IOException {
        return buffer.skip(n);
    }

    public byte readByte() throws IOException {
        return buffer.read();
    }

    public short readShort() throws IOException {
        castBlock.position(0);
        castBlock.limit(2);
        buffer.readAll(castBlock);
        return castBlock.getShort(0);
    }

    public int readInt() throws IOException {
        castBlock.position(0);
        castBlock.limit(4);
        buffer.readAll(castBlock);
        return castBlock.getInt(0);
    }

    public long readLong() throws IOException {
        castBlock.position(0);
        castBlock.limit(8);
        buffer.readAll(castBlock);
        return castBlock.getLong(0);
    }

    public float readFloat() throws IOException {
        castBlock.position(0);
        castBlock.limit(4);
        buffer.readAll(castBlock);
        return castBlock.getFloat(0);
    }

    public double readDouble() throws IOException {
        castBlock.position(0);
        castBlock.limit(8);
        buffer.readAll(castBlock);
        return castBlock.getDouble(0);
    }
}

