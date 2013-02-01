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
package org.msgpack;

import org.msgpack.template.TemplateRegistry;

/**
 * <p>
 * This is basic class to use MessagePack for Java. It creates serializers and
 * deserializers for objects of classes.
 * </p>
 * 
 * <p>
 * See <a
 * href="http://wiki.msgpack.org/display/MSGPACK/QuickStart+for+Java">Quick
 * Start for Java</a> on MessagePack wiki.
 * </p>
 * 
 */
public class MessagePack {
    private TemplateRegistry registry;

    /**
     * 
     * @since 0.6.0
     */
    public MessagePack() {
        registry = new TemplateRegistry(null);
    }

    /**
     * 
     * @since 0.6.0
     * @param msgpack
     */
    public MessagePack(MessagePack msgpack) {
        registry = new TemplateRegistry(msgpack.registry);
    }

    protected MessagePack(TemplateRegistry registry) {
        this.registry = registry;
    }

    // TODO #MN
    // TODO #MN
    // TODO #MN
}
