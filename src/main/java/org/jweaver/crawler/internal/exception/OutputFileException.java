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
 * The OutputFileException class represents an unchecked exception that occurs when there is an
 * issue with an output file or directory.
 */
public class OutputFileException extends RuntimeException {

  /**
   * Constructs a new OutputFileException (RuntimeException) with the specified cause.
   *
   * @param e The cause of the exception
   */
  public OutputFileException(Exception e) {
    super(e.getMessage());
  }
}
