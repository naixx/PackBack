package me.packbag.android.db.api

import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import me.packbag.android.db.model.Item

public fun intArrayToString(ids: IntArray?): String = ids?.joinToString().orEmpty()
public fun stringToIntArray(data: String?): Array<Int> = data?.splitBy(", ")?.map { it.toInt() }.orEmpty().toTypedArray()
