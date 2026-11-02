package org.advancedkeyboard.app.ime

import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import org.advancedkeyboard.app.R
import org.advancedkeyboard.app.cursor.CursorController
import org.advancedkeyboard.app.cursor.SelectionManager
import org.advancedkeyboard.app.cursor.SpacebarTrackpadGesture
import org.advancedkeyboard.app.editing.ClipboardHelper
import org.advancedkeyboard.app.editing.EditingShortcutDispatcher
import org.advancedkeyboard.app.modifiers.ModifierKey
import org.advancedkeyboard.app.modifiers.ModifierState
import org.advancedkeyboard.app.modifiers.ModifierStateManager
import org.advancedkeyboard.app.settings.KeyboardPreferences
import org.advancedkeyboard.app.settings.SettingsActivity
import org.advancedkeyboard.app.suggestions.SuggestionEngine
import org.advancedkeyboard.app.themes.KeyboardTheme
import org.advancedkeyboard.app.ui.AdvancedToolbarView
import org.advancedkeyboard.app.ui.EditingPadView
import org.advancedkeyboard.app.ui.NormalKeyboardView

class AdvancedInputMethodService : InputMethodService() {
    private lateinit var preferences: KeyboardPreferences
    private lateinit var audioHapticManager: AudioHapticManager
    private lateinit var modifierManager: ModifierStateManager
    private lateinit var keyActionHandler: KeyActionHandler
    private lateinit var selectionManager: SelectionManager
    private lateinit var cursorController: CursorController
    private lateinit var shortcutDispatcher: EditingShortcutDispatcher
    private lateinit var clipboardHelper: ClipboardHelper
    private lateinit var suggestionEngine: SuggestionEngine
    private lateinit var trackpadGesture: SpacebarTrackpadGesture

    private var activeMode: KeyboardMode = KeyboardMode.ADVANCED
    private var currentTheme: KeyboardTheme = KeyboardTheme.get(org.advancedkeyboard.app.themes.ThemeType.DARK_SLATE)
    private var isShifted: Boolean = false
    private var isSymbolsMode: Boolean = false

    private var rootView: View? = null
    private var contentFrame: FrameLayout? = null
    private var modifierScrollBar: HorizontalScrollView? = null
    private var modifierContainer: LinearLayout? = null
    private var suggestionContainer: LinearLayout? = null
    private var btnModeAdvanced: TextView? = null
    private var btnModeEditing: TextView? = null

    private var normalKeyboardView: NormalKeyboardView? = null
    private var advancedToolbarView: AdvancedToolbarView? = null
    private var editingPadView: EditingPadView? = null

    override fun onCreate() {
        super.onCreate()
        preferences = KeyboardPreferences(this)
        audioHapticManager = AudioHapticManager(this)
        selectionManager = SelectionManager()
        cursorController = CursorController(selectionManager)
        shortcutDispatcher = EditingShortcutDispatcher()
        clipboardHelper = ClipboardHelper(this)
        suggestionEngine = SuggestionEngine()

        modifierManager = ModifierStateManager(doubleTapThresholdMs = 350L) { key, state ->
            advancedToolbarView?.updateModifierState(key, state)
        }
        keyActionHandler = KeyActionHandler(modifierManager)
        trackpadGesture = SpacebarTrackpadGesture(cursorController)
        currentTheme = KeyboardTheme.get(preferences.themeType)
    }

    override fun onCreateInputView(): View {
        val view = layoutInflater.inflate(R.layout.input_view, null)
        rootView = view

        contentFrame = view.findViewById(R.id.keyboard_content_frame)
        modifierScrollBar = view.findViewById(R.id.modifier_scroll_bar)
        modifierContainer = view.findViewById(R.id.modifier_container)
        suggestionContainer = view.findViewById(R.id.suggestion_container)
        btnModeAdvanced = view.findViewById(R.id.btn_mode_advanced)
        btnModeEditing = view.findViewById(R.id.btn_mode_editing)

        btnModeAdvanced?.setOnClickListener { setMode(KeyboardMode.ADVANCED) }
        btnModeEditing?.setOnClickListener { setMode(KeyboardMode.EDITING) }

        view.findViewById<TextView>(R.id.btn_settings_quick)?.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }

        initNormalView()
        initAdvancedToolbar()
        initEditingPad()

