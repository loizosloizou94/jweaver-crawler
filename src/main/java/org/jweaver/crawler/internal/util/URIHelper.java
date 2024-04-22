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

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * The URIHelper class provides utility methods for handling and validating URIs. It includes
 * methods for checking URI validity, determining if a URI is external, transforming URIs for
 * comparison, and checking if a URI's content type is allowed.
 */
public class URIHelper {

  static final List<String> IGNORED_EXTENSIONS =
      Arrays.asList(
          "7z",
          "7zip",
          "bz2",
          "rar",
          "tar",
          "tar.gz",
          "xz",
          "zip",
          "mng",
          "pct",
          "bmp",
          "gif",
          "jpg",
          "jpeg",
          "png",
          "pst",
          "psp",
          "tif",
          "tiff",
          "ai",
          "drw",
          "dxf",
          "eps",
          "ps",
          "svg",
          "cdr",
          "ico",
          "mp3",
          "wma",
          "ogg",
          "wav",
          "ra",
          "aac",
          "mid",
          "au",
          "aiff",
          "3gp",
          "asf",
          "asx",
          "avi",
          "mov",
          "mp4",
          "mpg",
          "qt",
          "rm",
          "swf",
          "wmv",
          "m4a",
          "m4v",
          "flv",
          "webm",
          "xls",
          "xlsx",
          "ppt",
          "pptx",
          "pps",
          "doc",
          "docx",
          "odt",
          "ods",
          "odg",
          "odp",
          "css",
          "pdf",
          "exe",
          "bin",
          "rss",
          "dmg",
          "iso",
          "apk",
          "template",
          "torrent");

  static final List<String> ALLOWED_CONTENT_TYPES =
      Arrays.asList("text/html", "text/plain", "application/json", "application/javascript");

  private URIHelper() {}

  /**
   * Check if the provided URI is valid
   *
   * @param url Url to validate
   * @return true if the provided URI is valid
   */
  public static boolean isValidUri(String url) {
    return UrlValidator.getInstance().isValid(url);
  }

  /**
   * Checks if the child URI is external to the base URI.
   *
   * @param baseUrl The base URL.
   * @param childUrl The child URL.
   * @return True if the child URI is external, false otherwise.
   */
  public static boolean isExternalUri(final String baseUrl, final String childUrl) {
    Objects.requireNonNull(baseUrl);
    Objects.requireNonNull(childUrl);
    var baseUri = URI.create(baseUrl);
    var childUri = URI.create(childUrl);
    return (!equalHostUri(baseUri.getHost(), childUri.getHost()));
  }

  /**
   * Checks if two host URIs are equal after transformation.
   *
   * @param host1 The first host URI.
   * @param host2 The second host URI.
   * @return True if the transformed host URIs are equal, false otherwise.
   */
  static boolean equalHostUri(String host1, String host2) {
    if (host1 == null || host2 == null) return false;
    return transformUri(host1).equals(transformUri(host2));
  }

  /**
   * Transforms a URI for comparison by removing 'www.' prefix if present.
   *
   * @param uri The URI to transform.
   * @return The transformed URI.
   */
  static String transformUri(String uri) {
    if (uri.startsWith(Constants.WWW_STR)) {
      return uri.substring(4);
    }
    return uri;
  }

  /**
   * Checks if a URL's extension is allowed.
   *
   * @param uri The URI to check.
   * @return True if the URL's extension is allowed, false otherwise.
   */
  public static boolean isAllowedUrl(String uri) {
    for (var ext : IGNORED_EXTENSIONS) {
      if (uri.endsWith("." + ext)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if a content type is allowed.
   *
   * @param contentType The content type to check.
   * @return True if the content type is allowed, false otherwise.
   */
  public static boolean isAllowedContentType(String contentType) {
    for (var ext : ALLOWED_CONTENT_TYPES) {
      if (contentType.contains(ext)) {
        return true;
      }
    }
    return false;
  }
}
