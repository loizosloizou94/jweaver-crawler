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

package org.jweaver.crawler.internal.util;

import java.io.File;
import java.io.IOException;

/** This utility class provides file-related operations. Avoid adding more libraries */
public final class FileUtils {

  private FileUtils() {}

  /**
   * Creates a directory if it does not exist.
   *
   * @param dir The directory to create.
   * @param createDirectoryIfNotExisting If true, creates the directory if it does not exist;
   *     otherwise, throws an IOException.
   * @throws IOException If an I/O error occurs while creating the directory, or if the directory
   *     already exists as a file.
   */
  public static void mkdir(final File dir, final boolean createDirectoryIfNotExisting)
      throws IOException {
    if (!dir.exists()) {
      if (!createDirectoryIfNotExisting) {
        throw new IOException("The directory " + dir.getAbsolutePath() + " does not exist.");
      }
      if (!dir.mkdirs()) {
        throw new IOException("Could not create directory " + dir.getAbsolutePath());
      }
    }
    if (!dir.isDirectory()) {
      throw new IOException(
          "File " + dir + " exists and is not a directory. Unable to create directory.");
    }
  }
}
