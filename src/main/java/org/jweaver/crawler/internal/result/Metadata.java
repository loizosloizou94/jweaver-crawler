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
 * The Metadata record represents metadata associated with a web page. It includes information such
 * as the source of the page, its depth in the crawling hierarchy, the timestamp when it was
 * retrieved, and the number of characters in the content.
 *
 * @param source the uri of the document
 * @param depth the crawling depth
 * @param retrievedOn timestamp of retrieval datetime
 * @param characters a count of document characters
 */
public record Metadata(String source, int depth, String retrievedOn, int characters) {}
