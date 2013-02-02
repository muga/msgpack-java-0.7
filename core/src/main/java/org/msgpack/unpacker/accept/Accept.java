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

public interface Accept {
    public void acceptBoolean(boolean v);

    public void acceptInteger(byte v);

    public void acceptInteger(int v);

    public void acceptInteger(short v);

    public void acceptInteger(long v);

    public void acceptIntegerUnsigned64(long v);

    public void acceptRaw(byte[] raw);

    public void acceptEmptyRaw();

    public void acceptNil();

    public void acceptFloat(float v);

    public void acceptFloat(double v);

    public void acceptArrayHeader(int size);

    public void acceptMapHeader(int size);
}

