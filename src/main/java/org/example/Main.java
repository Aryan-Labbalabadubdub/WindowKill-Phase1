package org.example;

import model.entityModel.Skill;
import view.menu.MainMenu;

import javax.swing.*;
import java.awt.*;

import static controller.constants.UIConstants.ORBITRON_FONT;
import static view.containers.GlassFrame.getGlassFrame;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            System.setProperty("sun.java2d.opengl", "true");
            System.setProperty("awt.useSystemAAFontSettings", "on");
            UIManager.getLookAndFeelDefaults().put("defaultFont", ORBITRON_FONT.deriveFont(15f));
            getGlassFrame();
            Skill.initializeSkills();
            MainMenu.getINSTANCE().togglePanel();
        });
    }
}