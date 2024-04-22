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
 * The NodeError record represents an error associated with a specific node during web crawling. It
 * includes information such as the URI of the node, its depth in the crawling hierarchy, and the
 * error message.
 *
 * @param uri URI of the node
 * @param depth Current depth of the node
 * @param error Error message during crawling attempt
 */
public record NodeError(String uri, int depth, String error) {}
