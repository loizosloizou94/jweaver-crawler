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

package org.jweaver.crawler.internal.result;

/**
 * The ResponseData record represents the response data received from a web request. It includes the
 * HTTP status code and the body of the response.
 *
 * @param <T> The type of the response body.
 * @param body HTTP response body
 * @param statusCode HTTP statusCode
 */
public record ResponseData<T>(int statusCode, T body) {

  /**
   * Checks if the response indicates a successful request.
   *
   * @return True if the status code is in the range of 200 to 299 (inclusive), indicating success;
   *     false otherwise.
   */
  public boolean isSuccess() {
    return statusCode >= 200 && statusCode < 300;
  }
}
