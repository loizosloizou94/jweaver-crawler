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
 * The Connection record represents a connection between a parent URI and a child URI, along with
 * the depth of the connection. It is used to model relationships between web pages during web
 * crawling.
 *
 * @param parent the parent URI
 * @param child the child URI
 * @param depth the depth where this connection found
 */
public record Connection(String parent, String child, int depth) {}
