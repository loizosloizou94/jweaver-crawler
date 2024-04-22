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

/**
 * The ErrorResultPage record represents a result page containing an error encountered during web
 * crawling. It includes the URI of the page, the depth at which the error occurred, and the error
 * content. This record implements the ResultPage interface.
 *
 * @param uri page URI
 * @param depth current depth
 * @param content error message
 */
public record ErrorResultPage(String uri, int depth, String content) implements ResultPage {

  /**
   * Creates an ErrorResultPage instance based on the provided PageLink and error content.
   *
   * @param pageLink The PageLink representing the URI and depth of the error page.
   * @param content The error message or the website content.
   * @return An ErrorResultPage instance.
   */
  public static ErrorResultPage create(PageLink pageLink, String content) {
    return new ErrorResultPage(pageLink.url(), pageLink.depth(), content);
  }
}
