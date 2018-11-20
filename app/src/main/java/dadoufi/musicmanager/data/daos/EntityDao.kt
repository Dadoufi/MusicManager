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

package dadoufi.musicmanager.data.daos

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import io.reactivex.Completable


interface EntityDao<E> {

    @Insert
    fun insert(entity: E): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(entity: E): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReplaceCompletable(entity: E): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReplace(entity: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entities: List<E>)

    @Update
    fun updateCompletable(entity: E): Completable

    @Update
    fun update(entity: E)

    @Delete
    fun deleteCompletable(entity: E): Completable

    @Delete
    fun delete(entity: E)

    @Delete
    fun deleteCompletable(entities: List<E>): Completable


}