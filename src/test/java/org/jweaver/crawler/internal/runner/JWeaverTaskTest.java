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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.Test;
import org.jweaver.crawler.internal.parse.JWeaverDocumentParser;
import org.jweaver.crawler.internal.result.ErrorResultPage;
import org.jweaver.crawler.internal.result.PageLink;
import org.jweaver.crawler.internal.result.ResponseData;
import org.jweaver.crawler.internal.result.SuccessResultPage;
import org.jweaver.crawler.internal.test.Constants;
import org.jweaver.crawler.internal.write.JWeaverFileWriter;
import org.mockito.Mock;
import org.mockito.Mockito;

class JWeaverTaskTest {

  final String htmlBody =
      """
<!DOCTYPE html>
<html>
<body>

<title>THIS IS THE TITLE</title>
<h1>My First Heading</h1>
<p>My first paragraph.</p>

<a href="https://www.w3schools.com/">Visit W3Schools.com!</a>
<a href="https://www.w3schools.com/">Visit W3Schools.com!</a>


</body>
</html>
""";
  @Mock private JWeaverTask jWeaverTask = mock(JWeaverTask.class);

  @Test
  void testGetIdValidTask_Success() {
    var builder = new JWeaverBuilderImpl();
    builder.build(Set.of(Constants.TEST_BASE_URI, Constants.TEST_SECONDARY_URI));
    var impl = new JWeaverCrawlerImpl(builder);
    jWeaverTask = impl.getTaskList().getFirst();
    assertNotNull(jWeaverTask.getId());
  }

  // reflection is necessary to initialize mocked class state
  @Test
  void testStartExecutionWithRootSuccessResult_Success()
      throws NoSuchFieldException, IllegalAccessException {
    jWeaverTask = mock(JWeaverTask.class);

    var pageLinkQueue = JWeaverTask.class.getDeclaredField("pageLinkQueue");
    pageLinkQueue.setAccessible(true);
    pageLinkQueue.set(jWeaverTask, new ConcurrentLinkedQueue<>());

    var visitedUris = JWeaverTask.class.getDeclaredField("visitedUris");
    visitedUris.setAccessible(true);
    visitedUris.set(jWeaverTask, new HashSet<>());

    var baseUri = JWeaverTask.class.getDeclaredField("baseUri");
    baseUri.setAccessible(true);
    baseUri.set(jWeaverTask, Constants.TEST_BASE_URI);

    var connections = JWeaverTask.class.getDeclaredField("connections");
    connections.setAccessible(true);
    connections.set(jWeaverTask, new ArrayList<>());

    var maxDepth = JWeaverTask.class.getDeclaredField("maxDepth");
    maxDepth.setAccessible(true);
    maxDepth.set(jWeaverTask, 1);

    var writer = JWeaverTask.class.getDeclaredField("writer");
    writer.setAccessible(true);
    writer.set(jWeaverTask, Mockito.mock(JWeaverFileWriter.class));

    var pageLink = new PageLink(Constants.TEST_BASE_URI, 0);
    doCallRealMethod().when(jWeaverTask).start();
    doCallRealMethod().when(jWeaverTask).travelLinks();
    doCallRealMethod().when(jWeaverTask).processSuccessPage(any());
    var childLinks = new HashSet<PageLink>();
    for (int i = 0; i < 5; i++) {
      PageLink temp = new PageLink(Constants.TEST_BASE_URI_NEWS + i, 1);
      childLinks.add(temp);
      when(jWeaverTask.crawl(temp)).thenReturn(SuccessResultPage.create(temp, "", "", Set.of()));
    }
    when(jWeaverTask.crawl(pageLink))
        .thenReturn(SuccessResultPage.create(pageLink, "", "", childLinks));
    jWeaverTask.start();
    verify(jWeaverTask, times(1)).start();
  }

  @SuppressWarnings("unchecked")
  @Test
  void testDefaultHttpClientMockedResponse_Success()
      throws NoSuchFieldException, IllegalAccessException, IOException, InterruptedException {
    jWeaverTask = mock(JWeaverTask.class);
    var httpClient = JWeaverTask.class.getDeclaredField("httpClient");
    httpClient.setAccessible(true);
    var client = mock(HttpClient.class);
    httpClient.set(jWeaverTask, client);

    var politenessDelay = JWeaverTask.class.getDeclaredField("politenessDelay");
    politenessDelay.setAccessible(true);
    politenessDelay.set(jWeaverTask, Duration.ofSeconds(1));

    var temp = new PageLink(Constants.TEST_BASE_URI_NEWS, 1);
    doCallRealMethod().when(jWeaverTask).get(temp);
    var mockedResp = mock(HttpResponse.class);
    when(mockedResp.headers()).thenReturn(HttpHeaders.of(new HashMap<>(), (x, y) -> true));
    when(mockedResp.statusCode()).thenReturn(200);
    when(client.send(any(), any())).thenReturn(mockedResp);
    when(jWeaverTask.allowedContentType(anyMap())).thenReturn(true);
    ResponseData<String> responseData = jWeaverTask.get(temp);

    assertNotNull(responseData);
    assertEquals(200, responseData.statusCode());
    assertTrue(responseData.isSuccess());
  }

