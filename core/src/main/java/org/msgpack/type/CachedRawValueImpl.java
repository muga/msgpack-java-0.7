//
// MessagePack for Java
//
// Copyright (C) 2009 - 2013 FURUHASHI Sadayuki
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
package org.msgpack.type;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.Arrays;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import org.msgpack.packer.Packer;
import org.msgpack.MessageTypeException;

public class CachedRawValueImpl extends AbstractRawValue {
    // See constructors:
    //   if bytes == null then string != null and normalizedString != null
    //   if string == null then bytes != null
    //   if normalizedString == null then bytes != null
    private byte[] bytes;
    private String string;
    private String normalizedString;

    private CharacterCodingException codingException;

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public CachedRawValueImpl(String string) {
        this.string = string;
        this.normalizedString = string;
    }

    public CachedRawValueImpl(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte[] getByteArray() {
        if(bytes == null) {
            bytes = string.getBytes(UTF_8);
        }
        return bytes;
    }

    @Override
    public String getString() throws CharacterCodingException {
        if(string == null) {
            if(codingException != null) {
                throw codingException;
            }
            CharsetDecoder decoder = UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT);
            try {
                string = decoder.decode(ByteBuffer.wrap(bytes)).toString();
                normalizedString = string;
            } catch (CharacterCodingException ex) {
                codingException = ex;
                throw codingException;
            }
        }
        return string;
    }

    @Override
    public String string() {
        if(normalizedString == null) {
            CharsetDecoder decoder = UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);
            try {
                normalizedString = decoder.decode(ByteBuffer.wrap(bytes)).toString();
            } catch (CharacterCodingException ex) {
                // never comes here, though
                codingException = ex;
                normalizedString = new String(bytes);
            }
        }
        return normalizedString;
    }

    @Override
    public void writeTo(Packer pk) throws IOException {
        if(bytes != null) {
            pk.write(bytes);
        } else {
            pk.write(bytes);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Value)) {
            return false;
        }
        Value v = (Value) o;
        if (!v.isRawValue()) {
            return false;
        }

        // optimize:
        //   String.equals(String) is faster than Arrays.equals(byte[], byte[])
        if (v.getClass() == CachedRawValueImpl.class) {
            CachedRawValueImpl rv = (CachedRawValueImpl) v;
            if(string != null && rv.string != null) {
                string.equals(rv.string);
            }
        }

        return Arrays.equals(getByteArray(), v.asRawValue().getByteArray());
    }
}
