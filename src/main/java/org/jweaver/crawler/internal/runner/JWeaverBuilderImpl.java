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

import static java.util.Objects.requireNonNull;
import static org.jweaver.crawler.internal.util.BuilderValidator.requireNonEmpty;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Set;
import org.jweaver.crawler.JWeaverCrawler;
import org.jweaver.crawler.internal.parse.DocumentParser;
import org.jweaver.crawler.internal.write.ExportConfig;
import org.jweaver.crawler.internal.write.JWeaverWriter;

/**
 * A concrete implementation of the {@link JWeaverCrawler.Builder} interface used to configure and
 * build instances of {@link JWeaverCrawler}.
 */
public final class JWeaverBuilderImpl implements JWeaverCrawler.Builder {

  HttpClient httpClient;
  DocumentParser documentParser;
  JWeaverWriter writer;
  ExportConfig exportConfiguration;
  Duration politenessDelay;
  Integer maxDepth;
  Set<String> uriSet;

  /** Constructs a new JWeaverBuilderImpl instance. */
  public JWeaverBuilderImpl() {
    // return an empty builder instance
  }

  @Override
  public JWeaverCrawler.Builder httpClient(HttpClient httpClient) {
    requireNonNull(httpClient);
    this.httpClient = httpClient;
    return this;
  }

  @Override
  public JWeaverCrawler.Builder parser(DocumentParser documentParser) {
    requireNonNull(documentParser);
    this.documentParser = documentParser;
    return this;
  }

  @Override
  public JWeaverCrawler.Builder writer(JWeaverWriter writer) {
    requireNonNull(writer);
    this.writer = writer;
    return this;
  }

  @Override
  public JWeaverCrawler.Builder exportConfiguration(ExportConfig exportConfiguration) {
    requireNonNull(exportConfiguration);
    this.exportConfiguration = exportConfiguration;
    return this;
  }

  @Override
  public JWeaverCrawler.Builder politenessDelay(Duration politenessDelay) {
    requireNonNull(politenessDelay);
    this.politenessDelay = politenessDelay;
    return this;
  }

  @Override
  public JWeaverCrawler.Builder maxDepth(int maxDepth) {
    if (maxDepth <= 0) {
      throw new IllegalArgumentException("Depth must be greater than zero");
    }
    this.maxDepth = maxDepth;
    return this;
  }

  @Override
  public JWeaverCrawler build(Set<String> uriSet) {
    if (uriSet == null) throw new IllegalArgumentException("URI list must be provided");
    requireNonEmpty(uriSet, "URI set cannot be null or empty");
    this.uriSet = uriSet;
    return new JWeaverCrawlerImpl(this);
  }
}
