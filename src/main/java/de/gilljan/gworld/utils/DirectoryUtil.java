package de.gilljan.gworld.utils;

import de.gilljan.gworld.GWorld;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Utility class for directory-related operations.
 */
public class DirectoryUtil {
    /**
     * Checks if the specified map directory exists.
     *
     * @param name the name of the map directory
     * @return true if the directory exists and is a directory, false otherwise
     */
    public static boolean doesMapDirectoryExist(String name) {
        if (name == null) {
            return false;
        }
        File dir = new File(Bukkit.getWorldContainer(), name);
        return dir.exists() && dir.isDirectory();
    }

    public static boolean copyMapDirectory(String sourceName, String targetName) {
        if(!doesMapDirectoryExist(sourceName) || doesMapDirectoryExist(targetName)) {
            return false;
        }

        File sourceDir = new File(Bukkit.getWorldContainer(), sourceName);
        File targetDir = new File(Bukkit.getWorldContainer(), targetName);

        FileFilter filter = file -> {
            String fileName = file.getName();
            return !fileName.equalsIgnoreCase("session.lock") && !fileName.equalsIgnoreCase("uid.dat");
        };

        try {
            FileUtils.copyDirectory(sourceDir, targetDir, filter);
            return true;
        } catch (IOException ex) {
            GWorld.getInstance().getLogger().log(Level.SEVERE, "Error copying map directory", ex);

            try {
                FileUtils.deleteDirectory(targetDir);
            } catch (IOException deleteEx) {
                GWorld.getInstance().getLogger().log(Level.WARNING, "Error deleting target directory after failed copy", deleteEx);
            }
            return false;
        }

    }
}