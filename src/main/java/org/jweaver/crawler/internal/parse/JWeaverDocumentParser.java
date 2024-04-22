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
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * The JWeaverDocumentParser class is responsible for parsing HTML documents to extract relevant
 * information. It implements the DocumentParser interface and provides implementations to parse the
 * title, body, and links from HTML content.
 */
public final class JWeaverDocumentParser implements DocumentParser {

  private static final String LINK_HREF = "a[href]";
  private static final String LINK_ATTR_HREF = "abs:href";
  private static final String PARAGRAPH_ATTR = "p";
  private static final String TITLE_ATTR = "title";
  /** Constructs a new JWeaverDocumentParser instance. */
  public JWeaverDocumentParser() {
    // create a new JWeaverDocumentParser parser
  }

  @Override
  public String parseTitle(String htmlBody, String pageUri) {
    Document document = Jsoup.parse(htmlBody, pageUri);
    var titles = document.select(TITLE_ATTR);
    return titles.isEmpty() ? "" : titles.getFirst().text();
  }

  @Override
  public String parseBody(String htmlBody, String pageUri) {
    Document document = Jsoup.parse(htmlBody, pageUri);
    var elements = document.select(PARAGRAPH_ATTR);
    var stringBuilder = new StringBuilder();
    elements.eachText().forEach(p -> appendParagraph(stringBuilder, p));
    return stringBuilder.toString();
  }

  private void appendParagraph(StringBuilder instance, String paragraph) {
    instance.append(paragraph);
    instance.append("\n");
  }

  @Override
  public Set<String> parseLinks(String htmlBody, String pageUri) {
    Document document = Jsoup.parse(htmlBody, pageUri);
    var links = document.select(LINK_HREF);
    return links.stream().map(p -> p.attr(LINK_ATTR_HREF)).collect(Collectors.toUnmodifiableSet());
  }
}
