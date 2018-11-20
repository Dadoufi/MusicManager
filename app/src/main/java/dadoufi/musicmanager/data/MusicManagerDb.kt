/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dadoufi.musicmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dadoufi.musicmanager.data.daos.*
import dadoufi.musicmanager.data.entities.AlbumDetailsEntity
import dadoufi.musicmanager.data.entities.AlbumEntity
import dadoufi.musicmanager.data.entities.ArtistEntity
import dadoufi.musicmanager.data.entities.TrackEntity


/**
 * Main database description.
 */
@Database(
    entities = [
        AlbumEntity::class,
        ArtistEntity::class,
        AlbumDetailsEntity::class,
        TrackEntity::class
    ],
    version = 8,
    exportSchema = false
)
abstract class MusicManagerDb : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao
    abstract fun albumDetailsDao(): AlbumDetailsDao
    abstract fun trackDao(): TrackDao
    abstract fun albumInfoWithTracksDao(): AlbumDetailsWithTracksDao

}
