# JWeaver - Java Web Crawler

JWeaver Crawler is an open-source Java library designed for extracting text content from websites efficiently. It
provides essential functionality for tasks such as search engine development, data mining, and content aggregation.

# Prerequisites

**Java Development Kit (JDK)**: requires **JDK 21** or later to compile and run. If you haven't installed JDK 21 or
later yet, you can download it from the [official Oracle website](https://www.oracle.com/java/technologies/downloads/)
or use a package manager like [Homebrew](https://brew.sh/) for macOS or [SDKMAN!](https://sdkman.io/) for Unix-based
systems.

# Getting started

**Add Dependency**: Begin by adding the JWeaver Crawler Library as a dependency in your project. Find the latest version
on [Maven Central](https://central.sonatype.com/artifact/org.jweaver/crawler/versions)

#### Maven

```xml

<dependency>
    <groupId>org.jweaver</groupId>
    <artifactId>crawler</artifactId>
    <version>1.0.2</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'org.jweaver', name: 'crawler', version: '1.0.2'
```

---

# Usage

#### Quickstart

Create a new crawler with the **default** configurations by providing a set of URIs and start crawling. You can see the
generated files in the ***/output*** directory of the current path.

```java
var uris = Set.of("https://en.wikipedia.org/wiki/Computer_science",
        "https://crawler-test.com/");

var crawler = JWeaverCrawler.builder().build(uris);
crawler.runParallel();
```

<br>

#### Configuration

The library provides a wide range of configuration options, allowing the fine-tuning of parameters such as maximum
depth, politeness delay between requests, export configuration and the choice of HTTP client. This enables the
optimization of the crawling process for efficient resource usage, and adherence to web server policies.

```java
//var metadataEnabled = true;
//var exportConfig = ExportConfig.exportJson("/output", metadataEnabled);
var exportConfig = ExportConfig.exportMarkdown("/tmp/jweaver/output");

//customize your http client
var httpClient =
        HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

var crawler =
        JWeaverCrawler.builder()
                .exportConfiguration(exportConfig)
                .httpClient(httpClient)
                .maxDepth(3)
                .politenessDelay(Duration.ofSeconds(2))
                .build(uris);
```

#### Supported Types

| Export Type | Metadata | Extension |
|-------------|----------|-----------|
| Markdown    | False    | .md       |
| JSON        | True     | .json     |

#### Execution

> Please note that in both scenarios, the I/O threads responsible for writing the output to the files will continue to
> operate in the background:

- Parallel with Java Virtual Threads
- Sequentially

#### Extensibility

The library offers customization options for the extraction process and filewriting.

By implementing the DocumentParser interface, you can replace the internal document parser and define a custom one to
extract the required content for the internal processing from HTML pages.

```java
public interface DocumentParser {
    String parseTitle(String htmlBody, String pageUri);

    String parseBody(String htmlBody, String pageUri);

    Set<String> parseLinks(String htmlBody, String pageUri);
}
```

You can implement the JWeaverWriter interface which defines methods for processing and writing the results of the web
crawling process. Implementations of this interface are responsible for handling successful
crawled pages, error information, and connection maps generated during the crawling process.

```java
public interface JWeaverWriter {

    //Processes a successfully crawled page and writes the result
    void processSuccess(SuccessResultPage successResultPage, ExportConfig exportConfiguration);

    //Processes errors encountered during crawling and writes error information
    void processErrors(
            String baseUri, List<NodeError> nodeErrorList, ExportConfig exportConfiguration);

    // Processes connection map information generated during crawling and writes it
    void processConnectionMap(
            String baseUri, List<Connection> connections, ExportConfig exportConfiguration);
}
```

**Provide the implementations during the crawler configuration**

```java
//class CustomParserImpl implements DocumentParser{}
var myParserImpl = new CustomParserImpl();

//class MyFileWriter implements JWeaverWriter{}
var myFileWriter = new MyFileWriter();

var crawler = JWeaverCrawler.builder()
        .parser(myParserImpl)
        .writer(myFileWriter)
        .build(uris);
```

## License

This project is licensed under the [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.en.html) (
GPL-3.0).