package com.textflow.app.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * SCAFFOLDING ONLY — the real implementation is a later task.
 *
 * Manifest + config XML are already in place (see AndroidManifest.xml and
 * res/xml/accessibility_service_config.xml). When implemented, this service
 * will:
 *  1. Observe TYPE_VIEW_TEXT_SELECTION_CHANGED events to detect a selection.
 *  2. Read the selected text via [rootInActiveWindow] / event source.
 *  3. Surface the floating bubble (WindowManager overlay) so the user can
 *     type an @command.
 *  4. Replace the selection with the LLM result (Undo/Redo aware).
 */
class TextFlowAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // TODO(service): handle TYPE_VIEW_TEXT_SELECTION_CHANGED and
        //  TYPE_WINDOW_CONTENT_CHANGED (see config XML) — detect the selection,
        //  show the bubble, and apply command results.
    }

    override fun onInterrupt() {
        // TODO(service): clean up transient UI (bubble/card) if interrupted.
    }
}
