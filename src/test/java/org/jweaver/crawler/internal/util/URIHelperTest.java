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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class URIHelperTest {

  @Test
  void testNotAllowedUriJpegImage_ReturnFalse() {
    assertFalse(URIHelper.isAllowedUrl("https://192.168.10.2:8080/file.jpeg"));
  }

  @Test
  void testAllowedUnknownExtensionUri_ReturnTrue() {
    assertTrue(URIHelper.isAllowedUrl("https://192.168.10.2:8080/file.unknown"));
  }

  @Test
  void testExternalUri_ReturnSuccess() {
    assertTrue(URIHelper.isExternalUri("https://192.168.1.0:8080", "https://192.168.2.0:8080"));
  }

  @Test
  void testSameUrisWithoutWww_Success() {
    assertTrue(
        URIHelper.equalHostUri(
            "192.168.10.2:8080/file.unknown", "www.192.168.10.2:8080/file.unknown"));
    assertEquals(
        "192.168.10.2:8080/file.unknown",
        URIHelper.transformUri("www.192.168.10.2:8080/file.unknown"));
  }
}
