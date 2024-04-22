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

import java.util.List;
import org.jweaver.crawler.internal.result.Connection;
import org.jweaver.crawler.internal.result.NodeError;
import org.jweaver.crawler.internal.result.SuccessResultPage;

/**
 * The JWeaverWriter interface defines methods for processing and writing the results of the web
 * crawling process. Implementations of this interface are responsible for handling successful
 * crawled pages, error information, and connection maps generated during the crawling process.
 */
public interface JWeaverWriter {

  /**
   * Processes a successfully crawled page and writes the result using the provided export
   * configuration.
   *
   * @param successResultPage The success result page containing information about the crawled page.
   * @param exportConfiguration The export configuration specifying how the result should be
   *     written.
   */
  void processSuccess(SuccessResultPage successResultPage, ExportConfig exportConfiguration);

  /**
   * Processes errors encountered during crawling and writes error information using the provided
   * export configuration.
   *
   * @param baseUri The base URI of the page where the errors occurred.
   * @param nodeErrorList A list of NodeError objects containing information about the errors of
   *     failed crawled uris
   * @param exportConfiguration The export configuration
   */
  void processErrors(
      String baseUri, List<NodeError> nodeErrorList, ExportConfig exportConfiguration);

  /**
   * Processes connection map information generated during crawling and writes it using the provided
   * export configuration.
   *
   * @param baseUri The base URI of the page.
   * @param connections A list of Connection objects representing connections between pages.
   * @param exportConfiguration The export configuration
   */
  void processConnectionMap(
      String baseUri, List<Connection> connections, ExportConfig exportConfiguration);
}
