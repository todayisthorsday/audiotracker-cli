package com.audiotracker.audiotracker_cli;

import com.audiotracker.audiotracker_cli.AudiobookCLI;

import com.audiotracker.audiotracker_cli.menu.CLIMenu;

public class Main {
    public static void main(String[] args) {
        CLIMenu menu = new CLIMenu();
        menu.start();
    }
}

