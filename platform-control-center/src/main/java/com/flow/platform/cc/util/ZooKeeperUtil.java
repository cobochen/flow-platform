/*
 * Copyright 2017 flow.ci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flow.platform.cc.util;

import com.flow.platform.cc.domain.ZkServer;
import com.flow.platform.util.Logger;
import java.io.IOException;
import java.util.Properties;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

/**
 * @author yh@firim
 */
public class ZooKeeperUtil {

    public static Boolean start(ZkServer zkServer, Properties properties) {
        Logger logger = new Logger(ZooKeeperUtil.class);

        try {
            QuorumPeerConfig quorumPeerConfig = new QuorumPeerConfig();
            quorumPeerConfig.parseProperties(properties);

            ServerConfig configuration = new ServerConfig();
            configuration.readFrom(quorumPeerConfig);

            new Thread(() -> {
                try {
                    zkServer.runFromConfig(configuration);
                } catch (IOException e) {
                    logger.traceMarker("start", String.format("start zookeeper error - %s", e));
                }
            }).start();

            return true;
        } catch (Exception e) {
            logger.traceMarker("start", String.format("start zookeeper error - %s", e));
            return false;
        }

    }
}