        setMode(activeMode)
        return view
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        currentTheme = KeyboardTheme.get(preferences.themeType)
        modifierManager.resetAll()
        selectionManager.reset()
        isShifted = false
        isSymbolsMode = false
        applyThemeToViews()
    }

    private fun initNormalView() {
        normalKeyboardView = NormalKeyboardView(
            context = this,
            onCommitText = { text ->
                provideFeedback()
                keyActionHandler.commitText(currentInputConnection, text)
                if (isShifted) {
                    isShifted = false
                    updateNormalViewConfig()
                }
            },
            onDelete = {
                provideFeedback()
                keyActionHandler.handleDelete(currentInputConnection)
            },
            onEnter = {
                provideFeedback()
                keyActionHandler.handleEnter(currentInputConnection, currentInputEditorInfo)
            },
            onToggleShift = {
                provideFeedback()
                isShifted = !isShifted
                updateNormalViewConfig()
            },
            onShowSymbols = { symbols ->
                provideFeedback()
                isSymbolsMode = symbols
                updateNormalViewConfig()
            },
            trackpadGesture = trackpadGesture
        )
    }

    private fun initAdvancedToolbar() {
        advancedToolbarView = AdvancedToolbarView(
            context = this,
            modifierManager = modifierManager,
            onModifierClick = { mod ->
                provideFeedback()
                modifierManager.onModifierTapped(mod)
            },
            onSpecialKeyClick = { keyCode ->
                provideFeedback()
                keyActionHandler.sendSpecialKey(currentInputConnection, keyCode)
            }
        ).apply {
            fontSizeScale = preferences.fontSizeScale
        }
        modifierContainer?.removeAllViews()
        modifierContainer?.addView(advancedToolbarView)
    }

    private fun initEditingPad() {
        editingPadView = EditingPadView(
            context = this,
            cursorController = cursorController,
            selectionManager = selectionManager,
            shortcutDispatcher = shortcutDispatcher,
            getInputConnection = { currentInputConnection }
        ).apply {
            fontSizeScale = preferences.fontSizeScale
        }
    }

    fun setMode(mode: KeyboardMode) {
        activeMode = mode
        contentFrame?.removeAllViews()

        btnModeAdvanced?.setTextColor(if (mode == KeyboardMode.ADVANCED) currentTheme.accentColor else currentTheme.textSecondaryColor)
        btnModeEditing?.setTextColor(if (mode == KeyboardMode.EDITING) currentTheme.accentColor else currentTheme.textSecondaryColor)

        when (mode) {
            KeyboardMode.NORMAL, KeyboardMode.ADVANCED -> {
                modifierScrollBar?.visibility = View.VISIBLE
                updateNormalViewConfig()
                contentFrame?.addView(normalKeyboardView)
            }
            KeyboardMode.EDITING -> {
                modifierScrollBar?.visibility = View.GONE
                editingPadView?.setTheme(currentTheme)
                editingPadView?.fontSizeScale = preferences.fontSizeScale
                contentFrame?.addView(editingPadView)
            }
        }
    }

    private fun updateNormalViewConfig() {
        normalKeyboardView?.updateConfig(
            theme = currentTheme,
            isShifted = isShifted,
            isSymbols = isSymbolsMode,
            showNumberRow = preferences.showNumberRow,
            showBorders = preferences.showKeyBorders,
            fontSizeScale = preferences.fontSizeScale
        )
    }

    private fun provideFeedback() {
        audioHapticManager.performHapticFeedback(
            enabled = preferences.vibrateOnKeypress,
            durationMs = preferences.vibrationStrengthMs
        )
        audioHapticManager.playSoundEffect(preferences.soundOnKeypress)
    }

    private fun applyThemeToViews() {
        rootView?.setBackgroundColor(currentTheme.backgroundColor)
        rootView?.findViewById<View>(R.id.accessory_strip)?.setBackgroundColor(currentTheme.surfaceColor)
        modifierScrollBar?.setBackgroundColor(currentTheme.surfaceColor)
        advancedToolbarView?.setTheme(currentTheme)
        advancedToolbarView?.fontSizeScale = preferences.fontSizeScale
        normalKeyboardView?.updateConfig(
            theme = currentTheme,
            isShifted = isShifted,
            isSymbols = isSymbolsMode,
            showNumberRow = preferences.showNumberRow,
            showBorders = preferences.showKeyBorders,
            fontSizeScale = preferences.fontSizeScale
        )
        editingPadView?.setTheme(currentTheme)
        editingPadView?.fontSizeScale = preferences.fontSizeScale
    }
}
