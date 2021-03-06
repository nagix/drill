/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.drill.exec.store.image;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.common.expression.SchemaPath;
import org.apache.drill.common.logical.StoragePluginConfig;
import org.apache.drill.exec.ops.FragmentContext;
import org.apache.drill.exec.server.DrillbitContext;
import org.apache.drill.exec.store.RecordReader;
import org.apache.drill.exec.store.RecordWriter;
import org.apache.drill.exec.store.dfs.DrillFileSystem;
import org.apache.drill.exec.store.dfs.easy.EasyFormatPlugin;
import org.apache.drill.exec.store.dfs.easy.EasyWriter;
import org.apache.drill.exec.store.dfs.easy.FileWork;
import org.apache.hadoop.conf.Configuration;

public class ImageFormatPlugin extends EasyFormatPlugin<ImageFormatConfig> {

  private final static String DEFAULT_NAME = "image";

  public ImageFormatPlugin(String name, DrillbitContext context, Configuration fsConf,
                           StoragePluginConfig storageConfig) {
    super(name, context, fsConf, storageConfig, new ImageFormatConfig(), true, false, false, false,
        Collections.<String>emptyList(), DEFAULT_NAME);
  }

  public ImageFormatPlugin(String name, DrillbitContext context, Configuration fsConf,
                           StoragePluginConfig storageConfig, ImageFormatConfig formatConfig) {
    super(name, context, fsConf, storageConfig, formatConfig, true, false, false, false,
        formatConfig.getExtensions(), DEFAULT_NAME);
  }

  @Override
  public RecordReader getRecordReader(FragmentContext context, DrillFileSystem dfs, FileWork fileWork,
      List<SchemaPath> columns, String userName) throws ExecutionSetupException {
    return new ImageRecordReader(context, dfs, fileWork.getPath(),
        ((ImageFormatConfig)formatConfig).hasFileSystemMetadata(),
        ((ImageFormatConfig)formatConfig).isDescriptive(),
        ((ImageFormatConfig)formatConfig).getTimeZone());
  }

  @Override
  public RecordWriter getRecordWriter(FragmentContext context, EasyWriter writer) throws IOException {
    throw new UnsupportedOperationException("Drill doesn't currently support writing to image files.");
  }

  @Override
  public int getReaderOperatorType() {
    return 4002;
  }

  @Override
  public int getWriterOperatorType() {
    throw new UnsupportedOperationException("Drill doesn't currently support writing to image files.");
  }

  @Override
  public boolean supportsPushDown() {
    return true;
  }
}
