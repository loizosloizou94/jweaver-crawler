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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import org.jweaver.crawler.internal.parse.DocumentParser;
import org.jweaver.crawler.internal.result.Connection;
import org.jweaver.crawler.internal.result.ErrorResultPage;
import org.jweaver.crawler.internal.result.NodeError;
import org.jweaver.crawler.internal.result.PageLink;
import org.jweaver.crawler.internal.result.ResponseData;
import org.jweaver.crawler.internal.result.ResultPage;
import org.jweaver.crawler.internal.result.SuccessResultPage;
import org.jweaver.crawler.internal.util.Constants;
import org.jweaver.crawler.internal.util.URIHelper;
import org.jweaver.crawler.internal.write.ExportConfig;
import org.jweaver.crawler.internal.write.JWeaverWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Handles the crawling process for a base URI * */
public final class JWeaverTask {

  private static final Logger log = LoggerFactory.getLogger(JWeaverTask.class);
  private final Long id;
  private final Queue<PageLink> pageLinkQueue;
  private final Set<String> visitedUris;
  private final HttpClient httpClient;
  private final DocumentParser parser;
  private final List<Connection> connections;
  private final JWeaverWriter writer;
  private final String baseUri;
  private final Duration politenessDelay;
  private final List<NodeError> nodeErrorList;
  private final Integer maxDepth;
  private final ExportConfig exportConfiguration;

  /**
   * Constructs a new JWeaverTask with the specified parameters.
   *
   * @param baseUri The base URI for the task.
   * @param httpClient The HTTP client used for making requests.
   * @param politenessDelay The politeness delay between requests.
   * @param exportConfiguration The export configuration for data export options.
   * @param maxDepth The maximum depth to crawl.
   * @param documentParser The document parser for extracting information from HTML.
   * @param writer The writer for exporting results.
   */
  JWeaverTask(
      String baseUri,
      HttpClient httpClient,
      Duration politenessDelay,
      ExportConfig exportConfiguration,
      Integer maxDepth,
      DocumentParser documentParser,
      JWeaverWriter writer) {

    this.id = generateExecutionId();
    this.baseUri = baseUri;
    this.connections = new ArrayList<>();
    this.visitedUris = new HashSet<>();
    this.pageLinkQueue = new ConcurrentLinkedQueue<>();
    this.politenessDelay = politenessDelay;
    this.parser = documentParser;
    this.maxDepth = maxDepth;
    this.writer = writer;
    this.httpClient = httpClient;
    this.exportConfiguration = exportConfiguration;
    this.nodeErrorList = new ArrayList<>();
  }

  /**
   * Generates a unique execution ID using a random number generator.
   *
   * @return The generated execution ID.
   */
  long generateExecutionId() {
    long executionId = Random.from(RandomGenerator.getDefault()).nextLong(100);
    if (executionId < 0) {
      executionId = (-1) * executionId;
    }
    return executionId >= 0 ? executionId : (-1 * executionId);
  }

  /**
   * Retrieves the unique ID of this task.
   *
   * @return The unique ID of this task.
   */
  Long getId() {
    return this.id;
  }

  /**
   * Initiates the crawling process by starting from the root URL.
   *
   * <p>This method crawls the root URL specified by {@code baseUri} by invoking the {@link
   * #crawl(PageLink)} method. If the root page is a success result page, it adds the root page to
   * the list of result pages, adds its child links to the page link queue, and establishes
   * connections between the root URL and its child links. It then proceeds to traverse through the
   * child links by invoking {@link #travelLinks()}. If the root page is an error result page, it
   * logs an error message and throws an {@link IllegalStateException}.
   *
   * <p>It then generates the output file for connection maps and errors using the writer.
   */
  void start() {
    log.info("Starting execution for URI {}", baseUri);
    var rootPage = crawl(new PageLink(baseUri, 0));
    visitedUris.add(baseUri);
    if (rootPage instanceof SuccessResultPage successResultPage) {
      pageLinkQueue.addAll(successResultPage.linkSet());
      successResultPage
          .linkSet()
          .forEach(p -> connections.add(new Connection(this.baseUri, p.url(), 0)));
      travelLinks();
    } else if (rootPage instanceof ErrorResultPage errorResultPage) {
      log.error("Base URL [{}] responds with {}", errorResultPage.uri(), errorResultPage.content());
      throw new IllegalStateException("Unable to fetch ResultPage for root URL");
    }
    writer.processConnectionMap(baseUri, connections, this.exportConfiguration);
    writer.processErrors(baseUri, nodeErrorList, exportConfiguration);
  }

  /**
   * Travels through the links in the page link queue, crawling each link and processing the
   * resulting pages.
   *
   * <p>This method iterates through the page link queue, polling each link and crawling it if it
   * has not been visited before and its depth does not exceed the maximum depth limit. After
   * crawling each link, it processes the resulting page by invoking {@link
   * #processSuccessPage(SuccessResultPage)} if the page is a success result page, or {@link
   * #processFailurePage(ErrorResultPage)} if the page is an error result page.
   */
  void travelLinks() {
    while (!pageLinkQueue.isEmpty()) {
      PageLink nextLink = pageLinkQueue.poll();
      if (visitedUris.contains(nextLink.url()) || (nextLink.depth() > this.maxDepth)) {
        continue;
      }
      var page = crawl(nextLink);
      visitedUris.add(nextLink.url());
      // change to switch pattern matching when sonarqube update the profile
      // to support missing default case for sealed interfaces
      if (page instanceof SuccessResultPage successResultPage) {
        processSuccessPage(successResultPage);
      } else if (page instanceof ErrorResultPage errorResultPage) {
        processFailurePage(errorResultPage);
      }
    }
  }

