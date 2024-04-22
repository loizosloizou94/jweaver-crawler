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

import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.jweaver.crawler.JWeaverCrawler;
import org.jweaver.crawler.internal.parse.DocumentParser;
import org.jweaver.crawler.internal.result.Connection;
import org.jweaver.crawler.internal.result.NodeError;
import org.jweaver.crawler.internal.result.SuccessResultPage;
import org.jweaver.crawler.internal.test.Constants;
import org.jweaver.crawler.internal.write.ExportConfig;
import org.jweaver.crawler.internal.write.JWeaverWriter;

class JWeaverBuilderImplTest {

  static final Set<String> uriSet = Set.of(Constants.TEST_BASE_URI);

  @Test
  void testCreateCrawlerWithProvidedHttpClient_success() {
    var crawler =
        JWeaverCrawler.builder().httpClient(HttpClient.newBuilder().build()).build(uriSet);
    assertNotNull(crawler);
  }

  @Test
  void testCreateCrawlerWithNegativeDepth_failedWithIllegalArgument() {
    var builder = JWeaverCrawler.builder();
    var ex = assertThrows(IllegalArgumentException.class, () -> builder.maxDepth(-1));
    assertEquals("Depth must be greater than zero", ex.getMessage());
  }

  @Test
  void testCreateCrawlerWithEmptyUriSet_failedWithIllegalArgument() {
    var builder = JWeaverCrawler.builder();
    Set<String> emptySet = Set.of();
    var ex = assertThrows(IllegalArgumentException.class, () -> builder.build(emptySet));
    assertEquals("URI set cannot be null or empty", ex.getMessage());
  }

  @Test
  void testCreateCrawlerWithoutUris_failedWithIllegalArgument() {
    var builder = JWeaverCrawler.builder();
    var ex = assertThrows(IllegalArgumentException.class, () -> builder.build(null));
    assertEquals("URI list must be provided", ex.getMessage());
  }

  @Test
  void testCreateCrawlerWithFullArgs_success() throws IOException {
    var crawler =
        JWeaverCrawler.builder()
            .httpClient(HttpClient.newBuilder().build())
            .parser(
                new DocumentParser() {
                  @Override
                  public String parseTitle(String htmlBody, String pageUri) {
                    return "";
                  }

                  @Override
                  public String parseBody(String htmlBody, String pageUri) {
                    return "";
                  }

                  @Override
                  public Set<String> parseLinks(String htmlBody, String pageUri) {
                    return Set.of();
                  }
                })
            .writer(
                new JWeaverWriter() {
                  @Override
                  public void processSuccess(
                      SuccessResultPage successResultPage, ExportConfig exportConfiguration) {}

                  @Override
                  public void processErrors(
                      String baseUri,
                      List<NodeError> nodeErrorList,
                      ExportConfig exportConfiguration) {}

                  @Override
                  public void processConnectionMap(
                      String baseUri,
                      List<Connection> connections,
                      ExportConfig exportConfiguration) {}
                })
            .politenessDelay(Duration.ofSeconds(3))
            .maxDepth(3)
            .exportConfiguration(ExportConfig.exportJson(Constants.TEST_OUTPUT_DIR, true))
            .build(uriSet);

    assertNotNull(crawler);
    var file = new File(Constants.TEST_OUTPUT_DIR);
    file.deleteOnExit();
  }
}
