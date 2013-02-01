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
import org.msgpack.unpacker.Unpacker;
import org.msgpack.MessageTypeException;
import org.msgpack.MessageUnpackable;

public class MessageUnpackableTemplate extends
		AbstractUnpackerTemplate<MessageUnpackable> {
	private Class<?> targetClass;

	MessageUnpackableTemplate(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public MessageUnpackable read(Unpacker u, MessageUnpackable to,
			boolean required) throws IOException {
		if (!required && u.trySkipNil()) {
			return null;
		}
		if (to == null) {
			try {
				to = (MessageUnpackable) targetClass.newInstance();
			} catch (InstantiationException e) {
				throw new MessageTypeException(e);
			} catch (IllegalAccessException e) {
				throw new MessageTypeException(e);
			}
		}
		to.readFrom(u);
		return to;
	}
}
