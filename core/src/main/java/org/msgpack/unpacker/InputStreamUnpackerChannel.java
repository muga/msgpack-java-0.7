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

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class InputStreamUnpackerChannel implements UnpackerChannel {
    private DataInputStream stream;

    public InputStreamUnpackerChannel(InputStream stream) {
        this.stream = new DataInputStream(stream);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return stream.read(b, off, len);
    }

    public int skip(int n) throws IOException {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public byte readByte() throws IOException {
        return stream.readByte();
    }

    public short readShort() throws IOException {
        return stream.readShort();
    }

    public int readInt() throws IOException {
        return stream.readInt();
    }

    public long readLong() throws IOException {
        return stream.readLong();
    }

    public float readFloat() throws IOException {
        return stream.readFloat();
    }

    public double readDouble() throws IOException {
        return stream.readDouble();
    }

    public void close() throws IOException {
        stream.close();
    }
}
