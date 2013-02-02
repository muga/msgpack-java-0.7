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
package org.msgpack.buffer;

import java.io.IOException;
import java.io.Closeable;
import java.io.Flushable;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public interface Buffer extends Closeable, Flushable {
    public ReadableByteChannel getInputChannel();

    public WritableByteChannel getOutputChannel();

    public void clear();

    public int size();

    public boolean isEmpty();

    public void write(byte b) throws IOException;

    public void write(byte[] b, int off, int len) throws IOException;

    public void write(ByteBuffer src) throws IOException;

    public byte read() throws IOException;

    public int read(ByteBuffer dst) throws IOException;

    public int readAll(ByteBuffer dst) throws IOException;

    public int skip(int n) throws IOException;

    public int skipAll(int n) throws IOException;

    public int transferTo(WritableByteChannel dst) throws IOException;
}