  @Test
  void testCrawlParseHtml_Success() throws IOException, InterruptedException {
    when(jWeaverTask.get(any())).thenReturn(new ResponseData<>(200, htmlBody));
    doCallRealMethod().when(jWeaverTask).crawl(any());
    var pageLink = new PageLink(Constants.TEST_BASE_URI, 1);
    when(jWeaverTask.createFromHtmlBody(htmlBody, pageLink))
        .thenReturn(
            SuccessResultPage.create(
                pageLink, parseTitle(pageLink.url()), parseBody(pageLink.url()), Set.of()));
    doCallRealMethod().when(jWeaverTask).addChildLinks(any(), any());
    var resultPage = jWeaverTask.crawl(pageLink);
    assertNotNull(resultPage);
    assertTrue(resultPage.content().contains("My first paragraph"));
    when(jWeaverTask.skipUrl(any(), any())).thenReturn(false);
    var pageLinks = jWeaverTask.addChildLinks(parseLinks(pageLink.url()), pageLink);
    assertNotNull(pageLinks);
    assertEquals(1, pageLinks.size());
    var successResultPage = (SuccessResultPage) resultPage;
    assertEquals("THIS IS THE TITLE", successResultPage.title());
  }

  @Test
  void testCrawlProcessErrorResultPage_Failure() throws IOException, InterruptedException {
    when(jWeaverTask.get(any())).thenReturn(new ResponseData<>(400, htmlBody));
    doCallRealMethod().when(jWeaverTask).crawl(any());
    PageLink pageLink = new PageLink(Constants.TEST_BASE_URI, 1);
    when(jWeaverTask.createFromHtmlBody(htmlBody, pageLink))
        .thenReturn(
            SuccessResultPage.create(
                pageLink, parseTitle(pageLink.url()), parseBody(pageLink.url()), Set.of()));
    var resultPage = jWeaverTask.crawl(pageLink);
    var errorResultPage = (ErrorResultPage) resultPage;
    assertNotNull(resultPage);
    assertEquals(htmlBody, errorResultPage.content());
  }

  @Test
  void testCrawlProcessWhenHttpException_Failure() throws IOException, InterruptedException {
    when(jWeaverTask.get(any())).thenThrow(new HttpTimeoutException("connection timeout"));
    doCallRealMethod().when(jWeaverTask).crawl(any());
    var pageLink = new PageLink(Constants.TEST_BASE_URI, 1);
    when(jWeaverTask.createFromHtmlBody(htmlBody, pageLink))
        .thenReturn(
            SuccessResultPage.create(
                pageLink, parseTitle(pageLink.url()), parseBody(pageLink.url()), Set.of()));
    var resultPage = jWeaverTask.crawl(pageLink);
    var errorResultPage = (ErrorResultPage) resultPage;
    assertNotNull(resultPage);
    assertEquals("connection timeout", errorResultPage.content());
  }

  @Test
  void testCrawlProcessWhenHttpExceptionButZeroDepth_Failure()
      throws IOException, InterruptedException {
    when(jWeaverTask.get(any())).thenThrow(new HttpTimeoutException("connection timeout"));
    doCallRealMethod().when(jWeaverTask).crawl(any());
    doCallRealMethod().when(jWeaverTask).maybeThrow(any(), any());
    var pageLink = new PageLink(Constants.TEST_BASE_URI, 0);
    when(jWeaverTask.createFromHtmlBody(htmlBody, pageLink))
        .thenReturn(
            SuccessResultPage.create(
                pageLink, parseTitle(pageLink.url()), parseBody(pageLink.url()), Set.of()));
    var ex = assertThrows(IllegalStateException.class, () -> jWeaverTask.crawl(pageLink));
    assertNotNull(ex);
    assertEquals("Root URL request failed for https://192.168.1.10:8080", ex.getMessage());
  }

  @Test
  void testWhenZeroDepth_ThrowException() {
    var pageLink = new PageLink(Constants.TEST_BASE_URI, 0);
    doCallRealMethod().when(jWeaverTask).maybeThrow(any(), any());
    var timeoutException = new HttpTimeoutException("Timeout Exception");
    var ex =
        assertThrows(
            IllegalStateException.class, () -> jWeaverTask.maybeThrow(pageLink, timeoutException));
    assertNotNull(ex);
    assertEquals("Root URL request failed for https://192.168.1.10:8080", ex.getMessage());
  }

  @Test
  void testWhenNoContentTypeHeader_ReturnFalse() {
    doCallRealMethod().when(jWeaverTask).allowedContentType(anyMap());
    Map<String, List<String>> headerMap = new HashMap<>();
    headerMap.put("Content-Type", List.of("text/xml"));
    assertFalse(jWeaverTask.allowedContentType(headerMap));
  }

  @Test
  void testWhenHtmlContentTypeHeader_ReturnTrue() {
    doCallRealMethod().when(jWeaverTask).allowedContentType(anyMap());
    Map<String, List<String>> headerMap = new HashMap<>();
    headerMap.put("Content-Type", List.of("text/html"));
    assertTrue(jWeaverTask.allowedContentType(headerMap));
  }

  @Test
  void testKeepUnknownContentType_ReturnTrue() {
    doCallRealMethod().when(jWeaverTask).allowedContentType(anyMap());
    Map<String, List<String>> headerMap = new HashMap<>();
    headerMap.put("random-header", List.of("random-val"));
    assertTrue(jWeaverTask.allowedContentType(headerMap));
  }

  String parseBody(String uri) {
    var parser = new JWeaverDocumentParser();
    return parser.parseBody(htmlBody, uri);
  }

  String parseTitle(String uri) {
    var parser = new JWeaverDocumentParser();
    return parser.parseTitle(htmlBody, uri);
  }

  Set<String> parseLinks(String uri) {
    var parser = new JWeaverDocumentParser();
    return parser.parseLinks(htmlBody, uri);
  }
}
