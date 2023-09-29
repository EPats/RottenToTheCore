package io.github.epats.rottentothecore.client.render;

import com.google.common.collect.ImmutableList;
import io.github.epats.rottentothecore.Config;
import io.github.epats.rottentothecore.client.ClientData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a 'thought' or message that can be displayed on the screen with
 * various visual effects like fade-in and fade-out. It also supports custom
 * formatting and replacement behaviors.
 */
public class Thought {
    private static final int QUICK_FADE_OFFSET = 30;
    private MutableComponent message;
    private int displayTicks;
    private int fadeInThreshold;
    private int fadeOutThreshold;
    private ReplacementBehaviour replacementBehaviour;
    private ImmutableList<MutableComponent> messageLines;

    /**
     * Constructs a Thought with default display ticks, fade-in and fade-out thresholds, and replacement behaviour.
     *
     * @param message The content of the thought.
     */
    public Thought(MutableComponent message) {
        this(message, Config.ClientConfig.thoughtDisplayTicks, 0, Config.ClientConfig.thoughtDisplayTicks);
    }

    /**
     * Constructs a Thought with specified display ticks, fade-in and fade-out thresholds but default replacement behaviour.
     *
     * @param message The content of the thought.
     * @param displayTicks Duration for which the thought should be displayed.
     * @param fadeInThreshold Duration for the fade-in effect.
     * @param fadeOutThreshold Duration before the fade-out effect starts.
     */
    public Thought(MutableComponent message, int displayTicks, int fadeInThreshold, int fadeOutThreshold) {
        this(message, displayTicks, fadeInThreshold, fadeOutThreshold, ReplacementBehaviour.ADD_TO_QUEUE);
    }

    /**
     * Constructs a Thought with default display ticks, fade-in and fade-out thresholds.
     *
     * @param message The content of the thought.
     * @param replacementBehaviour Replacement behaviour for this thought if other thoughts are present/queued.
     */
    public Thought(MutableComponent message, ReplacementBehaviour replacementBehaviour) {
        this(message, Config.ClientConfig.thoughtDisplayTicks, 0, Config.ClientConfig.thoughtDisplayTicks, replacementBehaviour);
    }

    /**
     * Constructs a Thought with specified display ticks, fade-in and fade-out thresholds but default replacement behaviour.
     *
     * @param message The content of the thought.
     * @param displayTicks Duration for which the thought should be displayed.
     * @param fadeInThreshold Duration for the fade-in effect.
     * @param fadeOutThreshold Duration before the fade-out effect starts.
     * @param replacementBehaviour Replacement behaviour for this thought if other thoughts are present/queued.
     */
    public Thought(MutableComponent message, int displayTicks, int fadeInThreshold, int fadeOutThreshold, ReplacementBehaviour replacementBehaviour) {
        this.message = message;
        this.displayTicks = displayTicks;
        this.fadeInThreshold = fadeInThreshold;
        this.fadeOutThreshold = fadeOutThreshold;
        this.replacementBehaviour = replacementBehaviour;
        this.messageLines = splitMessage(message);
    }

    /**
     * Constructs a Thought from a saved ByteBuf (e.g. for message handling).
     *
     * @param buf ByteBuf where data is stored.
     */
    public Thought(FriendlyByteBuf buf) {
        this.message = (MutableComponent) buf.readComponent();
        this.displayTicks = buf.readInt();
        this.fadeInThreshold = buf.readInt();
        this.fadeOutThreshold = buf.readInt();
        this.replacementBehaviour = ReplacementBehaviour.values()[buf.readInt()];
        this.messageLines = splitMessage(message);
    }

    /**
     * Saves a Thought to a ByteBuf (e.g. for message handling).
     *
     * @param buf ByteBuf where data is stored.
     */
    public void serializeToBuffer(FriendlyByteBuf buf){
        buf.writeComponent(this.message);
        buf.writeInt(this.displayTicks);
        buf.writeInt(this.fadeInThreshold);
        buf.writeInt(this.fadeOutThreshold);
        buf.writeInt(this.replacementBehaviour.ordinal());
    }

    public int getDisplayTicks() {
        return this.displayTicks;
    }

    public int getFadeOutThreshold() {
        return this.fadeOutThreshold;
    }

    public ReplacementBehaviour getReplacementBehaviour() {
        return this.replacementBehaviour;
    }

    public void setDifferentFadeOut(int newFadeOutThreshold) {
        this.fadeOutThreshold = newFadeOutThreshold;
    }

