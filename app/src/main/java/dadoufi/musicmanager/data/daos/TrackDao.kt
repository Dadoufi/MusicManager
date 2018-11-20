package dadoufi.musicmanager.data.daos

import androidx.room.Dao
import dadoufi.musicmanager.data.entities.TrackEntity


@Dao
abstract class TrackDao : EntityDao<TrackEntity>