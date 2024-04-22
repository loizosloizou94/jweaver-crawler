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

import java.util.Collection;

/**
 * The BuilderValidator class provides utility methods for validating builder parameters. It ensures
 * that parameters are not null or empty before being used to construct objects.
 */
public class BuilderValidator {
  private BuilderValidator() {}

  /**
   * Validates that the specified object is not null or empty.
   *
   * @param object The object to validate.
   * @param message The error message to be used if the validation fails.
   * @param <T> The type of the object.
   * @return The validated object.
   * @throws NullPointerException if the object is null.
   * @throws IllegalArgumentException if the object is an empty string or collection.
   */
  public static <T> T requireNonEmpty(T object, String message) {
    if (object == null) throw new NullPointerException(message);
    if (object instanceof String c && c.isEmpty()) throw new IllegalArgumentException(message);
    return object;
  }

  /**
   * Validates that the specified collection is not null or empty.
   *
   * @param object The collection to validate.
   * @param message The error message to be used if the validation fails.
   * @param <T> The type of elements in the collection.
   * @return The validated collection.
   * @throws NullPointerException if the collection is null.
   * @throws IllegalArgumentException if the collection is empty.
   */
  public static <T> Collection<T> requireNonEmpty(Collection<T> object, String message) {
    if (object == null) throw new NullPointerException(message);
    if (object instanceof Collection<T> c && c.isEmpty())
      throw new IllegalArgumentException(message);

    return object;
  }

  /**
   * Validates that the specified object is not null.
   *
   * @param object The object to validate.
   * @param <T> The type of the object.
   * @return The validated object.
   * @throws NullPointerException if the object is null.
   */
  public static <T> T requireNonEmpty(T object) {
    if (object == null) throw new NullPointerException();
    if (object instanceof String c && c.isEmpty()) throw new IllegalArgumentException();
    return object;
  }

  /**
   * Validates that the specified collection is not null.
   *
   * @param object The collection to validate.
   * @param <T> The type of elements in the collection.
   * @return The validated collection.
   * @throws NullPointerException if the collection is null.
   */
  public static <T> Collection<T> requireNonEmpty(Collection<T> object) {
    if (object == null) throw new NullPointerException();
    if (object instanceof Collection<T> c && c.isEmpty()) throw new IllegalArgumentException();
    return object;
  }
}
