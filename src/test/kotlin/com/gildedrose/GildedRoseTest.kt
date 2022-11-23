package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GildedRoseTest {

    @Test
    fun `data correct after 1 update`() {
        testOutputAfterDays(
            1, "+5 Dexterity Vest, 9, 19\n" +
                    "Aged Brie, 1, 1\n" +
                    "Elixir of the Mongoose, 4, 6\n" +
                    "Sulfuras, Hand of Ragnaros, 0, 80\n" +
                    "Sulfuras, Hand of Ragnaros, -1, 80\n" +
                    "Backstage passes to a TAFKAL80ETC concert, 14, 21\n" +
                    "Backstage passes to a TAFKAL80ETC concert, 9, 50\n" +
                    "Backstage passes to a TAFKAL80ETC concert, 4, 50"
        )
    }

    @Test
    fun `data correct after 2 updates`() {
        testOutputAfterDays(
            2, "+5 Dexterity Vest, 8, 18\n" +
                    "Aged Brie, 0, 2\n" +
                    "Elixir of the Mongoose, 3, 5\n" +
                    "Sulfuras, Hand of Ragnaros, 0, 80\n" +
                    "Sulfuras, Hand of Ragnaros, -1, 80\n" +
                    "Backstage passes to a TAFKAL80ETC concert, 13, 22\n" +
                    "Backstage passes to a TAFKAL80ETC concert, 8, 50\n" +
                    "Backstage passes to a TAFKAL80ETC concert, 3, 50"
        )
    }

    @Test
    fun `data correct after 6 updates`() {
        testOutputAfterDays(
            6, "+5 Dexterity Vest, 4, 14\n" +
                    "Aged Brie, -4, 10\n" +
                    "Elixir of the Mongoose, -1, 0\n" +
                    "Sulfuras, Hand of Ragnaros, 0, 80\n" +
                    "Sulfuras, Hand of Ragnaros, -1, 80\n" +
                    "Backstage passes to a TAFKAL80ETC concert, 9, 27\n" +
                    "Backstage passes to a TAFKAL80ETC concert, 4, 50\n" +
                    "Backstage passes to a TAFKAL80ETC concert, -1, 0"
        )
    }

    private fun testOutputAfterDays(days: Int, expectedResult: String) {
        val app = GildedRose(mockItems())

        repeat(days) {
            app.updateQuality()
        }

        val result = buildString {
            app.items.forEach { appendLine(it) }
        }.trimEnd()
        assertEquals(expectedResult, result)

    }

    @Test
    fun `regular item should decrease sellIn and quality each day`() {
        val item = Item("Regular item", 5, 20)
        val app = GildedRose(arrayOf(item))

        app.updateQuality()

        item.assertEquals(expectedSellIn = 4)
        item.assertEquals(expectedQuality = 19)

        app.updateQuality()

        item.assertEquals(expectedSellIn = 3)
        item.assertEquals(expectedQuality = 18)
    }

    @Test
    fun `quality degrades by 2 when the sell by date has passed`() {
        val item = Item("Regular item", 0, 20)
        val app = GildedRose(arrayOf(item))

        app.updateQuality()

        item.assertEquals(expectedSellIn = -1)
        item.assertEquals(expectedQuality = 18)
    }

    @Test
    fun `quality should not be negative`() {
        val item = Item("Regular item", 5, 0)
        val app = GildedRose(arrayOf(item))

        app.updateQuality()

        assert(item.quality >= 0)
    }

    @Test
    fun `Aged Brie quality increases over time`() {
        val quality = 0
        val item = Item(AGED_BRIE, 5, quality)
        val app = GildedRose(arrayOf(item))

        app.updateQuality()

        assert(item.quality > quality)
    }

    @Test
    fun `quality can not be more then 50`() {
        val item = Item(AGED_BRIE, 5, 50)
        val app = GildedRose(arrayOf(item))

        app.updateQuality()

        assert(item.quality <= 50)
    }

    @Test
    fun `Sulfuras, Hand of Ragnaros never changes quality and sellIn`() {
        val item = Item(SULFURAS, 5, 50)
        val app = GildedRose(arrayOf(item))

        app.updateQuality()

        item.assertEquals(expectedSellIn = 5, expectedQuality = 50)
    }

    @Test
    fun `Backstage passes quality increases by 1 when sellIn is more then 10`() {
        val item = Item(BACKSTAGE_PASSES, 15, 20)
        val app = GildedRose(arrayOf(item))

        app.updateQuality()

        item.assertEquals(expectedQuality = 21)
    }


    @Test
    fun `Backstage passes quality increases by 2 when sellIn is 10 or less`() {
        val item = Item(BACKSTAGE_PASSES, 10, 20)
        val app = GildedRose(arrayOf(item))

        app.updateQuality()

        item.assertEquals(expectedQuality = 22)
    }

    @Test
    fun `Backstage passes quality increases by 3 when sellIn is 5 or less`() {
        val item = Item(BACKSTAGE_PASSES, 5, 20)
        val app = GildedRose(arrayOf(item))

        app.updateQuality()

        item.assertEquals(expectedQuality = 23)
    }

    @Test
    fun `Backstage passes quality is 0 when the sellIn is 0`() {
        val item = Item(BACKSTAGE_PASSES, 0, 20)
        val app = GildedRose(arrayOf(item))

        app.updateQuality()

        item.assertEquals(expectedQuality = 0)
    }

    private fun Item.assertEquals(
        expectedName: String? = null,
        expectedSellIn: Int? = null,
        expectedQuality: Int? = null
    ) {
        expectedName?.let { assertEquals(it, this.name) }
        expectedSellIn?.let { assertEquals(it, sellIn) }
        expectedQuality?.let { assertEquals(it, quality) }
    }

    private fun mockItems(): Array<Item> {
        return arrayOf(
            Item("+5 Dexterity Vest", 10, 20), //
            Item("Aged Brie", 2, 0), //
            Item("Elixir of the Mongoose", 5, 7), //
            Item("Sulfuras, Hand of Ragnaros", 0, 80), //
            Item("Sulfuras, Hand of Ragnaros", -1, 80),
            Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
            Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
            Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
            // this conjured item does not work properly yet
//            Item("Conjured Mana Cake", 3, 6)
        )
    }
}

private const val AGED_BRIE = "Aged Brie"
private const val SULFURAS = "Sulfuras, Hand of Ragnaros"
private const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"