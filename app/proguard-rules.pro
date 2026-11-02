-keep class org.advancedkeyboard.app.ime.AdvancedInputMethodService { *; }
-keep class org.advancedkeyboard.app.settings.SettingsActivity { *; }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
