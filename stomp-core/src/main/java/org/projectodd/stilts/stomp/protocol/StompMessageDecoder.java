/*
 * Copyright 2011 Red Hat, Inc, and individual contributors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.projectodd.stilts.stomp.protocol;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.projectodd.stilts.logging.Logger;
import org.projectodd.stilts.stomp.protocol.StompFrame.Command;
import org.projectodd.stilts.stomp.spi.StompMessageFactory;

public class StompMessageDecoder extends OneToOneDecoder {
    
    private StompMessageFactory messageFactory;

    public StompMessageDecoder(Logger log, StompMessageFactory messageFactory) {
        this.log = log;
        this.messageFactory = messageFactory;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        log.info( "message decode: " + msg );
        if (msg instanceof StompContentFrame) {
            StompContentFrame frame = (StompContentFrame) msg;
            boolean isError = false;
            if (frame.getCommand() == Command.ERROR) {
                isError = true;
            }
            return this.messageFactory.createMessage( frame.getHeaders(), frame.getContent(), isError );
        }
        return null;
    }

    private Logger log;

}
