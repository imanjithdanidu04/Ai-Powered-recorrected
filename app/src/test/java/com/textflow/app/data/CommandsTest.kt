package com.textflow.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CommandsTest {

    @Test
    fun `all thirteen canonical commands are present`() {
        val triggers = Commands.all.map { it.trigger }
        assertEquals(13, triggers.size)
        // Canonical corrected list — @formal and @funny must be included.
        val expected = listOf(
            "typi", "fix", "summ", "polite", "casual", "expand",
            "translate", "bullet", "improve", "rephrase", "emoji",
            "formal", "funny",
        )
        assertEquals(expected, triggers)
    }

    @Test
    fun `every command has a trigger, label and non-blank prompt`() {
        Commands.all.forEach { command ->
            assertTrue("trigger blank for ${command.trigger}", command.trigger.isNotBlank())
            assertTrue("label blank for ${command.trigger}", command.label.isNotBlank())
            assertTrue("prompt blank for ${command.trigger}", command.systemPrompt.isNotBlank())
            assertEquals("@${command.trigger}", command.formattedTrigger)
        }
    }

    @Test
    fun `filter matches by trigger prefix, case-insensitive`() {
        assertEquals(listOf("fix"), Commands.filter("fi").map { it.trigger })
        assertEquals(listOf("fix"), Commands.filter("FIX").map { it.trigger })
        assertEquals(listOf("formal", "funny"), Commands.filter("f").map { it.trigger })
    }

    @Test
    fun `filter matches label text`() {
        assertEquals(listOf("translate"), Commands.filter("english").map { it.trigger })
        assertEquals(listOf("emoji"), Commands.filter("emoji").map { it.trigger })
    }

    @Test
    fun `empty query returns all commands`() {
        assertEquals(Commands.all, Commands.filter(""))
        assertEquals(Commands.all, Commands.filter("   "))
    }

    @Test
    fun `all prompts enforce the return-only rule`() {
        Commands.all.forEach { command ->
            assertTrue(
                "prompt for ${command.trigger} should contain 'Return only'",
                command.systemPrompt.contains("Return only"),
            )
        }
    }
}
