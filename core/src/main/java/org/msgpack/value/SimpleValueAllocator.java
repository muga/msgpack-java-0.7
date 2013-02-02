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

public class SimpleValueAllocator implements ValueAllocator {
    public NilValue createNilValue() {
        return NilValue.getInstance();
    }

    public BooleanValue createBooleanValue(boolean v) {
        if (v) {
            return TrueValueImpl.getInstance();
        } else {
            return FalseValueImpl.getInstance();
        }
    }

    public IntegerValue createIntegerValue(int v) {
        return new IntValueImpl(v);
    }

    public IntegerValue createIntegerValue(long v) {
        return new LongValueImpl(v);
    }

    public IntegerValue createIntegerValue(BigInteger v) {
        return new BigIntegerValueImpl(v);
    }

    public FloatValue createFloatValue(float v) {
        return new FloatValueImpl(v);
    }

    public FloatValue createFloatValue(double v) {
        return new DoubleValueImpl(v);
    }

    public RawValue createEmptyRawValue() {
        return CachedRawValueImpl.getEmptyInstance();
    }

    public RawValue createRawValue(byte[] b) {
        return createRawValue(b, false);
    }

    public RawValue createRawValue(byte[] b, boolean gift) {
        return new CachedRawValueImpl(b, gift);
    }

    public RawValue createRawValue(byte[] b, int off, int len) {
        return new CachedRawValueImpl(b, off, len);
    }

    public RawValue createRawValue(String s) {
        return new CachedRawValueImpl(s);
    }

    public RawValue createRawValue(ByteBuffer bb) {
        int pos = bb.position();
        try {
            byte[] buf = new byte[bb.remaining()];
            bb.get(buf);
            return new CachedRawValueImpl(buf, true);
        } finally {
            bb.position(pos);
        }
    }

    public ArrayValue createEmptyArrayValue() {
        return ArrayValueImpl.getEmptyInstance();
    }

    public ArrayValue createArrayValue(Value[] array) {
        if (array.length == 0) {
            return ArrayValueImpl.getEmptyInstance();
        }
        return createArrayValue(array, false);
    }

    public ArrayValue createArrayValue(Value[] array, boolean gift) {
        if (array.length == 0) {
            return ArrayValueImpl.getEmptyInstance();
        }
        return new ArrayValueImpl(array, gift);
    }

    public MapValue createEmptyMapValue() {
        return SequentialMapValueImpl.getEmptyInstance();
    }

    public MapValue createMapValue(Value[] kvs) {
        if (kvs.length == 0) {
            return SequentialMapValueImpl.getEmptyInstance();
        }
        return createMapValue(kvs, false);
    }

    public MapValue createMapValue(Value[] kvs, boolean gift) {
        if (kvs.length == 0) {
            // TODO EmptyMapValueImpl?
            return SequentialMapValueImpl.getEmptyInstance();
        }
        return new SequentialMapValueImpl(kvs, gift);
    }
}
