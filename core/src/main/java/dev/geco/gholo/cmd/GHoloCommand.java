package dev.geco.gholo.cmd;

import java.awt.image.*;
import java.io.File;
import java.util.*;

import org.jetbrains.annotations.*;

import org.bukkit.*;
import org.bukkit.event.player.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import dev.geco.gholo.GHoloMain;
import dev.geco.gholo.objects.*;
import dev.geco.gholo.util.*;

public class GHoloCommand implements CommandExecutor {

    private final GHoloMain GPM;

    public GHoloCommand(GHoloMain GPluginMain) { GPM = GPluginMain; }

    public static List<String> COMMAND_LIST = List.of("help", "list", "create", "info", "remove", "rename", "relocate", "tphere", "tpto", "align", "addrow", "insertrow", "setrow", "removerow", "copyrows", "data", "setimage", "importdata");

    @Override
    public boolean onCommand(@NotNull CommandSender Sender, @NotNull Command Command, @NotNull String Label, String[] Args) {

        if(!GPM.getPManager().hasPermission(Sender, "Holo")) {

            GPM.getMManager().sendMessage(Sender, "Messages.command-permission-error");
            return true;
        }

        if(Args.length == 0) {
            GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-use-error");
            return true;
        }

        GHolo holo;

        switch(Args[0].toLowerCase()) {
            case "help":
                GPM.getMManager().sendMessage(Sender, "HoloHelpCommand.header");
                for(String helpRow : COMMAND_LIST) {
                    GPM.getMManager().sendMessage(Sender, "HoloHelpCommand." + helpRow.toLowerCase());
                }
                GPM.getMManager().sendMessage(Sender, "HoloHelpCommand.footer");
                break;
            case "list":
                break;
            case "create":
                if(!(Sender instanceof Player player)) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-sender-error");
                    break;
                }
                if(Args.length == 1) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-create-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo != null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-create-exist-error", "%Holo%", holo.getId());
                    break;
                }
                GPM.getHoloManager().createHolo(Args[1], player.getLocation());
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-create", "%Holo%", Args[1]);
                break;
            case "info":
                if(Args.length == 1) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-info-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                GPM.getMManager().sendMessage(Sender, "HoloInfoCommand.header", "%Holo%", holo.getId());
                int row = 1;
                for(GHoloRow holoRow : holo.getRows()) {
                    GPM.getMManager().sendMessage(Sender, "HoloInfoCommand.row", "%Row%", row, "%Content%", holoRow.getContent());
                    row++;
                }
                GPM.getMManager().sendMessage(Sender, "HoloInfoCommand.footer", "%Holo%", holo.getId());
                break;
            case "remove":
                if(Args.length == 1) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-remove-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                GPM.getHoloManager().deleteHolo(holo);
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-remove", "%Holo%", holo.getId());
                break;
            case "rename":
                if(Args.length <= 2) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-rename-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                GHolo newIdHolo = GPM.getHoloManager().getHolo(Args[2]);
                if(newIdHolo != null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-rename-exist-error", "%Holo%", newIdHolo.getId());
                    break;
                }
                String oldId = holo.getId();
                GPM.getHoloManager().updateId(holo, Args[2]);
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-rename", "%Holo%", holo.getId(), "%OldHolo%", oldId);
                break;
            case "relocate":
                if(Args.length <= 4) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-relocate-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                try {
                    Location location = holo.getLocation();
                    location.set(Double.parseDouble(Args[2]), Double.parseDouble(Args[3]), Double.parseDouble(Args[4]));
                    GPM.getHoloManager().updateLocation(holo, location);
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-relocate", "%Holo%", holo.getId());
                } catch (NumberFormatException e) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-relocate-location-error");
                }
                break;
            case "tphere":
                if(!(Sender instanceof Player player)) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-sender-error");
                    break;
                }
                if(Args.length == 1) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-tphere-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                GPM.getHoloManager().updateLocation(holo, player.getLocation());
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-tphere", "%Holo%", holo.getId());
                break;
            case "tpto":
                if(!(Sender instanceof Player player)) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-sender-error");
                    break;
                }
                if(Args.length == 1) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-tpto-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                player.teleport(holo.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-tpto", "%Holo%", holo.getId());
                break;
            case "align":
                if(Args.length <= 3) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-align-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                GHolo alignOnHolo = GPM.getHoloManager().getHolo(Args[2]);
                if(alignOnHolo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[2].toLowerCase());
                    break;
                }
                String axis = Args[3].toLowerCase();
                Location holoLocation = holo.getLocation();
                Location alignOnHoloLocation = alignOnHolo.getLocation();
                String appliedAxis = "";
                if(axis.contains("x")) {
                    holoLocation.setX(alignOnHoloLocation.getX());
                    appliedAxis += "x";
                }
                if(axis.contains("y")) {
                    holoLocation.setY(alignOnHoloLocation.getY());
                    appliedAxis += "y";
                }
                if(axis.contains("z")) {
                    holoLocation.setZ(alignOnHoloLocation.getZ());
                    appliedAxis += "z";
                }
                if(appliedAxis.isEmpty()) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-align-axis-error", "%Axis%", axis);
                    break;
                }
                GPM.getHoloManager().updateLocation(holo, holoLocation);
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-align", "%Holo%", holo.getId(), "%Axis%", appliedAxis, "%AlignOnHolo%", alignOnHolo.getId());
                break;
            case "positionrow":
                // TODO: positionrow <Id> <Row> <x/y/z/yaw/pitch> <Value>
                break;
            case "addrow":
                if(Args.length <= 2) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-addrow-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                StringBuilder addIdStringBuilder = new StringBuilder();
                for(int arg = 2; arg <= Args.length - 1; arg++) addIdStringBuilder.append(Args[arg]).append(" ");
                addIdStringBuilder.deleteCharAt(addIdStringBuilder.length() - 1);
                GPM.getHoloManager().createHoloRow(holo, addIdStringBuilder.toString());
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-addrow", "%Holo%", holo.getId(), "%Content%", addIdStringBuilder.toString());
                break;
            case "insertrow":
                if(Args.length <= 3) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-insertrow-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                try {
                    GHoloRow holoRow = holo.getRow(Integer.parseInt(Args[2]) - 1);
                    if(holoRow == null) {
                        GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-insertrow-row-error", "%Row%", Args[2]);
                        break;
                    }
                    StringBuilder insertIdStringBuilder = new StringBuilder();
                    for(int arg = 3; arg <= Args.length - 1; arg++) insertIdStringBuilder.append(Args[arg]).append(" ");
                    insertIdStringBuilder.deleteCharAt(insertIdStringBuilder.length() - 1);
                    GPM.getHoloManager().insertHoloRow(holo, holoRow.getRow(), insertIdStringBuilder.toString(), true);
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-insertrow", "%Holo%", holo.getId(), "%Row%", Integer.parseInt(Args[2]), "%Content%", insertIdStringBuilder.toString());
                } catch (NumberFormatException e) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-insertrow-row-error", "%Row%", Args[2]);
                }
                break;
            case "setrow":
                if(Args.length <= 3) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-setrow-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                try {
                    GHoloRow holoRow = holo.getRow(Integer.parseInt(Args[2]) - 1);
                    if(holoRow == null) {
                        GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-setrow-row-error", "%Row%", Args[2]);
                        break;
                    }
                    StringBuilder setIdStringBuilder = new StringBuilder();
                    for(int arg = 3; arg <= Args.length - 1; arg++) setIdStringBuilder.append(Args[arg]).append(" ");
                    setIdStringBuilder.deleteCharAt(setIdStringBuilder.length() - 1);
                    GPM.getHoloManager().updateHoloRowContent(holoRow, setIdStringBuilder.toString());
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-setrow", "%Holo%", holo.getId(), "%Row%", Integer.parseInt(Args[2]), "%Content%", setIdStringBuilder.toString());
                } catch (NumberFormatException e) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-setrow-row-error", "%Row%", Args[2]);
                }
                break;
            case "removerow":
                if(Args.length <= 2) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-removerow-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                try {
                    GHoloRow holoRow = holo.getRow(Integer.parseInt(Args[2]) - 1);
                    if(holoRow == null) {
                        GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-removerow-row-error", "%Row%", Args[2]);
                        break;
                    }
                    GPM.getHoloManager().removeHoloRow(holoRow, true);
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-removerow", "%Holo%", holo.getId(), "%Row%", Integer.parseInt(Args[2]));
                } catch (NumberFormatException e) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-removerow-row-error", "%Row%", Args[2]);
                }
                break;
            case "copyrows":
                if(Args.length <= 2) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-copyrows-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                GHolo copyToHolo = GPM.getHoloManager().getHolo(Args[2]);
                if(copyToHolo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                GPM.getHoloManager().copyRows(holo, copyToHolo);
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-copyrows", "%Holo%", holo.getId(), "%CopyToHolo%", copyToHolo.getId());
                break;
            case "data":
                if(Args.length <= 4) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                GHoloRowData rowData = null;
                int arg = 3;
                GHoloRow holoRow = null;
                switch (Args[2].toLowerCase()) {
                    case "default":
                        rowData = holo.getDefaultRowData();
                        break;
                    case "row":
                        try {
                            holoRow = holo.getRow(Integer.parseInt(Args[3]) - 1);
                            if (holoRow == null) {
                                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data-row-error", "%Row%", Args[3]);
                                return true;
                            }
                            rowData = holoRow.getRowData();
                            arg = 4;
                        } catch (NumberFormatException e) {
                            GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data-row-error", "%Row%", Args[3]);
                            return true;
                        }
                        break;
                }
                if(rowData == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data-use-error");
                    break;
                }
                GHoloRowUpdateType updateType = null;
                // TODO: On default value reset to null
                switch (Args[arg].toLowerCase()) {
                    case "range":
                        try {
                            double range = Double.parseDouble(Args[arg + 1]);
                            rowData.setRange(range);
                            updateType = GHoloRowUpdateType.RANGE;
                        } catch (NumberFormatException e) {
                            GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data-value-error", "%Data%", Args[arg].toLowerCase(), "%Value%", Args[arg + 1]);
                        }
                        break;
                    case "background_color":
                        String backgroundColor = Args[arg + 1];
                        rowData.setBackgroundColor(backgroundColor);
                        updateType = GHoloRowUpdateType.BACKGROUND_COLOR;
                        break;
                    case "text_opacity":
                        try {
                            byte textOpacity = Byte.parseByte(Args[arg + 1]);
                            rowData.setTextOpacity(textOpacity);
                            updateType = GHoloRowUpdateType.TEXT_OPACITY;
                        } catch (NumberFormatException e) {
                            GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data-value-error", "%Data%", Args[arg].toLowerCase(), "%Value%", Args[arg + 1]);
                        }
                        break;
                    case "text_shadow":
                        try {
                            boolean textShadow = Boolean.parseBoolean(Args[arg + 1]);
                            rowData.setTextShadow(textShadow);
                            updateType = GHoloRowUpdateType.TEXT_SHADOW;
                        } catch (NumberFormatException e) {
                            GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data-value-error", "%Data%", Args[arg].toLowerCase(), "%Value%", Args[arg + 1]);
                        }
                        break;
                    case "billboard":
                        try {
                            Display.Billboard billboard = Display.Billboard.valueOf(Args[arg + 1].toUpperCase());
                            rowData.setBillboard(billboard.name().toLowerCase());
                            updateType = GHoloRowUpdateType.BILLBOARD;
                        } catch (IllegalArgumentException e) {
                            GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data-value-error", "%Data%", Args[arg].toLowerCase(), "%Value%", Args[arg + 1].toUpperCase());
                        }
                        break;
                    case "see_through":
                        try {
                            boolean seeThrough = Boolean.parseBoolean(Args[arg + 1]);
                            rowData.setSeeThrough(seeThrough);
                            updateType = GHoloRowUpdateType.SEE_THROUGH;
                        } catch (NumberFormatException e) {
                            GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data-value-error", "%Data%", Args[arg].toLowerCase(), "%Value%", Args[arg + 1]);
                        }
                        break;
                    case "size":
                        try {
                            float size = Float.parseFloat(Args[arg + 1]);
                            rowData.setSize(size);
                            updateType = GHoloRowUpdateType.SIZE;
                        } catch (NumberFormatException e) {
                            GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data-value-error", "%Data%", Args[arg].toLowerCase(), "%Value%", Args[arg + 1]);
                        }
                        break;
                }
                if(holoRow == null) {
                    for(GHoloRow updateHoloRow : holo.getRows()) updateHoloRow.getHoloRowEntity().publishUpdate(updateType);
                } else {
                    holoRow.getHoloRowEntity().publishUpdate(updateType);
                }
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-data", "%Data%", Args[arg].toLowerCase(), "%Value%", Args[arg + 1].toLowerCase());
                break;
            case "setimage":
                if(Args.length <= 3) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-setimage-use-error");
                    break;
                }
                holo = GPM.getHoloManager().getHolo(Args[1]);
                if(holo == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-exist-error", "%Holo%", Args[1].toLowerCase());
                    break;
                }
                BufferedImage bufferedImage = null;
                switch (Args[2].toLowerCase()) {
                    case "file":
                        File imageFile = new File(ImageUtil.IMAGE_FOLDER, Args[3]);
                        if(!imageFile.exists()) break;
                        bufferedImage = ImageUtil.getBufferedImage(imageFile);
                        break;
                    case "url":
                        bufferedImage = ImageUtil.getBufferedImage(Args[3]);
                        break;
                    case "avatar":
                    case "helm":
                        OfflinePlayer target;
                        try {
                            target = Bukkit.getOfflinePlayer(UUID.fromString(Args[3]));
                        } catch (Throwable e) {
                            target = Bukkit.getOfflinePlayer(Args[3]);
                        }
                        bufferedImage = ImageUtil.getBufferedImage(target, Args[2].equalsIgnoreCase("helm"));
                        break;
                    default:
                        GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-setimage-use-error");
                        return true;
                }
                if(bufferedImage == null) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-setimage-image-error", "%Type%", Args[2].toLowerCase(), "%Source%", Args[3]);
                    break;
                }
                List<String> rows;
                if(Args.length > 4) {
                    try {
                        if(Args[4].contains(":")) {
                            String[] sizes = Args[4].split(":");
                            rows = new ImageUtil(bufferedImage, Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1])).getLines();
                        } else rows = new ImageUtil(bufferedImage, Integer.parseInt(Args[4])).getLines();
                    } catch (Throwable e) {
                        GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-setimage-size-error");
                        return true;
                    }
                }  else rows = new ImageUtil(bufferedImage).getLines();
                GPM.getHoloManager().setRows(holo, rows);
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-setimage", "%Holo%", holo.getId(), "%Type%", Args[2].toLowerCase(), "%Source%", Args[3]);
                break;
            case "importdata":
                if(Args.length == 1) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-importdata-use-error");
                    break;
                }
                String plugin = Args[1].toLowerCase();
                if(!GPM.getHoloImportManager().AVAILABLE_PLUGIN_IMPORTS.contains(plugin)) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-importdata-exist-error", "%Plugin%", Args[1]);
                    break;
                }
                int imported = GPM.getHoloImportManager().importFromPlugin(plugin);
                if(imported < 0) {
                    GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-importdata-import-error", "%Plugin%", plugin);
                    break;
                }
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-importdata", "%Plugin%", plugin, "%Imported%", imported);
                break;
            default:
                GPM.getMManager().sendMessage(Sender, "Messages.command-gholo-use-error");
        }

        return true;
    }

}