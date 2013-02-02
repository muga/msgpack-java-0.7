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
import org.msgpack.packer.PackerChannel;
import org.msgpack.unpacker.UnpackerChannel;

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

    public void readAll(byte[] b, int off, int len) throws IOException {
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

    public void readAll(ByteBuffer dst) throws IOException {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public void skipAll(int len) throws IOException {
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

    public int read(byte[] b, int off, int len) throws IOException {
        int n = len;
        while(true) {
            ByteBuffer src = link.peekFirst();
            int srcrem = src.remaining();

            if(n <= srcrem) {
                src.get(b, off, n);
                return len;
            }
            src.get(b, off, srcrem);
            off += srcrem;
            n -= srcrem;

            removeFirstLink();

            if(link.isEmpty()) {
                if(!tryMoveLastToLink()) {
                    if(n == len) {
                        return -1;
                    }
                    return len - n;
                }
            }
        }
    }

    public int read(ByteBuffer dst) throws IOException {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    public int skip(int len) throws IOException {
        // TODO
        throw new UnsupportedOperationException("not implemented yet");
    }

    private ByteBuffer ensureWritableSize(int n) throws IOException {
        assert(n <= 9);

        if(last != null && n <= last.remaining()) {
            return last;
        }

        ByteBuffer bb = allocateBuffer();
        if(last != null) {
            moveLastToLink();
        }
        last = bb;

        return bb;
    }

    private ByteBuffer moveLastToLink() throws IOException {
        ByteBuffer bb = last;
        if(bb.position() > 0) {
            bb.limit(bb.position());
            bb.position(0);
            link.addLast(bb);
        }
        last = null;
        return bb;
    }

    private boolean tryMoveLastToLink() throws IOException {
        if(last != null && last.position() > 0) {
            last.limit(last.position());
            last.position(0);
            link.addLast(last);
            last = null;
            return true;
        }
        return false;
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

            bb = moveLastToLink();
            bb.get(accumulator, off, n);

            return accumulatorBuffer;
        }

        throw new EOFException();
    }

    private void removeFirstLink() {
        ByteBuffer bb = link.removeFirst();
        if(last == null) {
            bb.position(0);
            bb.limit(bb.capacity());
            last = bb;
        } else {
            releaseBuffer(bb);
        }
    }

    private ByteBuffer allocateBuffer() {
        // TODO
        return ByteBuffer.allocateDirect(512);
    }

    private void releaseBuffer(ByteBuffer bb) {
    }

    public PackerChannel getPackerChannelView() {
        return new PackerChannelView();
    }

    public UnpackerChannel getUnpackerChannelView() {
        return new UnpackerChannelView();
    }

    private class PackerChannelView implements PackerChannel {
        public void writeByteArray(byte[] b, int off, int len) throws IOException {
            write(b, off, len);
        }

        public void writeByteBuffer(ByteBuffer bb) throws IOException {
            write(bb);
        }

        public void writeByte(byte v) throws IOException {
            write(v);
        }

        public void writeShort(short v) throws IOException {
            ensureWritableSize(2).putShort(v);
        }

        public void writeInt(int v) throws IOException {
            ensureWritableSize(4).putInt(v);
        }

        public void writeLong(long v) throws IOException {
            ensureWritableSize(8).putLong(v);
        }

        public void writeFloat(float v) throws IOException {
            ensureWritableSize(4).putFloat(v);
        }

        public void writeDouble(double v) throws IOException {
            ensureWritableSize(8).putDouble(v);
        }

        public void writeByteAndByte(byte b, byte v) throws IOException {
            ByteBuffer dst = ensureWritableSize(3);
            dst.put(b);
            dst.put(v);
        }

        public void writeByteAndShort(byte b, short v) throws IOException {
            ByteBuffer dst = ensureWritableSize(3);
            dst.put(b);
            dst.putShort(v);
        }

        public void writeByteAndInt(byte b, int v) throws IOException {
            ByteBuffer dst = ensureWritableSize(5);
            dst.put(b);
            dst.putInt(v);
        }

        public void writeByteAndLong(byte b, long v) throws IOException {
            ByteBuffer dst = ensureWritableSize(9);
            dst.put(b);
            dst.putLong(v);
        }

        public void writeByteAndFloat(byte b, float v) throws IOException {
            ByteBuffer dst = ensureWritableSize(5);
            dst.put(b);
            dst.putFloat(v);
        }

        public void writeByteAndDouble(byte b, double v) throws IOException {
            ByteBuffer dst = ensureWritableSize(9);
            dst.put(b);
            dst.putDouble(v);
        }

        public void flush() throws IOException {
        }

        public void close() throws IOException {
        }
    }

    private class UnpackerChannelView implements UnpackerChannel {
        public int read(byte[] b, int off, int len) throws IOException {
            return LinkedBuffer.this.read(b, off, len);
        }

        public int skip(int n) throws IOException {
            return LinkedBuffer.this.skip(n);
        }

        public byte readByte() throws IOException {
            return LinkedBuffer.this.read();
        }

        public short readShort() throws IOException {
            return LinkedBuffer.this.ensureReadableSize(2).getShort();
        }

        public int readInt() throws IOException {
            return LinkedBuffer.this.ensureReadableSize(4).getInt();
        }

        public long readLong() throws IOException {
            return LinkedBuffer.this.ensureReadableSize(8).getLong();
        }

        public float readFloat() throws IOException {
            return LinkedBuffer.this.ensureReadableSize(4).getFloat();
        }

        public double readDouble() throws IOException {
            return LinkedBuffer.this.ensureReadableSize(8).getDouble();
        }

        public void close() throws IOException {
        }
    }
}

