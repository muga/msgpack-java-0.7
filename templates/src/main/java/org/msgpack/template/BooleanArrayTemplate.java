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

public class BooleanArrayTemplate extends AbstractCommonTemplate<boolean[]> {
    private BooleanArrayTemplate() {
    }

	public void write(Packer packer, boolean[] target, boolean required)
			throws IOException {
		if (target == null) {
			if (required) {
				throw new MessageTypeException("Attempted to write null");
			}
			packer.writeNil();
			return;
		}
		packer.writeArrayHeader(target.length);
		for (boolean a : target) {
			packer.write(a);
		}
	}

	public boolean[] read(Unpacker unpacker, boolean[] to, boolean required)
			throws IOException {
		if (!required && unpacker.trySkipNil()) {
			return null;
		}
		int n = unpacker.readArrayHeader();
		if (to == null || to.length != n) {
			to = new boolean[n];
		}
		for (int i = 0; i < n; i++) {
			to[i] = unpacker.readBoolean();
		}
		return to;
	}

	static public BooleanArrayTemplate getInstance() {
		return instance;
	}

	static final BooleanArrayTemplate instance = new BooleanArrayTemplate();
}
