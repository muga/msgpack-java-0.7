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
import java.util.Map;
import org.msgpack.packer.Packer;
import org.msgpack.MessageTypeException;

public class MapPackerTemplate<K, V> extends AbstractPackerTemplate<Map<K, V>> {
	private PackerTemplate<K> keyPackerTemplate;
	private PackerTemplate<V> valuePackerTemplate;

	public MapPackerTemplate(PackerTemplate<K> keyPackerTemplate,
			PackerTemplate<V> valuePackerTemplate) {
		this.keyPackerTemplate = keyPackerTemplate;
		this.valuePackerTemplate = valuePackerTemplate;
	}

	public void write(Packer packer, Map<K, V> target, boolean required)
			throws IOException {
		if (!(target instanceof Map)) {
			if (target == null) {
				if (required) {
					throw new MessageTypeException("Attempted to write null");
				}
				packer.writeNil();
				return;
			}
			throw new MessageTypeException("Target is not a Map but "
					+ target.getClass());
		}
		Map<K, V> map = (Map<K, V>) target;
		packer.writeMapHeader(map.size());
		for (Map.Entry<K, V> pair : map.entrySet()) {
			keyPackerTemplate.write(packer, pair.getKey());
			valuePackerTemplate.write(packer, pair.getValue());
		}
	}
}
