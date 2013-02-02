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
package org.msgpack.buffer;

import java.util.Deque;
import java.util.ArrayDeque;
import java.io.IOException;
import java.io.EOFException;
import java.nio.ByteBuffer;

public class LinkedBuffer {
    private static ByteBuffer EMPTY = ByteBuffer.allocate(0);

    private Deque<ByteBuffer> link;
    private ByteBuffer last;

    private byte[] accumulator;
    private ByteBuffer accumulatorBuffer;

    public LinkedBuffer() {
        this.link = new ArrayDeque();
        this.accumulator = new byte[9];
        this.accumulatorBuffer = ByteBuffer.wrap(accumulator);
    }

    public void clear() {
        link.clear();
    }

    public int size() {
        int total = 0;
        for(ByteBuffer bb : link) {
            total += bb.remaining();
        }
        if(last != null) {
            total += last.position();
        }
        return total;
    }

    public boolean isEmpty() {
        for(ByteBuffer bb : link) {
            if(bb.hasRemaining()) {
                return false;
            }
        }
        if(last != null && last.position() > 0) {
            return false;
        }
        return true;
    }

    public void write(byte b) throws IOException {
        ByteBuffer dst = ensureWritableSize(1);
        dst.put(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        while(true) {
            ByteBuffer dst = ensureWritableSize(1);
            int dstrem = dst.remaining();
            if(dstrem >= len) {
                dst.put(b, off, len);
                return;
            }
            dst.put(b, off, dstrem);
            off += dstrem;
            len -= dstrem;
        }
    }

    public void write(ByteBuffer src) throws IOException {
        while(true) {
            ByteBuffer dst = ensureWritableSize(1);
            int dstrem = dst.remaining();
            int srcrem = src.remaining();
            if(dstrem >= srcrem) {
                dst.put(src);
                return;
            }
            int lim = src.limit();
            src.limit(src.position() + dstrem);
            try {
                dst.put(src);
            } finally {
                src.limit(lim);
            }
        }
    }

    public byte read() throws IOException {
        ByteBuffer src = ensureReadableSize(1);
        byte result = src.get();
        if(!src.hasRemaining()) {
            removeFirstLink();
        }
        return result;
    }

    public void read(byte[] b, int off, int len) throws IOException {
        while(true) {
            ByteBuffer src = ensureReadableSize(1);
            if(len < src.remaining()) {
                src.get(b, off, len);
                return;
            }
            int rem = src.remaining();
            src.get(b, off, rem);
            len -= rem;
            off += rem;
            removeFirstLink();
        }
    }

    public void read(ByteBuffer dst) throws IOException {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public void skip(int len) throws IOException {
        while(true) {
            ByteBuffer src = ensureReadableSize(1);
            if(len < src.remaining()) {
                src.position(src.position() + len);
                return;
            }
            int rem = src.remaining();
            src.position(src.position() + rem);
            len -= rem;
            removeFirstLink();
        }
    }

    private ByteBuffer ensureWritableSize(int n) throws IOException {
        assert(n <= 9);

        if(last != null && n <= last.remaining()) {
            return last;
        }

        ByteBuffer bb = allocateBuffer();
        if(last != null) {
            last.limit(last.position());
            last.position(0);
            link.addLast(last);
        }
        last = bb;

        return bb;
    }

    private ByteBuffer ensureReadableSize(int n) throws IOException {
        assert(n <= 9);

        int off = 0;
        int usedLink = 0;
        for(ByteBuffer bb : link) {
            if(n <= bb.remaining()) {
                for(int i=0; i < usedLink; i++) {
                    removeFirstLink();
                }
                bb.get(accumulator, off, n);
                return accumulatorBuffer;
            }

            int rem = bb.remaining();
            int pos = bb.position();
            bb.get(accumulator, off, rem);
            bb.position(pos);

            n -= rem;
            off += rem;
            usedLink++;
        }

        ByteBuffer bb = last;
        if(bb != null && n <= bb.position()) {
            for(int i=0; i < usedLink; i++) {
                removeFirstLink();
            }

            last = null;
            bb.limit(bb.position());
            bb.position(0);
            bb.get(accumulator, off, n);
            link.addLast(bb);

            return accumulatorBuffer;
        }

        throw new EOFException();
    }

    private void removeFirstLink() {
        ByteBuffer bb = link.removeFirst();
        if(link.isEmpty() && last == null) {
            bb.position(0);
            bb.limit(bb.capacity());
            last = bb;
        }
    }

    private ByteBuffer allocateBuffer() {
        // TODO
        return ByteBuffer.allocateDirect(512);
    }
}

