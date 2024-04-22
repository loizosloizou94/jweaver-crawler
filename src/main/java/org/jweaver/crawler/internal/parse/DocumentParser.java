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

package org.jweaver.crawler.internal.parse;

import java.util.Set;

/**
 * The DocumentParser interface defines methods for extracting relevant information from HTML
 * documents. Implementations of this interface are responsible for parsing HTML content to extract
 * titles, bodies, and links from web pages.
 */
public interface DocumentParser {

  /**
   * Parses the HTML body of a web page and extracts the title.
   *
   * @param htmlBody The HTML body of the web page.
   * @param pageUri The URI of the web page.
   * @return The title of the web page.
   */
  String parseTitle(String htmlBody, String pageUri);

  /**
   * Parses the HTML body of a web page and extracts the main content body.
   *
   * @param htmlBody The HTML body of the web page.
   * @param pageUri The URI of the web page.
   * @return The main content body of the web page.
   */
  String parseBody(String htmlBody, String pageUri);

  /**
   * Parses the HTML body of a web page and extracts the links contained within it.
   *
   * @param htmlBody The HTML body of the web page.
   * @param pageUri The URI of the web page.
   * @return A set of URIs representing the links found in the web page.
   */
  Set<String> parseLinks(String htmlBody, String pageUri);
}
