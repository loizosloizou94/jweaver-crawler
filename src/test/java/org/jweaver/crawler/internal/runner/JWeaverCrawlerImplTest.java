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

package org.jweaver.crawler.internal.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.jweaver.crawler.internal.test.Constants.TEST_BASE_URI;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JWeaverCrawlerImplTest {

  @Mock JWeaverCrawlerImpl crawler;

  @BeforeEach
  void setupTest() {
    var builder = new JWeaverBuilderImpl();
    builder.build(Set.of(TEST_BASE_URI));
    crawler = Mockito.mock(JWeaverCrawlerImpl.class);
  }

  @Test
  void testCrawlerInstanceWithEmptyMissingUriBuilder_ThrowNullPointer() {
    var builder = new JWeaverBuilderImpl();
    assertThrows(NullPointerException.class, () -> new JWeaverCrawlerImpl(builder));
  }

  @Test
  void testCrawlerInstanceWithDefaultBuilderProvided_Success() {
    var builder = new JWeaverBuilderImpl();
    builder.build(Set.of(TEST_BASE_URI));
    var crawler = new JWeaverCrawlerImpl(builder);
    assertNotNull(crawler);
  }

  @Test
  void testRunSeq_Success() {
    assertNotNull(crawler.getTaskList());
    crawler.run();
    verify(crawler, times(1)).run();
    verify(crawler, never()).runParallel();
  }

  @Test
  void testCrawlerEmptyUriSet_Failure() {
    var builder = new JWeaverBuilderImpl();
    var uriSet = Set.of("invalid:8080");
    var ex = assertThrows(IllegalArgumentException.class, () -> builder.build(uriSet));
    assertEquals("No valid uris provided", ex.getMessage());
  }
}
