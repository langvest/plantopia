package by.langvest.plantopia.command;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.command.special.PlantopiaWipeCommand;
import by.langvest.toolkit.event.RegisterCommandsEvent;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.NotNull;

public class PlantopiaCommands {
    public static void setup(@NotNull RegisterCommandsEvent event) {
        event.register(
            Commands.literal(Plantopia.MOD_ID)
                .then(
                    Commands.literal("wipe")
                        .requires(source -> source.hasPermission(2))
                        .then(
                            Commands.argument("radius", IntegerArgumentType.integer(0, 128))
                                .executes(context -> {
                                    var player = context.getSource().getPlayer();
                                    int radius = IntegerArgumentType.getInteger(context, "radius");
                                    PlantopiaWipeCommand.wipeChunksAroundPlayer(player, radius);
                                    return 1;
                                })
                        )
                )

        );
    }
}
