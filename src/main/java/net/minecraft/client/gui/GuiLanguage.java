package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage extends GuiScreen
{
    /** The parent Gui screen */
    protected GuiScreen parentScreen;

    /** The List GuiSlot object reference. */
    private GuiLanguage.List list;

    /** Reference to the GameSettings object. */
    private final GameSettings game_settings_3;

    /** Reference to the LanguageManager object. */
    private final LanguageManager languageManager;

    /** The button to confirm the current settings. */
    private GuiOptionButton confirmSettingsBtn;

    public GuiLanguage(GuiScreen screen, GameSettings gameSettingsObj, LanguageManager manager)
    {
        this.parentScreen = screen;
        this.game_settings_3 = gameSettingsObj;
        this.languageManager = manager;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.add(this.confirmSettingsBtn = new GuiOptionButton(6, this.width / 2 - 75, this.height - 38, I18n.format("gui.done")));
        this.list = new GuiLanguage.List(this.mc);
        this.list.registerScrollButtons(7, 8);
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 5:
                    break;

                case 6:
                    this.mc.displayGuiScreen(this.parentScreen);
                    break;

                default:
                    this.list.actionPerformed(button);
            }
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("options.language"), this.width / 2, 16, -1);
        this.drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning") + ")", this.width / 2, this.height - 56, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class List extends GuiSlot
    {
        private final java.util.List<String> langCodeList = Lists.newArrayList();
        private final Map<String, Language> languageMap = Maps.newHashMap();

        public List(Minecraft mcIn)
        {
            super(mcIn, GuiLanguage.this.width, GuiLanguage.this.height, 32, GuiLanguage.this.height - 65 + 4, 18);

            for (Language language : GuiLanguage.this.languageManager.getLanguages())
            {
                this.languageMap.put(language.getLanguageCode(), language);
                this.langCodeList.add(language.getLanguageCode());
            }
        }

        protected int getSize()
        {
            return this.langCodeList.size();
        }

        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
        {
            Language language = this.languageMap.get(this.langCodeList.get(slotIndex));
            GuiLanguage.this.languageManager.setCurrentLanguage(language);
            GuiLanguage.this.game_settings_3.language = language.getLanguageCode();
            this.mc.refreshResources();
            GuiLanguage.this.fontRendererObj.setUnicodeFlag(GuiLanguage.this.languageManager.isCurrentLocaleUnicode() || GuiLanguage.this.game_settings_3.forceUnicodeFont);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.isCurrentLanguageBidirectional());
            GuiLanguage.this.confirmSettingsBtn.displayString = I18n.format("gui.done");
            GuiLanguage.this.game_settings_3.saveOptions();
        }

        protected boolean isSelected(int slotIndex)
        {
            return this.langCodeList.get(slotIndex).equals(GuiLanguage.this.languageManager.getCurrentLanguage().getLanguageCode());
        }

        protected int getContentHeight()
        {
            return this.getSize() * 18;
        }

        protected void drawBackground()
        {
            GuiLanguage.this.drawDefaultBackground();
        }

        protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
        {
            GuiLanguage.this.fontRendererObj.setBidiFlag(true);
            GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRendererObj, this.languageMap.get(this.langCodeList.get(entryID)).toString(), this.width / 2, p_180791_3_ + 1, -1);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.languageManager.getCurrentLanguage().isBidirectional());
        }
    }
}