  /**
   * Processes a successfully crawled page.
   *
   * @param page The successfully crawled page.
   */
  void processSuccessPage(SuccessResultPage page) {
    pageLinkQueue.addAll(page.linkSet());
    page.linkSet().forEach(p -> connections.add(new Connection(page.uri(), p.url(), page.depth())));
    writeOutput(page);
  }

  /**
   * Processes a failed crawling attempt.
   *
   * @param errorResultPage The error result page containing information about the failure.
   */
  void processFailurePage(ErrorResultPage errorResultPage) {
    NodeError nodeError =
        new NodeError(errorResultPage.uri(), errorResultPage.depth(), errorResultPage.content());
    nodeErrorList.add(nodeError);
  }

  /**
   * Writes the output for a successfully crawled page.
   *
   * @param successResultPage The successfully crawled page.
   */
  void writeOutput(SuccessResultPage successResultPage) {
    Thread.ofVirtual()
        .name(Constants.WRITER_THREAD_NAME + getId())
        .start(() -> writer.processSuccess(successResultPage, this.exportConfiguration));
  }

  /**
   * Determines whether to skip crawling a URL based on certain conditions.
   *
   * @param baseUri The base URI of the task.
   * @param childUri The URI to be checked.
   * @return {@code true} if the URL should be skipped, otherwise {@code false}.
   */
  boolean skipUrl(String baseUri, String childUri) {
    return (!URIHelper.isAllowedUrl(childUri)
        || !URIHelper.isValidUri(childUri)
        || URIHelper.isExternalUri(baseUri, childUri));
  }

  /**
   * Adds child links to the set of links to be crawled.
   *
   * @param linkUriSet The set of child link URIs.
   * @param pageLink The parent page link.
   * @return The set of child page links.
   */
  Set<PageLink> addChildLinks(Set<String> linkUriSet, PageLink pageLink) {
    Set<String> filteredUris = new HashSet<>();
    for (var childUri : linkUriSet) {
      if (skipUrl(this.baseUri, childUri)) continue;
      filteredUris.add(childUri);
    }
    return filteredUris.stream()
        .map(p -> new PageLink(p, pageLink.depth() + 1))
        .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Crawls a page specified by the provided link.
   *
   * @param link The link representing the page to be crawled.
   * @return The result page obtained from crawling the specified page.
   */
  ResultPage crawl(PageLink link) {
    try {
      ResponseData<String> responseData = get(link);
      if (responseData.isSuccess()) {
        return createFromHtmlBody(responseData.body(), link);
      } else {
        return ErrorResultPage.create(link, responseData.body());
      }
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      return ErrorResultPage.create(link, ex.getLocalizedMessage());
    } catch (Exception ex) {
      maybeThrow(link, ex);
      return ErrorResultPage.create(link, ex.getLocalizedMessage());
    }
  }

  /**
   * Handles exceptions that may occur during crawling. An exception will be thrown if the current
   * depth is 0, meaning we failed to retrieve the root node
   *
   * @param pageLink The page link associated with the exception.
   * @param e The exception that occurred.
   */
  void maybeThrow(PageLink pageLink, Exception e) {
    if (pageLink.depth() == 0) {
      var msg = String.format("Root URL request failed for %s", pageLink.url());
      log.error(msg, e);
      throw new IllegalStateException(msg);
    }
  }

  /**
   * Sends an HTTP GET request to the specified link and retrieves the response.
   *
   * @param link The link to send the request to.
   * @return The response data containing the status code and body of the response.
   * @throws IOException If an I/O error occurs.
   * @throws InterruptedException If the operation is interrupted.
   */
  ResponseData<String> get(PageLink link) throws IOException, InterruptedException {
    Thread.sleep(this.politenessDelay);
    log.trace("Crawling {} with depth {}", link.url(), link.depth());
    var httpRequest =
        HttpRequest.newBuilder(URI.create(link.url()))
            .GET()
            .timeout(httpClient.connectTimeout().orElse(Duration.ofSeconds(1)))
            .build();
    var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    var headers = response.headers().map();
    if (allowedContentType(headers)) {
      return new ResponseData<>(response.statusCode(), response.body());
    } else {
      throw new IllegalArgumentException("Content-Type not allowed");
    }
  }

  /**
   * Determines whether the content type of the response is allowed.
   *
   * @param headerMap The map containing the response headers.
   * @return {@code true} if the content type is allowed, otherwise {@code false}.
   */
  boolean allowedContentType(Map<String, List<String>> headerMap) {
    String contentKey = null;
    for (var key : headerMap.keySet()) {
      if (key.equalsIgnoreCase(Constants.CONTENT_TYPE_STR)) {
        contentKey = key;
        break;
      }
    }
    if (contentKey == null) {
      return true;
    }
    var allowedTypes =
        headerMap.get(contentKey).stream().filter(URIHelper::isAllowedContentType).count();
    return allowedTypes > 0;
  }

  /**
   * Creates a success result page from the HTML body of a crawled page.
   *
   * @param htmlBody The HTML body of the crawled page.
   * @param pageLink The link representing the crawled page.
   * @return The success result page created from the HTML body.
   */
  SuccessResultPage createFromHtmlBody(String htmlBody, PageLink pageLink) {
    var content = parser.parseBody(htmlBody, pageLink.url());
    Set<String> linkUriSet = parser.parseLinks(htmlBody, pageLink.url());
    var childLinks = addChildLinks(linkUriSet, pageLink);
    var title = parser.parseTitle(htmlBody, pageLink.url());
    return SuccessResultPage.create(pageLink, title, content, childLinks);
  }
}
