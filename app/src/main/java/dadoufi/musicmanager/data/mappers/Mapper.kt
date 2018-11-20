/*
 * Copyright 2018 Google, Inc.
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

package dadoufi.musicmanager.data.mappers

interface Mapper<F, T> {
    fun map(from: F): T
}

interface PagedMapper<F, T> {
    fun map(from: F, page: Int): T
}

interface PagedParamMapper<F, T, P> {
    fun map(from: F, param: P?, page: Int): T
}

interface IndexedPagedParamMapper<F, T, P> {
    fun map(from: F, param: P?, page: Int, index: Long): T
}

interface IndexedParamMapper<F, T, P> {
    fun map(from: F, param: P?, index: Int): T
}

interface IndexedMapper<F, T> {
    fun map(index: Int, from: F): T
}