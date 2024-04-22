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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jweaver.crawler.internal.test.Constants;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TaskExecutorImplTest {

  @Mock final TaskExecutorImpl taskExecutor = mock(TaskExecutorImpl.class);

  @Test
  void taskExecutorRunSeqWithValidList_Success() {
    var builder = new JWeaverBuilderImpl();
    doCallRealMethod().when(taskExecutor).run(any());
    doNothing().when(taskExecutor).runSingleTask(any());
    builder.build(Set.of(Constants.TEST_BASE_URI, Constants.TEST_SECONDARY_URI));
    taskExecutor.run(new JWeaverCrawlerImpl(builder).getTaskList());
    verify(taskExecutor, times(1)).run(anyList());
    verify(taskExecutor, times(2)).runSingleTask(any());
  }

  @Test
  void taskExecutorRunParallelWithValidList_Success() {
    var builder = new JWeaverBuilderImpl();
    doNothing().when(taskExecutor).runSingleTask(any());
    doCallRealMethod().when(taskExecutor).runParallel(any());
    builder.build(Set.of(Constants.TEST_BASE_URI));
    taskExecutor.runParallel(new JWeaverCrawlerImpl(builder).getTaskList());
    verify(taskExecutor, times(1)).runParallel(anyList());
    verify(taskExecutor, times(1)).runSingleTask(any());
  }
}
