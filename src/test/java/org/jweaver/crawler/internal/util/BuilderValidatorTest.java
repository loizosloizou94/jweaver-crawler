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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.jweaver.crawler.internal.exception.JWeaverExecutionException;
import org.jweaver.crawler.internal.exception.OutputFileException;

class BuilderValidatorTest {

  @Test
  void testNonEmptyValidatorWithEmptyStr_ThrowsIllegalArgumentException() {
    Exception ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> BuilderValidator.requireNonEmpty("", "error_message"));

    assertEquals("error_message", ex.getMessage());
    assertEquals(IllegalArgumentException.class, ex.getClass());
  }

  @Test
  void testNonEmptyValidatorWithNullArg_throwsNullPointerException() {
    Exception ex =
        assertThrows(
            NullPointerException.class,
            () -> BuilderValidator.requireNonEmpty(null, "error_message"));

    assertEquals("error_message", ex.getMessage());
    assertEquals(NullPointerException.class, ex.getClass());

    ex = assertThrows(NullPointerException.class, () -> BuilderValidator.requireNonEmpty(null));
    assertEquals(NullPointerException.class, ex.getClass());

    ex = assertThrows(IllegalArgumentException.class, () -> BuilderValidator.requireNonEmpty(""));

    assertEquals(IllegalArgumentException.class, ex.getClass());
    assertNull(ex.getMessage());
  }

  @Test
  void testExecutionExceptionSuperClass_expectRuntime() {
    var ex = new JWeaverExecutionException("Thread interrupted");
    assertEquals("Thread interrupted", ex.getMessage());
    assertEquals(RuntimeException.class, ex.getClass().getSuperclass());
  }

  @Test
  void testOutputExceptionSuperClass_expectRuntime() {
    var ex = new OutputFileException(new IOException("Invalid directory"));
    assertEquals("Invalid directory", ex.getMessage());
    assertEquals(RuntimeException.class, ex.getClass().getSuperclass());
  }

  @Test
  void testRequireNonEmptyWithValidObj() {
    var val = BuilderValidator.requireNonEmpty(BigDecimal.ZERO);
    assertNotNull(val);
    assertEquals(BigDecimal.ZERO, val);
    val = BuilderValidator.requireNonEmpty(BigDecimal.ZERO, "Value cannot be null");
    assertNotNull(val);
    assertEquals(BigDecimal.ZERO, val);
  }
}
