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
import java.io.EOFException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.msgpack.value.Value;

public class ValueReaderIterator implements Iterator<Value> {
    private ValueReader reader;
    private Value nextValue;
    private IOException exception;

    public ValueReaderIterator(ValueReader reader) {
        this.reader = reader;
    }

    public boolean hasNext() {
        if(nextValue != null) {
            return true;
        } else if(exception != null) {
            return false;
        }
        try {
            nextValue = reader.read();
            return true;
        } catch (EOFException ex) {
            return false;
        } catch (IOException ex) {
            exception = ex;
            return false;
        }
    }

    public Value next() {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }
        return nextValue;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public IOException getException() {
        return exception;
    }

    public void throwException() throws IOException {
        throw exception;
    }
}

