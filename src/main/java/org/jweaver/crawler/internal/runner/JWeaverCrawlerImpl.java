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

package org.jweaver.crawler.internal.runner;

import static org.jweaver.crawler.internal.util.BuilderValidator.requireNonEmpty;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jweaver.crawler.JWeaverCrawler;
import org.jweaver.crawler.internal.parse.DocumentParser;
import org.jweaver.crawler.internal.parse.JWeaverDocumentParser;
import org.jweaver.crawler.internal.util.URIHelper;
import org.jweaver.crawler.internal.write.ExportConfig;
import org.jweaver.crawler.internal.write.JWeaverFileWriter;
import org.jweaver.crawler.internal.write.JWeaverWriter;

/** A concrete implementation of {@link JWeaverCrawler} providing web crawling functionality. */
public final class JWeaverCrawlerImpl implements JWeaverCrawler {

  private static final Integer DEFAULT_MAX_DEPTH = 3;
  private static final Duration DEFAULT_POLITENESS_DELAY = Duration.ofSeconds(3);
  private final HttpClient httpClient;
  private final DocumentParser parser;
  private final JWeaverWriter writer;
  private final Duration politenessDelay;
  private final List<JWeaverTask> taskList;
  private final TaskExecutor taskExecutor;
  private final Integer maxDepth;
  private final ExportConfig exportConfiguration;

  /**
   * Constructs a new JWeaverCrawlerImpl instance.
   *
   * @param builder The builder containing necessary parameters to construct the crawler.
   */
  public JWeaverCrawlerImpl(JWeaverBuilderImpl builder) {
    requireNonEmpty(builder.uriSet);
    if (builder.politenessDelay == null) {
      this.politenessDelay = DEFAULT_POLITENESS_DELAY;
    } else {
      this.politenessDelay = builder.politenessDelay;
    }
    this.parser = Objects.requireNonNullElseGet(builder.documentParser, JWeaverDocumentParser::new);
    this.maxDepth = Objects.requireNonNullElse(builder.maxDepth, DEFAULT_MAX_DEPTH);
    this.writer = Objects.requireNonNullElseGet(builder.writer, JWeaverFileWriter::create);
    if (builder.httpClient == null) {
      this.httpClient =
          HttpClient.newBuilder()
              .followRedirects(HttpClient.Redirect.ALWAYS)
              .connectTimeout(Duration.ofSeconds(5))
              .build();
    } else {
      this.httpClient = builder.httpClient;
    }
    this.exportConfiguration =
        Objects.requireNonNullElseGet(builder.exportConfiguration, ExportConfig::exportDefault);
    this.taskExecutor = TaskExecutorImpl.create();

    this.taskList = getExecutionList(builder.uriSet);
  }

  private JWeaverTask createExecution(String baseUri) {
    return new JWeaverTask(
        baseUri,
        this.httpClient,
        this.politenessDelay,
        this.exportConfiguration,
        this.maxDepth,
        this.parser,
        this.writer);
  }

  @Override
  public void run() {
    taskExecutor.run(this.taskList);
  }

  @Override
  public void runParallel() {
    taskExecutor.runParallel(this.taskList);
  }

  /**
   * Generates a list of JWeaverTask instances based on the provided set of URIs.
   *
   * @param uriSet The set of URIs to be processed.
   * @return A list of JWeaverTask instances ready for execution.
   * @throws IllegalArgumentException if no valid URIs are provided.
   */
  private List<JWeaverTask> getExecutionList(Set<String> uriSet) {
    // Filter out invalid URIs
    var filtered = uriSet.stream().filter(URIHelper::isValidUri).collect(Collectors.toSet());
    // Ensure at least one valid URI is present
    if (filtered.isEmpty()) throw new IllegalArgumentException("No valid uris provided");
    // Create JWeaverTask instances for each valid URI
    return filtered.stream().map(this::createExecution).toList();
  }

  List<JWeaverTask> getTaskList() {
    return this.taskList;
  }
}
