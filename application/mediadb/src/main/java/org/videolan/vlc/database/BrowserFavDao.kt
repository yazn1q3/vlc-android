/*******************************************************************************
 *  BrowserFavDao.kt
 * ****************************************************************************
 * Copyright © 2018 VLC authors and VideoLAN
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 ******************************************************************************/

package org.videolan.vlc.database

import android.net.Uri
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.videolan.vlc.mediadb.models.BrowserFav

@Dao
interface BrowserFavDao {

    /** Insert or update a favorite item safely */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(browserFav: BrowserFav)

    /** Get a favorite by its URI, returns empty list if not found */
    @Query("SELECT * FROM fav_table WHERE uri = :uri")
    fun get(uri: Uri): Flow<List<BrowserFav>>

    /** Get all favorites as a Flow for reactive updates */
    @Query("SELECT * FROM fav_table")
    fun getAll(): Flow<List<BrowserFav>>

    /** Get all network favorites (type = 0) */
    @Query("SELECT * FROM fav_table WHERE type = 0")
    fun getAllNetworkFavs(): Flow<List<BrowserFav>>

    /** Get all local favorites (type = 1) */
    @Query("SELECT * FROM fav_table WHERE type = 1")
    fun getAllLocalFavs(): Flow<List<BrowserFav>>

    /** Delete a favorite by URI */
    @Query("DELETE FROM fav_table WHERE uri = :uri")
    suspend fun delete(uri: Uri)

    /** Delete all favorites (optional utility) */
    @Query("DELETE FROM fav_table")
    suspend fun deleteAll()

    /** Check if a favorite exists by URI */
    @Query("SELECT EXISTS(SELECT 1 FROM fav_table WHERE uri = :uri)")
    fun exists(uri: Uri): Flow<Boolean>
}
