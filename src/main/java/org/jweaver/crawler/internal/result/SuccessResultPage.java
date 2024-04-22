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

package org.jweaver.crawler.internal.result;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * The SuccessResultPage record represents a successful result page obtained during web crawling. It
 * includes information such as the URI, title, content, links, metadata, and depth of the page.
 * This record implements the ResultPage interface.
 *
 * @param uri page URI
 * @param title document title
 * @param content document content
 * @param linkSet child links on the page
 * @param metadata collected metadata of the page
 * @param depth current depth
 */
public record SuccessResultPage(
    String uri, String title, String content, Set<PageLink> linkSet, Metadata metadata, int depth)
    implements ResultPage {

  /**
   * Creates a SuccessResultPage instance based on the provided PageLink, title, content, and link
   * set.
   *
   * @param pageLink The PageLink representing the URI and depth of the result page.
   * @param title The title of the result page.
   * @param content The content of the result page.
   * @param linkSet The set of links found in the result page.
   * @return A SuccessResultPage instance.
   */
  public static SuccessResultPage create(
      PageLink pageLink, String title, String content, Set<PageLink> linkSet) {
    return new SuccessResultPage(
        pageLink.url(),
        title,
        content,
        linkSet,
        new Metadata(
            pageLink.url(), pageLink.depth(), LocalDateTime.now().toString(), content.length()),
        pageLink.depth());
  }
}
