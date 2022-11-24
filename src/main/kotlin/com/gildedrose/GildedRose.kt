package com.gildedrose

import com.gildedrose.model.GlidedRoseItem
import com.gildedrose.model.toGlidedRoseItem

class GildedRose(var items: Array<Item>) {
    private val glidedRoseItems: List<GlidedRoseItem> = items.map { it.toGlidedRoseItem() }

    fun updateQuality() {
        glidedRoseItems.forEach { it.nextDay() }
    }
}
