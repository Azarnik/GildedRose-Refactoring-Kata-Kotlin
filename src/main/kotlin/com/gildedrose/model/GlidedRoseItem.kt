package com.gildedrose.model

import com.gildedrose.Item

abstract class GlidedRoseItem(var item: Item) {
    fun nextDay() {
        updateQuality()
        updateSellIn()
    }

    protected open fun updateQuality() {
        val decreaseFor = if (item.sellIn <= 0) 2 else 1
        setQuality(item.quality - decreaseFor)
    }

    protected open fun updateSellIn() {
        item.sellIn--
    }

    protected fun setQuality(value: Int) {
        item.quality = value.coerceIn(0, 50)
    }
}

internal class RegularItem(item: Item) : GlidedRoseItem(item)

internal class AgedBrie(item: Item) : GlidedRoseItem(item) {
    override fun updateQuality() {
        setQuality(item.quality + 1)
    }
}

internal class Sulfuras(item: Item) : GlidedRoseItem(item) {
    override fun updateQuality() {}

    override fun updateSellIn() {}
}

internal class BackstagePasses(item: Item) : GlidedRoseItem(item) {
    override fun updateQuality() {
        val value = when (item.sellIn) {
            in 11..Int.MAX_VALUE -> item.quality + 1
            in 6..10 -> item.quality + 2
            in 1..5 -> item.quality + 3
            else -> 0
        }
        setQuality(value)
    }
}

internal fun Item.toGlidedRoseItem(): GlidedRoseItem {
    return when (name) {
        AGED_BRIE -> AgedBrie(this)
        SULFURAS -> Sulfuras(this)
        BACKSTAGE_PASSES -> BackstagePasses(this)
        else -> RegularItem(this)
    }
}

private const val AGED_BRIE = "Aged Brie"
private const val SULFURAS = "Sulfuras, Hand of Ragnaros"
private const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"

