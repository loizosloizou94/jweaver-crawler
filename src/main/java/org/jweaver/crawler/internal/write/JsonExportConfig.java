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
 * The JsonExportConfig record represents the configuration for exporting data in JSON format. It
 * implements the ExportConfig interface and specifies the path where the exported files will be
 * stored, as well as whether metadata should be included in the export.
 *
 * @param path The export path of the generated files
 * @param metadata Enable or disable metadata on the generated file
 */
public record JsonExportConfig(String path, boolean metadata) implements ExportConfig {

  /**
   * Retrieves the export file format, which is JSON for this configuration.
   *
   * @return The export file format, which is JSON.
   */
  @Override
  public ExportFileFormat format() {
    return ExportFileFormat.JSON;
  }
}
