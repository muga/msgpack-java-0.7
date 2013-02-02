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
import java.math.BigInteger;
import org.msgpack.value.Value;
import org.msgpack.value.ValueAllocator;
import org.msgpack.unpacker.accept.Accept;
import org.msgpack.value.Value;
import org.msgpack.unpacker.UnpackerStack.StackType;

public class ValueReader implements Closeable, Iterable<Value> {
    private Unpacker unpacker;
    private UnpackerStack stack;
    private ValueReaderAccept accept;
    private Value result;

    private ValueAllocator allocator;

    private class ValueReaderAccept implements Accept {
        public void acceptNil() {
            objectComplete(allocator.createNilValue());
        }

        public void acceptBoolean(boolean v) {
            objectComplete(allocator.createBooleanValue(v));
        }

        public void acceptInt(int v) {
            objectComplete(allocator.createIntegerValue(v));
        }

        public void acceptLong(long v) {
            objectComplete(allocator.createIntegerValue(v));
        }

        public void acceptUnsignedLong(long v) {
            Value value;
            if(v < 0L) {
                BigInteger bi = BigInteger.valueOf(v + Long.MAX_VALUE + 1L).setBit(63);
                value = allocator.createIntegerValue(bi);
            } else {
                value = allocator.createIntegerValue(v);
            }
            objectComplete(value);
        }

        public void acceptByteArray(byte[] raw) {
            objectComplete(allocator.createRawValue(raw, true));  // use gift=true
        }

        public void acceptEmptyByteArray() {
            objectComplete(allocator.createEmptyRawValue());
        }

        public void acceptFloat(float v) {
            objectComplete(allocator.createFloatValue(v));
        }

        public void acceptDouble(double v) {
            objectComplete(allocator.createFloatValue(v));
        }

        public void acceptArrayHeader(int size) throws UnpackException {
            if(size == 0) {
                objectComplete(allocator.createEmptyArrayValue());
                return;
            }
            pushArray(size);
        }

        public void acceptMapHeader(int size) throws UnpackException {
            if(size == 0) {
                objectComplete(allocator.createEmptyMapValue());
                return;
            }
            pushMap(size);
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

    private void objectComplete(Value v) {
        while(!stack.isEmpty()) {
            switch(stack.getTopType()) {
            case ARRAY_ELEMENT:
                {
                    Value[] array = stack.getTopContainer();
                    array[array.length - stack.getTopCount()] = v;
                    if(stack.decrTopCount() > 0) {
                        return;
                    }
                    stack.pop();
                    v = allocator.createArrayValue(array, true);
                }
                break;
            case MAP_KEY:
                {
                    Value[] kvs = stack.getTopContainer();
                    kvs[kvs.length - stack.getTopCount()] = v;
                    stack.decrTopCount();
                    stack.setTopType(StackType.MAP_VALUE);
                    return;
                }
            case MAP_VALUE:
                {
                    Value[] kvs = stack.getTopContainer();
                    kvs[kvs.length - stack.getTopCount()] = v;
                    if(stack.decrTopCount() > 0) {
                        stack.setTopType(StackType.MAP_KEY);
                        return;
                    }
                    stack.pop();
                    v = allocator.createMapValue(kvs, true);
                }
                break;
            }
        }
        this.result = v;
    }

    private void pushArray(int size) throws MessageStackException {
        stack.push(StackType.ARRAY_ELEMENT, size, new Value[size]);
    }

    private void pushMap(int size) throws MessageStackException {
        stack.push(StackType.MAP_KEY, size, new Value[size*2]);
    }

    public ValueReaderIterator iterator() {
        return new ValueReaderIterator(this);
    }

    public void close() throws IOException {
        unpacker.close();
    }
}

