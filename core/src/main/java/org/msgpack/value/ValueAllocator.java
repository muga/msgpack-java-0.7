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

public interface ValueAllocator {
    public NilValue createNilValue();

    public BooleanValue createBooleanValue(boolean v);

    public IntegerValue createIntegerValue(byte v);

    public IntegerValue createIntegerValue(short v);

    public IntegerValue createIntegerValue(int v);

    public IntegerValue createIntegerValue(long v);

    public IntegerValue createIntegerValue(BigInteger v);

    public FloatValue createFloatValue(float v);

    public FloatValue createFloatValue(double v);

    public RawValue createRawValue();

    public RawValue createRawValue(byte[] b);

    public RawValue createRawValue(byte[] b, boolean gift);

    public RawValue createRawValue(byte[] b, int off, int len);

    public RawValue createRawValue(String s);

    public RawValue createRawValue(ByteBuffer bb);

    public ArrayValue createArrayValue();

    public ArrayValue createArrayValue(Value[] array);

    public ArrayValue createArrayValue(Value[] array, boolean gift);

    public MapValue createMapValue();

    public MapValue createMapValue(Value[] kvs);

    public MapValue createMapValue(Value[] kvs, boolean gift);
}

