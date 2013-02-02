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
import java.util.HashMap;
import org.msgpack.unpacker.Unpacker;

public class MapUnpackerTemplate<K, V> extends
		AbstractUnpackerTemplate<Map<K, V>> {
	private UnpackerTemplate<K> keyUnpackerTemplate;
	private UnpackerTemplate<V> valueUnpackerTemplate;

	public MapUnpackerTemplate(UnpackerTemplate<K> keyUnpackerTemplate,
			UnpackerTemplate<V> valueUnpackerTemplate) {
		this.keyUnpackerTemplate = keyUnpackerTemplate;
		this.valueUnpackerTemplate = valueUnpackerTemplate;
	}

	public Map<K, V> read(Unpacker u, Map<K, V> to, boolean required)
			throws IOException {
		if (!required && u.trySkipNil()) {
			return null;
		}
		int n = u.readMapHeader();
		Map<K, V> map;
		if (to != null) {
			map = (Map<K, V>) to;
			map.clear();
		} else {
			map = new HashMap<K, V>(n);
		}
		for (int i = 0; i < n; i++) {
			K key = keyUnpackerTemplate.read(u, null);
			V value = valueUnpackerTemplate.read(u, null);
			map.put(key, value);
		}
		return map;
	}
}
