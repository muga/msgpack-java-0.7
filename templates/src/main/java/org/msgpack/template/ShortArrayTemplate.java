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
package org.msgpack.template;

import java.io.IOException;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;
import org.msgpack.MessageTypeException;

public class ShortArrayTemplate extends AbstractCommonTemplate<short[]> {
	private ShortArrayTemplate() {
	}

	public void write(Packer packer, short[] target, boolean required)
			throws IOException {
		if (target == null) {
			if (required) {
				throw new MessageTypeException("Attempted to write null");
			}
			packer.writeNil();
			return;
		}
		packer.writeArrayHeader(target.length);
		for (short a : target) {
			packer.write(a);
		}
	}

	public short[] read(Unpacker unpacker, short[] to, boolean required)
			throws IOException {
		if (!required && unpacker.trySkipNil()) {
			return null;
		}
		int n = unpacker.readArrayHeader();
		if (to == null || to.length != n) {
			to = new short[n];
		}
		for (int i = 0; i < n; i++) {
			to[i] = unpacker.readShort();
		}
		return to;
	}

	static public ShortArrayTemplate getInstance() {
		return instance;
	}

	static final ShortArrayTemplate instance = new ShortArrayTemplate();
}
