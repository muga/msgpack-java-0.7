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
package org.msgpack.packer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import org.msgpack.buffer.Buffer;

public class MessagePacker extends AbstractPacker {
    public Buffer buffer;
    public PackerChannel ch;

    @Override
    protected void writeByte(byte v) throws IOException {
        if(v < -(1 << 5)) {
            ch.writeByteAndByte((byte) 0xd0, v);
        } else {
            ch.writeByte(v);
        }
    }

    @Override
    protected void writeShort(short v) throws IOException {
        if(v < -(1 << 5)) {
            if(v < -(1 << 7)) {
                // signed 16
                ch.writeByteAndShort((byte) 0xd1, v);
            } else {
                // signed 8
                ch.writeByteAndByte((byte) 0xd0, (byte) v);
            }
        } else if(v < (1 << 7)) {
            // fixnum
            ch.writeByte((byte) v);
        } else {
            if(v < (1 << 8)) {
                // unsigned 8
                ch.writeByteAndByte((byte) 0xcc, (byte) v);
            } else {
                // unsigned 16
                ch.writeByteAndShort((byte) 0xcd, v);
            }
        }
    }

    @Override
    protected void writeInt(int v) throws IOException {
        if(v < -(1 << 5)) {
            if(v < -(1 << 15)) {
                // signed 32
                ch.writeByteAndInt((byte) 0xd2, v);
            } else if(v < -(1 << 7)) {
                // signed 16
                ch.writeByteAndShort((byte) 0xd1, (short) v);
            } else {
                // signed 8
                ch.writeByteAndByte((byte) 0xd0, (byte) v);
            }
        } else if(v < (1 << 7)) {
            // fixnum
            ch.writeByte((byte) v);
        } else {
            if(v < (1 << 8)) {
                // unsigned 8
                ch.writeByteAndByte((byte) 0xcc, (byte) v);
            } else if(v < (1 << 16)) {
                // unsigned 16
                ch.writeByteAndShort((byte) 0xcd, (short) v);
            } else {
                // unsigned 32
                ch.writeByteAndInt((byte) 0xce, v);
            }
        }
    }

    @Override
    protected void writeLong(long v) throws IOException {
        if(v < -(1L << 5)) {
            if(v < -(1L << 15)) {
                if(v < -(1L << 31)) {
                    // signed 64
                    ch.writeByteAndLong((byte) 0xd3, v);
                } else {
                    // signed 32
                    ch.writeByteAndInt((byte) 0xd2, (int) v);
                }
            } else {
                if(v < -(1 << 7)) {
                    // signed 16
                    ch.writeByteAndShort((byte) 0xd1, (short) v);
                } else {
                    // signed 8
                    ch.writeByteAndByte((byte) 0xd0, (byte) v);
                }
            }
        } else if(v < (1 << 7)) {
            // fixnum
            ch.writeByte((byte) v);
        } else {
            if(v < (1L << 16)) {
                if(v < (1 << 8)) {
                    // unsigned 8
                    ch.writeByteAndByte((byte) 0xcc, (byte) v);
                } else {
                    // unsigned 16
                    ch.writeByteAndShort((byte) 0xcd, (short) v);
                }
            } else {
                if(v < (1L << 32)) {
                    // unsigned 32
                    ch.writeByteAndInt((byte) 0xce, (int) v);
                } else {
                    // unsigned 64
                    ch.writeByteAndLong((byte) 0xcf, v);
                }
            }
        }
    }

    @Override
    protected void writeBigInteger(BigInteger v) throws IOException {
        if(v.bitLength() <= 63) {
            writeLong(v.longValue());
        } else if(v.bitLength() == 64 && v.signum() == 1) {
            // unsigned 64
            ch.writeByteAndLong((byte) 0xcf, v.longValue());
        } else {
            throw new IllegalArgumentException(
                    "MessagePack can't serialize BigInteger larger than (2^64)-1");
        }
    }

    @Override
    protected void writeBoolean(boolean v) throws IOException {
        if(v) {
            // true
            ch.writeByte((byte) 0xc3);
        } else {
            // false
            ch.writeByte((byte) 0xc2);
        }
    }

    @Override
    protected void writeFloat(float v) throws IOException {
        ch.writeByteAndFloat((byte) 0xca, v);
    }

    @Override
    protected void writeDouble(double v) throws IOException {
        ch.writeByteAndDouble((byte) 0xcb, v);
    }

    @Override
    protected void writeByteArray(byte[] b, int off, int len) throws IOException {
        if(len < 32) {
            ch.writeByte((byte) (0xa0 | len));
        } else if(len < 65536) {
            ch.writeByteAndShort((byte) 0xda, (short) len);
        } else {
            ch.writeByteAndInt((byte) 0xdb, len);
        }
        ch.writeByteArray(b, off, len);
    }

    @Override
    protected void writeByteBuffer(ByteBuffer bb) throws IOException {
        int len = bb.remaining();
        if(len < 32) {
            ch.writeByte((byte) (0xa0 | len));
        } else if(len < 65536) {
            ch.writeByteAndShort((byte) 0xda, (short) len);
        } else {
            ch.writeByteAndInt((byte) 0xdb, len);
        }
        int pos = bb.position();
        try {
            ch.writeByteBuffer(bb);
        } finally {
            bb.position(pos);
        }
    }

    @Override
    protected void writeString(String s) throws IOException {
        byte[] b;
        try {
            b = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new InternalError("UTF-8 character coding must be supported by Java VM to pack strings");
        }
        writeByteArray(b, 0, b.length);
    }

    @Override
    public Packer writeArrayHeader(int size) throws IOException {
        if(size < 0) {
            throw new IllegalArgumentException("Negative array size");
        }
        if(size < 16) {
            // FixArray
            ch.writeByte((byte) (0x90 | size));
        } else if (size < 65536) {
            ch.writeByteAndShort((byte) 0xdc, (short) size);
        } else {
            ch.writeByteAndInt((byte) 0xdd, size);
        }
        return this;
    }

    @Override
    public Packer writeMapHeader(int size) throws IOException {
        if(size < 0) {
            throw new IllegalArgumentException("Negative map size");
        }
        if(size < 16) {
            // FixMap
            ch.writeByte((byte) (0x80 | size));
        } else if(size < 65536) {
            ch.writeByteAndShort((byte) 0xde, (short) size);
        } else {
            ch.writeByteAndInt((byte) 0xdf, size);
        }
        return this;
    }

    @Override
    public Packer writeNil() throws IOException {
        ch.writeByte((byte) 0xc0);
        return this;
    }

    @Override
    public void close() throws IOException {
        buffer.close();
    }

    @Override
    public void flush() throws IOException {
        buffer.flush();
    }
}

