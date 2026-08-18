package com.textflow.app.data

/**
 * A single `@command`: its trigger, short label, and the **exact** system
 * prompt sent to the server-side LLM proxy together with the user's selected
 * text.
 *
 * The 13 commands below are canonical per the build spec §2 (the older
 * 11-command mockup list is wrong — `@formal` and `@funny` are included).
 * Prompts are verbatim; do not edit them here. The spec's shared prompt rules
 * (keep input language, return only the transformed text, ~2x output cap) are
 * enforced by the LLM proxy on the server — no API keys live in this client.
 */
data class TextFlowCommand(
    /** Trigger without the '@' prefix, e.g. "fix". */
    val trigger: String,
    /** Short 2–4 word description shown in the UI. */
    val label: String,
    /** Verbatim system prompt (spec §2). */
    val systemPrompt: String,
) {
    /** "@fix"-style trigger as displayed in the UI. */
    val formattedTrigger: String get() = "@$trigger"
}

object Commands {

    /** All 13 canonical commands, in spec table order. */
    val all: List<TextFlowCommand> = listOf(
        TextFlowCommand(
            trigger = "typi",
            label = "Answer questions",
            systemPrompt = "Respond in the same language as the input. Provide only the most relevant and complete answer. Do not add explanations, introductions, or extra text. Return only the answer.",
        ),
        TextFlowCommand(
            trigger = "fix",
            label = "Fix grammar & spelling",
            systemPrompt = "Correct grammar, spelling, and punctuation in the input text. Keep the same language and meaning. Return only the corrected text.",
        ),
        TextFlowCommand(
            trigger = "summ",
            label = "Summarize text",
            systemPrompt = "Summarize the text in one or two sentences, in the same language. Return only the summary.",
        ),
        TextFlowCommand(
            trigger = "polite",
            label = "Make it polite",
            systemPrompt = "Rewrite the text in a polite, professional tone, in the same language. Return only the rewritten text.",
        ),
        TextFlowCommand(
            trigger = "casual",
            label = "Make it casual",
            systemPrompt = "Rewrite the text in a casual, friendly tone, in the same language. Return only the rewritten text.",
        ),
        TextFlowCommand(
            trigger = "expand",
            label = "Add more detail",
            systemPrompt = "Expand the text with more relevant detail, keeping the same language and meaning. Return only the expanded text.",
        ),
        TextFlowCommand(
            trigger = "translate",
            label = "Translate to English",
            systemPrompt = "Translate the text into English. Return only the translated text.",
        ),
        TextFlowCommand(
            trigger = "bullet",
            label = "Convert to bullets",
            systemPrompt = "Convert the text into clear, concise bullet points, in the same language. Return only the bullet list.",
        ),
        TextFlowCommand(
            trigger = "improve",
            label = "Improve writing",
            systemPrompt = "Improve the clarity and quality of the writing while keeping the meaning and language the same. Return only the improved text.",
        ),
        TextFlowCommand(
            trigger = "rephrase",
            label = "Say differently",
            systemPrompt = "Rephrase the text completely while keeping the same meaning and language. Return only the rephrased text.",
        ),
        TextFlowCommand(
            trigger = "emoji",
            label = "Add emojis",
            systemPrompt = "Add relevant emojis to make the text more engaging, in the same language. Return only the enhanced text.",
        ),
        TextFlowCommand(
            trigger = "formal",
            label = "Formalize tone",
            systemPrompt = "Rewrite the text in a formal, professional tone, in the same language. Return only the rewritten text.",
        ),
        TextFlowCommand(
            trigger = "funny",
            label = "Add humor",
            systemPrompt = "Rewrite the text with light humor, keeping the same language and core meaning. Return only the rewritten text.",
        ),
    )

    /**
     * Live filter used by the command autocomplete card. Matches the trigger
     * by prefix/contains or the label by substring, case-insensitively. An
     * empty query returns every command.
     */
    fun filter(query: String): List<TextFlowCommand> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return all
        return all.filter { command ->
            command.trigger.startsWith(q) ||
                command.trigger.contains(q) ||
                command.label.lowercase().contains(q)
        }
    }
}
