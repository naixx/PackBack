package me.packbag.android.util

import android.content.Context
import android.view.LayoutInflater

public val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)
