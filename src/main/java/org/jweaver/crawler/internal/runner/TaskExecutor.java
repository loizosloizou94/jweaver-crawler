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

import java.util.List;

/**
 * The TaskExecutor interface defines methods for executing tasks either in parallel or
 * sequentially. Implementations of this interface are responsible for executing a list of
 * JWeaverTask objects.
 */
public interface TaskExecutor {

  /**
   * Executes the specified list of tasks in parallel.
   *
   * @param taskList The list of tasks to execute.
   */
  void runParallel(List<JWeaverTask> taskList);

  /**
   * Executes the specified list of tasks sequentially.
   *
   * @param taskList The list of tasks to execute.
   */
  void run(List<JWeaverTask> taskList);
}
