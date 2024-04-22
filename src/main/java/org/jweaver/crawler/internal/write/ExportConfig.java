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

import java.io.File;
import java.io.IOException;
import org.jweaver.crawler.internal.util.Constants;
import org.jweaver.crawler.internal.util.FileUtils;

/**
 * The ExportConfig interface defines methods for configuring data export options. Implementations
 * of this interface specify the path, format, and metadata settings for exporting data. This
 * interface permits specific implementations: JsonExportConfig and MarkdownExportConfig.
 */
public sealed interface ExportConfig permits JsonExportConfig, MarkdownExportConfig {

  /**
   * Creates and returns a new ExportConfig instance configured for exporting data in Markdown
   * format. If the specified output path does not exist, it will be created.
   *
   * @param outputPath The path where exported data will be saved.
   * @return A new ExportConfig instance for Markdown export.
   * @throws IOException If an I/O error occurs while creating the output directory.
   */
  static ExportConfig exportMarkdown(String outputPath) throws IOException {
    FileUtils.mkdir(new File(outputPath), true);
    return new MarkdownExportConfig(outputPath);
  }

  /**
   * Creates and returns a new ExportConfig instance configured for exporting data in JSON format.
   * If the specified output path does not exist, it will be created.
   *
   * @param outputPath The path where exported data will be saved.
   * @param metadata True if metadata should be included in the export, false otherwise.
   * @return A new ExportConfig instance for JSON export.
   * @throws IOException If an I/O error occurs while creating the output directory.
   */
  static ExportConfig exportJson(String outputPath, boolean metadata) throws IOException {
    FileUtils.mkdir(new File(outputPath), true);
    return new JsonExportConfig(outputPath, metadata);
  }

  /**
   * Creates and returns a new ExportConfig instance with default settings for exporting data in
   * Markdown format. The output path is set to the default output path specified in the Constants
   * class.
   *
   * @return A new ExportConfig instance with default settings for Markdown export.
   */
  static ExportConfig exportDefault() {
    return new MarkdownExportConfig(Constants.DEFAULT_OUTPUT_PATH);
  }

  /**
   * Returns the path where exported data will be saved.
   *
   * @return The export path.
   */
  String path();

  /**
   * Returns the file format for exported data.
   *
   * @return The export file format.
   */
  ExportFileFormat format();

  /**
   * Returns a boolean indicating whether metadata should be included in the export.
   *
   * @return True if metadata should be included, false otherwise.
   */
  boolean metadata();
}
