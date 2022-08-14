package org.wch.commons.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FileUtilsTest {

    @Test
    public void test() throws Exception {
        String sourceFolderPath = "C:\\Windows_Own_Workspace_Folder\\outPutFile\\test1/";
        String targetFolderPath = "C:\\Windows_Own_Workspace_Folder\\outPutFile\\test/";
        FileUtils.copyFolder(sourceFolderPath,targetFolderPath);

    }
}