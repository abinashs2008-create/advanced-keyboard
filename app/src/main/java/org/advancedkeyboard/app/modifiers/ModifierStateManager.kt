package org.advancedkeyboard.app.modifiers

import android.os.SystemClock

class ModifierStateManager(
    private val doubleTapThresholdMs: Long = 350L,
    private val listener: ((ModifierKey, ModifierState) -> Unit)? = null
) {
    private val states = mutableMapOf<ModifierKey, ModifierState>()
    private val lastTapTimes = mutableMapOf<ModifierKey, Long>()

    init {
        resetAll()
    }

    fun resetAll() {
        for (key in ModifierKey.values()) {
            states[key] = ModifierState.IDLE
            lastTapTimes[key] = 0L
        }
    }

    fun getState(key: ModifierKey): ModifierState {
        return states[key] ?: ModifierState.IDLE
    }

    fun onModifierTapped(key: ModifierKey): ModifierState {
        val now = SystemClock.uptimeMillis()
        val currentState = getState(key)
        val lastTap = lastTapTimes[key] ?: 0L
        val isDoubleTap = (now - lastTap) <= doubleTapThresholdMs

        val newState = when (currentState) {
            ModifierState.IDLE -> ModifierState.ONE_SHOT
            ModifierState.ONE_SHOT -> {
                if (isDoubleTap && key.canLock) {
                    ModifierState.LOCKED
                } else {
                    ModifierState.IDLE
                }
            }
            ModifierState.LOCKED -> ModifierState.IDLE
        }

        states[key] = newState
        lastTapTimes[key] = now
        listener?.invoke(key, newState)
        return newState
    }

    fun onKeyDispatched() {
        for (key in ModifierKey.values()) {
            if (states[key] == ModifierState.ONE_SHOT) {
                states[key] = ModifierState.IDLE
                listener?.invoke(key, ModifierState.IDLE)
            }
        }
    }

    fun computeMetaState(): Int {
        var mask = 0
        for ((key, state) in states) {
            if (state != ModifierState.IDLE) {
                mask = mask or key.metaFlag
            }
        }
        return mask
    }

    fun isCtrlActive(): Boolean = getState(ModifierKey.CTRL) != ModifierState.IDLE
    fun isAltActive(): Boolean = getState(ModifierKey.ALT) != ModifierState.IDLE
    fun isShiftActive(): Boolean = getState(ModifierKey.SHIFT) != ModifierState.IDLE
    fun isFnActive(): Boolean = getState(ModifierKey.FN) != ModifierState.IDLE
    fun isCapsActive(): Boolean = getState(ModifierKey.CAPS) != ModifierState.IDLE
}
