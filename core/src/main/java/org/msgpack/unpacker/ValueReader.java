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

import java.lang.Iterable;
import java.io.IOException;
import java.io.Closeable;
import org.msgpack.value.Value;
import org.msgpack.value.ValueAllocator;
import org.msgpack.unpacker.accept.Accept;

public class ValueReader implements Closeable, Iterable<Value> {
    private Unpacker unpacker;
    private UnpackerStack stack;
    private ValueReaderAccept accept;
    private Value result;

    private ValueAllocator allocator;

    private class ValueReaderAccept implements Accept {
        public void acceptNil() {
            if(stack.isEmpty()) {
                result = allocator.createNilValue();
            }
        }

        public void acceptBoolean(boolean v) {
        }

        public void acceptInt(int v) {
        }

        public void acceptLong(long v) {
        }

        public void acceptUnsignedLong(long v) {
        }

        public void acceptByteArray(byte[] raw) {
        }

        public void acceptEmptyByteArray() {
        }

        public void acceptFloat(float v) {
        }

        public void acceptDouble(double v) {
        }

        public void acceptArrayHeader(int size) {
        }

        public void acceptMapHeader(int size) {
        }
    }

    public ValueReader(Unpacker unpacker) {
        this.unpacker = unpacker;
        this.stack = new UnpackerStack();
        this.accept = new ValueReaderAccept();
    }

    public Value read() throws IOException {
        result = null;
        do {
            unpacker.readToken(accept);
        } while(result == null);
        return result;
    }

    public ValueReaderIterator iterator() {
        return new ValueReaderIterator(this);
    }

    public void close() throws IOException {
        unpacker.close();
    }
}

