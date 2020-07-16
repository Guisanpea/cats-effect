/*
 * Copyright 2020 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cats.effect.unsafe

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration
import scala.scalajs.concurrent.JSExecutionContext
import scala.scalajs.js.timers

abstract private[unsafe] class IORuntimeCompanionPlatform { self: IORuntime.type =>
  def defaultComputeExecutionContext: ExecutionContext = JSExecutionContext.queue
  def defaultScheduler: Scheduler = new Scheduler {
    def sleep(delay: FiniteDuration, task: Runnable): Runnable = {
      val handle = timers.setTimeout(delay)(task.run())
      () => timers.clearTimeout(handle)
    }

    def nowMillis() = System.currentTimeMillis()
    def monotonicNanos() = System.nanoTime()
  }

  lazy val global: IORuntime = IORuntime(defaultComputeExecutionContext, defaultScheduler, () => ())
}