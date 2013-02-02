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

import org.msgpack.value.Value;

class UnpackerStack {
    public static enum StackType {
        ARRAY_ELEMENT,
        MAP_KEY,
        MAP_VALUE;
    };

    private int depth;
    private StackType[] types;
    private int[] counts;
    private Value[][] containers;

    public boolean isEmpty() {
        return depth == 0;
    }

    public int getDepth() {
        return depth;
    }

    public void pop() {
        --depth;
    }

    public void push(StackType type, int count, Value[] container) throws MessageStackException {
        if(getCapacity() - depth <= 0) {
            throw new MessageStackException("stack level too deep");
        }

        types[depth] = type;
        counts[depth] = count;
        containers[depth] = container;

        ++depth;
    }

    public StackType getTopType() {
        return types[depth-1];
    }

    public void setTopType(StackType type) {
        types[depth-1] = type;
    }

    public int getTopCount() {
        return counts[depth-1];
    }

    public Value[] getTopContainer() {
        return containers[depth-1];
    }

    public int getCapacity() {
        return types.length;
    }

    public int decrTopCount() {
        return --counts[depth-1];
    }

    public void reset() {
        depth = 0;
    }
}

