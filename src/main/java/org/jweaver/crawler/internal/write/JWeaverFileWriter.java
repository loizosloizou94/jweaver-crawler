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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.jweaver.crawler.internal.exception.OutputFileException;
import org.jweaver.crawler.internal.result.Connection;
import org.jweaver.crawler.internal.result.NodeError;
import org.jweaver.crawler.internal.result.SuccessResultPage;
import org.jweaver.crawler.internal.util.Constants;
import org.jweaver.crawler.internal.util.FileUtils;

/** A concrete implementation of the {@link JWeaverWriter} interface for writing data to files. */
public final class JWeaverFileWriter implements JWeaverWriter {

  static final int MIN_BYTES_ALLOWED = 400;
  static final ObjectMapper objectMapper = new ObjectMapper();

  private JWeaverFileWriter() {}

  /**
   * Constructs a new JWeaverFileWriter instance.
   *
   * @return JWeaverFileWriter instance
   */
  public static JWeaverFileWriter create() {
    return new JWeaverFileWriter();
  }

  @Override
  public void processSuccess(SuccessResultPage resultPage, ExportConfig config) {
    if (resultPage.content() == null || resultPage.content().isEmpty()) return;
    try {
      if (resultPage.content().getBytes(StandardCharsets.UTF_8).length < MIN_BYTES_ALLOWED) {
        return;
      }
      var metadata = config.metadata() ? resultPage.metadata() : null;
      var output = new Output(metadata, resultPage.title(), resultPage.content());
      var data =
          switch (config.format()) {
            case JSON -> convertToJson(output);
            case MARKDOWN -> MarkdownTemplate.create(output.title(), output.content());
          };
      var filename = createSuccessPageFileName(resultPage.uri(), config);
      var file = getFile(config.path(), filename);
      writeBuffer(file, data);
    } catch (IOException e) {
      throw new OutputFileException(e);
    }
  }

  @Override
  public void processErrors(
      String baseUri, List<NodeError> nodeErrorList, ExportConfig exportConfiguration) {
    try {
      writeOptionFile(exportConfiguration, nodeErrorList, baseUri, Constants.ERRORS_PREFIX);
    } catch (IOException e) {
      throw new OutputFileException(e);
    }
  }

  @Override
  public void processConnectionMap(
      String uri, List<Connection> connections, ExportConfig exportConfiguration) {
    try {
      writeOptionFile(exportConfiguration, connections, uri, Constants.CONNECTIONS_PREFIX);
    } catch (IOException e) {
      throw new OutputFileException(e);
    }
  }

  <T> void writeOptionFile(ExportConfig exportConfiguration, T object, String uri, String prefix)
      throws IOException {
    var content = convertToJson(object);
    var filename = createCustomFileName(prefix, uri);
    var file = getFile(exportConfiguration.path(), filename);
    writeBuffer(file, content);
  }

  void writeBuffer(File file, String data) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(data);
    }
  }

  <T> String convertToJson(T object) throws JsonProcessingException {
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return objectMapper.writeValueAsString(object);
  }

  String createSuccessPageFileName(String host, ExportConfig exportConfiguration) {
    var uniqueId = String.format("%06d", generateFileId());
    return getPrettyHostName(host)
        + "-"
        + uniqueId
        + DateTimeFormatter.ofPattern(Constants.FILE_EXPORT_DT_FORMAT).format(LocalDateTime.now())
        + exportConfiguration.format().extension();
  }

  String createCustomFileName(String prefix, String host) {
    return prefix + "-" + getPrettyHostName(host) + ExportFileFormat.JSON.extension();
  }

  String getPrettyHostName(String linkUri) {
    var uri = URI.create(linkUri);
    return uri.getHost().replace(".", "_");
  }

  long generateFileId() {
    long id = Random.from(RandomGenerator.getDefault()).nextLong(10000);
    if (id < 0) {
      id = (-1) * id;
    }
    return id >= 0 ? id : (-1 * id);
  }

  /**
   * Constructs a File object representing the specified file path. If the directory specified by
   * the original path does not exist, it creates it.
   *
   * @param originalPath The original path where the file should be located.
   * @param filename The name of the file.
   * @return A File object representing the constructed file path.
   * @throws IOException If an I/O error occurs while creating the directory.
   */
  File getFile(final String originalPath, String filename) throws IOException {
    var path = originalPath;
    if (!originalPath.endsWith("/")) {
      path = path.concat("/");
    }
    FileUtils.mkdir(new File(path), true);
    return new File(path + filename);
  }

  /** The MarkdownTemplate class provides static method for creating Markdown content. */
  static class MarkdownTemplate {

    private MarkdownTemplate() {}

    /**
     * Creates Markdown content with the specified title, content, and source.
     *
     * @param title The title of the Markdown content.
     * @param content The content of the Markdown.
     * @return The generated Markdown content as a string.
     */
    public static String create(String title, String content) {
      StringBuilder markdown = new StringBuilder();
      var titleStr = String.format("### %s", title);
      markdown.append(titleStr).append("\n\n");
      var contentSplit = content.split("\n");
      for (var entry : contentSplit) {
        markdown.append(entry).append("\n\n");
      }
      return markdown.toString();
    }
  }
}
