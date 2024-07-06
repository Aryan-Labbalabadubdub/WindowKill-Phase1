package view.menu;

import controller.constants.DefaultMethods;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static controller.UserInterfaceController.*;
import static controller.constants.DimensionConstants.SKILL_TREE_DIMENSION;
import static controller.constants.UIConstants.*;
import static controller.constants.UIMessageConstants.*;

public class SkillTree extends PanelB {

    static ArrayList<Component> categories = new ArrayList<>();
    static ArrayList<ArrayList<Component>> skills = new ArrayList<>();
    private static SkillTree INSTANCE;

    private SkillTree() {
        super(SKILL_TREE_DIMENSION.getValue());
        updateSkillTree();
        ButtonB back = new ButtonB(ButtonB.ButtonType.small_menu_button, "BACK", (int) BACK_BUTTON_WIDTH.getValue(), BACK_BUTTON_FONT_SCALE.getValue(), false) {{
            addActionListener(e -> {
                SkillTree.getINSTANCE().togglePanel();
                MainMenu.getINSTANCE().togglePanel();
            });
        }};
        constraints.gridwidth = 1;
        horizontalBulkAdd(categories);
        constraints.gridy++;
        for (ArrayList<Component> levelSkills : skills) {
            constraints.gridy++;
            constraints.gridx = 0;
            horizontalBulkAdd(levelSkills);
        }
        constraints.gridx = 0;
        constraints.gridwidth = categories.size();
        add(back, false, true);
    }

    public static void updateSkillTree() {
        categories.clear();
        skills.clear();


        HashMap<String, Integer> skillCategories = getSkillCategoryInfo();
        HashMap<String, ArrayList<String>> skillSet = getSkillSet();
        HashMap<String, Boolean> acquiredStates = getSkillsAcquiredState();
        HashMap<String, Integer> costs = getSkillsCosts();
        String activeSkillName = getActiveSkill();

        for (String categoryName : skillCategories.keySet()) {
            ButtonB.ButtonType type = switch (skillCategories.get(categoryName)) {
                case 0 -> ButtonB.ButtonType.category0;
                case 1 -> ButtonB.ButtonType.category1;
                case 2 -> ButtonB.ButtonType.category2;
                case 3 -> ButtonB.ButtonType.category3;
                default -> null;
            };
            ButtonB category = new ButtonB(type, categoryName, (int) SKILL_BUTTON_WIDTH.getValue(), SKILL_FONT_SIZE.getValue(), true, false);
            category.setIconTextGap((int) SKILL_TEXT_OFFSET.getValue());
            category.setVerticalTextPosition(SwingConstants.TOP);
            categories.add(category);
        }
        int level = 0;
        while (true) {
            boolean finished = true;
            ArrayList<Component> levelSkills = new ArrayList<>();
            for (String categoryName : skillCategories.keySet()) {
                if (skillSet.get(categoryName).size() > level) {
                    finished = false;
                    String name = skillSet.get(categoryName).get(level);
                    ButtonB.ButtonType type = acquiredStates.get(name) ? ButtonB.ButtonType.skill_unlocked : ButtonB.ButtonType.skill_locked;
                    if (name.equals(activeSkillName)) type = ButtonB.ButtonType.active_skill;

                    ButtonB skill = new ButtonB(type, name, (int) SKILL_BUTTON_WIDTH.getValue(), SKILL_FONT_SIZE.getValue(),
                            type.equals(ButtonB.ButtonType.active_skill), false);
                    ButtonB.ButtonType finalType = type;
                    skill.addActionListener(e -> {
                        switch (finalType) {
                            case skill_locked -> {
                                int action = JOptionPane.showConfirmDialog(getINSTANCE(), DefaultMethods.PURCHASE_MESSAGE(costs.get(name)), PURCHASE_TITLE.getValue(), JOptionPane.YES_NO_OPTION);
                                if (action == JOptionPane.YES_OPTION) {
                                    if (purchaseSkill(name)) {
                                        int actionConfirm = JOptionPane.showOptionDialog(getINSTANCE(), DefaultMethods.SUCCESSFUL_PURCHASE_MESSAGE(name), SUCCESSFUL_PURCHASE_TITLE.getValue(),
                                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
                                        if (actionConfirm == JOptionPane.CLOSED_OPTION) updateSkillTree();
                                    }
                                }
                            }
                            case skill_unlocked -> {
                                int action = JOptionPane.showConfirmDialog(getINSTANCE(), DefaultMethods.ACTIVATE_MESSAGE(name), ACTIVATE_TITLE.getValue(), JOptionPane.YES_NO_OPTION);
                                if (action == JOptionPane.YES_OPTION) {
                                    setActiveSkill(name);
                                    updateSkillTree();
                                }
                            }
                        }
                    });
                    levelSkills.add(skill);
                } else levelSkills.add(null);
            }
            if (finished) break;
            skills.add(levelSkills);
            level++;
        }
    }

    public static SkillTree getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new SkillTree();
        return INSTANCE;
    }
}
