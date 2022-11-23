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
