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

package org.jweaver.crawler.internal.write;

/**
 * The ExportFileFormat enum represents the file formats supported for data export. Currently
 * supported formats are Markdown (.md) and JSON (.json).
 */
public enum ExportFileFormat {

  /** Markdown file format. */
  MARKDOWN(".md"),
  /** JSON file format. */
  JSON(".json");

  /** The file extension associated with the file format. */
  private final String extension;

  /**
   * Constructs an ExportFileFormat with the given file extension.
   *
   * @param extension The file extension associated with the file format.
   */
  ExportFileFormat(String extension) {
    this.extension = extension;
  }

  /**
   * Returns the file extension associated with the file format.
   *
   * @return The file extension (including the period).
   */
  public String extension() {
    return this.extension;
  }
}
