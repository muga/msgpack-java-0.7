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
import java.io.Closeable;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.msgpack.type.Value;

public interface Unpacker extends Closeable {
    public Value read() throws IOException;

    public void skip() throws IOException;

    public boolean trySkipNil() throws IOException;

    public int readArrayHeader() throws IOException;

    public int readMapHeader() throws IOException;

    public UnpackerIterator iterator();

    //public void feed(data) throws IOException;

    public Value readValue() throws IOException; // TODO is it needed??
    public boolean readBoolean() throws IOException;
    public byte readByte() throws IOException;
    public short readShort() throws IOException;
    public int readInt() throws IOException;
    public long readLong() throws IOException;
    public double readDouble() throws IOException;
    public float readFloat() throws IOException;
    public String readString() throws IOException;
    public byte[] readByteArray() throws IOException;
    public ByteBuffer readByteBuffer() throws IOException;
    public BigInteger readBigInteger() throws IOException;
}