    /**
     * Apply specified formatting to content if it starts with the given prefix.
     *
     * @param content The content string.
     * @param prefix The prefix indicating a specific format.
     * @param format The format to apply.
     * @param style The style to update.
     * @return Updated content after trimming the prefix.
     */
    private String applyFormatAndTrim(String content, String prefix, ChatFormatting format, Style style) {
        if (content.startsWith(prefix)) {
            style.applyFormat(format);
            return content.substring(prefix.length());
        }
        return content;
    }

    /**
     * Split the message based on custom formatting and content.
     *
     * @return A list of message components after applying the formatting.
     */
    private ImmutableList<MutableComponent> splitMessage(MutableComponent newMessage) {
        ImmutableList.Builder<MutableComponent> builder = ImmutableList.builder();
        String content = newMessage.getString();
        Style style = newMessage.getStyle();

        // Apply formatting based on custom tags.
        content = applyFormatAndTrim(content, "{b}", ChatFormatting.BOLD, style);
        content = applyFormatAndTrim(content, "{i}", ChatFormatting.ITALIC, style);
        content = applyFormatAndTrim(content, "{u}", ChatFormatting.UNDERLINE, style);
        content = applyFormatAndTrim(content, "{st}", ChatFormatting.STRIKETHROUGH, style);
        content = applyFormatAndTrim(content, "{ob}", ChatFormatting.OBFUSCATED, style);

        // Split message based on '/'
        for (String part : content.split("/")) {
            builder.add(Component.translatable(part).setStyle(style));
        }

        return builder.build();
    }

    public ImmutableList<MutableComponent> getMessageLines() {
        return this.messageLines;
    }

    /**
     * Calculate the opacity based on display time and fade durations.
     *
     * @param timeDisplayed The time the message has been displayed.
     * @return Calculated opacity value between 0 and 1.
     */
    public float calculateOpacity(int timeDisplayed) {
        // If within the steady state (full opacity)
        if (timeDisplayed >= this.fadeInThreshold && timeDisplayed <= this.fadeOutThreshold) {
            return 1.0F;
        }

        // If within the fade-in duration
        if (timeDisplayed < this.fadeInThreshold) {
            return timeDisplayed / (float) this.fadeInThreshold;
        }

        // If within the fade-out duration
        float opacityDrop =  (timeDisplayed - this.fadeOutThreshold) / (float) this.fadeOutThreshold;
        return 1.0F - opacityDrop;
    }

    /**
     * Enum to represent the different behaviors a new thought can have
     * in relation to the currently displayed thought or thoughts queue.
     */
    public enum ReplacementBehaviour {
        NO_MESSAGE {
            @Override
            public void handleReplacementBehaviour(ClientData clientData, Thought thought) {
                clientData.clearThoughtsQueue();
                clientData.clearCurrentThought();
            }
        },
        IMMEDIATE{
            @Override
            public void handleReplacementBehaviour(ClientData clientData, Thought thought) {
                clientData.clearThoughtsQueue();
                clientData.addThoughtToQueue(thought);
                clientData.getNextThought();
            }
        },
        QUICK_FADE{
            @Override
            public void handleReplacementBehaviour(ClientData clientData, Thought thought) {
                clientData.clearThoughtsQueue();
                clientData.addThoughtToQueue(thought);
                if(clientData.getDisplayTicks() < clientData.getCurrentThought().getFadeOutThreshold()) {
                    Thought currentThought = clientData.getCurrentThought();
                    currentThought.setDifferentFadeOut(currentThought.getDisplayTicks() - QUICK_FADE_OFFSET);
                    clientData.setDisplayTicks(currentThought.getFadeOutThreshold());
                }
            }
        },
        FADE_STARTING_NOW{
            @Override
            public void handleReplacementBehaviour(ClientData clientData, Thought thought) {
                clientData.clearThoughtsQueue();
                clientData.addThoughtToQueue(thought);
                if(clientData.getDisplayTicks() < clientData.getCurrentThought().getFadeOutThreshold()) {
                    clientData.setDisplayTicks(clientData.getCurrentThought().getFadeOutThreshold());
                }
            }
        },
        REPLACE_ON_BLANK{
            @Override
            public void handleReplacementBehaviour(ClientData clientData, Thought thought) {
                clientData.clearThoughtsQueue();
                clientData.addThoughtToQueue(thought);
            }
        },
        ADD_TO_QUEUE{
            @Override
            public void handleReplacementBehaviour(ClientData clientData, Thought thought) {
                clientData.addThoughtToQueue(thought);
            }
        };

        public abstract void handleReplacementBehaviour(ClientData clientData, Thought thought);
    }

}
