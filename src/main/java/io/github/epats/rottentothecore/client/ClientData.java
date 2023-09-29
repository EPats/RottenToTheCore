package io.github.epats.rottentothecore.client;

import com.mojang.blaze3d.platform.Window;
import io.github.epats.rottentothecore.Config;
import io.github.epats.rottentothecore.client.render.Thought;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * ClientData serves as a manager for displaying 'thoughts' on the game screen.
 * It handles the queueing, timing, and rendering of these messages.
 */
public class ClientData {
    /**
     * ClientData is a singleton class, ensuring only one instance is responsible for managing thoughts display.
     */
    private static final ClientData INSTANCE = new ClientData();
    private Thought currentThought;
    private int displayTicks = 0;
    private final List<Thought> thoughtsQueue = new ArrayList<>();

    private ClientData() {}

    public static ClientData getInstance() {
        return INSTANCE;
    }

    /**
     * Processes the given thought. If there's no current thought being displayed, the provided one is immediately queued and shown.
     * Otherwise, the behavior is determined by the thought's ReplacementBehaviour.
     *
     * @param thought The thought to be processed.
     */
    public void processNewThought(Thought thought) {
        if(this.currentThought == null) {
            this.addThoughtToQueue(thought);
            this.getNextThought();
            return;
        }
        thought.getReplacementBehaviour().handleReplacementBehaviour(this, thought);
    }

    /**
     * Adds the given thought to the end of the queue.
     *
     * @param thought The thought to be queued.
     */
    public void addThoughtToQueue(Thought thought) {
        this.thoughtsQueue.add(thought);
    }

    /**
     * Clears all thoughts from the queue.
     */
    public void clearThoughtsQueue() {
        this.thoughtsQueue.clear();
    }

    /**
     * Sets the display ticks for the current thought.
     * 0 is a new thought, and this ticks upwards until the thought reaches its specified display time.
     *
     * @param newDisplayTime The new display ticks to set.
     */
    public void setDisplayTicks(int newDisplayTime) {
        this.displayTicks = newDisplayTime;
    }


    /**
     * Retrieves the current display ticks for the thought being shown.
     *
     * @return The current display ticks.
     */
    public int getDisplayTicks() {
        return this.displayTicks;
    }

    /**
     * Retrieves the current thought being displayed or processed.
     *
     * @return The current thought.
     */
    public Thought getCurrentThought() {
        return this.currentThought;
    }

    /**
     * Clears the current thought being displayed and resets the display time.
     */
    public void clearCurrentThought() {
        this.setDisplayTicks(0);
        this.currentThought = null;
    }

    /**
     * Ticks the thoughts, advancing the display ticks and rendering the current thought on screen.
     * If a thought's display ticks exceeds its limit, it fetches the next thought from the queue.
     *
     * @param window The game window.
     * @param guiGraphics The graphics utility for rendering.
     */
    public void tickThoughts(Window window, GuiGraphics guiGraphics) {
        if(this.currentThought == null)
            return;

        this.renderCurrentThought(window, guiGraphics);
        this.displayTicks++;
        if(this.displayTicks > this.currentThought.getDisplayTicks())
            this.getNextThought();
    }

    /**
     * Fetches the next thought from the queue to be displayed.
     * If the queue is empty, it sets the current thought to null.
     */
    public void getNextThought() {
        if(this.thoughtsQueue.isEmpty()) {
            this.currentThought = null;
            return;
        }

        this.displayTicks = 0;
        this.currentThought = this.thoughtsQueue.remove(0);
    }

    /**
     * Render the thought message on the screen with fade-in and fade-out effects.
     *
     * @param window The window in which to render the message.
     * @param guiGraphics Graphics utility to render GUI elements.
     */
    private void renderCurrentThought(Window window, GuiGraphics guiGraphics){
        if(this.currentThought == null || !Config.ClientConfig.enableThoughts)
            return;

        Minecraft mc = Minecraft.getInstance();
        if(mc.isPaused())
            return;

        float opacity = this.currentThought.calculateOpacity(displayTicks);
        for (int i = 0; i < this.currentThought.getMessageLines().size(); i++) {
            this. renderThought(this.currentThought.getMessageLines().get(i), opacity, window, guiGraphics, mc, i);
        }
    }

    /**
     * Render individual message line on the screen with specified opacity.
     *
     * @param message The message to render.
     * @param opacity The opacity to use for rendering.
     * @param window The window in which to render the message.
     * @param guiGraphics Graphics utility to render GUI elements.
     * @param mc The Minecraft instance.
     * @param index The index position of the message in the list.
     */
    private void renderThought(MutableComponent message, float opacity, Window window, GuiGraphics guiGraphics, Minecraft mc, int index) {
        int width = mc.font.width(message.getVisualOrderText()) / 2;
        int mid = window.getGuiScaledWidth() / 2;
        int alpha = Math.round(opacity * 255);
        // Guard against low alphas: Due to a known rendering issue, very low alpha values are rendered as fully opaque.
        if(alpha < 10) return;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0D, 0.0D, 50.0D);
        guiGraphics.pose().scale(1.0F, 1.0F, 1.0F);
        guiGraphics.drawString(mc.font, message.getVisualOrderText(), mid - width, 40 + 10 * index, 16777215 + (alpha << 24));
        guiGraphics.pose().popPose();
    }


}
