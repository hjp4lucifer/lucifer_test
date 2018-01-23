.class public Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;
.super Landroid/preference/PreferenceActivity;

# interfaces
.implements Landroid/preference/Preference$OnPreferenceChangeListener;
.implements Landroid/preference/Preference$OnPreferenceClickListener;


# instance fields
.field private a:Landroid/preference/CheckBoxPreference;

.field private b:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

.field private c:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

.field private d:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

.field private e:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

.field private f:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

.field private g:Landroid/preference/Preference;

.field private h:Landroid/preference/Preference;

.field private i:Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

.field private j:Ljava/lang/String;


# direct methods
.method public constructor <init>()V
    .locals 1

    invoke-direct {p0}, Landroid/preference/PreferenceActivity;-><init>()V

    const-string v0, "Default"

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->j:Ljava/lang/String;

    return-void
.end method

.method private a(Landroid/content/SharedPreferences;Ljava/lang/String;I)I
    .locals 1

    invoke-static {p3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v0

    invoke-interface {p1, p2, v0}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/Integer;->parseInt(Ljava/lang/String;)I

    move-result v0

    return v0
.end method

.method private a()V
    .locals 2

    invoke-virtual {p0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->getPreferenceScreen()Landroid/preference/PreferenceScreen;

    move-result-object v1

    const-string v0, "txt_set_theme_bkg_color"

    invoke-virtual {v1, v0}, Landroid/preference/PreferenceScreen;->findPreference(Ljava/lang/CharSequence;)Landroid/preference/Preference;

    move-result-object v0

    check-cast v0, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->b:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    const-string v0, "txt_set_theme_bkg_color_fill_mode"

    invoke-virtual {v1, v0}, Landroid/preference/PreferenceScreen;->findPreference(Ljava/lang/CharSequence;)Landroid/preference/Preference;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->h:Landroid/preference/Preference;

    const-string v0, "txt_set_theme_font_color"

    invoke-virtual {v1, v0}, Landroid/preference/PreferenceScreen;->findPreference(Ljava/lang/CharSequence;)Landroid/preference/Preference;

    move-result-object v0

    check-cast v0, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->c:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    const-string v0, "txt_set_theme_sel_bkc"

    invoke-virtual {v1, v0}, Landroid/preference/PreferenceScreen;->findPreference(Ljava/lang/CharSequence;)Landroid/preference/Preference;

    move-result-object v0

    check-cast v0, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->d:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    const-string v0, "txt_set_theme_sel_tc"

    invoke-virtual {v1, v0}, Landroid/preference/PreferenceScreen;->findPreference(Ljava/lang/CharSequence;)Landroid/preference/Preference;

    move-result-object v0

    check-cast v0, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->e:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    const-string v0, "txt_set_theme_anno_bkc"

    invoke-virtual {v1, v0}, Landroid/preference/PreferenceScreen;->findPreference(Ljava/lang/CharSequence;)Landroid/preference/Preference;

    move-result-object v0

    check-cast v0, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->f:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    const-string v0, "txt_set_theme_bkg_use_img"

    invoke-virtual {v1, v0}, Landroid/preference/PreferenceScreen;->findPreference(Ljava/lang/CharSequence;)Landroid/preference/Preference;

    move-result-object v0

    check-cast v0, Landroid/preference/CheckBoxPreference;

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->a:Landroid/preference/CheckBoxPreference;

    const-string v0, "txt_set_theme_font_name"

    invoke-virtual {v1, v0}, Landroid/preference/PreferenceScreen;->findPreference(Ljava/lang/CharSequence;)Landroid/preference/Preference;

    move-result-object v0

    check-cast v0, Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->i:Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

    const-string v0, "txt_set_theme_bkg_img"

    invoke-virtual {v1, v0}, Landroid/preference/PreferenceScreen;->findPreference(Ljava/lang/CharSequence;)Landroid/preference/Preference;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->g:Landroid/preference/Preference;

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->g:Landroid/preference/Preference;

    invoke-virtual {v0, p0}, Landroid/preference/Preference;->setOnPreferenceClickListener(Landroid/preference/Preference$OnPreferenceClickListener;)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->a:Landroid/preference/CheckBoxPreference;

    invoke-virtual {v0, p0}, Landroid/preference/CheckBoxPreference;->setOnPreferenceChangeListener(Landroid/preference/Preference$OnPreferenceChangeListener;)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->i:Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

    invoke-virtual {v0, p0}, Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;->setOnPreferenceChangeListener(Landroid/preference/Preference$OnPreferenceChangeListener;)V

    invoke-direct {p0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->b()V

    return-void
.end method

.method private b()V
    .locals 7

    const/4 v6, 0x0

    const/4 v5, 0x1

    invoke-virtual {p0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->getPreferenceScreen()Landroid/preference/PreferenceScreen;

    move-result-object v0

    invoke-virtual {v0}, Landroid/preference/PreferenceScreen;->getSharedPreferences()Landroid/content/SharedPreferences;

    move-result-object v0

    new-instance v1, Lcom/kingreader/framework/a/c/a/r;

    invoke-direct {v1}, Lcom/kingreader/framework/a/c/a/r;-><init>()V

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    const-string v3, "txt_set_theme_bkg_use_img"

    iget-object v4, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    iget-boolean v4, v4, Lcom/kingreader/framework/a/c/a/m;->a:Z

    invoke-interface {v0, v3, v4}, Landroid/content/SharedPreferences;->getBoolean(Ljava/lang/String;Z)Z

    move-result v3

    iput-boolean v3, v2, Lcom/kingreader/framework/a/c/a/m;->a:Z

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    const-string v3, "txt_set_theme_bkg_img"

    iget-object v4, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    iget-object v4, v4, Lcom/kingreader/framework/a/c/a/m;->d:Ljava/lang/String;

    invoke-interface {v0, v3, v4}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    iput-object v3, v2, Lcom/kingreader/framework/a/c/a/m;->d:Ljava/lang/String;

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    const-string v3, "txt_set_theme_bkg_color"

    iget-object v4, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    iget v4, v4, Lcom/kingreader/framework/a/c/a/m;->b:I

    invoke-interface {v0, v3, v4}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v3

    iput v3, v2, Lcom/kingreader/framework/a/c/a/m;->b:I

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    const-string v3, "txt_set_theme_bkg_color_fill_mode"

    iget-object v4, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    iget v4, v4, Lcom/kingreader/framework/a/c/a/m;->c:I

    invoke-direct {p0, v0, v3, v4}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->a(Landroid/content/SharedPreferences;Ljava/lang/String;I)I

    move-result v3

    iput v3, v2, Lcom/kingreader/framework/a/c/a/m;->c:I

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->a:Lcom/kingreader/framework/a/c/a/n;

    const-string v3, "txt_set_theme_font_color"

    iget-object v4, v1, Lcom/kingreader/framework/a/c/a/r;->a:Lcom/kingreader/framework/a/c/a/n;

    iget v4, v4, Lcom/kingreader/framework/a/c/a/n;->d:I

    invoke-interface {v0, v3, v4}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v3

    iput v3, v2, Lcom/kingreader/framework/a/c/a/n;->a:I

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->a:Lcom/kingreader/framework/a/c/a/n;

    const-string v3, "txt_set_theme_font_name"

    iget-object v4, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->j:Ljava/lang/String;

    invoke-interface {v0, v3, v4}, Landroid/content/SharedPreferences;->getString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;

    move-result-object v3

    iput-object v3, v2, Lcom/kingreader/framework/a/c/a/n;->c:Ljava/lang/String;

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->d:Lcom/kingreader/framework/a/c/a/l;

    const-string v3, "txt_set_theme_sel_bkc"

    iget-object v4, v1, Lcom/kingreader/framework/a/c/a/r;->d:Lcom/kingreader/framework/a/c/a/l;

    iget v4, v4, Lcom/kingreader/framework/a/c/a/l;->a:I

    invoke-interface {v0, v3, v4}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v3

    iput v3, v2, Lcom/kingreader/framework/a/c/a/l;->a:I

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->d:Lcom/kingreader/framework/a/c/a/l;

    const-string v3, "txt_set_theme_sel_tc"

    iget-object v4, v1, Lcom/kingreader/framework/a/c/a/r;->d:Lcom/kingreader/framework/a/c/a/l;

    iget v4, v4, Lcom/kingreader/framework/a/c/a/l;->b:I

    invoke-interface {v0, v3, v4}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v3

    iput v3, v2, Lcom/kingreader/framework/a/c/a/l;->b:I

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->d:Lcom/kingreader/framework/a/c/a/l;

    const-string v3, "txt_set_theme_anno_bkc"

    iget-object v4, v1, Lcom/kingreader/framework/a/c/a/r;->d:Lcom/kingreader/framework/a/c/a/l;

    iget v4, v4, Lcom/kingreader/framework/a/c/a/l;->c:I

    invoke-interface {v0, v3, v4}, Landroid/content/SharedPreferences;->getInt(Ljava/lang/String;I)I

    move-result v0

    iput v0, v2, Lcom/kingreader/framework/a/c/a/l;->c:I

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->a:Landroid/preference/CheckBoxPreference;

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    iget-boolean v2, v2, Lcom/kingreader/framework/a/c/a/m;->a:Z

    invoke-virtual {v0, v2}, Landroid/preference/CheckBoxPreference;->setChecked(Z)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->b:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    iget-boolean v2, v2, Lcom/kingreader/framework/a/c/a/m;->a:Z

    if-nez v2, :cond_2

    move v2, v5

    :goto_0
    invoke-virtual {v0, v2}, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;->setEnabled(Z)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->h:Landroid/preference/Preference;

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    iget-boolean v2, v2, Lcom/kingreader/framework/a/c/a/m;->a:Z

    if-nez v2, :cond_3

    move v2, v5

    :goto_1
    invoke-virtual {v0, v2}, Landroid/preference/Preference;->setEnabled(Z)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->b:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    iget v2, v2, Lcom/kingreader/framework/a/c/a/m;->b:I

    invoke-virtual {v0, v2}, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;->b(I)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->c:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->a:Lcom/kingreader/framework/a/c/a/n;

    iget v2, v2, Lcom/kingreader/framework/a/c/a/n;->a:I

    invoke-virtual {v0, v2}, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;->b(I)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->d:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->d:Lcom/kingreader/framework/a/c/a/l;

    iget v2, v2, Lcom/kingreader/framework/a/c/a/l;->a:I

    invoke-virtual {v0, v2}, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;->b(I)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->e:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->d:Lcom/kingreader/framework/a/c/a/l;

    iget v2, v2, Lcom/kingreader/framework/a/c/a/l;->b:I

    invoke-virtual {v0, v2}, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;->b(I)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->f:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->d:Lcom/kingreader/framework/a/c/a/l;

    iget v2, v2, Lcom/kingreader/framework/a/c/a/l;->c:I

    invoke-virtual {v0, v2}, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;->b(I)V

    iget-object v0, v1, Lcom/kingreader/framework/a/c/a/r;->b:Lcom/kingreader/framework/a/c/a/m;

    invoke-virtual {v0}, Lcom/kingreader/framework/a/c/a/m;->d()Ljava/lang/String;

    move-result-object v0

    iget-object v2, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->g:Landroid/preference/Preference;

    if-nez v0, :cond_0

    const v0, 0x7f090120

    invoke-virtual {p0, v0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->getString(I)Ljava/lang/String;

    move-result-object v0

    :cond_0
    invoke-virtual {v2, v0}, Landroid/preference/Preference;->setSummary(Ljava/lang/CharSequence;)V

    invoke-virtual {p0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v2, 0x7f07002b

    invoke-virtual {v0, v2}, Landroid/content/res/Resources;->getStringArray(I)[Ljava/lang/String;

    move-result-object v0

    if-eqz v0, :cond_1

    iget-object v2, v1, Lcom/kingreader/framework/a/c/a/r;->a:Lcom/kingreader/framework/a/c/a/n;

    invoke-virtual {v2}, Lcom/kingreader/framework/a/c/a/n;->d()Z

    move-result v2

    if-nez v2, :cond_4

    iget-object v2, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->i:Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

    array-length v3, v0

    sub-int/2addr v3, v5

    aget-object v0, v0, v3

    invoke-virtual {v2, v0}, Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;->a(Ljava/lang/String;)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->i:Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

    iget-object v1, v1, Lcom/kingreader/framework/a/c/a/r;->a:Lcom/kingreader/framework/a/c/a/n;

    iget-object v1, v1, Lcom/kingreader/framework/a/c/a/n;->c:Ljava/lang/String;

    const/4 v2, 0x7

    invoke-virtual {v1, v2}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;->setSummary(Ljava/lang/CharSequence;)V

    :cond_1
    :goto_2
    return-void

    :cond_2
    move v2, v6

    goto :goto_0

    :cond_3
    move v2, v6

    goto :goto_1

    :cond_4
    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->i:Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

    iget-object v1, v1, Lcom/kingreader/framework/a/c/a/r;->a:Lcom/kingreader/framework/a/c/a/n;

    iget-object v1, v1, Lcom/kingreader/framework/a/c/a/n;->c:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;->a(Ljava/lang/String;)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->i:Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

    const v1, 0x7f090110

    invoke-virtual {p0, v1}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->getString(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;->setSummary(Ljava/lang/CharSequence;)V

    goto :goto_2
.end method


# virtual methods
.method protected onActivityResult(IILandroid/content/Intent;)V
    .locals 6

    invoke-super {p0, p1, p2, p3}, Landroid/preference/PreferenceActivity;->onActivityResult(IILandroid/content/Intent;)V

    sparse-switch p1, :sswitch_data_0

    :cond_0
    :goto_0
    return-void

    :sswitch_0
    if-eqz p2, :cond_0

    invoke-virtual {p3}, Landroid/content/Intent;->getData()Landroid/net/Uri;

    move-result-object v1

    if-eqz v1, :cond_0

    :try_start_0
    invoke-virtual {p0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->getContentResolver()Landroid/content/ContentResolver;

    move-result-object v0

    const/4 v2, 0x0

    const/4 v3, 0x0

    const/4 v4, 0x0

    const/4 v5, 0x0

    invoke-virtual/range {v0 .. v5}, Landroid/content/ContentResolver;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;

    move-result-object v0

    invoke-interface {v0}, Landroid/database/Cursor;->moveToFirst()Z

    invoke-virtual {p0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->getPreferenceScreen()Landroid/preference/PreferenceScreen;

    move-result-object v1

    invoke-virtual {v1}, Landroid/preference/PreferenceScreen;->getSharedPreferences()Landroid/content/SharedPreferences;

    move-result-object v1

    invoke-interface {v1}, Landroid/content/SharedPreferences;->edit()Landroid/content/SharedPreferences$Editor;

    move-result-object v1

    const/4 v2, 0x1

    invoke-interface {v0, v2}, Landroid/database/Cursor;->getString(I)Ljava/lang/String;

    move-result-object v2

    const-string v3, "txt_set_theme_bkg_use_img"

    const/4 v4, 0x1

    invoke-interface {v1, v3, v4}, Landroid/content/SharedPreferences$Editor;->putBoolean(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;

    const-string v3, "txt_set_theme_bkg_img"

    invoke-interface {v1, v3, v2}, Landroid/content/SharedPreferences$Editor;->putString(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;

    invoke-interface {v1}, Landroid/content/SharedPreferences$Editor;->commit()Z

    iget-object v1, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->g:Landroid/preference/Preference;

    invoke-virtual {v1, v2}, Landroid/preference/Preference;->setSummary(Ljava/lang/CharSequence;)V

    invoke-interface {v0}, Landroid/database/Cursor;->close()V
    :try_end_0
    .catch Ljava/lang/Error; {:try_start_0 .. :try_end_0} :catch_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_1

    goto :goto_0

    :catch_0
    move-exception v0

    goto :goto_0

    :sswitch_1
    if-eqz p2, :cond_1

    new-instance v0, Ljava/lang/StringBuilder;

    invoke-direct {v0}, Ljava/lang/StringBuilder;-><init>()V

    const-string v1, "file://"

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string v1, "OP_OPEN_FILE_PATH"

    invoke-virtual {p3, v1}, Landroid/content/Intent;->getStringExtra(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->j:Ljava/lang/String;

    :cond_1
    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->i:Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

    iget-object v1, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->j:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;->setValue(Ljava/lang/String;)V

    invoke-direct {p0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->b()V

    goto :goto_0

    :catch_1
    move-exception v0

    goto :goto_0

    :sswitch_data_0
    .sparse-switch
        0x1 -> :sswitch_0
        0x6d -> :sswitch_1
    .end sparse-switch
.end method

.method protected onCreate(Landroid/os/Bundle;)V
    .locals 2

    invoke-super {p0, p1}, Landroid/preference/PreferenceActivity;->onCreate(Landroid/os/Bundle;)V

    const v0, 0x7f030006

    invoke-virtual {p0, v0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->setContentView(I)V

    invoke-virtual {p0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->getPreferenceManager()Landroid/preference/PreferenceManager;

    move-result-object v0

    const-string v1, "kingreaderv3"

    invoke-virtual {v0, v1}, Landroid/preference/PreferenceManager;->setSharedPreferencesName(Ljava/lang/String;)V

    const/high16 v0, 0x7f050000

    invoke-virtual {p0, v0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->addPreferencesFromResource(I)V

    invoke-direct {p0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->a()V

    return-void
.end method

.method public onPause()V
    .locals 0

    invoke-super {p0}, Landroid/preference/PreferenceActivity;->onPause()V

    invoke-static {p0}, Lcom/kingreader/framework/os/android/model/r;->c(Landroid/content/Context;)V

    return-void
.end method

.method public onPreferenceChange(Landroid/preference/Preference;Ljava/lang/Object;)Z
    .locals 5

    const/4 v4, 0x2

    const/4 v2, 0x0

    const/4 v3, 0x1

    invoke-virtual {p1}, Landroid/preference/Preference;->getKey()Ljava/lang/String;

    move-result-object v0

    const-string v1, "txt_set_theme_font_name"

    invoke-virtual {v0, v1}, Ljava/lang/String;->compareToIgnoreCase(Ljava/lang/String;)I

    move-result v1

    if-nez v1, :cond_1

    check-cast p2, Ljava/lang/String;

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->i:Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

    invoke-virtual {v0}, Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;->getValue()Ljava/lang/String;

    move-result-object v0

    iput-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->j:Ljava/lang/String;

    const-string v0, "sdcard"

    invoke-virtual {p2, v0}, Ljava/lang/String;->equalsIgnoreCase(Ljava/lang/String;)Z

    move-result v0

    if-eqz v0, :cond_0

    invoke-static {p0}, Lcom/kingreader/framework/os/android/ui/main/a/b;->e(Landroid/content/Context;)Ljava/lang/String;

    move-result-object v0

    invoke-static {v4}, Lcom/kingreader/framework/os/android/ui/activity/OpenFileActivity;->c(I)Ljava/util/HashMap;

    move-result-object v1

    invoke-static {v0, v3, v1, v4}, Lcom/kingreader/framework/os/android/ui/activity/OpenFileActivity;->a(Ljava/lang/String;ILjava/util/HashMap;I)Landroid/os/Bundle;

    move-result-object v0

    new-instance v1, Landroid/content/Intent;

    const-class v2, Lcom/kingreader/framework/os/android/ui/activity/OpenFileActivity;

    invoke-direct {v1, p0, v2}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    invoke-virtual {v1, v0}, Landroid/content/Intent;->putExtras(Landroid/os/Bundle;)Landroid/content/Intent;

    const/16 v0, 0x6d

    invoke-virtual {p0, v1, v0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->startActivityForResult(Landroid/content/Intent;I)V

    :goto_0
    move v0, v3

    :goto_1
    return v0

    :cond_0
    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->i:Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;

    invoke-virtual {v0, p2}, Lcom/kingreader/framework/os/android/ui/preference/ListDescPreference;->setValue(Ljava/lang/String;)V

    invoke-direct {p0}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->b()V

    goto :goto_0

    :cond_1
    const-string v1, "txt_set_theme_bkg_use_img"

    invoke-virtual {v0, v1}, Ljava/lang/String;->compareToIgnoreCase(Ljava/lang/String;)I

    move-result v1

    if-nez v1, :cond_4

    check-cast p2, Ljava/lang/Boolean;

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->b:Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;

    invoke-virtual {p2}, Ljava/lang/Boolean;->booleanValue()Z

    move-result v1

    if-nez v1, :cond_2

    move v1, v3

    :goto_2
    invoke-virtual {v0, v1}, Lcom/kingreader/framework/os/android/ui/preference/ColorPickerPreference2;->setEnabled(Z)V

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->h:Landroid/preference/Preference;

    invoke-virtual {p2}, Ljava/lang/Boolean;->booleanValue()Z

    move-result v1

    if-nez v1, :cond_3

    move v1, v3

    :goto_3
    invoke-virtual {v0, v1}, Landroid/preference/Preference;->setEnabled(Z)V

    move v0, v3

    goto :goto_1

    :cond_2
    move v1, v2

    goto :goto_2

    :cond_3
    move v1, v2

    goto :goto_3

    :cond_4
    const-string v1, "txt_set_theme_bkg_img"

    invoke-virtual {v0, v1}, Ljava/lang/String;->compareToIgnoreCase(Ljava/lang/String;)I

    move-result v0

    if-nez v0, :cond_6

    if-eqz p2, :cond_5

    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->g:Landroid/preference/Preference;

    check-cast p2, Ljava/lang/String;

    invoke-virtual {v0, p2}, Landroid/preference/Preference;->setSummary(Ljava/lang/CharSequence;)V

    :goto_4
    move v0, v3

    goto :goto_1

    :cond_5
    iget-object v0, p0, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->g:Landroid/preference/Preference;

    const v1, 0x7f090120

    invoke-virtual {v0, v1}, Landroid/preference/Preference;->setSummary(I)V

    goto :goto_4

    :cond_6
    move v0, v3

    goto :goto_1
.end method

.method public onPreferenceClick(Landroid/preference/Preference;)Z
    .locals 3

    const/4 v2, 0x1

    :try_start_0
    invoke-virtual {p1}, Landroid/preference/Preference;->getKey()Ljava/lang/String;

    move-result-object v0

    const-string v1, "txt_set_theme_bkg_img"

    invoke-virtual {v0, v1}, Ljava/lang/String;->compareToIgnoreCase(Ljava/lang/String;)I

    move-result v0

    if-nez v0, :cond_0

    new-instance v0, Landroid/content/Intent;

    const-string v1, "android.intent.action.GET_CONTENT"

    invoke-direct {v0, v1}, Landroid/content/Intent;-><init>(Ljava/lang/String;)V

    const-string v1, "image/*"

    invoke-virtual {v0, v1}, Landroid/content/Intent;->setType(Ljava/lang/String;)Landroid/content/Intent;

    const/4 v1, 0x0

    invoke-static {v0, v1}, Landroid/content/Intent;->createChooser(Landroid/content/Intent;Ljava/lang/CharSequence;)Landroid/content/Intent;

    move-result-object v0

    const/4 v1, 0x1

    invoke-virtual {p0, v0, v1}, Lcom/kingreader/framework/os/android/ui/activity/AppSettingActivity;->startActivityForResult(Landroid/content/Intent;I)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    :cond_0
    :goto_0
    return v2

    :catch_0
    move-exception v0

    invoke-virtual {v0}, Ljava/lang/Exception;->getMessage()Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v0}, Lcom/kingreader/framework/os/android/ui/uicontrols/bf;->a(Landroid/app/Activity;Ljava/lang/String;)V

    goto :goto_0
.end method

.method public onResume()V
    .locals 0

    invoke-super {p0}, Landroid/preference/PreferenceActivity;->onResume()V

    invoke-static {p0}, Lcom/kingreader/framework/os/android/model/r;->b(Landroid/content/Context;)V

    return-void
.end method
