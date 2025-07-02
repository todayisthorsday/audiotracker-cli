package com.audiotracker.audiotracker_cli;

import com.audiotracker.audiotracker_cli.menu.CLIMenu;

public class AudiotrackerCliApplication {
    public static void main(String[] args) {
        CLIMenu menu = new CLIMenu();
        menu.start();
    }
}

