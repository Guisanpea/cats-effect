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

package cats.effect
package laws

import cats.effect.kernel.{Bracket, Outcome, TemporalRegion}

trait TemporalRegionLaws[R[_[_], _], F[_], E] extends TemporalLaws[R[F, *], E] with ConcurrentRegionLaws[R, F, E] {
  implicit val F: TemporalRegion[R, F, E]
}

object TemporalRegionLaws {
  def apply[R[_[_], _], F[_], E](implicit
                                 F0: TemporalRegion[R, F, E],
                                 B0: Bracket.Aux[F, E, Outcome[R[F, *], E, *]]): TemporalRegionLaws[R, F, E] =
    new TemporalRegionLaws[R, F, E] {
      val F = F0
      val B = B0
    }
}