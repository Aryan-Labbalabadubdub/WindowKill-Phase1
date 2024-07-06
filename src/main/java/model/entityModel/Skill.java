package model.entityModel;

import controller.constants.Variables;
import model.characterModels.EpsilonModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static controller.constants.EntityConstants.SKILL_COOLDOWN_IN_MINUTES;

public class Skill implements ActionListener {
    public static Collection<Skill> allSkills = new ArrayList<>();
    public static HashMap<SkillType, ArrayList<Skill>> skillSet = new HashMap<>(Map.of(
            SkillType.ATTACK, new ArrayList<>(),
            SkillType.GUARD, new ArrayList<>(),
            SkillType.POLYMORPHIA, new ArrayList<>()
    ));
    public static Skill activeSkill;
    public String name;
    public int cost;
    public boolean acquired = false;
    public long lastSkillTime = 0;

    public Skill(String name, int cost, SkillType type) {
        this.name = name;
        this.cost = cost;
        skillSet.get(type).add(this);
    }

    public static void initializeSkills() {
        allSkills.add(new Skill("WRIT OF ARES", 750, SkillType.ATTACK) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Variables.EPSILON_MELEE_DAMAGE += 2;
                Variables.EPSILON_RANGED_DAMAGE += 2;
            }
        });
        allSkills.add(new Skill("WRIT OF ACESO", 500, SkillType.GUARD) {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Timer((int) TimeUnit.SECONDS.toNanos(1), e1 -> EpsilonModel.getINSTANCE().addHealth(1)).start();
            }
        });
        allSkills.add(new Skill("WRIT OF PROTEUS", 1000, SkillType.POLYMORPHIA) {
            @Override
            public void actionPerformed(ActionEvent e) {
                EpsilonModel.getINSTANCE().addVertex();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public void fire() {
        long now = System.nanoTime();
        if (now - lastSkillTime >= TimeUnit.MINUTES.toNanos(SKILL_COOLDOWN_IN_MINUTES.getValue())) {
            actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
            lastSkillTime = now;
        }
    }

    public enum SkillType {
        ATTACK, GUARD, POLYMORPHIA
    }

}
