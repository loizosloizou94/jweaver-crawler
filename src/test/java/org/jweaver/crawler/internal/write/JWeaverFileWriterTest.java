/*
 * Copyright (C) 2024  Loizos Loizou
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.jweaver.crawler.internal.write;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.jweaver.crawler.internal.result.Connection;
import org.jweaver.crawler.internal.result.Metadata;
import org.jweaver.crawler.internal.result.NodeError;
import org.jweaver.crawler.internal.result.PageLink;
import org.jweaver.crawler.internal.result.SuccessResultPage;
import org.jweaver.crawler.internal.test.Constants;
import org.mockito.Mock;

class JWeaverFileWriterTest {

  static final String hostName = "https://192.168.12.0:8080";
  @Mock final JWeaverFileWriter mockedInstance = mock(JWeaverFileWriter.class);
  final JWeaverFileWriter fileWriter = JWeaverFileWriter.create();

  @Test
  void getPrettyHostName_Success() {
    String filename = fileWriter.getPrettyHostName(hostName);
    assertEquals("192_168_12_0", filename);
  }

  @Test
  void testCreateCustomFileName_Success() {
    String filename = fileWriter.createCustomFileName("test", hostName);
    assertEquals("test-192_168_12_0.json", filename);
  }

  @Test
  void testCreateSuccessPageFileName_Success() {
    ExportConfig exportConfig = ExportConfig.exportDefault();
    String filename = fileWriter.createSuccessPageFileName(hostName, exportConfig);
    assertTrue(filename.contains(fileWriter.getPrettyHostName(hostName)));
    assertTrue(filename.endsWith(".md"));
    assertNotNull(filename);
  }

  @Test
  void testGenerateFileIdentifier_Success() {
    long val = fileWriter.generateFileId();
    assertTrue(val < 10000);
    assertTrue(val >= 0);
  }

  @Test
  void serializeOutputJsonContent_Success() throws JsonProcessingException {
    var jsonText =
        "{\"metadata\":{\"source\":\"https://192.168.12.0:8080\",\"depth\":1,\"retrievedOn\":\"123456T654321\",\"characters\":11},\"title\":\"JWeaver\",\"content\":\"Hello World\"}";
    var dateTime = "123456T654321";
    var metadata = new Metadata(hostName, 1, dateTime, 11);
    var output = new Output(metadata, "JWeaver", "Hello World");
    var json = fileWriter.convertToJson(output);
    assertEquals(jsonText, json);
  }

  @Test
  void serializeOutputMarkdownContent_Success() {
    var markdownText =
        """
### JWeaver

Hello World



""";
    var markdown = JWeaverFileWriter.MarkdownTemplate.create("JWeaver", "Hello World");
    assertEquals(markdownText.trim(), markdown.trim());
  }

  @Test
  void testWriteSuccessResultPage() throws IOException {
    var successResultPage =
        SuccessResultPage.create(new PageLink(hostName, 1), "JWeaver", "a".repeat(405), Set.of());
    doCallRealMethod().when(mockedInstance).processSuccess(any(), any());
    doNothing().when(mockedInstance).writeBuffer(any(), any());
    mockedInstance.processSuccess(successResultPage, ExportConfig.exportDefault());
    verify(mockedInstance, times(1)).getFile(any(), any());
  }

  @Test
  void testWriteConnectionMap_Success() throws IOException {
    var connectionList = new ArrayList<Connection>();
    connectionList.add(new Connection(hostName, hostName, 1));
    doCallRealMethod()
        .when(mockedInstance)
        .processConnectionMap(hostName, connectionList, ExportConfig.exportDefault());
    doCallRealMethod().when(mockedInstance).writeOptionFile(any(), any(), any(), any());
    doNothing().when(mockedInstance).writeBuffer(any(), any());
    mockedInstance.processConnectionMap(hostName, connectionList, ExportConfig.exportDefault());
    verify(mockedInstance, times(1)).getFile(any(), any());
  }

  @Test
  void testWriteErrorNodes_Success() throws IOException {
    var errors = new ArrayList<NodeError>();
    errors.add(new NodeError(hostName, 1, "connection timeout"));
    doCallRealMethod()
        .when(mockedInstance)
        .processErrors(hostName, errors, ExportConfig.exportDefault());
    doCallRealMethod().when(mockedInstance).writeOptionFile(any(), any(), any(), any());
    doNothing().when(mockedInstance).writeBuffer(any(), any());
    mockedInstance.processErrors(hostName, errors, ExportConfig.exportDefault());
    verify(mockedInstance, times(1)).getFile(any(), any());
  }

  @Test
  void testWriteSuccessResultPageContentLessBytes_Failure() throws IOException {
    var successResultPage =
        SuccessResultPage.create(new PageLink(hostName, 1), "JWeaver", "a".repeat(30), Set.of());
    doCallRealMethod().when(mockedInstance).processSuccess(any(), any());
    doNothing().when(mockedInstance).writeBuffer(any(), any());
    mockedInstance.processSuccess(
        successResultPage, ExportConfig.exportMarkdown(Constants.TEST_OUTPUT_DIR));
    var file = new File(Constants.TEST_OUTPUT_DIR);
    file.deleteOnExit();
    verify(mockedInstance, times(0)).getFile(any(), any());
  }

  @Test
  void testWriterFile_ReturnValidFile() throws IOException {
    var fileWriter1 = JWeaverFileWriter.create();
    var file = fileWriter1.getFile(Constants.TEST_OUTPUT_DIR, "filename");
    file.deleteOnExit();
    var tempDir = new File(Constants.TEST_OUTPUT_DIR);
    tempDir.deleteOnExit();
    assertNotNull(file);
  }
}
