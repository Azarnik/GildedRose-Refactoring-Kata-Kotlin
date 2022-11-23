package com.gildedrose.model

import com.gildedrose.Item

sealed class GlidedRoseItem(private var item: Item) {
    protected open val maxQuality = 50

    var sellIn: Int
        get() = item.sellIn
        protected set(value) {
            item.sellIn = value
        }

    var quality: Int
        get() = item.quality
        protected set(value) {
            item.quality = value.coerceIn(0, maxQuality)
        }

    fun nextDay() {
        updateQuality()
        updateSellIn()
    }

    protected open fun updateQuality() {
        quality -= if (sellIn <= 0) 2 else 1
    }

    protected open fun updateSellIn() {
        sellIn--
    }

    override fun toString() = item.toString()
}

internal class RegularItem(item: Item) : GlidedRoseItem(item)

internal class AgedBrie(item: Item) : GlidedRoseItem(item) {
    override fun updateQuality() {
        quality += if (sellIn <= 0) 2 else 1
    }
}

internal class Sulfuras(item: Item) : GlidedRoseItem(item) {
    override fun updateQuality() {}

    override fun updateSellIn() {}
}

internal class BackstagePasses(item: Item) : GlidedRoseItem(item) {
    override fun updateQuality() {
        val value = when (sellIn) {
            in 6..10 -> quality + 2
            in 1..5 -> quality + 3
            else -> if (sellIn <= 0) 0 else quality + 1
        }
        quality = value
    }
}

internal class Conjured(item: Item) : GlidedRoseItem(item) {
    override fun updateQuality() {
        quality -= if (sellIn <= 0) 4 else 2
    }
}

internal fun Item.toGlidedRoseItem(): GlidedRoseItem {
    return when (name) {
        AGED_BRIE -> AgedBrie(this)
        SULFURAS -> Sulfuras(this)
        BACKSTAGE_PASSES -> BackstagePasses(this)
        CONJURED -> Conjured(this)
        else -> RegularItem(this)
    }
}

private const val AGED_BRIE = "Aged Brie"
private const val SULFURAS = "Sulfuras, Hand of Ragnaros"
private const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"
private const val CONJURED = "Conjured Mana Cake"
