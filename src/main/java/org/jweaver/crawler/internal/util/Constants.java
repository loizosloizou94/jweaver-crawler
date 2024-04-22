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

/** This class contains constants used throughout the crawling process. */
public class Constants {

  /** The header key for specifying the content type. */
  public static final String CONTENT_TYPE_STR = "Content-Type";

  /** The prefix for the writer thread name. */
  public static final String WRITER_THREAD_NAME = "jweaver-writer-";

  /** The prefix for the runner thread name. */
  public static final String RUNNER_THREAD_NAME = "jweaver-runner-";

  /** The string representation for 'www'. */
  public static final String WWW_STR = "www";

  /** The default output path for file export. */
  public static final String DEFAULT_OUTPUT_PATH = "output/";

  /** The prefix for connections. */
  public static final String CONNECTIONS_PREFIX = "connections";

  /** The prefix for errors. */
  public static final String ERRORS_PREFIX = "errors";

  /** The date-time format for file export. */
  public static final String FILE_EXPORT_DT_FORMAT = "yyyyMMddHHmmssSSS";

  /** Private constructor to prevent instantiation of this class. */
  private Constants() {}
}
