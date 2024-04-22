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

package org.jweaver.crawler;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Set;
import org.jweaver.crawler.internal.parse.DocumentParser;
import org.jweaver.crawler.internal.parse.JWeaverDocumentParser;
import org.jweaver.crawler.internal.runner.JWeaverBuilderImpl;
import org.jweaver.crawler.internal.write.ExportConfig;
import org.jweaver.crawler.internal.write.JWeaverFileWriter;
import org.jweaver.crawler.internal.write.JWeaverWriter;

/**
 * Represents the JWeaverCrawler abstract class, which facilitates web crawling operations. This
 * class provides methods for executing the crawler either in parallel or sequentially. Users can
 * customize various attributes of the crawler with the Builder pattern.
 */
public interface JWeaverCrawler {

  /**
   * Returns a new instance of the builder for configuring and creating a JWeaverCrawler.
   *
   * @return A new instance of the builder.
   */
  static Builder builder() {
    return new JWeaverBuilderImpl();
  }

  /**
   * <b>This should be the preferred choice to run the crawler</b>.
   *
   * <p>Runs the different JWeaverTask in parallel using Virtual Threads, crawling multiple uris (of
   * different hosts) concurrently.
   */
  void runParallel();

  /**
   * Runs the executions sequentially.
   *
   * <p><b>Consider using {@link #runParallel()} for improved performance.</b>
   */
  void run();

  /**
   * The Builder interface provides methods for building and customize an instance of
   * JWeaverCrawler.
   */
  interface Builder {

    /**
     * Sets the HTTP client to be used by the crawler for making HTTP requests.
     *
     * <p><b>Default</b> Will use a default HttpClient if not provided with connection timeout 5
     * seconds and follow redirects policy to AlWAYS
     *
     * @param httpClient The HTTP client to be used. (Optional)
     * @return This builder instance.
     */
    Builder httpClient(HttpClient httpClient);

    /**
     * Sets the document parser to be used by the crawler for parsing relevant information from HTML
     * body.
     *
     * <p><b>Default</b> Will use the {@link JWeaverDocumentParser} if not provided.
     *
     * @param documentParser The document parser to be used. (Optional)
     * @return This builder instance.
     */
    Builder parser(DocumentParser documentParser);

    /**
     * Sets the writer for exporting the crawled data.
     *
     * <p><b>Default</b> Will use a {@link JWeaverFileWriter} if not provided
     *
     * @param writer The writer for exporting data. (Optional)
     * @return This builder instance for method chaining.
     */
    Builder writer(JWeaverWriter writer);

    /**
     * Sets the export configuration for configuring data export options.
     *
     * <p><b>Default</b> Will use the default {@link ExportConfig#exportDefault()} which uses a
     * Markdown format and the 'output'/ path for files.
     *
     * @param configuration The export configuration. (Optional)
     * @return This builder instance for method chaining.
     */
    Builder exportConfiguration(ExportConfig configuration);

    /**
     * Sets the politeness delay between consecutive requests made by the crawler to the same host
     *
     * <p><b>Default</b> 3 seconds.
     *
     * @param duration The politeness delay duration. (Optional)
     * @return This builder instance for method chaining.
     */
    Builder politenessDelay(Duration duration);

    /**
     * Sets the maximum depth of crawling .
     *
     * <p><b>Default</b> 3
     *
     * @param maxDepth The maximum depth of crawling. (Optional)
     * @return This builder instance for method chaining.
     */
    Builder maxDepth(int maxDepth);

    /**
     * Builds and returns a new instance of JWeaverCrawler with the configured parameters.
     *
     * @param uriList The required initial set of URIs to start crawling from. Each URI should be
     *     from a different host. For each URI, a new JWeaverTask (execution) will be created.
     * @return A new instance of JWeaverCrawler.
     */
    JWeaverCrawler build(Set<String> uriList);
  }
}
