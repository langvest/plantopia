package by.langvest.toolkit.event;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public abstract class RegisterCommandsEvent extends Event {
    public abstract void register(LiteralArgumentBuilder<CommandSourceStack> command);
}
