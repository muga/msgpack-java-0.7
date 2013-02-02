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
package org.msgpack.unpacker.accept;

import org.msgpack.MessageTypeException;

public abstract class AbstractAccept implements Accept {
    @Override
    public void acceptNil() {
        throw new MessageTypeException("Unexpected nil value");
    }

    @Override
    public void acceptBoolean(boolean v) {
        throw new MessageTypeException("Unexpected boolean value");
    }

    @Override
    public void acceptInt(int v) {
        throw new MessageTypeException("Unexpected integer value");
    }

    @Override
    public void acceptLong(long v) {
        throw new MessageTypeException("Unexpected integer value");
    }

    @Override
    public void acceptUnsignedLong(long v) {
        throw new MessageTypeException("Unexpected integer value");
    }

    @Override
    public void acceptByteArray(byte[] raw) {
        throw new MessageTypeException("Unexpected raw value");
    }

    @Override
    public void acceptEmptyByteArray() {
        throw new MessageTypeException("Unexpected raw value");
    }

    @Override
    public void acceptFloat(float v) {
        throw new MessageTypeException("Unexpected float value");
    }

    @Override
    public void acceptDouble(double v) {
        throw new MessageTypeException("Unexpected float value");
    }

    @Override
    public void acceptArrayHeader(int size) {
        throw new MessageTypeException("Unexpected array value");
    }

    @Override
    public void acceptMapHeader(int size) {
        throw new MessageTypeException("Unexpected map value");
    }
}
