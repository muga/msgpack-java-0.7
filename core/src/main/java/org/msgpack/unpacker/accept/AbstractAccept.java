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
    public void acceptBoolean(boolean v) {
        throw new MessageTypeException("Unexpected boolean value");
    }

    public void acceptInteger(byte v) {
        throw new MessageTypeException("Unexpected integer value");
    }

    public void acceptInteger(int v) {
        throw new MessageTypeException("Unexpected integer value");
    }

    public void acceptInteger(short v) {
        throw new MessageTypeException("Unexpected integer value");
    }

    public void acceptInteger(long v) {
        throw new MessageTypeException("Unexpected integer value");
    }

    public void acceptIntegerUnsigned64(long v) {
        throw new MessageTypeException("Unexpected integer value");
    }

    public void acceptRaw(byte[] raw) {
        throw new MessageTypeException("Unexpected raw value");
    }

    public void acceptEmptyRaw() {
        throw new MessageTypeException("Unexpected raw value");
    }

    public void acceptNil() {
        throw new MessageTypeException("Unexpected nil value");
    }

    public void acceptFloat(float v) {
        throw new MessageTypeException("Unexpected float value");
    }

    public void acceptFloat(double v) {
        throw new MessageTypeException("Unexpected float value");
    }

    public void acceptArrayHeader(int size) {
        throw new MessageTypeException("Unexpected array value");
    }

    public void acceptMapHeader(int size) {
        throw new MessageTypeException("Unexpected map value");
    }
}
