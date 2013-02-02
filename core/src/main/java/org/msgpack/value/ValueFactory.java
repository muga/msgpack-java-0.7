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
package org.msgpack.value;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public abstract class ValueFactory {
    private static SimpleValueAllocator allocator;

    public static NilValue createNilValue() {
        return allocator.createNilValue();
    }

    public static BooleanValue createBooleanValue(boolean v) {
        return allocator.createBooleanValue(v);
    }

    public static IntegerValue createIntegerValue(int v) {
        return allocator.createIntegerValue(v);
    }

    public static IntegerValue createIntegerValue(long v) {
        return allocator.createIntegerValue(v);
    }

    public static IntegerValue createIntegerValue(BigInteger v) {
        return allocator.createIntegerValue(v);
    }

    public static FloatValue createFloatValue(float v) {
        return allocator.createFloatValue(v);
    }

    public static FloatValue createFloatValue(double v) {
        return allocator.createFloatValue(v);
    }

    public static RawValue createEmptyRawValue() {
        return allocator.createEmptyRawValue();
    }

    public static RawValue createRawValue(byte[] b) {
        return allocator.createRawValue(b);
    }

    public static RawValue createRawValue(byte[] b, boolean gift) {
        return allocator.createRawValue(b, gift);
    }

    public static RawValue createRawValue(byte[] b, int off, int len) {
        return allocator.createRawValue(b, off, len);
    }

    public static RawValue createRawValue(String s) {
        return allocator.createRawValue(s);
    }

    public static RawValue createRawValue(ByteBuffer bb) {
        return allocator.createRawValue(bb);
    }

    public static ArrayValue createEmptyArrayValue() {
        return allocator.createEmptyArrayValue();
    }

    public static ArrayValue createArrayValue(Value[] array) {
        return allocator.createArrayValue(array);
    }

    public static ArrayValue createArrayValue(Value[] array, boolean gift) {
        return allocator.createArrayValue(array, gift);
    }

    public static MapValue createEmptyMapValue() {
        return allocator.createEmptyMapValue();
    }

    public static MapValue createMapValue(Value[] kvs) {
        return allocator.createMapValue(kvs);
    }

    public static MapValue createMapValue(Value[] kvs, boolean gift) {
        return allocator.createMapValue(kvs, gift);
    }
}
