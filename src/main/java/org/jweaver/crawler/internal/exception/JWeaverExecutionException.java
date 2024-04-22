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

package org.jweaver.crawler.internal.exception;

/**
 * The JWeaverExecutionException class represents an unchecked exception that occurs during the
 * execution of a JWeaver task.
 */
public class JWeaverExecutionException extends RuntimeException {

  /**
   * Constructs a new JWeaverExecutionException (RuntimeException) with the specified detail
   * message.
   *
   * @param message The detail message
   */
  public JWeaverExecutionException(String message) {
    super(message);
  }
}
