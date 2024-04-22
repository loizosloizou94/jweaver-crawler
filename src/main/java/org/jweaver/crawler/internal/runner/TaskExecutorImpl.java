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

import java.util.ArrayList;
import java.util.List;
import org.jweaver.crawler.internal.exception.JWeaverExecutionException;
import org.jweaver.crawler.internal.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A concrete implementation of the {@link TaskExecutor} interface responsible for executing tasks.
 */
public class TaskExecutorImpl implements TaskExecutor {

  private static final Logger log = LoggerFactory.getLogger(TaskExecutorImpl.class);

  /** Private constructor to prevent instantiation from outside the class. */
  private TaskExecutorImpl() {}

  /**
   * Creates a new instance of TaskExecutorImpl.
   *
   * @return A new TaskExecutorImpl instance.
   */
  public static TaskExecutorImpl create() {
    return new TaskExecutorImpl();
  }

  @Override
  public void runParallel(List<JWeaverTask> tasks) {
    log.info("Initializing parallel execution for {} tasks", tasks.size());
    var executions = new ArrayList<Thread>();
    for (JWeaverTask execution : tasks) {
      executions.add(
          Thread.ofVirtual()
              .name(Constants.RUNNER_THREAD_NAME + execution.getId())
              .start(() -> runSingleTask(execution)));
    }
    try {
      for (var t : executions) t.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Execution interrupted", e);
      throw new JWeaverExecutionException("Thread execution interrupted");
    }
  }

  @Override
  public void run(List<JWeaverTask> taskList) {
    log.info("Initializing seq execution for {} tasks", taskList.size());
    for (JWeaverTask task : taskList) {
      runSingleTask(task);
    }
  }

  void runSingleTask(JWeaverTask task) {
    var startTime = System.currentTimeMillis();
    task.start();
    long timeTaken = System.currentTimeMillis() - startTime;
    log.debug("Execution with id {} took {} ms", task.getId(), timeTaken);
  }
}
