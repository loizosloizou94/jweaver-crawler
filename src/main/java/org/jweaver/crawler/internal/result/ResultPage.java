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
 * The ResultPage interface represents a result page obtained during web crawling. It is sealed and
 * permits specific implementations: SuccessResultPage and ErrorResultPage.
 */
public sealed interface ResultPage permits SuccessResultPage, ErrorResultPage {

  /**
   * Returns the URI of the result page.
   *
   * @return The URI of the result page.
   */
  String uri();

  /**
   * Returns the content of the result page.
   *
   * @return The content of the result page.
   */
  String content();
}
