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
 * The MarkdownExportConfig record represents the configuration for exporting data in Markdown
 * format. It implements the ExportConfig interface and specifies the path where the exported files
 * will be stored.
 *
 * @param path The export path of the generated files
 */
public record MarkdownExportConfig(String path) implements ExportConfig {

  /**
   * Retrieves the export file format, which is Markdown for this configuration.
   *
   * @return The export file format, which is Markdown.
   */
  @Override
  public ExportFileFormat format() {
    return ExportFileFormat.MARKDOWN;
  }

  /**
   * Specifies whether metadata should be included in the export. For Markdown export configuration,
   * metadata is not included.
   *
   * @return False, indicating that metadata is not included in the export.
   */
  @Override
  public boolean metadata() {
    return false;
  }
}
