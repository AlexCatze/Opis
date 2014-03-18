package mcp.mobius.opis.commands.server;

import java.util.HashMap;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.network.server.Packet_DataListAmountEntities;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;

public class CommandAmountEntities extends CommandBase implements IOpisCommand{

	@Override
	public String getCommandName() {
		return "opis_nent";
	}
	
	@Override
	public String getCommandNameOpis() {
		return this.getCommandName();
	}
	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		HashMap<String, Integer> ents;
		
		if (astring.length == 1 && astring[0].equals("all"))
			ents = EntityManager.getCumulativeEntities(false);
		else
			ents = EntityManager.getCumulativeEntities(true);
		
		if (icommandsender instanceof EntityPlayer)
			((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_DataListAmountEntities.create(ents));
		else{
			for (String s : ents.keySet())
				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("%s : %s", s, ents.get(s))));
		}
		
	}
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
		if (sender  instanceof DedicatedServer) return true;
		if ((sender instanceof EntityPlayerMP) && ((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;
		if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "Opens a summary of the number of entities on the server, by type.";
	}

}
