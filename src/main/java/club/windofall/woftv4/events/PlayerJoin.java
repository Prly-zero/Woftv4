package club.windofall.woftv4.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.player.PlayerJoinEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        TextComponent textComponent = new TextComponent(ChatColor.GREEN+player.getDisplayName());
        TextComponent textComponent1 = new TextComponent(" 加入了游戏。");
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "UUID: " + player.getUniqueId())));
        event.setJoinMessage(null);
        for (Player p: Bukkit.getOnlinePlayers()
             ) {
            p.spigot().sendMessage(textComponent,textComponent1);
        }

    }
}
