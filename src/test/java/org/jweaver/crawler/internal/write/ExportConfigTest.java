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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.jweaver.crawler.internal.test.Constants;

class ExportConfigTest {

  @Test
  void testJsonExport_Success() throws IOException {
    var exportConfig = ExportConfig.exportJson(Constants.TEST_OUTPUT_DIR, false);
    assertEquals(".json", exportConfig.format().extension());
  }

  @Test
  void testMarkdownExport_Success() throws IOException {
    var exportConfig = ExportConfig.exportMarkdown(Constants.TEST_OUTPUT_DIR);
    assertEquals(".md", exportConfig.format().extension());
  }

  @Test
  void testDefaultExport_Success() {
    var exportConfig = ExportConfig.exportDefault();
    assertEquals(".md", exportConfig.format().extension());
    assertFalse(exportConfig.metadata());
  }
}
